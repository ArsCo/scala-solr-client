package ars.solr.client.request

import org.apache.solr.client.solrj.SolrQuery.SortClause

/**
  *
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.0
  */
package object implicits {
  val ScorePseudoField: String = "score"
  val AllFields: String = "*"

  val asc: (String) => SortClause = SortClause.asc
  val desc: (String) => SortClause = SortClause.desc
}
