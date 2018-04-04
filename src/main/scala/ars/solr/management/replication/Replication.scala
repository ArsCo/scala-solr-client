package ars.solr.management.replication

import ars.solr.config.SolrCoreConfig

/** Apache Solr replication factory object.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
object Replication {

  /**
    * Creates master replication manager.
    *
    * @param core the Apache Solr core configuration
    * @return the new instance of master replication manager
    */
  def createMaster(core: SolrCoreConfig): MasterReplicationManager =
    new DefaultMasterReplicationManager(core)

  /**
    * Creates slave replication manager.
    *
    * @param core the Apache Solr core configuration
    * @return the new instance of master slave manager
    */
  def createSlave(core: SolrCoreConfig): SlaveReplicationManager =
    new DefaultSlaveReplicationManager(core)
}
