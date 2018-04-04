package ars.solr.pool

import ars.solr.client.ScalaSolrClient
import ars.solr.config.SolrCoreConfig
import org.apache.solr.client.solrj.impl.HttpSolrServer

/** Scala solr connection factory.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
class ScalaSolrConnectionFactory(config: SolrCoreConfig) extends SolrConnectionFactory[ScalaSolrClient](config) {

//  protected override type Client = SolrClient

  override protected def createConnection(): ScalaSolrClient = {
    new ScalaSolrClient(new HttpSolrServer(config.coreUrl))
  }
  override protected def closeConnection(connection: ScalaSolrClient): Unit = connection.shutdown()
  override protected def isConnectionAlive(connection: ScalaSolrClient): Boolean = true
}

object ScalaSolrConnectionFactory {
  def apply(url: String, coreName: String): ScalaSolrConnectionFactory = {
    new ScalaSolrConnectionFactory(SolrCoreConfig(url, coreName))
  }
}
