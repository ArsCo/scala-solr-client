package ars.solr.client.mapping

import ars.solr.client.SolrDocumentMap

import scala.util.Try

/** Mappers the instance of [[SolrDocumentMap]] to the instance of [[To]].
  *
  * @tparam To the destination type
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
trait CanMapTo[+To] {

  /** Tries to map document to the instance of [[To]]. If it isn't possible then [[scala.util.Failure]]
    * will be returned.
    *
    * @param from the source instance
    *
    * @throws IllegalArgumentException if `from` is `null`
    *
    * @return the destination instance or error description
    */
  def map(from: SolrDocumentMap): Try[To]
}
