package ars.solr.pool

import ars.solr.config.SolrCoreConfig
import org.apache.solr.client.solrj.impl.HttpSolrServer

/** Native solr client (connection) factory.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
class HttpSolrConnectionFactory(config: SolrCoreConfig) extends SolrConnectionFactory[HttpSolrServer](config) {

//  protected override type Client = HttpSolrServer

  override protected def createConnection(): HttpSolrServer = {

    // TODO LEGACY CODE FOR SOLR 4.9 (REMOVE WHEN POSSIBLE)
    new HttpSolrServer(config.coreUrl)

    // TODO NEW CODE FOR SOLR 6.x
//    new HttpSolrServer.Builder()
//      .allowCompression(true)
//      .withBaseSolrUrl(config.coreUrl)
//      .build()
  }

  override protected def closeConnection(connection: HttpSolrServer): Unit = connection.shutdown()
  override protected def isConnectionAlive(connection: HttpSolrServer): Boolean = true
}
