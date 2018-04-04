package ars.solr.test

import java.io.File

import ars.solr.client.ScalaSolrClient
import ars.solr.pool.{SolrConnectionPoolFactory, SolrExecutor}
import io.github.andrebeat.pool.Pool
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.core.CoreContainer.createAndLoad

/** Abstract Apache Solr embedded server test base class with a directory configuration.
  *
  * @param coreName the Apache Solr core name
  * @param solrHomeDir the Apache Solr home directory
  * @param solrConfigFile the Apache Solr configuration file
  * @param poolSize the Apache Solr connection pool size
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
class AbstractSolrDirectoryTest(
  protected override val coreName: String,
  protected val solrHomeDir: String,
  protected val solrConfigFile: File,
  protected val poolSize: Int = 10) extends SolrTest {

  override protected def newClient(): EmbeddedSolrServer = {
    val container = createAndLoad(solrHomeDir, solrConfigFile)
    new EmbeddedSolrServer(container, coreName)
  }

  override protected def newConnectionPool(): Pool[ScalaSolrClient] = {
    val connectionFactory = new ScalaSolrEmbeddedConnectionFactory(newClient)
    val poolFactory = new SolrConnectionPoolFactory(connectionFactory, poolSize)
    poolFactory.create()
  }

  override protected def newExecutor(): SolrExecutor = {
    new SolrExecutor(newConnectionPool())
  }
}
