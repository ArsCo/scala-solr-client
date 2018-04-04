package ars.solr.management.replication.response

import org.json4s.{DefaultFormats, JValue}

/** Conversion JSON part to instance.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
trait FromJson[T] {

  protected implicit val formats = DefaultFormats

  /**
    * Extracts data from JSON part and the creates instance of `T`.
    *
    * @param json the JSON body
    * @return the result instance
    */
  def fromJson(json: JValue): T
}
