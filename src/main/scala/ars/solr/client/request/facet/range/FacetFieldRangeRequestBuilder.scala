package ars.solr.client.request.facet.range

import java.util.Date

import ars.precondition.RequireUtils.{require, requireNotBlank, requireNotNull}
import org.apache.solr.client.solrj.SolrQuery

/**
  * Created by ars on 03/04/2017.
  */
class FacetFieldRangeRequestBuilder[T](solrQuery: SolrQuery, field: String) {
  requireNotNull(solrQuery, "solrQuery")
  requireNotBlank(field, "field")

  def dateRange(start: Date, end: Date, gap: String)
               (implicit t: T =:= Date): FacetFieldRangeRequestBuilder[T] = { // TODO
    requireNotNull(start, "start")
    requireNotNull(end, "end")
    require(start.before(end) || start.equals(end), "start < end") // TODO Is equals need?
    requireNotBlank(gap, "gap")

    solrQuery.addDateRangeFacet(field, start, end, gap)
    this
  }

  def numericRangeFacet(start: Number, end: Number, gap: Number)
                       (implicit t: T =:= Date): FacetFieldRangeRequestBuilder[T] = {
    requireNotNull(start, "start")
    requireNotNull(end, "end")
    require(start.doubleValue() <  end.doubleValue(), "start < end") // TODO Is equals need?
    requireNotNull(gap, "gap")

    solrQuery.addNumericRangeFacet(field, start, end, gap)
    this
  }
}


