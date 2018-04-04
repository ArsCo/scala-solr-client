package ars.solr.client

import ars.solr.AbstractBaseTest
import ars.solr.config.SolrCoreConfig
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.impl.{ConcurrentUpdateSolrServer, HttpSolrServer, LBHttpSolrServer}
import org.apache.solr.common.SolrInputDocument

/** Tests for [[ScalaSolrClient]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
class ScalaSolrClientTest extends AbstractBaseTest {

  val core = new SolrCoreConfig("http://url/", "goodCoreName")
  val core2 = new SolrCoreConfig("http://url2/", "goodCoreName2")

  // ---------- OBJECT ----------

  "ScalaSolrClient.createClient" must "validate args" in {
    intercept[IllegalArgumentException] {
      ScalaSolrClient.createClient(null)
    }
  }

  it must "creates new instance of ScalaSolrClient with HttpSolrServer" in {
    val c = ScalaSolrClient.createClient(core)
    assert(c != null)

    val n = c.nativeClient
    assert(n != null)
    assert(n.isInstanceOf[HttpSolrServer])
  }

  "ScalaSolrClient.createLoadBalancedClient" must "validate args" in {
    intercept[IllegalArgumentException] {
      ScalaSolrClient.createLoadBalancedClient(null)
    }

    intercept[IllegalArgumentException] {
      ScalaSolrClient.createLoadBalancedClient(Seq[SolrCoreConfig]() :_*)
    }
  }

  it must "creates new instance of ScalaSolrClient with LBHttpSolrServer" in {
    val c = ScalaSolrClient.createLoadBalancedClient(core, core2)
    assert(c != null)

    val n = c.nativeClient
    assert(n != null)
    assert(n.isInstanceOf[LBHttpSolrServer])
    // No need to test which cores in LBHttpSolrServer
  }

  "ScalaSolrClient.createConcurrentUpdateClient" must "validate args" in {
    intercept[IllegalArgumentException] {
      ScalaSolrClient.createConcurrentUpdateClient(null, 3000, 10)
    }

    intercept[IllegalArgumentException] {
      ScalaSolrClient.createConcurrentUpdateClient(core, -1, 10)
    }

    intercept[IllegalArgumentException] {
      ScalaSolrClient.createConcurrentUpdateClient(core, 0, 10)
    }

    intercept[IllegalArgumentException] {
      ScalaSolrClient.createConcurrentUpdateClient(core, 3000, -1)
    }

    intercept[IllegalArgumentException] {
      ScalaSolrClient.createConcurrentUpdateClient(core, 3000, 0)
    }
  }

  it must "creates new instance of ScalaSolrClient with ConcurrentUpdateSolrServer" in {
    val c = ScalaSolrClient.createConcurrentUpdateClient(core, 3000, 10)
    assert(c != null)

    val n = c.nativeClient
    assert(n != null)
    assert(n.isInstanceOf[ConcurrentUpdateSolrServer])
    // No need to test other params of ConcurrentUpdateSolrServer
  }

  // ---------- CLASS ----------

  private def createNoActionMockClient: ScalaSolrClient = {
    val mockSolrServer = mock[SolrServer]
    new ScalaSolrClient(mockSolrServer)
  }

  "ScalaSolrClient" must "validate constructor args" in {
    intercept[IllegalArgumentException] {
      new ScalaSolrClient(null)
    }
  }

  "add(SolrInputDocument, Int)" must "validate args" in {
    val client = createNoActionMockClient

    client.add(new SolrInputDocument(), 4)
    client.add(new SolrInputDocument(), -1)

    intercept[IllegalArgumentException] {
      client.add(null, 2)
    }

    intercept[IllegalArgumentException] {
      client.add(null, -5)
    }

    intercept[IllegalArgumentException] {
      client.add(null, 0)
    }
  }

  "add(SolrInputDocument, Int)" must "call underlying clint method" in {
    val doc = new SolrInputDocument()

    val mockSolrServer = mock[SolrServer]
    (mockSolrServer.add(_: SolrInputDocument, _: Int)).expects(doc, 4)

    val client = new ScalaSolrClient(mockSolrServer)

    client.add(doc, 4)
  }

}
