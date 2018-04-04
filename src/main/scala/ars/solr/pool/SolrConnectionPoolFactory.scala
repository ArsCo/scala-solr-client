package ars.solr.pool

import ars.solr.client.ScalaSolrClient
import ars.solr.config.SolrCoreConfig
import ars.solr.pool.SolrConnectionFactory.{createLegacyNativeFactory, createLoadBalancedScalaFactory, createScalaFactory}
import io.github.andrebeat.pool.{Pool, ReferenceType}
import org.apache.solr.client.solrj.impl.HttpSolrServer

import scala.concurrent.duration.Duration

/** Solr connection pool factory.
  *
  * @param factory the solr connection factory
  * @param capacity the pool capacity
  * @param maxIdleTime the maximum amount of the time that objects are allowed to
  *        idle in the pool before being evicted
  * @param referenceType the reference type
  * @tparam Client the client type
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
class SolrConnectionPoolFactory[Client <: AnyRef](factory: SolrConnectionFactory[Client],
                                                  capacity: Int,
                                                  maxIdleTime: Duration = Duration.Inf,
                                                  referenceType: ReferenceType = ReferenceType.Strong) {



  private[this] val createMethod: () => Client = factory.create
  private[this] val closeMethod: Client => Unit = factory.close
  private[this] val isAliveMethod: Client => Boolean = factory.isAlive

  /**
    * Creates a new pool instance.
    *
    * @return the new pool instance
    */
  def create(): Pool[Client] = {
    Pool[Client](capacity, createMethod, referenceType, maxIdleTime, _ => (), closeMethod, isAliveMethod)
  }
}

object SolrConnectionPoolFactory {

  /**
    * Creates new instance of scala Solr client pool instance.
    *
    * @param config the solr configuration
    * @param capacity the pool capacity
    * @param maxIdleTime the maximum amount of the time that objects are allowed to
    *        idle in the pool before being evicted
    * @param referenceType the reference type
    * @return the new instance of scala Solr client pool
    */
  def createScalaPool(config: SolrCoreConfig,
                      capacity: Int,
                      maxIdleTime: Duration = Duration.Inf,
                      referenceType: ReferenceType = ReferenceType.Strong): Pool[ScalaSolrClient] = {
    new SolrConnectionPoolFactory(createScalaFactory(config), capacity,
      maxIdleTime, referenceType).create()
  }


  def createLoadBalancedScalaPool(configs: SolrCoreConfig*)(
                                  capacity: Int,
                                  maxIdleTime: Duration = Duration.Inf,
                                  referenceType: ReferenceType = ReferenceType.Strong): Pool[ScalaSolrClient] = {
    new SolrConnectionPoolFactory(createLoadBalancedScalaFactory(configs :_*), capacity,
      maxIdleTime, referenceType).create()
  }

  /**
    * Creates new instance of legacy Solr client pool instance.
    *
    * @param config the solr configuration
    * @param capacity the pool capacity
    * @param maxIdleTime the maximum amount of the time that objects are allowed to
    *        idle in the pool before being evicted
    * @param referenceType the reference type
    * @return the new instance of legacy Solr client pool
    */
  def createLegacyNativePool(config: SolrCoreConfig,
                             capacity: Int,
                             maxIdleTime: Duration = Duration.Inf,
                             referenceType: ReferenceType = ReferenceType.Strong): Pool[HttpSolrServer] = {
    new SolrConnectionPoolFactory(createLegacyNativeFactory(config), capacity,
      maxIdleTime, referenceType).create()
  }
}
