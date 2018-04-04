package ars.solr.client.request

import ars.precondition.RequireUtils.{requireAllNotBlank, requireNotBlank}
import ars.solr.client.request.common.Switchable
import org.apache.solr.client.solrj.SolrQuery

/** Apache Solr Scala statistics request builder.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
class StatisticsRequestBuilder(solrQuery: SolrQuery) extends Switchable[StatisticsRequestBuilder] {

  /** @inheritdoc */
  override def switch(isOn: Boolean): StatisticsRequestBuilder = {
    solrQuery.setGetFieldStatistics(isOn)
    this
  }

  def fieldStatistics(field: String): StatisticsRequestBuilder = {
    requireNotBlank(field, "field")

    solrQuery.setGetFieldStatistics(field)
    this
  }

  def fieldCalcDistinct(fields: (String, Boolean)*): StatisticsRequestBuilder = {
    requireNotBlank(fields, "fields")

    fields.foreach { case (field, isDistinct) =>
      solrQuery.addStatsFieldCalcDistinct(field, isDistinct)
    }
    this
  }

  def noFieldCalcDistinct(isDistinct: Boolean): StatisticsRequestBuilder = {
    solrQuery.addStatsFieldCalcDistinct(null, isDistinct)
    this
  }

  def addFieldCalcDistinct(field: String, isDistinct: Boolean): StatisticsRequestBuilder = {
    requireNotBlank(field, "field")

    solrQuery.addStatsFieldCalcDistinct(field, isDistinct)
    this
  }

  def addStatsFacet(field: String, facets: String*): StatisticsRequestBuilder = {
    requireNotBlank(field, "field")
    requireNotBlank(facets, "facets")
    requireAllNotBlank(facets, "facets")

    solrQuery.addStatsFieldFacets(field, facets :_*)
    this
  }


  //  def addFieldStatistics(fields: String*): StatisticsRequestBuilder = {
  //    solrQuery.addGetFieldStatistics(String... field) // TODO No
  //    this
  //  }
}
