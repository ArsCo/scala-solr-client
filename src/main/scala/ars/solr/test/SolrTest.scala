package ars.solr.test

import ars.solr.client.ScalaSolrClient
import ars.solr.pool.SolrExecutor
import io.github.andrebeat.pool.Pool
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.scalatest.FlatSpec

/** Abstract Apache Solr embedded server test base class.
  * It uses [[FlatSpec]] ScalaTest syntax.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
trait SolrTest extends FlatSpec {

  /** The instance of Apache Solr executor. */
  val executor: SolrExecutor = newExecutor()

  /**
    * @return the Apache Solr core name.
    */
  protected def coreName(): String

  /**
    * @return the new instance of Apache Solr client
    */
  protected def newClient(): EmbeddedSolrServer

  /**
    * @return the new instance of connection pool.
    */
  protected def newConnectionPool(): Pool[ScalaSolrClient]

  /**
    * @return the new instance of Apache Solr executor.
    */
  protected def newExecutor(): SolrExecutor
}
