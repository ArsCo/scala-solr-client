package ars.solr.client

import ars.precondition.RequireUtils.requireNotNull
import ars.solr.client.mapping.CanMapTo
import org.apache.solr.client.solrj.response.QueryResponse

/** Default implementation of [[QueryResult]].
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
class DefaultQueryResult(val nativeResponse: QueryResponse) extends QueryResult {

  requireNotNull(nativeResponse, "nativeResponse")

  private[this] lazy val nativeResults = nativeResponse.getResults

  /** @inheritdoc */
  override lazy val numFound: Long = nativeResults.getNumFound

  /** @inheritdoc */
  override lazy val maxScore: Option[Float] = Option(nativeResults.getMaxScore).map(_.toFloat)

  /** @inheritdoc */
  override lazy val start: Long = nativeResults.getStart

  /** @inheritdoc */
  override lazy val documents: Seq[Map[String, Any]] = toScalaMapSeq(nativeResponse.getResults)

  /** @inheritdoc */
  override def documents[Result](predicate: Predicate[Result])
                                (implicit mapper: CanMapTo[Result]): Seq[Result] = {
    requireNotNull(predicate, "predicate")
    requireNotNull(mapper, "mapper")

    for {
      scalaSolrDoc <- toScalaMapSeq(nativeResponse.getResults)
      result = mapper.map(scalaSolrDoc) if predicate(result)
      successResult <- result.toOption
    } yield successResult
  }
}