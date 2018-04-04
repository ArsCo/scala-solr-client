package ars.solr.management.replication.response

import ars.precondition.RequireUtils.requireNotNull

/** Apache Solr response.
  *
  * @param header the header
  * @param body the body
  * @tparam Body the body type
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class Response[Body <: AnyRef](header: Header, body: Body) {
  requireNotNull(header, "header")
  requireNotNull(body, "body")
}


