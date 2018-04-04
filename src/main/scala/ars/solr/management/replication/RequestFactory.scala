package ars.solr.management.replication

import ars.solr.config.SolrCoreConfig
import ars.solr.util.HttpUtils._

import scalaj.http.{Http, HttpRequest}

/** Apache Solr replication request factory.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
class RequestFactory(core: SolrCoreConfig) {

  protected val baseUrl: String = core.coreUrl + "/replication"

  /**
    * Creates base command request object.
    *
    * @param command the command name
    * @return the request object
    */
  def baseRequest(command: String): HttpRequest = {
    Http(baseUrl)
      .param("command", command)
      .param("wt", "json")
  }

  /**
    * Creates file list command request object.
    *
    * @param generation the generation
    * @return the request object
    */
  def fileListRequest(generation: Long): HttpRequest = {
    baseRequest("filelist")
      .param("generation", generation.toString)
  }

  /**
    * Creates replication request.
    *
    * @param isEnable whether enable or disable replication
    * @return the request object
    */
  def replicationRequest(isEnable: Boolean): HttpRequest = {
    baseRequest(replicationStatusCommandName(isEnable))
  }

  /**
    * Creates backup request.
    *
    * @param name the name
    * @param location the location
    * @param numberToKeep the number of keep requests
    * @return the request object
    */
  def createBackupRequest(name: Option[String] = None, location: Option[String] = None,
                          numberToKeep: Option[Int] = None): HttpRequest = {
    baseRequest("backup")
      .addOptParam("name", name)
      .addOptParam("location", location)
      .addOptParam("numberToKeep", numberToKeep)
  }

  /**
    * Creates delete backup request.
    *
    * @param name the name
    * @param location the location
    * @return the request object
    */
  def deleteBackupRequest(name: Option[String] = None,
                          location: Option[String] = None): HttpRequest = {
    baseRequest("deletebackup")
      .addOptParam("name", name)
      .addOptParam("location", location)
  }

  /**
    * Creates polling request.
    *
    * @param isEnable whether enable or disable polling
    * @return the request object
    */
  def pollRequest(isEnable: Boolean): HttpRequest = {
    baseRequest(pollStatusCommandName(isEnable))
  }

  protected def replicationStatusCommandName(isEnable: Boolean) = enable(isEnable, "replication")
  protected def pollStatusCommandName(isEnable: Boolean) = enable(isEnable, "poll")

  protected def enable(isEnable: Boolean, name: String) =
    (if (isEnable) "enable" else "disable") + name
}
