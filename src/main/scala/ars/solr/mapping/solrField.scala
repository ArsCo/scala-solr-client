package ars.solr.mapping

import scala.annotation.StaticAnnotation

/**
  * Created by ars on 19/03/2017.
  */
class solrField[From, To](fieldName: String, converter: SolrFieldConverter[From, To]) extends StaticAnnotation {

}

/**
  *
  * @tparam From
  * @tparam To
  */
trait SolrFieldConverter[From, To] {
  def convert(from: From): To
}

trait SolrClassConverter