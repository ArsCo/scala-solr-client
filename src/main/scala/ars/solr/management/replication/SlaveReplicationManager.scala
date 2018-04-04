package ars.solr.management.replication

import ars.solr.management._
import ars.solr.management.replication.response._

/** Slave replication manager.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
trait SlaveReplicationManager extends ReplicationManager {

  /**
    * Forces the specified slave to fetch a copy of the index from its master.
    *
    * Executes fetch index request:
    * `http://slave_host:port/solr/core_name/replication?command=fetchindex`.
    *
    * @return the response
    */
  def fetchIndex(): Response[FetchIndexBody]

  /**
    * Aborts copying an index from a master to the specified slave.
    *
    * Executes abort fetch index request:
    * `http://slave_host:port/solr/core_name/replication?command=abortfetch`.
    *
    * @return the response
    */
  def abortFetch(): Response[AbortFetchBody]

  /**
    * Enables/disables the specified slave to poll for changes on the master.
    *
    * @param isEnable the `true` to enable and the `false` to disable
    * @return the response
    */
  def poll(isEnable: Boolean): Response[PollBody]

  /**
    * Enables the specified slave to poll for changes on the master.
    * This is the syntax sugar for `poll(true)` method invocation.
    *
    * @return the response
    */
  def enablePoll(): Response[PollBody] = poll(true)

  /**
    * Disables the specified slave from polling for changes on the master.
    * This is the syntax sugar for `poll(false)` method invocation.
    *
    * @return the response
    */
  def disablePoll(): Response[PollBody] = poll(false)
}
