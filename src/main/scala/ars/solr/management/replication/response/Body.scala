package ars.solr.management.replication.response

import ars.precondition.RequireUtils._
import org.json4s.JValue

// ---------- ReplicationManager ----------

/** Apache Solr index version replication response body.
  *
  * @param indexVersion the index version
  * @param generation the generation
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class IndexBody(indexVersion: Long, generation: Long)

object IndexBody extends FromJson[IndexBody] {

  /** @inheritdoc */
  override def fromJson(json: JValue): IndexBody = {
    requireNotNull(json, "json")

    val indexVersion = (json \ "indexversion").extract[Long]
    val generation = (json \ "generation").extract[Long]

    IndexBody(indexVersion, generation)
  }
}

/** Apache Solr file list replication response body.
  *
  * @param fileList the files list
  * @param confFiles the configuration files list
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class FileListBody(fileList: Seq[File], confFiles: Seq[File]) {
  requireNotNull(fileList, "fileList")
  requireNotNull(confFiles, "confFiles")
}

object FileListBody extends FromJson[FileListBody] {

  /** @inheritdoc */
  override def fromJson(json: JValue): FileListBody = {
    requireNotNull(json, "json")

    val fileList = (json \ "filelist").extract[List[File]]
    val confFiles = (json \ "confFiles").extract[List[File]]

    FileListBody(fileList, confFiles)
  }
}

/** Apache Solr file response body part.
  *
  * @param name the file name
  * @param size the file size
  * @param checksum the file checksum
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class File(name: String, size: Long, checksum: Option[Long]) {
  requireNotBlank(name, "name")
  requireNonNegative(size, "size")
  requireNotNull(checksum, "checksum")
}

object File extends FromJson[File] {

  /** @inheritdoc */
  override def fromJson(json: JValue): File = {
    requireNotNull(json, "json")

    val name = (json \ "name").extract[String]
    val size = (json \ "size").extract[Long]
    val checksum = (json \ "checksum").extractOpt[Long]

    File(name, size, checksum)
  }
}

// ---------- MasterReplicationManager ----------

/** Apache Solr create backup response body.
  *
  * @param status the status
  * @param exception the exception message
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class CreateBackupBody(status: String, exception: Option[String]) {
  requireNotBlank(status, "status")
  requireNotNull(exception, "exception")
  optional(exception) { requireNotNull(_, "exception") }
}

object CreateBackupBody extends FromJson[CreateBackupBody] {

  /** @inheritdoc */
  override def fromJson(json: JValue): CreateBackupBody = {
    requireNotNull(json, "json")

    val status = (json \ "status").extract[String]
    val exception = (json \ "exception").extractOpt[String]

    CreateBackupBody(status, exception) // TODO
  }
}

/** Apache Solr delete backup response body.
  *
  * @param status the status
  * @param error the error
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class DeleteBackupBody(status: Option[String], error: Option[Error]) {
  requireNotBlank(status, "status")
  optional(status) { requireNotNull(_, "status") }
  requireNotNull(error, "error")
  optional(error) { requireNotNull(_, "error") }
}

object DeleteBackupBody extends FromJson[DeleteBackupBody] {

  /** @inheritdoc */
  override def fromJson(json: JValue): DeleteBackupBody = {
    requireNotNull(json, "json")

    val status = (json \ "status").extractOpt[String]
    val error = (json \ "exception").extractOpt[Error]

    DeleteBackupBody(status, error)
  }
}

/** Apache Solr delete backup error response body part.
  *
  * @param msg the message
  * @param code the code
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class Error(msg: String, code: Int) {
  requireNotNull(msg, "msg")
}

object Error extends FromJson[Error] {
  /** @inheritdoc */
  override def fromJson(json: JValue): Error = {
    requireNotNull(json, "json")

    val status = (json \ "msg").extract[String]
    val code = (json \ "code").extract[Int]

    Error(status, code)
  }
}

// ---------- SlaveReplicationManager ----------

/** Apache Solr fetch index response body.
  *
  * @param status the status
  * @param message the message
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class FetchIndexBody(status: String, message: Option[String]) {
  requireNotBlank(status, "status")
  requireNotNull(message, "message")
  optional(message) { requireNotNull(_, "message") }
}

object FetchIndexBody extends FromJson[FetchIndexBody] {

  /** @inheritdoc */
  override def fromJson(json: JValue): FetchIndexBody = {
    requireNotNull(json, "json")

    val status = (json \ "status").extract[String]
    val message = (json \ "message").extractOpt[String]

    FetchIndexBody(status, message)
  }
}

/** Apache Solr abort fetch index response body.
  *
  * @param status the status
  * @param message the message
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class AbortFetchBody(status: String, message: Option[String]) {
  requireNotBlank(status, "status")
  requireNotNull(message, "message")
  optional(message) { requireNotNull(_, "message") }
}

object AbortFetchBody extends FromJson[AbortFetchBody] {

  /** @inheritdoc */
  override def fromJson(json: JValue): AbortFetchBody = {
    requireNotNull(json, "json")

    val status = (json \ "status").extract[String]
    val message = (json \ "message").extractOpt[String]

    AbortFetchBody(status, message)
  }
}

/** Apache Solr poll response body.
  *
  * @param status the status
  * @param message the message
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class PollBody(status: String, message: Option[String]) {
  requireNotBlank(status, "status")
  requireNotNull(message, "message")
}

object PollBody extends FromJson[PollBody] {

  /** @inheritdoc */
  override def fromJson(json: JValue): PollBody = {
    requireNotNull(json, "json")

    val status = (json \ "status").extract[String]
    val message = (json \ "message").extractOpt[String]

    PollBody(status, message)
  }
}

/** Apache Solr details response body.
  *
  * @param indexSize the index size
  * @param indexPath the index path
  * @param commits the commits info (unimplemented)
  * @param isMaster whether master or not
  * @param isSlave whether slave or not
  * @param indexVersion the index version
  * @param generation the generation
  * @param master the master details
  * @param slave the slave details
  * @param backup the backup
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class DetailsBody(indexSize: String,
                             indexPath: String,
                             commits: Seq[Commit],
                             isMaster: Boolean,
                             isSlave: Boolean,
                             indexVersion: Long,
                             generation: Long,
                             master: Option[Master],
                             slave: Option[Slave],
                             backup: Seq[String]) {

  requireNotBlank(indexSize, "indexSize")
  requireNotBlank(indexPath, "indexPath")
  requireNotNull(commits, "commits")
  requireNotNull(master, "master")
  requireNotNull(slave, "slave")
  requireNotNull(backup, "backup")
}

object DetailsBody extends FromJson[DetailsBody] {

  /** @inheritdoc */
  override def fromJson(json: JValue): DetailsBody = {
    requireNotNull(json, "json")

    val details = json \ "details"

    val indexSize = (details \ "indexSize").extract[String]
    val indexPath = (details \ "indexPath").extract[String]

    val commits = Seq[Commit]() // TODO Unimplemented

    val isMaster = (details \ "isMaster").extract[String].toBoolean
    val isSlave = (details \ "isSlave").extract[String].toBoolean

    val indexVersion = (details \ "indexVersion").extract[Long]
    val generation = (details \ "generation").extract[Long]

    val master = (details \ "master").extractOpt[Master]
    val slave = (details \ "slave").extractOpt[Slave]

    val backup = (details \ "backup").extract[Seq[String]]

    DetailsBody(indexSize, indexPath, commits,
      isMaster, isSlave, indexVersion, generation, master, slave, backup)
  }
}

/** Apache Solr details response commits info body part.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class Commit() // TODO Unimplemented

/** Apache Solr details response master info body part.
  *
  * @param confFiles the configuration files
  * @param replicateAfter the replication actions
  * @param replicationEnabled whether replication enabled or not
  * @param replicableVersion the replicable version
  * @param replicableGeneration the replicable generation
  */
final case class Master(confFiles: String,
                        replicateAfter: Seq[String],
                        replicationEnabled: Boolean,
                        replicableVersion: Long,
                        replicableGeneration: Long) {

  requireNotBlank(confFiles, "confFiles")
  requireNotNull(replicateAfter, "replicateAfter")
}

/** Apache Solr details response master info body part.
  *
  * @param masterDetails the master info
  * @param masterUrl the master URL
  * @param pollInterval the polling interval
  * @param nextExecutionAt the next polling exetution time
  * @param indexReplicatedAt the current index replication time
  * @param indexReplicatedAtList the index replication times
  * @param replicationFailedAtList the index replication failure times
  * @param timesIndexReplicated the number of data replications
  * @param confFilesReplicated the replicated configuration files
  * @param timesConfigReplicated the number of configuration files replications
  * @param confFilesReplicatedAt the time of configuration files replication
  * @param lastCycleBytesDownloaded the last cycle downloaded bytes
  * @param timesFailed the number of failures
  * @param replicationFailedAt the list time of replication failure
  * @param previousCycleTimeInSeconds the previous replication duration
  * @param currentDate the current time
  * @param isPollingDisabled whether or not pooling was disabled
  * @param isReplicating whether or not replication was enabled
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
final case class Slave(masterDetails: Master,
                       masterUrl: String,
                       pollInterval: String,
                       nextExecutionAt: String,
                       indexReplicatedAt: String,
                       indexReplicatedAtList: Seq[String],
                       replicationFailedAtList: Seq[String],
                       timesIndexReplicated: String,
                       confFilesReplicated: String,
                       timesConfigReplicated: String,
                       confFilesReplicatedAt: String,
                       lastCycleBytesDownloaded: String,
                       timesFailed: String,
                       replicationFailedAt: String,
                       previousCycleTimeInSeconds: String,
                       currentDate: String,
                       isPollingDisabled: Boolean,
                       isReplicating: Boolean) {

  requireNotNull(masterDetails, "masterDetails")
  requireNotBlank(masterUrl, "masterUrl")
  requireNotBlank(pollInterval, "pollInterval")
  requireNotBlank(nextExecutionAt, "nextExecutionAt")
  requireNotBlank(indexReplicatedAt, "indexReplicatedAt")
  requireNotNull(indexReplicatedAtList, "indexReplicatedAtList")
  requireNotNull(replicationFailedAtList, "replicationFailedAtList")
  requireNotBlank(timesIndexReplicated, "timesIndexReplicated")
  requireNotBlank(confFilesReplicated, "confFilesReplicated")
  requireNotBlank(timesConfigReplicated, "timesConfigReplicated")
  requireNotBlank(confFilesReplicatedAt, "confFilesReplicatedAt")
  requireNotBlank(lastCycleBytesDownloaded, "lastCycleBytesDownloaded")
  requireNotBlank(timesFailed, "timesFailed")
  requireNotBlank(replicationFailedAt, "replicationFailedAt")
  requireNotBlank(previousCycleTimeInSeconds, "previousCycleTimeInSeconds")
  requireNotBlank(currentDate, "currentDate")
}


