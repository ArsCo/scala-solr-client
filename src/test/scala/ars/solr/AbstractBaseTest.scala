package ars.solr

import java.io.File

import com.typesafe.scalalogging.Logger
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.core.CoreContainer
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Inspectors, Matchers}

/**
  * Abstract base class for all tests.
  */
abstract class AbstractBaseTest extends FlatSpec
  with Matchers
  with Inspectors
  with MockFactory {

  val testSolrCoreName = "test-core"
  val testSolrHomePath = "src/test/resources/testdata/solr"

  def createTestSolrServer: EmbeddedSolrServer = {

    val homeDir = new File(testSolrHomePath)

    AbstractBaseTest.logger.error(s"HOME: ${homeDir.getAbsolutePath}")


    val container = new CoreContainer(testSolrHomePath)
    container.load()
    new EmbeddedSolrServer(container, testSolrCoreName)
  }




//  val solrConfigFileName = "solr.xml"







}


object AbstractBaseTest {
  private def logger = Logger[AbstractBaseTest]
}
