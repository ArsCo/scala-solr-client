package ars.solr.client.request

/** Query parser.
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
abstract sealed class SolrQueryParser(val name: String)

object SolrQueryParsers {
  case object Lucene extends SolrQueryParser("lucene")
  case object DisMax extends SolrQueryParser("dismax")
}
