package ars.solr.test

import ars.solr.client.ScalaSolrClient
import ars.solr.config.SolrCoreConfig
import ars.solr.pool.ScalaSolrConnectionFactory
//import jp.sf.amateras.solr.scala.SolrClient
import org.apache.solr.client.solrj.SolrServer

/**
  * Scala Apache Solr embedded connection factory.
  *
  * @param server
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
class ScalaSolrEmbeddedConnectionFactory(server: () => SolrServer)
  extends ScalaSolrConnectionFactory(new SolrCoreConfig("111", "222"))  {
  protected override def createConnection(): ScalaSolrClient = {
    new ScalaSolrClient(server())
  }
}
