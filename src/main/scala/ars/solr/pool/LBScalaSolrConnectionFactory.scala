package ars.solr.pool

import ars.solr.client.ScalaSolrClient
import ars.solr.config.SolrCoreConfig
import org.apache.solr.client.solrj.impl.LBHttpSolrServer

/**
  * Created by ars on 26/02/2017.
  */
class LBScalaSolrConnectionFactory(configs: SolrCoreConfig*) extends ScalaSolrConnectionFactory(configs.head) { // TODO

  override protected def createConnection(): ScalaSolrClient = {
    val coreUrls = configs.map(_.coreUrl)
    new ScalaSolrClient(new LBHttpSolrServer(coreUrls :_*))
  }
}
