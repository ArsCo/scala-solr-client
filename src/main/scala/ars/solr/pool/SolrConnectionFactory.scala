package ars.solr.pool

import ars.solr.client.ScalaSolrClient
import ars.solr.config.SolrCoreConfig
import ars.solr.util.LogUtils
import org.apache.solr.client.solrj.impl.HttpSolrServer

/** Solr connection factory.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
trait ConnectionFactory[Client] {

  /**
    * Creates new instance of connection.
    *
    * @return the new instance of connection
    */
  def create(): Client

  /**
    * Close already created instance of connection.
    *
    * @param connection the closing connection
    */
  def close(connection: Client)

  /**
    * Checks that connection is alive.
    *
    * @param connection the checking connection
    *
    * @return `true` if connection is alive and `false` otherwise
    */
  def isAlive(connection: Client): Boolean
}


/** Solr connection factory.
  *
  * @param config the solr core configuration
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
abstract class SolrConnectionFactory[Client <: AnyRef](config: SolrCoreConfig) extends ConnectionFactory[Client] { // TODO
  import SolrConnectionFactory._
  import logger._


  def create(): Client = {
    log(s"Create new connection for '${config.coreName}'.") {
      protect(s"Connection has not been created for '${config.coreName}'.") {
        createConnection()
      }
    }
  }


  def close(connection: Client) {
    log(s"Close connection for '${config.coreName}'.") {
      protect(s"Connection has not been closed for '${config.coreName}'.") {
        closeConnection(connection)
      }
     }
  }


  def isAlive(connection: Client): Boolean = {
    log(s"Try to validate is connection alive.") {
      protect {
        isConnectionAlive(connection)
      }
    }
  }

  protected def createConnection(): Client
  protected def closeConnection(connection: Client): Unit
  protected def isConnectionAlive(connection: Client): Boolean
}

object SolrConnectionFactory {

  val logger = new LogUtils[SolrConnectionFactory[_]]()

  def createScalaFactory(config: SolrCoreConfig): SolrConnectionFactory[ScalaSolrClient] = {
    new ScalaSolrConnectionFactory(config)
  }

  def createLegacyNativeFactory(config: SolrCoreConfig): SolrConnectionFactory[HttpSolrServer] = {
    new HttpSolrConnectionFactory(config)
  }

  def createLoadBalancedScalaFactory(configs: SolrCoreConfig*): SolrConnectionFactory[ScalaSolrClient] = {
    new LBScalaSolrConnectionFactory(configs :_*)
  }
}





