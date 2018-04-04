package ars.solr.client.request

import ars.precondition.RequireUtils._
import ars.solr.client._
import ars.solr.client.request.facet.FacetRequestBuilder
import ars.solr.client.request.highlight.HighlightRequestBuilder
import org.apache.lucene.search.Query
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrQuery.SortClause
import org.apache.solr.client.solrj.SolrRequest.METHOD
import org.apache.solr.client.solrj.SolrRequest.METHOD.GET

import scala.collection.JavaConversions._

/** Apache Solr Scala request builder.
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
class RequestBuilder(query: String)(implicit client: ScalaSolrClient) {
  requireNotNull(client, "client")
  requireNotBlank(query, "query")

  private val solrQuery = new SolrQuery(query)

  /**
    * Adds fields to the request. Scala proxy for [[SolrQuery.setFields(*]].
    * To request default fields (from Solr configuration) use [[defaultFields()]] method.
    *
    * @param fields the fields (must not be null or empty sequence)
    *
    * @return the `this` instance
    */
  def fields(fields: String*): RequestBuilder = {
    requireNotBlank(fields, "fields")
    requireAllNotBlank(fields, "fields")

    solrQuery.setFields(fields :_*)
    this
  }

  /**
    * Removes all explicit fields from the request and uses the default fields
    * (from the request handler configuration).
    *
    * @return the `this` instance
    */
  def defaultFields(): RequestBuilder = {
    solrQuery.setFields(null)
    this
  }

  /**
    * Sets the offset of rows that will be returned by the request.
    * The proxy method for [[SolrQuery.setStart()]].
    * To execute request with default offset (from Solr configuration) use [[defaultOffset()]] method.
    *
    * @param offset the number
    *
    * @return the `this` instance
    */
  def offset(offset: Int): RequestBuilder = {
    requireNonNegative(offset, "offset")

    solrQuery.setStart(offset)
    this
  }

  /**
    * Removes explicit offset (`start` request parameter) from the request and uses the default offset
    * (from the request handler configuration).
    *
    * @return the `this` instance
    */
  def defaultOffset(): RequestBuilder = {
    solrQuery.setStart(null)
    this
  }

  /**
    * Sets the maximum number of rows that will be returned.
    * The proxy method for [[SolrQuery.setRows()]].
    * To execute request with default limit (from Solr configuration) use [[defaultLimit()]] method.
    *
    * @param limit the limit
    *
    * @return the `this` instance
    */
  def limit(limit: Int): RequestBuilder = {
    requireNonNegative(limit, "limit")

    solrQuery.setRows(limit)
    this
  }

  /**
    * Removes explicit limit (`rows` request parameter) from the request and uses the default limit
    * (from the request handler configuration).
    *
    * @return the `this` instance
    */
  def defaultLimit(): RequestBuilder = {
    solrQuery.setRows(null)
    this
  }

  /**
    * Sets the sorting clauses to the request.
    *
    * Example:
    * {{{
    *   import SortClause._
    *
    *   val queryBuilder: QueryBuilder = ...
    *
    *   queryBuilder.sorts(asc("field_1"), desc("field_2"))
    * }}}
    *
    * @param clauses the sorting clauses
    *
    * @return the `this` instance
    */
  def sorts(clauses: SortClause*): RequestBuilder = {
    requireNotBlank(clauses, "clauses")
    requireAllNotNull(clauses, "clauses")

    solrQuery.setSorts(clauses)
    this
  }

  /**
    * Removes explicit sort clauses from the request and uses the defaults
    * (from the request handler configuration).
    *
    * @return the `this` instance
    */
  def defaultSorts(): RequestBuilder = {
    solrQuery.clearSorts()
    this
  }

  /**
    * Sets the request handler (`qt` parameter).
    * If this is not set, then the default of `/select` is assumed.
    *
    * @param handler the request handler
    *
    * @return the `this` instance
    */
  def handler(handler: String): RequestBuilder = {
    requireNotBlank(handler, "handler")

    solrQuery.setRequestHandler(handler)
    this
  }

  /**
    * Sets the default (`/select`) request handler (`qt` parameter).
    *
    * @return the `this` instance
    */
  def selectHandler(): RequestBuilder = {
    solrQuery.setRequestHandler(null)
    this
  }

  /**
    * Sets the maximum time allowed for this request. If the request takes more time
    * than the specified milliseconds, a timeout occurs and partial (or no)
    * results may be returned.
    *
    * @param milliseconds the time in milliseconds
    *
    * @return the `this` instance
    */
  def timeAllowed(milliseconds: Int): RequestBuilder = {
    requireNonNegative(milliseconds, "milliseconds")

    solrQuery.setTimeAllowed(milliseconds)
    this
  }

  /**
    * Removes explicit allowed request time from the request and uses the default
    * (from the request handler configuration).
    *
    * @return the `this` instance
    */
  def defaultTimeAllowed(): RequestBuilder = {
    solrQuery.setTimeAllowed(null)
    this
  }

  /**
    * Sets the request filter queries (`fq` parameter).
    *
    * @param queries the queries
    *
    * @return the `this` instance
    */
  def filterQuery(queries: String*): RequestBuilder = {
    requireNotNull(queries, "query")
    requireAllNotBlank(queries, "query")

    solrQuery.setFilterQueries(queries :_*)
    this
  }

  /**
    * Removes explicit filter queries from the request and uses the default
    * (from the request handler configuration).
    *
    * @return the `this` instance
    */
  def defaultFilterQuery(): RequestBuilder = {
    solrQuery.getFilterQueries.foreach(solrQuery.removeFilterQuery)
    this
  }

  /**
    * Sets a custom request parameter.
    *
    * @param name the parameter name
    * @param values the parameter values
    *
    * @return the `this` instance
    */
  def set(name: String, values: String*): RequestBuilder = {
    requireNotBlank(name, "name")
    requireNotNull(values, "values")
    requireNotBlank(values, "values")

    solrQuery.setParam(name, values :_*)
    this
  }

  /**
    * Sets a custom boolean request parameter.
    *
    * @param name the parameter name
    * @param value the boolean parameter value
    *
    * @return the `this` instance
    */
  def set(name: String, value: Boolean): RequestBuilder = {
    requireNotBlank(name, "name")

    solrQuery.setParam(name, value)
    this
  }

  /**
    * Removes a custom request parameter.
    *
    * @param name the parameter name
    *
    * @return the `this` instance
    */
  def remove(name: String): RequestBuilder = {
    solrQuery.setParam(name, null)
    this
  }

  /**
    * Whether or not include score.
    *
    * @param isIncludeScore if `true` then score will be included, otherwise -- not included
    *
    * @return the `this` instance
    */
  def includeScore(isIncludeScore: Boolean): RequestBuilder = {
    solrQuery.setIncludeScore(isIncludeScore)
    this
  }

  /**
    * Whether or not show debug information.
    *
    * @param isShow if `true` then debug information will be shown, otherwise -- not shown
    *
    * @return the `this` instance
    */
  def showDebug(isShow: Boolean): RequestBuilder = {
    solrQuery.setShowDebugInfo(isShow)
    this
  }

  /**
    * Executes request.
    * This is the terminating operation of the request builder.
    *
    * @param method the HTTP method to use for the request, such as GET or POST
    *
    * @return the result of the request
    */
  def execute(method: METHOD = GET): QueryResult = client.query(solrQuery, method)

  /**
    * The enter point to the request stream processing.
    *
    * @return the stream processing request builder
    */
  def stream(): StreamRequestBuilder = new StreamRequestBuilder(client, solrQuery)

  // ---------- Sub builders ---------

  /**
    * Sets the highlighting parameters.
    *
    * @param builder the highlighting builder
    *
    * @return the `this` instance
    */
  def highlights(builder: (HighlightRequestBuilder) => Unit): RequestBuilder = {
    requireNotNull(builder, "builder")

    builder(new HighlightRequestBuilder(solrQuery))
    this
  }

  /**
    * Sets the facets parameters.
    *
    * @param builder the facets builder
    *
    * @return the `this` instance
    */
  def facets(builder: (FacetRequestBuilder) => Unit): RequestBuilder = {
    requireNotNull(builder, "builder")

    builder(new FacetRequestBuilder(solrQuery))
    this
  }

  /**
    * Sets the terms parameters.
    *
    * @param builder the terms builder
    *
    * @return the `this` instance
    */
  def terms(builder: (TermsRequestBuilder) => Unit): RequestBuilder = {
    builder(new TermsRequestBuilder(solrQuery))
    this
  }

  /**
    * Sets the statistics parameters.
    *
    * @param builder the statistics builder
    *
    * @return the `this` instance
    */
  def statistics(builder: (StatisticsRequestBuilder) => Unit): RequestBuilder = {
    builder(new StatisticsRequestBuilder(solrQuery))
    this
  }
}

object RequestBuilder {

  /**
    * @param queryString the query string
    * @param client the client
    *
    * @return the new instance of request builder
    */
  def apply(queryString: String)(implicit client: ScalaSolrClient): RequestBuilder =
    new RequestBuilder(queryString)

  /**
    * @param query the query
    * @param client the client
    *
    * @return the new instance of request builder
    */
  def apply(query: Query)(implicit client: ScalaSolrClient): RequestBuilder =
    new RequestBuilder(query.toString)
}
