package ars.solr.pool

import java.util.concurrent.atomic.AtomicBoolean

import ars.precondition.RequireUtils.requireNotNull
import ars.solr.client.{QueryResult, ScalaSolrClient}
import ars.solr.pool.SolrExecutor.{inTransaction, tryWithSolr, withSolr}
import com.typesafe.scalalogging.Logger
import io.github.andrebeat.pool
import io.github.andrebeat.pool.{Lease, Pool}
import org.apache.lucene.search.Query
import org.apache.solr.client.solrj.SolrServerException

import scala.concurrent.duration.Duration

/** Solr connection pool.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
class SolrExecutor(val pool: Pool[ScalaSolrClient]) extends AutoCloseable {
  requireNotNull(pool, "pool")

  protected[this] val closed = new AtomicBoolean(false)

  /**
    * Closes executor.
    */
  override def close() {
    pool.close()
  }

  /**
    * Syntax sugar alias for [[SolrExecutor.withSolr()]].
    *
    * @param block the executing block of code
    * @tparam Result the result type
    * @return the result of `block` execution
    */
  def apply[Result](block: => ScalaSolrClient => Result): Result = withSolr(pool)(block)

  /**
    * Syntax sugar alias for [[SolrExecutor.tryWithSolr()]].
    *
    * @param block the executing block of code
    * @tparam Result the result type
    * @return the result of `block` execution
    */
  def apply[Result](duration: Duration)
                   (block: => ScalaSolrClient => Result): Option[Result] = tryWithSolr(pool, duration)(block)

  /**
    * Syntax sugar alias for [[SolrExecutor.tryWithSolr()]].
    *
    * @param block the executing block of code
    * @tparam Result the result type
    * @return the result of `block` execution
    */
  def ~[Result](block: => ScalaSolrClient => Result): Option[Result] = tryWithSolr(pool)(block)

  /**
    * Syntax sugar alias for [[SolrExecutor.inTransaction())]].
    *
    * @param client the Apache Solr client
    * @param block the executing block of code
    * @tparam Result the result type
    * @return the result of `block` execution
    */
  def <>[Result](block: => ScalaSolrClient => Result)
                                      (implicit client: ScalaSolrClient): Result = {
    inTransaction(client)(block)
  }
}

object SolrExecutor {

  /**
    * Executes the `block` of code with a `client` and returns the result of execution.
    * This method don't close client after execution and don't release it to the pool.
    *
    * @param client the client
    * @param block the executing block of code
    * @tparam Result the result type
    * @return the result of `block` execution
    */
  def withClient[Result](client: ScalaSolrClient)
                        (block: => ScalaSolrClient => Result): Result = {
    try {
      block(client)
    } catch {
      case e: Exception =>
        logger.error("", e)
        throw e;
    }
  }

  /**
    * Executes the `block` of code with a client acquired form `lease` and returns the result of execution.
    * This method gets a client from lease, executes the `block` and releases the client to the pool.
    *
    * @param lease the lease
    * @param block the executing block of code
    * @tparam Result the result type
    * @return the result of `block` execution
    */
  def withClient[Result](lease: pool.Lease[ScalaSolrClient])
                                (block: => ScalaSolrClient => Result): Result = {
    lease { client => withClient(client)(block) }
  }

  /**
    * Executes the `block` of code with a `client` and returns the result of execution.
    * This method gets a client from pool, executes the `block` and releases the client to the pool.
    * If there's no free client in the pool, bound with this instance, method blocks execution.
    *
    * @param pool the pool
    * @param block the executing block of code
    * @tparam Result the result type
    * @return the result of `block` execution
    */
  def withSolr[Result](pool: Pool[ScalaSolrClient])(block: => ScalaSolrClient => Result): Result = {
    withClient[Result](pool.acquire())(block)
  }

  /**
    * Executes the `block` of code with a `client` and returns the result of execution.
    * This method gets a client from pool, executes the `block` and releases the client to the pool.
    * If there's no free client in the pool, bound with this instance, method try to get client
    * during `duration` ant then returns `None`.
    *
    * @param pool the pool
    * @param duration the duration
    * @param block the executing block of code
    * @tparam Result the result type
    * @return the result of `block` execution
    */
  def tryWithSolr[Result](pool: Pool[ScalaSolrClient], duration: Duration)
                         (block: => ScalaSolrClient => Result): Option[Result] = {
    executeOrNothing(pool.tryAcquire(duration))(block)
  }

  /**
    * Executes the `block` of code with a `client` and returns the result of execution.
    * This method gets a client from pool, executes the `block` and releases the client to the pool.
    * If there's no free client in the pool, bound with this instance, method immediately returns `None`.
    *
    * @param pool the pool
    * @param block the executing block of code
    * @tparam Result the result type
    * @return the result of `block` execution
    */
  def tryWithSolr[Result](pool: Pool[ScalaSolrClient])
                         (block: => ScalaSolrClient => Result): Option[Result] = {
    executeOrNothing(pool.tryAcquire())(block)
  }

  /**
    * Executes the `block` consistently in transaction.
    *
    * If there is an exception to be thrown during block execution all changes will be "rollbacked".
    * If there is no problem during block execution all changes will be committed.
    *
    * @param client the Apache Solr client
    * @param block the executing block of code
    * @tparam Result the result type
    * @return the result of `block` execution
    */
  def inTransaction[Result](client: ScalaSolrClient)
                           (block: => ScalaSolrClient => Result): Result = {
    try {
      val result = block(client)
      client.commit()
      result
    } catch {
      case e: SolrServerException =>
        client.rollback()
        logger.error("There is an error in Solr transaction execution", e)
        throw e
    }
  }

  /**
    * Executes the `query` with the `client` and requests the `fields` then map
    * returned ''single'' result `DocumentMap` to `Result` with the function `f`.
    *
    * If query returns more than one result then if `moreException` is not `None`, it
    * will be thrown.
    *
    * @param client the Apache Solr client
    * @param query the query
    * @param fields the requesting fields
    * @param f the mapping function
    * @param moreException the exception throwing if there is more than one result
    * @tparam Result the result type
    * @return the result of `None` if there is no result
    */
  def requestSingle[Result](client: ScalaSolrClient, query: Query, fields: String*)
                           (f: Map[String, Any] => Result, moreException: Option[Exception] = None): Option[Result] = {

    val map = queryTopTwoResultMaps(client, query)
    if (map.numFound == 1) {
      Some(f(map.documents.head))
    } else if (map.numFound > 1) {
      logger.error(s"More than single result for the query: $query")
      moreException.foreach(throw _)
      None
    } else {
      None
    }
  }

  /**
    * Executes the `query` with the `client` and requests the `fields` then map
    * returned ''first'' result `DocumentMap` to `T` with the function `f`.
    *
    * @param client the Apache Solr client
    * @param query the query
    * @param fields the requesting fields
    * @param f the mapping function
    * @tparam Result the result type
    * @return the result of `None` if there is no result
    */
  def requestFirst[Result]
                  (client: ScalaSolrClient, query: Query, fields: String*)
                  (f: Map[String, Any] => Result): Option[Result] = {

    val map = queryTopTwoResultMaps(client, query)
    if (map.numFound >= 1) {
      Some(f(map.documents.head))
    } else {
      None
    }
  }

  /**
    * Executes the `query` with the `client` and requests the `rows` documents
    * from `start` (requests the `fields`) then map each
    * returned result `DocumentMap` to `T` with the function `f`.
    *
    * @param client the Apache Solr client
    * @param query the query
    * @param fields the requesting fields
    * @param f the mapping function
    * @param start the start position
    * @param rows the number of requested documents
    * @tparam Result the result type
    * @return the result sequence
    */
  def requestSequence[Result]
                     (client: ScalaSolrClient, query: Query, fields: String*) // TODO Fields????
                     (f: Map[String, Any] => Result, start: Int = 0, rows: Int = 10): Seq[Result] = {

    val map = queryResultMap(client, query, start, rows)
    if (map.numFound > 0) map.documents.map(v => f(v))
    else Nil
  }

  // TODO Javadoc
  def requestNumber[Result]
                   (client: ScalaSolrClient, query: Query): Long = {
    val map = queryResultMap(client, query)
    map.numFound // TODO Will not work....
  }

  /** Implicit convertor from `SolrExecutor[Client]` to `Pool[Client]`. */
  implicit def executorToPool(executor: SolrExecutor): Pool[ScalaSolrClient] = executor.pool

  /** Implicit convertor from `Pool[Client]` to `SolrExecutor[Client]`. */
  implicit def poolToExecutor(pool: Pool[ScalaSolrClient]): SolrExecutor = new SolrExecutor(pool)



  private def queryResultMap[T](client: ScalaSolrClient, query: Query,
                                start: Int = 0, rows: Int = Int.MaxValue): QueryResult = {

    client.buildQuery(query).offset(start).limit(rows).execute()
  }

  private def queryTopTwoResultMaps[T](client: ScalaSolrClient, query: Query): QueryResult = {
    queryResultMap[T](client, query, 0, 10)
  }

  private def executeOrNothing[Result](lease: Option[Lease[ScalaSolrClient]])
                                              (block: => ScalaSolrClient => Result): Option[Result] = {
    lease.map(l => withClient[Result](l)(block))
  }

  private val logger = Logger[SolrExecutor]
}

