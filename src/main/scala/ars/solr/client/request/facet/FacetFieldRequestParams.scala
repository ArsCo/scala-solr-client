package ars.solr.client.request.facet

import org.apache.lucene.search.Query

/** Apache Solr Scala facets request params.
  *
  * It provides wrapper methods to add raw Apache Solr params:
  * - `facet` (will be set to `true`)
  * - `facet.query` ([[addQueries()]])
  * - `facet.field` ([[addFields()]])
  * - `facet.prefix` ([[prefix()]])
  * - `facet.contains` ([[contains()]])
  * - `facet.sort` ([[sortBy()]])
  * - `facet.limit` ([[limit()]], [[unlimited()]])
  * - `facet.offset` ([[offset()]])
  * - `facet.mincount` ([[minCount()]])
  * - `facet.missing` ([[withMissing()]])
  * - `facet.method` ([[method()]])
  * - `facet.enum.cache.minDf` ([[minFrequency()]])
  * - `facet.exists` ([[binary()]])
  * - `facet.excludeTerms` ([[addExcludingTerms()]])
  * - `facet.overrequest.count` ([[overrequest()]])
  * - `facet.overrequest.ratio` ([[overrequest()]])
  * - `facet.threads` ([[limitThreads()]], [[unlimitedThreads()]])
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
trait FacetFieldRequestParams {

  /**
    * Adds facet query strings to the request.
    * Each invocation of this method appends query strings to the request.
    *
    * The Apache Solr request parameter `facet.query` will be added for each query string.
    *
    * @param queries the queries (must be non-blank sequence of non-blank strings)
    *
    * @throws IllegalArgumentException if `queries` is blank or contains blank string
    *
    * @return the `this` instance
    */
  def addQueries(queries: String*): FacetRequestBuilder

  /**
    * Adds facet queries to the request.
    * Each invocation of this method appends queries to the request.
    *
    * The Apache Solr request parameter `facet.query` will be added for each query.
    *
    * @param queries the queries (must be non-blank sequence of queries)
    *
    * @throws IllegalArgumentException if `queries` is blank or contains null query
    *
    * @return the `this` instance
    */
  def addQueries(queries: Query*): FacetFieldRequestParams

  /**
    * Adds facet fields to the request.
    * Each invocation of this method appends fields to the request.
    *
    * The Apache Solr request parameter `facet.field` will be added for each field.
    *
    * @param fields the fields (must be non-blank sequence of non-blank strings)
    *
    * @throws IllegalArgumentException if `fields` is blank or contains blank string
    *
    * @return the `this` instance
    */
  def addFields(fields: String*): FacetFieldRequestParams

  /**
    * Sets facet fields prefix to the request.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameter `facet.prefix` will be set.
    *
    * @param prefix the prefix (must be non-blank string)
    *
    * @throws IllegalArgumentException if `prefix` is blank
    *
    * @return the `this` instance
    */
  def prefix(prefix: String): FacetFieldRequestParams

  /**
    * Sets facet fields prefix to the request.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameter `facet.contains` will be set if `isCaseSensitive` is `true` and
    * `facet.contains.ignoreCase` otherwise.
    *
    * @param substring the substring (must be non-blank string)
    * @param isCaseSensitive if `false` then character case will be ignored
    *
    * @throws IllegalArgumentException if `substring` is blank
    *
    * @return the `this` instance
    */
  def contains(substring: String, isCaseSensitive: Boolean = true): FacetFieldRequestParams

  /**
    * Sets facet sorting type to the request.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameter `facet.sort` will be set.
    *
    * @param sortType the sorting type (must be non-null)
    *
    * @throws IllegalArgumentException if `sortType` is `null`
    *
    * @return the `this` instance
    */
  def sortBy(sortType: FacetSortType): FacetFieldRequestParams

  /**
    * Sets the limit of the result facets count.
    * Each invocation of this method replaces the previous value and value that was set by [[unlimited()]] method.
    *
    * The Apache Solr request parameter `facet.limit` will be set.
    *
    * @param limit the limit (must be non-negative)
    *
    * @throws IllegalArgumentException if `limit` is negative
    *
    * @return the `this` instance
    */
  def limit(limit: Int): FacetFieldRequestParams

  /**
    * Sets the limit of the result facets to infinity.
    * Each invocation of this method replaces the value that was set by [[limit()]] method.
    *
    * The Apache Solr request parameter `facet.limit` will be set to `-1`.
    *
    * @return the `this` instance
    */
  def unlimited(): FacetFieldRequestParams

  /**
    * Sets the limit of the result facets.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameter `facet.offset` will be set.
    *
    * @param offset the offset (must be non-negative)
    *
    * @throws IllegalArgumentException if `offset` is negative
    *
    * @return the `this` instance
    */
  def offset(offset: Int): FacetFieldRequestParams

  /**
    * Sets the minimum facet count required for a facet field to be included in the response.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameter `facet.mincount` will be set.
    *
    * @param minCount the minimum count (must be non-negative)
    *
    * @throws IllegalArgumentException if `count` is negative
    *
    * @return the `this` instance
    */
  def minCount(minCount: Int): FacetFieldRequestParams

  /**
    * If calls, indicates that, in addition to the Term-based constraints of a facet field,
    * a count of all results that match the query but which have no facet value for the field
    * should be computed and returned in the response.
    *
    * The Apache Solr request parameter `facet.missing` will be set to `true`.
    *
    * @return the `this` instance
    */
  def withMissing(): FacetFieldRequestParams

  /**
    * Sets the type of algorithm or method Solr should use when faceting a field.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameter `facet.method` will be set.
    *
    * @param method the method (must be non-null)
    *
    * @throws IllegalArgumentException if `method` is `null`
    *
    * @return the `this` instance
    */
  def method(method: FacetMethod): FacetFieldRequestParams

  /**
    * Sets the parameter that indicates the minimum document frequency (the number of documents matching a term)
    * for which the `filterCache` should be used when determining the constraint count for that term.
    * This parameter can be set only if `method=FacetMethod.Enum`.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameter `facet.enum.cache.minDf` will be set.
    *
    * @param frequency the frequency (non-negative)
    *
    * @throws IllegalArgumentException if `frequency` is negative
    *
    * @return the `this` instance
    */
  def minFrequency(frequency: Int): FacetFieldRequestParams

  /**
    * Call it to cap facet counts by 1. It can be used only on non-trie fields (such as strings).
    * It may speed up facet counting on large indices and/or high-cardinality facet values.
    *
    * The Apache Solr request parameter `facet.exists` will be set to `true`.
    *
    * @return the `this` instance
    */
  def binary(): FacetFieldRequestParams

  /**
    * Excludes the terms from the result of facet query.
    * Each invocation of this method appends excluding terms to the request.
    *
    * The Apache Solr request parameter `facet.excludeTerms` will be set.
    *
    * @param terms the terms to be excluded (non-blank sequence of non-blank strings)
    *
    * @throws IllegalArgumentException if `fields` is blank or contains blank string
    *
    * @return the `this` instance
    */
  def addExcludingTerms(terms: String*): FacetFieldRequestParams

  /**
    * Sets the overrequest parameters.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameters `facet.overrequest.count` and `facet.overrequest.ratio` will be set.
    *
    * @param count the count (must be positive)
    * @param ratio the ratio (must be positive)
    *
    * @throws IllegalArgumentException if `count` or `ratio` is non-positive
    *
    * @return the `this` instance
    */
  def overrequest(count: Int, ratio: Float): FacetFieldRequestParams

  /**
    * Sets the maximum number of threads for concurrent facet request execution.
    * Each invocation of this method replaces the previous value and value that was set by [[unlimitedThreads()]] method.
    *
    * The Apache Solr request parameter `facet.threads` will be set.
    *
    * @param number weather or not remove terms from facet counts but keep them in the index
    *
    * @throws IllegalArgumentException if `count` or `ratio` is non-positive
    *
    * @return the `this` instance
    */
  def limitThreads(number: Int): FacetFieldRequestParams

  /**
    * Sets unlimited number of threads for concurrent facet request execution.
    * Each invocation of this method replaces the previous value and value that was set by [[limitThreads()]] method.
    *
    * The Apache Solr request parameter `facet.threads` will be set to `-1`.
    *
    * @return the `this` instance
    */
  def unlimitedThreads(): FacetFieldRequestParams
}
