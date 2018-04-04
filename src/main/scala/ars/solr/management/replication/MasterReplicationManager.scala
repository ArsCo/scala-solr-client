package ars.solr.management.replication

import ars.solr.management._
import ars.solr.management.replication.response.{CreateBackupBody, DeleteBackupBody, Header, Response}

/** Master replication manager.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
trait MasterReplicationManager extends ReplicationManager {

  /**
    * Enables/Disables replication on the master for all its slaves.
    *
    * Executes change replication status request:
    * `http://master_host:port/solr/core_name/replication?command=<...>`.
    *
    * @param enable the `true` to enable and the `false` to disable replication
    * @return the response header
    */
  def replication(enable: Boolean): Header

  /**
    * Enables replication on the master for all its slaves.
    * This is the syntax sugar for `replication(true)` method invocation.
    *
    * Executes enable replication request:
    * `http://master_host:port/solr/core_name/replication?command=enablereplication`.
    *
    * @return the response header
    */
  def enableReplication(): Header = replication(true)

  /**
    * Disables replication on the master for all its slaves.
    * This is the syntax sugar for `replication(false)` method invocation.
    *
    * Executes disable replication request:
    * `http://master_host:port/solr/core_name/replication?command=disablereplication`.
    *
    * @return the response header
    */
  def disableReplication(): Header = replication(false)

  /**
    * Creates a backup on master if there are committed index data in the server;
    * otherwise, does nothing. This method is useful for making periodic backups.
    *
    * Executes backup request:
    * `http://master_host:port/solr/core_name/replication?command=backup`.
    *
    * @param name the backup name. The snapshot will be created in a directory called
    *             `snapshot.< name >` within the data directory of the core.
    *             By default the name is generated using date in `yyyyMMddHHmmssSSS` format.
    *             If `location` argument is passed, that would be used instead of the data directory.
    * @param location the backup location
    * @param numberToKeep the number to keep. It can be used with the backup command
    *                     unless the `maxNumberOfBackups` initialization parameter has
    *                     been specified on the handler – in which case `maxNumberOfBackups`
    *                     is always used and attempts to use the `numberToKeep` parameter
    *                     will cause an error.
    * @return the response
    */
  def createBackup(name: Option[String] = None,
                   location: Option[String] = None,
                   numberToKeep: Option[Int] = None): Response[CreateBackupBody]

  /**
    * Delete any backup created using the [[createBackup()]] method.
    *
    * Executes backup delete request:
    * `http://master_host:port/solr/core_name/replication?command=deletebackup`.
    *
    * @param name the backup name. A snapshot with the name `snapshot.< name >` must exist.
    *             If not, an error is thrown.
    * @param location the location
    * @return the response
    */
  def deleteBackup(name: Option[String] = None,
                   location: Option[String] = None): Response[DeleteBackupBody]
}
