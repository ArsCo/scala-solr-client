package ars.solr.client.request

import ars.solr.client.QueryResult
import org.apache.solr.client.solrj.SolrRequest.METHOD
import org.apache.solr.client.solrj.SolrRequest.METHOD.GET

/**
  *
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.0
  */
trait RequestExecution {

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
}
