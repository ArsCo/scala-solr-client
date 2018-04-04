package ars.solr.config

import ars.solr.AbstractBaseTest

/**
  * Created by ars on 23/01/2017.
  */
class SolrCoreConfigTest extends AbstractBaseTest {

  val testServerUrl = "http://test.url"
  override val testSolrCoreName = "test-core"
  val testCoreUrl = s"$testServerUrl/$testSolrCoreName"

  "SolrCoreConfig" must "validate args" in {
    intercept[IllegalArgumentException] {
      SolrCoreConfig(null, "core")
    }

    intercept[IllegalArgumentException] {
      SolrCoreConfig("", "core")
    }

    intercept[IllegalArgumentException] {
      SolrCoreConfig("  ", "core")
    }

    intercept[IllegalArgumentException] {
      SolrCoreConfig("url", null)
    }

    intercept[IllegalArgumentException] {
      SolrCoreConfig("url", "")
    }

    intercept[IllegalArgumentException] {
      SolrCoreConfig("url", " ")
    }

    SolrCoreConfig("url", "core")
    SolrCoreConfig("url/", "core")
  }

  it must "be valid" in {
    val s = SolrCoreConfig(testServerUrl, testSolrCoreName)
    assert(s != null)
    assertResult(testServerUrl)(s.serverUrl)
    assertResult(testSolrCoreName)(s.coreName)
    assertResult(testServerUrl + "/" + testSolrCoreName)(s.coreUrl)

    val s1 = SolrCoreConfig(testServerUrl + "/", testSolrCoreName)
    assert(s1 != null)
    assertResult(testServerUrl)(s1.serverUrl)
    assertResult(testSolrCoreName)(s1.coreName)
    assertResult(testServerUrl + "/" + testSolrCoreName)(s1.coreUrl)
  }

  it must "have apply method" in {
    val con = new SolrCoreConfig(testServerUrl, testSolrCoreName)
    val app = SolrCoreConfig(testServerUrl, testSolrCoreName)

    assert(con != null)
    assert(app != null)
    assert(con == app)
  }

  it must "have equals and hashCode" in {
    val s1 = SolrCoreConfig(testServerUrl, testSolrCoreName)
    val s2 = SolrCoreConfig(testServerUrl, testSolrCoreName)

    assert(s1 ne s2)
    assert(s1.hashCode() == s2.hashCode())
    assert(s1 equals s2)
  }

  "parseCoreUrl" must "parse valid core url to (serverUrl, coreName)" in {


    assertResult((testServerUrl, testSolrCoreName)) {
      SolrCoreConfig.parseCoreUrl(testCoreUrl)
    }
  }

  it must "throw IAE if there's bad coreUrl format" in {
    intercept[IllegalArgumentException] {
      SolrCoreConfig.parseCoreUrl("sdfasdfsdf")
    }

    intercept[IllegalArgumentException] {
      SolrCoreConfig.parseCoreUrl("/fff")
    }

    intercept[IllegalArgumentException] {
      SolrCoreConfig.parseCoreUrl("fff/")
    }
  }

  "apply(coreUrl: String)" must "validate arg" in {
    intercept[IllegalArgumentException] {
      SolrCoreConfig(null)
    }

    intercept[IllegalArgumentException] {
      SolrCoreConfig("   ")
    }
  }

  it must "valid instantiate SolrCoreConfig" in {
    val c = SolrCoreConfig(testCoreUrl)
    assert(c != null)
    assertResult(c.serverUrl)(testServerUrl)
    assertResult(c.coreUrl)(testCoreUrl)
    assertResult(c.coreName)(testSolrCoreName)

  }

  "toString" must "returns coreName" in {
    val c = SolrCoreConfig(testCoreUrl)
    assertResult(testCoreUrl)(c.toString)
  }
}
