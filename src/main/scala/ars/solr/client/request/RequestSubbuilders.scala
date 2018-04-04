package ars.solr.client.request

import ars.precondition.RequireUtils.requireNotNull
import ars.solr.client.request.facet.FacetRequestBuilder
import ars.solr.client.request.highlight.HighlightRequestBuilder

/**
  *
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.0
  */
trait RequestSubbuilders {
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
