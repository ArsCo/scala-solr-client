package ars.solr.client

import org.scalatest.Suites

/** All tests for package `ars.solr.client`.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
class AllPackageTests extends Suites(
  new packageTest,

  new QueryCallbackTest,
  new MappingErrorModeTest,
  new QueryResultTest,
  new ScalaSolrClientTest
)
