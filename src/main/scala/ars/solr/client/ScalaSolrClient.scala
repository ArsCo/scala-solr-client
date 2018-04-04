package ars.solr.client

import java.io.IOException
import java.lang.Float

import ars.precondition.Predicates.BoundTypes.Inclusive
import ars.precondition.RequireUtils.{requireNotBlank, _}
import ars.solr.client.request.RequestBuilder
import ars.solr.config.SolrCoreConfig
import org.apache.lucene.search.Query
import org.apache.solr.client.solrj.SolrRequest.METHOD
import org.apache.solr.client.solrj.SolrRequest.METHOD.GET
import org.apache.solr.client.solrj.impl.{HttpSolrClient, ConcurrentUpdateSolrClient, LBHttpSolrClient}
import org.apache.solr.client.solrj.{SolrClient, SolrServerException, StreamingResponseCallback}
import org.apache.solr.common.params.SolrParams
import org.apache.solr.common.{SolrDocument, SolrInputDocument}

import scala.collection.JavaConversions._

/** Apache Solr Scala client. It's a wrapper around Apache SolrJ [[SolrClient]] class
  * with Scala syntax sugar.
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
class ScalaSolrClient(val nativeClient: SolrClient) {
  requireNotNull(nativeClient, "nativeClient")

  // ---------- Add documents ----------

  /**
    * Adds the documents to the index. Proxy method for [[SolrClient.add(*]].
    *
    * @param documents the documents
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def add(documents: SolrInputDocument*): Unit = add(-1, documents :_*)

  /**
    * Adds the documents to the index. Proxy method for [[SolrClient.add(*]].
    *
    * @param commitWithInMillis the commit time in milliseconds or `-1` if there's no
    * @param documents the documents
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def add(commitWithInMillis: Int, documents: SolrInputDocument*): Unit = add(null, -1, documents :_*)

  /**
    * Adds the documents to the index. Proxy method for [[SolrClient.add(*]].
    *
    * @param collection the collection
    * @param documents the documents
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def add(collection: Collection, documents: SolrInputDocument*): Unit = add(collection, -1, documents :_*)

  /**
    * Adds the documents to the index. Proxy method for [[SolrClient.add(*]].
    *
    * @param documents the documents
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def add(documents: Iterator[SolrInputDocument]): Unit = add(null, documents)

  /**
    * Adds the documents to the index. Proxy method for [[SolrClient.add(*]].
    *
    * @param collection the collection
    * @param documents the documents
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def add(collection: Collection, documents: Iterator[SolrInputDocument]): Unit = {
    requireNotNull(collection, "collection")
    requireNotNull(documents, "documents")

    nativeClient.add(collection.name, documents)
  }

  /**
    * Adds the documents to the index. Proxy method for [[SolrClient.add(*]].
    *
    * @param collection the Solr collection
    * @param commitWithInMillis the commit time in milliseconds or `-1` if there's no
    * @param documents the documents
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def add(collection: Collection, commitWithInMillis: Int, documents: SolrInputDocument*): Unit = {
    requireNotNull(collection, "collection")
    requireNumberFrom(commitWithInMillis, "commitWithInMillis")(-1, Inclusive)
    requireNotNull(documents, "documents")

    nativeClient.add(collection.name, documents, commitWithInMillis)
  }

  // ---------- Delete documents ----------

  /**
    * Deletes documents by ids from index. Proxy method for [[SolrClient.deleteById(*]].
    *
    * @param ids the documents ids
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def deleteById(ids: String*): Unit = deleteById(-1, ids :_*)

  /**
    * Deletes documents by ids from index. Proxy method for [[SolrClient.deleteById(*]].
    *
    * @param commitWithInMillis the commit time in milliseconds or `-1` if there's no
    * @param ids the documents ids
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def deleteById(commitWithInMillis: Int, ids: String*): Unit = deleteById(null, commitWithInMillis, ids :_*)

  /**
    * Deletes documents by ids from index. Proxy method for [[SolrClient.deleteById(*]].
    *
    * @param collection the collection
    * @param ids the documents ids
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def deleteById(collection: Collection, ids: String*): Unit = deleteById(collection, -1, ids :_*)

  /**
    * Deletes documents by ids from index. Proxy method for [[SolrClient.deleteById(*]].
    *
    * @param collection the collection
    * @param commitWithInMillis the commit time in milliseconds or `-1` if there's no
    * @param ids the documents ids
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def deleteById(collection: Collection, commitWithInMillis: Int, ids: String*): Unit = {
    requireNotNull(collection, "collection")
    requireNumberFrom(commitWithInMillis, "commitWithInMillis")(-1, Inclusive)
    requireNotBlank(ids, "ids")
    requireAllNotBlank(ids, "ids")

    nativeClient.deleteById(collection.name, ids, commitWithInMillis)
  }

  /**
    * Deletes documents by query. Proxy method for [[SolrClient.deleteByQuery(*]].
    *
    * @param query the query
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def deleteByQuery(query: Query): Unit = deleteByQuery(-1, query)

  /**
    * Deletes documents by query. Proxy method for [[SolrClient.deleteByQuery(*]].
    *
    * @param commitWithInMillis the commit time in milliseconds or `-1` if there's no
    * @param query the query
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def deleteByQuery(commitWithInMillis: Int, query: Query): Unit = deleteByQuery(null, -1, query)

  /**
    * Deletes documents by query. Proxy method for [[SolrClient.deleteByQuery(*]].
    *
    * @param collection the collection
    * @param query the query
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalArgumentException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def deleteByQuery(collection: Collection, query: Query): Unit = deleteByQuery(collection, -1, query)

  /**
    * Deletes documents by query. Proxy method for [[SolrClient.deleteByQuery(*]].
    *
    * @param collection the collection
    * @param commitWithInMillis the commit time in milliseconds or `-1` if there's no
    * @param query the query
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @since 0.0.3
    */
  @throws[IllegalStateException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def deleteByQuery(collection: Collection, commitWithInMillis: Int, query: Query): Unit = {
    requireNotNull(collection, "collection")
    requireNumberFrom(commitWithInMillis, "commitWithInMillis")(-1, Inclusive)
    requireNotNull(query, "query")

    nativeClient.deleteByQuery(collection.name, query.toString, commitWithInMillis)
  }

  // ---------- Get document by id ----------

  /**
    * Gets document by id.
    *
    * @param id the id
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @return the document if exists
    */
  @throws[IllegalStateException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def getById(id: String): Option[SolrDocumentMap] = {
    requireNotBlank(id, "id")

    _getById(id)
  }

  /**
    * Gets document by id.
    *
    * @param collection the collection
    * @param id the id
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @return the document if exists
    */
  @throws[IllegalStateException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def getById(collection: Collection, id: String): Option[SolrDocumentMap] = {
    requireNotNull(collection, "collection")
    requireNotBlank(id, "id")

    _getById(id, collection = Some(collection))
  }

  /**
    * Gets document by id.
    *
    * @param params the request parameters
    * @param id the id
    *
    * @throws IllegalArgumentException if illegal arguments
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    *
    * @return the document if exists
    */
  @throws[IllegalStateException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def getById(params: SolrParams, id: String): Option[SolrDocumentMap] = {
    requireNotNull(params, "params")
    requireNotBlank(id, "id")

    _getById(id, params = Some(params))
  }

  /**
    * Gets document by id.
    *
    * @param collection the collection
    * @param params the request parameters
    * @param id the id
    *
    * @return the document if exists
    */
  @throws[IllegalStateException]("if illegal arguments")
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def getById(collection: Collection, params: SolrParams, id: String): Option[SolrDocumentMap] = {
    requireNotNull(collection, "collection")
    requireNotNull(params, "params")
    requireNotBlank(id, "id")

    _getById(id, Some(collection), Some(params))
  }

  private def _getById(id: String,
                       collection: Option[Collection] = None,
                       params: Option[SolrParams] = None): Option[SolrDocumentMap] = {

    val c = collection.map(_.name).orNull
    val p = params.orNull
    val r = nativeClient.getById(c, id, p)
    Option(r).map(nativeDocToScala)
  }

  /**
    * Gets documents by ids.
    * @param ids
    * @return
    */
  def getById(ids: String*): Seq[SolrDocumentMap] = {
    requireNotBlank(ids, "ids")
    requireAllNotBlank(ids, "ids")

    _getById(ids)
  }

  def getByIds(collection: Collection, ids: String*): Seq[SolrDocumentMap] = {
    requireNotNull(collection, "collection")
    requireNotBlank(ids, "ids")
    requireAllNotBlank(ids, "ids")

    _getById(ids, collection = Some(collection))
  }

  def getByIds(params: SolrParams, ids: String*): Seq[SolrDocumentMap] = {
    requireNotNull(params, "params")
    requireNotBlank(ids, "ids")
    requireAllNotBlank(ids, "ids")

    _getById(ids, params = Some(params))
  }

  private def _getById(ids: Seq[String],
                       collection: Option[Collection] = None,
                       params: Option[SolrParams] = None,
                       ): Seq[SolrDocumentMap] = {

    val c = collection.map(_.name).orNull
    val p = params.orNull
    val r = nativeClient.getById(c, ids, p)
    if (r == null) Seq() else toScalaMapSeq(r)
  }



  // ---------- Query documents ----------

  /**
    * Performs the query. Proxy method for [[SolrClient.query(*]].
    *
    * @param queryParams the object holding all key/value parameters to send along the request
    * @param method the HTTP method to use for the request, such as GET or POST
    *
    * @return the result of query
    */
  @throws[IllegalStateException]("if illegal arguments")
  @throws[SolrServerException]("if Solr server problems occurs")
  def query(queryParams: SolrParams, method: METHOD = GET): QueryResult = { // TODO Result type
    requireNotNull(queryParams, "queryParams")
    requireNotNull(method, "method")

    val response = nativeClient.query(queryParams, method)
    QueryResult(response)
  }

  /**
    * Performs the query and streams the results. Proxy method for [[SolrClient.queryAndStreamResponse(*]]
    * but with more concise Scala syntax.
    *
    * @param queryParams the object holding all key/value parameters to send along the request
    * @param callback the callback object
    */
  @throws[IllegalStateException]("if illegal arguments")
  @throws[SolrServerException]("if Solr server problems occurs")
  def streamQuery(queryParams: SolrParams)(callback: QueryCallback): Unit = {
    requireNotNull(queryParams, "queryParams")
    requireNotNull(callback, "callback")

    val nativeCallback = new StreamingResponseCallback {
      override def streamSolrDocument(d: SolrDocument): Unit = callback.document(d)
      override def streamDocListInfo(numFound: Long, start: Long, maxScore: Float): Unit = {
        val m = if (maxScore == null) None else Some(maxScore.floatValue())
        callback.info(numFound, start, m)
      }

    }
    nativeClient.queryAndStreamResponse(queryParams, nativeCallback)
  }

  /**
    * Initial method to get query builder. This method is another syntax to build
    * queries (without direct use of [[SolrParams]] class).
    *
    * Example:
    * {{{
    *   val client: ScalaSolrClient = ...
    *   val query: Query = ...
    *   val result: QueryResult = client.buildQuery(query)
    *                               .start(10).rows(5)
    *                               .fields("name", "value")
    *                               .execute();
    *   ...
    * }}}
    *
    * @param query the query
    * @return the new instance of [[RequestBuilder]]
    */
  def buildQuery(query: Query): RequestBuilder = {
    requireNotNull(query, " query")
    new RequestBuilder(query.toString)(this)
  }

  // ---------- Client operations ----------

  /**
    * Performs an explicit commit, causing pending documents to be committed for indexing.
    * Proxy method for [[SolrClient.commit(*]].
    *
    * @param waitFlush block until index changes are flushed to disk
    * @param waitSearcher block until a new searcher is opened and registered as the
    *                     main query searcher, making the changes visible
    * @param softCommit makes index changes visible while neither fsync-ing index files nor
    *                   writing a new index descriptor
    *
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    */
  def commit(waitFlush: Boolean = true,
             waitSearcher: Boolean = true,
             softCommit: Boolean = false): Unit = {
    commit(null, waitFlush, waitSearcher, softCommit)
  }

  /**
    * Performs an explicit commit, causing pending documents to be committed for indexing.
    * Proxy method for [[SolrClient.commit(*]].
    *
    * @param collection the Solr collection
    * @param waitFlush block until index changes are flushed to disk
    * @param waitSearcher block until a new searcher is opened and registered as the
    *                     main query searcher, making the changes visible
    * @param softCommit makes index changes visible while neither fsync-ing index files nor
    *                   writing a new index descriptor
    *
    * @throws IOException if there is a low-level I/O error
    * @throws SolrServerException if Solr server problems occurs
    */
  def commit(collection: String,
             waitFlush: Boolean = true,
             waitSearcher: Boolean = true,
             softCommit: Boolean = false): Unit = {
    nativeClient.commit(collection, waitFlush, waitSearcher, softCommit)
  }




  /**
    * Performs a rollback of all non-committed documents pending.
    * Proxy method for [[SolrClient.rollback(*]].
    */
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def rollback(): Unit = nativeClient.rollback()

  /**
    * Release allocated resources. Proxy method for [[SolrClient.close(*]]
    */
  def close(): Unit = nativeClient.close()

  // ---------- Solr server operations ----------

  /**
    * Issues a ping request to check if the server is alive.
    * Proxy method for [[SolrClient.ping(*]].
    */
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def ping(): Unit = nativeClient.ping()

  /**
    * Performs an explicit optimize, causing a merge of all segments to one.
    * Proxy method for [[SolrClient.optimize(*]].
    *
    * @param waitFlush block until index changes are flushed to disk
    * @param waitSearcher block until a new searcher is opened and registered as the
    *                     main query searcher, making the changes visible
    * @param maxSegments optimizes down to at most this number of segments
    */
  @throws[IOException]("if there is a low-level I/O error.")
  @throws[SolrServerException]("if Solr server problems occurs")
  def optimize(waitFlush: Boolean = true, waitSearcher: Boolean = true, maxSegments: Int = 1): Unit = {
    nativeClient.optimize(waitFlush, waitSearcher, maxSegments)
  }

  // ========== DEPRECATED ==========

  @deprecated("Temporary method for backward compatibility")
  def add(map: Map[String, Any]): Unit = {
    requireNotNull(map, "map")
    val doc = new SolrInputDocument
    map.foreach { case (name, value) =>
      doc.addField(name, value)
    }

    add(doc, -1) // TODO fix
  }

}

object ScalaSolrClient {

  /**
    * Creates the [[ScalaSolrClient]] instance that uses new [[HttpSolrClient]] instance as underlying client.
    *
    * @param core the core configuration
    * @return the new instance
    *
    * @since 0.0.4
    */
  @throws[IllegalArgumentException]("if argument is null value")
  def createClient(core: SolrCoreConfig): ScalaSolrClient = {
    requireNotNull(core, "core")

    val client = new HttpSolrClient(core.coreUrl)
    new ScalaSolrClient(client)
  }

  /**
    * Creates [[ScalaSolrClient]] instance that uses new [[LBHttpSolrClient]] instance as underlying client.
    *
    * @param cores the configuration of cores
    * @return the new instance
    *
    * @since 0.0.4
    */
  @throws[IllegalArgumentException]("if argument is null or empty list")
  def createLoadBalancedClient(cores: SolrCoreConfig*): ScalaSolrClient = {
    requireNotBlank(cores, "cores")
    requireAllNotNull(cores, "cores")

    val coreUrls = cores.map(_.coreName)
    val client = new LBHttpSolrClient(coreUrls: _*)
    new ScalaSolrClient(client)
  }

  /**
    * Creates [[ScalaSolrClient]] instance that uses new [[LBHttpSolrClient]] instance as underlying client.
    *
    * @param core the core configuration
    * @param queueSize the concurrent queue size
    * @param threadCount the max number of parallel threads
    * @return the new instance
    *
    * @since 0.0.4
    */
  @throws[IllegalArgumentException]("if any argument is invalid")
  def createConcurrentUpdateClient(core: SolrCoreConfig,
                                   queueSize: Int,
                                   threadCount: Int): ScalaSolrClient = {
    requireNotNull(core, "core")
    requirePositive(queueSize, "queueSize")
    requirePositive(threadCount, "threadCount")

    val client = new ConcurrentUpdateSolrClient(core.coreUrl, queueSize, threadCount)
    new ScalaSolrClient(client)
  }
}
