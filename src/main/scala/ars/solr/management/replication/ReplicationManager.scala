package ars.solr.management.replication

import ars.solr.management.replication.response.{DetailsBody, FileListBody, IndexBody, Response}

/** Replication manager.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
trait ReplicationManager {

  /**
    * Returns the version of the latest replicatable index on the specified master or slave.
    *
    * Executes index version request:
    * `http://host:port/solr/core_name/replication?command=indexversion`.
    *
    * @return the response
    */
  def indexVersion(): Response[IndexBody]

  /**
    * Retrieves a list of Lucene files present in the specified host's index.
    * You can discover the generation number of the index by running the [[indexVersion()]] method.
    *
    * Executes file list request:
    * `http://host:port/solr/core_name/replication?command=filelist&generation=<...>`.
    *
    * @param generation the generation
    * @return the response
    */
  @throws[IllegalArgumentException]("if any argument is illegal")
  def fileList(generation: Long): Response[FileListBody]

  /**
    * Retrieves configuration details and current status.
    *
    * @return the response
    */
  def details(): Response[DetailsBody]
}
