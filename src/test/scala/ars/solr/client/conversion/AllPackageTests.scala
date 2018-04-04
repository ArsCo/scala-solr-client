package ars.solr.client.conversion

import org.scalatest.Suites


/** All tests for package `ars.solr.client.conversion`.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
class AllPackageTests extends Suites(
  new DefaultValueTest,

  new CanConvertToTest,
  new AbstractCanConvertToTest,
  new ConverterSeqCanConvertToTest,

  new ConverterTest,

  new ConversionAPITest
)
