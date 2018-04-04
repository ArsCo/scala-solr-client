package ars.solr.client.request.facet

import ars.precondition.RequireUtils.{requireNonNegative, requireNotBlank, requireNotNull}
import ars.solr.meta.experimental
import ars.solr.client.request.facet.FacetRequestBuilder.{FacetExists, fieldParam}
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.common.params.FacetParams._

/** Apache Solr Scala per-field facets request builder.
  *
  * @param solrQuery the Apache Solr query
  * @param field the field
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */

class FacetFieldRequestBuilder(solrQuery: SolrQuery, field: String) {
  import FacetFieldRequestBuilder._

  requireNotNull(solrQuery, "solrQuery")
  requireNotBlank(field, "field")

  /**
    * Adds per field facet prefix to the request.
    * The Apache Solr request parameter `f.fieldname.facet.prefix` will be added.
    *
    * @param prefix the prefix
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def prefix(prefix: String): FacetFieldRequestBuilder = {
    requireNotBlank(prefix, "prefix")

    solrQuery.setFacetPrefix(field, prefix)
    this
  }

  /**
    * Adds per field facet prefix to the request.
    * The Apache Solr request parameter `f.fieldname.facet.contains` will be added.
    *
    * @param substring the substring
    * @param isIgnoreCase weather or not ignore character case
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def contains(substring: String, isIgnoreCase: Boolean = false): FacetFieldRequestBuilder = {
    requireNotBlank(substring, "substring")

    solrQuery.set(facetContains(field, isIgnoreCase), substring)
    this
  }

  /**
    * Adds facet sorting type to the request.
    * The Apache Solr request parameter `f.fieldname.facet.sort` will be added.
    *
    * @param sortingType the substring
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def sort(sortingType: FacetSortType): FacetFieldRequestBuilder = {
    requireNotNull(sortingType, "sortingType")

    solrQuery.set(facetSort(field), sortingType.name)
    this
  }

  /**
    * Sets the limit of the result facets.
    * The Apache Solr request parameter `f.fieldname.facet.limit` will be set.
    *
    * @param limit the limit
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def limit(limit: Int): FacetFieldRequestBuilder = {
    requireNonNegative(limit, "limit")

    solrQuery.set(facetLimit(field), limit)
    this
  }

  /**
    * Sets the limit of the result facets to infinity.
    * The Apache Solr request parameter `f.fieldname.facet.limit` will be set to `-1`.
    *
    * @return the `this` instance
    */
  def unlimited(): FacetFieldRequestBuilder = {
    solrQuery.set(facetLimit(field), -1)
    this
  }

  /**
    * Removes explicit limit from the request and uses the default.
    * The Apache Solr request parameter `f.fieldname.facet.limit` will be removed from the request.
    *
    * @return the `this` instance
    */
  @experimental
  def defaultLimit(): FacetFieldRequestBuilder = {
    solrQuery.set(facetLimit(field), null)
    this
  }


  /**
    * Sets the limit of the result facets.
    * The Apache Solr request parameter `f.fieldname.facet.offset` will be set.
    *
    * @param offset the offset
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def offset(offset: Int): FacetFieldRequestBuilder = {
    requireNonNegative(offset, "offset")

    solrQuery.set(facetOffset(field), offset) // TODO Change when it'll be in SolrJ API
    this
  }

  /**
    * Sets the minimum counts required for a facet field to be included in the response.
    * The Apache Solr request parameter `facet.mincount` will be set.
    *
    * @param minCount the the minimum counts
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def minCount(minCount: Int): FacetFieldRequestBuilder = {
    requireNonNegative(minCount, "minCount")

    solrQuery.set(facetMinCount(field), minCount)
    this
  }

  /**
    * If set to `true`, this parameter indicates that, in addition to the Term-based constraints of a facet field,
    * a count of all results that match the query but which have no facet value for the field should be computed
    * and returned in the response.
    * The Apache Solr request parameter `facet.missing` will be set.
    *
    * @param isIncludeMissing weather or not include missing facet values
    * @return the `this` instance
    */
  def missing(isIncludeMissing: Boolean): FacetFieldRequestBuilder = {
    solrQuery.set(facetMissing(field), isIncludeMissing)
    this
  }

  /**
    * Sets the type of algorithm or method Solr should use when faceting a field.
    * The Apache Solr request parameter `facet.method` will be set.
    *
    * @param method the method
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def method(method: FacetMethod): FacetFieldRequestBuilder = {
    solrQuery.set(facetMethod(field), method.name)
    this
  }

  /**
    * Sets the type of algorithm or method Solr should use when faceting a field.
    * The Apache Solr request parameter `facet.exists` will be set.
    *
    * @param existsOnly weather or not to cap facet counts by 1
    * @return the `this` instance
    */
  def exists(existsOnly: Boolean): FacetFieldRequestBuilder = {
    solrQuery.set(facetExists(field), existsOnly)
    this
  }


}

object FacetFieldRequestBuilder {

  private[request] def facetContains(field: String) = fieldParam(field, FacetRequestBuilder.FacetContains)
  private[request] def facetContainsIgnoreCase(field: String) = fieldParam(field, FacetRequestBuilder.FacetContainsIgnoreCase)
  private[request] def facetExists(field: String) = fieldParam(field, FacetExists)

  private[request] def facetSort(field: String) = fieldParam(field, FACET_SORT)
  private[request] def facetLimit(field: String) = fieldParam(field, FACET_LIMIT)
  private[request] def facetOffset(field: String) = fieldParam(field, FACET_OFFSET)
  private[request] def facetMinCount(field: String) = fieldParam(field, FACET_MINCOUNT)
  private[request] def facetMissing(field: String) = fieldParam(field, FACET_MISSING)
  private[request] def facetMethod(field: String) = fieldParam(field, FACET_METHOD)



  private[request] def facetContains(field: String, isIgnoreCase: Boolean): String = {
    if (isIgnoreCase) facetContainsIgnoreCase(field) else facetContains(field)
  }
}
