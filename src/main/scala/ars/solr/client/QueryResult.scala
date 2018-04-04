package ars.solr.client

import ars.solr.client.Predicates.newAbortPredicate
import ars.solr.client.mapping.CanMapTo
import org.apache.solr.client.solrj.response.QueryResponse

/** The result of query execution.
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
trait QueryResult {

  /**
    * @return the number of found documents
    */
  def numFound: Long

  /**
    * @return the starting position
    */
  def start: Long

  /**
    * @return the maximum score
    */
  def maxScore: Option[Float]

  /**
    * Gets result documents as [[SolrDocumentMap]].
    *
    * @return the result documents
    */
  def documents: Seq[SolrDocumentMap]

  /**
    * Tries to get result documents as instances of `Result` type. This method uses `mapper` to
    * map [[SolrDocumentMap]] to `Result` type.
    *
    * Each mapped document is filtered with predicate.
    * If predicate returns `false` then document will not be included into result sequence, otherwise
    * it will be included only if it was mapped successfully (mapping result is `Success(_)`).
    * If `predicate` throws an exception then it will be propagated up to callee method.
    *
    * @param predicate the filter predicate
    * @param mapper the mapper
    *
    * @tparam Result the result type
    *
    * @return the sequence of converted result documents
    */
  def documents[Result](predicate: Predicate[Result] = newAbortPredicate())
                       (implicit mapper: CanMapTo[Result]): Seq[Result]
}

object QueryResult {

  /**
    * @param native the native Apache SolrJ response
    *
    * @return the response instance
    */
  def apply(native: QueryResponse): QueryResult = new DefaultQueryResult(native)
}
