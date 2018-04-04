package ars.solr.client.request

import org.apache.solr.client.solrj.SolrQuery.SortClause




/** Apache Solr Scala common request params.
  *
  * It provides wrapper methods to add raw Apache Solr params:
  * - `defType` ([[CommonRequestParams.queryParser]]) +
  * - `fl` ([[CommonRequestParams.addFields()]])
  * - `start` ([[CommonRequestParams.offset()]])
  * - `rows` ([[CommonRequestParams.limit()]])
  * - `sort` ([[CommonRequestParams.sorts()]]) +
  * - `qt` ([[CommonRequestParams.handler()]])
  * - `timeAllowed` ([[CommonRequestParams.timeAllowed()]])
  * - `fq` ([[CommonRequestParams.filterQuery()]]) +
  * - others ([[CommonRequestParams.set()]])
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.5
  */
trait CommonRequestParams {


  def queryParser(parser: SolrQueryParser)

  /**
    * Adds fields to the request.
    * Each invocation of this method appends fields to the request.
    *
    * The Apache Solr request parameter `fl` will be added for each field.
    *
    * ```Example:```
    * {{{
    *   addFields(AllFields, ScorePseudoField)
    *   addFields("field_1")
    *   addFields("field_1", "field_2")
    *   addFields("function(field_1, field_2)", "field_2")
    * }}}
    *
    * @param fields the fields (must be non-blank sequence of non-blank strings)
    *
    * @throws IllegalArgumentException if `fields` is blank or contains blank string
    *
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if `fields` is blank or contains blank string")
  def addFields(fields: String*): CommonRequestParams


  /**
    * Sets the offset of rows that will be returned by the request.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameter `start` will be set.
    *
    * @param offset the offset (must be non-negative)
    *
    * @throws IllegalArgumentException if `offset` is negative
    *
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if `offset` is negative")
  def offset(offset: Int): CommonRequestParams


  /**
    * Sets the maximum number of rows that will be returned.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameter `rows` will be set.
    *
    * @param limit the limit (must be non-negative)
    *
    * @throws IllegalArgumentException if `limit` is negative
    *
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if `limit` is negative")
  def limit(limit: Int): CommonRequestParams


  /**
    * Sets the sorting clauses to the request.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameter `sort` will be set.
    *
    * ```Example:```
    * {{{
    *   import ars.solr.client.request.implicits._
    *
    *   val r: CommonRequestParams = ...
    *
    *   r.sortBy(asc("field_1"))
    *   r.sortBy(desc("field_2"))
    *   r.sortBy(asc("field_1"), desc("field_2"))
    *   r.sortBy(asc(ScorePseudoField))
    *   r.sortBy(desc(ScorePseudoField))
    * }}}
    *
    * @param clauses the sorting clauses (must be non-blank sequence of non-null values)
    *
    * @throws IllegalArgumentException if `limit` is negative
    *
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if `limit` is negative")
  def sortBy(clauses: SortClause*): CommonRequestParams

  /**
    * Sets the request handler (`qt` parameter).
    * If this is not set, then the default of `/select` is assumed.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameter `qt` will be set.
    *
    * @param handler the request handler
    *
    * @throws IllegalArgumentException if `handler` is blank
    *
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if `handler` is blank")
  def handler(handler: String): CommonRequestParams


  /**
    * Sets the maximum time allowed for this request. If the request takes more time
    * than the specified milliseconds, a timeout occurs and partial (or no)
    * results may be returned.
    * Each invocation of this method replaces the previous value.
    *
    * The Apache Solr request parameter `timeAllowed` will be set.
    *
    * @param milliseconds the time in milliseconds
    *
    * @throws IllegalArgumentException
    *
    * @return the `this` instance
    */
  def timeAllowed(milliseconds: Int): CommonRequestParams
//  {
//    requireNonNegative(milliseconds, "milliseconds")
//
//    solrQuery.setTimeAllowed(milliseconds)
//    this
//  }


  /**
    * Sets the request filter queries (`fq` parameter).
    * Documents will only be included in the result if they are in the intersection of the document sets resulting from each instance of the parameter.
    *
    *
    * @param queries the queries
    *
    * @return the `this` instance
    */
  def filterQuery(queries: String*): CommonRequestParams
//  {
//    requireNotNull(queries, "query")
//    requireAllNotBlank(queries, "query")
//
//    solrQuery.setFilterQueries(queries :_*)
//    this
//  }


  /**
    * Sets a custom request parameter.
    *
    * @param name the parameter name
    * @param values the parameter values
    *
    * @return the `this` instance
    */
  def set(name: String, values: String*): CommonRequestParams


//  = {
//    requireNotBlank(name, "name")
//    requireNotNull(values, "values")
//    requireNotBlank(values, "values")
//
//    solrQuery.setParam(name, values :_*)
//    this
//  }

  /**
    * Sets a custom boolean request parameter.
    *
    * @param name the parameter name
    * @param value the boolean parameter value
    *
    * @return the `this` instance
    */
  def set(name: String, value: Boolean): CommonRequestParams
//
//  = {
//    requireNotBlank(name, "name")
//
//    solrQuery.setParam(name, value)
//    this
//  }

//  /**
//    * Removes a custom request parameter.
//    *
//    * @param name the parameter name
//    *
//    * @return the `this` instance
//    */
//  def remove(name: String): RequestBuilder = {
//    solrQuery.setParam(name, null)
//    this
//  }

  /**
    * Whether or not include score.
    *
    * @param isIncludeScore if `true` then score will be included, otherwise -- not included
    *
    * @return the `this` instance
    */
  def includeScore(isIncludeScore: Boolean): CommonRequestParams
//  = {
//    solrQuery.setIncludeScore(isIncludeScore)
//    this
//  }

  /**
    * Whether or not show debug information.
    *
    * @param isShow if `true` then debug information will be shown, otherwise -- not shown
    *
    * @return the `this` instance
    */
  def showDebug(isShow: Boolean): CommonRequestParams
//  = {
//    solrQuery.setShowDebugInfo(isShow)
//    this
//  }


}
