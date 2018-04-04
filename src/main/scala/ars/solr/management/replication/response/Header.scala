package ars.solr.management.replication.response

import ars.precondition.RequireUtils.requireNotNull
import ars.solr.exception.SolrException
import org.json4s.JValue

/** Apache Solr response header.
  *
  * @param status the responce status
  * @param queryTime the query time
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class Header(status: Long, queryTime: Long)

object Header extends FromJson[Header] {

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if any argument is invalid")
  @throws[SolrException]("if response status isn't success")
  override def fromJson(json: JValue): Header = {
    requireNotNull(json, "json")

    val responseHeader = json \ "responseHeader"
    val status = (responseHeader \ "status").extract[Long]
    if (status == 0) {
      val queryTime = (responseHeader \ "QTime").extract[Long]
      Header(status, queryTime)
    } else {
      throw new SolrException(s"Bad response status ($status): $json")
    }
  }
}
