package ars.solr.management.replication

import ars.precondition.RequireUtils._
import ars.solr.config.SolrCoreConfig
import JsonReplicationRequester._
import ars.solr.management.replication.response._
import org.json4s.JValue

/** The default implementation of [[ReplicationManager]].
  *
  * @param core the Apache Solr core configuration
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
abstract sealed class DefaultReplicationManager(val core: SolrCoreConfig) extends ReplicationManager {
  requireNotNull(core, "core")

  protected val requestFactory = new RequestFactory(core)

  import requestFactory._

  /** @inheritdoc */
  def indexVersion(): Response[IndexBody] = {
    executeBaseCommandAsJson("indexversion")(IndexBody.fromJson)
  }

  /** @inheritdoc */
  def fileList(generation: Long): Response[FileListBody] = {
    executeAsJson(fileListRequest(generation))(FileListBody.fromJson)
  }

  /** @inheritdoc */
  def details(): Response[DetailsBody] = {
    executeBaseCommandAsJson("details")(DetailsBody.fromJson)
  }

  protected def executeBaseCommandAsJson[T <: AnyRef](commandName: String)
                                 (bodyParser: (JValue) => T): Response[T] = {

    requireNotBlank(commandName, "commandName")
    requireNotNull(bodyParser, "bodyParser")

    executeAsJson(baseRequest(commandName))(bodyParser)
  }
}

/** The default implementation of [[ReplicationManager]].
  *
  * @param core the Apache Solr core configuration
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
class DefaultMasterReplicationManager(core: SolrCoreConfig) extends DefaultReplicationManager(core)
                                                            with MasterReplicationManager {
  import requestFactory._

  /** @inheritdoc */
  def replication(enable: Boolean): Header = {
    executeAsJson(replicationRequest(enable))(identity).header
  }

  /** @inheritdoc */
  def createBackup(name: Option[String] = None,
                   location: Option[String] = None,
                   numberToKeep: Option[Int] = None): Response[CreateBackupBody] = {
    validateCreateBackupArgs(name, location, numberToKeep)

    executeAsJson(createBackupRequest(name, location, numberToKeep))(CreateBackupBody.fromJson)
  }

  /** @inheritdoc */
  def deleteBackup(name: Option[String] = None,
                   location: Option[String] = None): Response[DeleteBackupBody] = {
    validateDeleteBackupArgs(name, location)

    executeAsJson(deleteBackupRequest(name, location))(DeleteBackupBody.fromJson)
  }

  private def validateCreateBackupArgs(name: Option[String],
                                       location: Option[String],
                                       numberToKeep: Option[Int]): Unit ={

    validateNotBlankOpional(name, "name")
    validateNotBlankOpional(location, "location")

    requireNotNull(numberToKeep, "numberToKeep")
    optional(numberToKeep) { requirePositive(_, "location")}
  }

  private def validateDeleteBackupArgs(name: Option[String], location: Option[String]): Unit = {
    validateNotBlankOpional(name, "name")
    validateNotBlankOpional(location, "location")
  }

  private def validateNotBlankOpional(value: Option[String], name: String): Unit ={
    requireNotNull(value, name)
    optional(value) { requireNotBlank(_, name) }
  }
}

/** The default implementation of [[SlaveReplicationManager]].
  *
  * @param core the Apache Solr core configuration
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
class DefaultSlaveReplicationManager(core: SolrCoreConfig) extends DefaultReplicationManager(core)
                                                           with SlaveReplicationManager {
  import requestFactory._

  /** @inheritdoc */
  def fetchIndex(): Response[FetchIndexBody] = {
    executeBaseCommandAsJson("fetchindex")(FetchIndexBody.fromJson)
  }

  /** @inheritdoc */
  def abortFetch(): Response[AbortFetchBody] = {
    executeBaseCommandAsJson("abortfetch")(AbortFetchBody.fromJson)
  }

  /** @inheritdoc */
  def poll(isEnable: Boolean): Response[PollBody] = {
    executeAsJson(pollRequest(isEnable))(PollBody.fromJson)
  }
}
