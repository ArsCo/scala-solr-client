package ars.solr.management.replication.response

import ars.solr.AbstractBaseTest

import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.native.JsonMethods._

/** Tests for ars.solr.management.replication.response body classes.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
class BodyTest extends AbstractBaseTest {


  def toJsonString(json: JsonAST.JObject): String = {
    compact(render(json))
  }

  def reparse(json: JsonAST.JObject) = {
    parse(compact(render(json)))
  }

  def fileToJson(file: File) = {
    ("name" -> file.name) ~
    ("size" -> file.size) ~
    ("checksum" -> file.checksum)
  }

  // ========== ReplicationManager ==========

  "IndexBody" must "validate args" in {
    // no op
  }

  "IndexBody.fromJson" must "validate args" in {
    intercept[IllegalArgumentException] {
      IndexBody.fromJson(null)
    }
  }

  it must "parse valid JSON" in {
    val s = ("indexversion" -> 1234567) ~ ("generation" -> 5678)

    assertResult(IndexBody(1234567, 5678)) {
      IndexBody.fromJson(s)
    }
  }

  // ----------

  val file1 = File("fileName1", 234, Some(123456))
  val file2 = File("fileName2", 345, None)

  val confFile1 = File("confFileName1", 456, Some(9876))
  val confFile2 = File("confFileName2", 789, None)

  "FileListBody" must "validate args" in {
    intercept[IllegalArgumentException] {
      FileListBody(null, Seq())
    }

    intercept[IllegalArgumentException] {
      FileListBody(Seq(), null)
    }
  }

  "FileListBody.fromJson" must "validate args" in {
    intercept[IllegalArgumentException] {
      FileListBody.fromJson(null)
    }
  }

  it must "parse valid JSON" in {

    val files = List(file1, file2)
    val confFiles = List(confFile1, confFile2)
    val s = ("filelist" -> files.map(fileToJson)) ~ ("confFiles" -> confFiles.map(fileToJson))

    assertResult(FileListBody(files, confFiles)) {
      FileListBody.fromJson(s)
    }
  }

  // ----------

  val name = "Name"
  val fileSize = 1234
  val checksum = Some(34567L)

  "File" must "validate args" in {

    File(name, fileSize, checksum)
    File(name, fileSize, None)
    File(name, 0, checksum)

    intercept[IllegalArgumentException] {
      File(null, fileSize, checksum)
    }

    intercept[IllegalArgumentException] {
      File("", fileSize, checksum)
    }

    intercept[IllegalArgumentException] {
      File(name, -3, checksum)
    }

    intercept[IllegalArgumentException] {
      File(name, fileSize, null)
    }
  }

  "File.fromJson" must "validate args" in {
    intercept[IllegalArgumentException] {
      File.fromJson(null)
    }
  }

  it must "parse valid JSON" in {
    val s = ("name" -> name) ~ ("size" -> fileSize) ~ ("checksum" -> checksum.get)

    assertResult(File(name, fileSize, checksum)) {
      File.fromJson(s)
    }

    val s1 = ("name" -> name) ~ ("size" -> fileSize)
    assertResult(File(name, fileSize, None)) {
      File.fromJson(s1)
    }

  }

  // ---------- MasterReplicationManager ----------

  val status = "Status"
  val exception = Some("Exception")

  "CreateBackupBody" must "validate args" in {
    intercept[IllegalArgumentException] {
      CreateBackupBody(null, exception)
    }

    intercept[IllegalArgumentException] {
      CreateBackupBody("  ", exception)
    }

    intercept[IllegalArgumentException] {
      CreateBackupBody(status, null)
    }
  }

  "CreateBackupBody.fromJson" must "validate args" in {
    intercept[IllegalArgumentException] {
      CreateBackupBody.fromJson(null)
    }
  }

  it must "parse valid JSON" in {
    val s = ("status" -> status) ~ ("exception" -> exception)
    assertResult(CreateBackupBody(status, exception)) {
      CreateBackupBody.fromJson(s)
    }

    val s1 = ("status" -> status)
    assertResult(CreateBackupBody(status, None)) {
      CreateBackupBody.fromJson(s1)
    }
  }

  val optStatus = Some("Status")
  val optError = Some(Error("mes", 345))

  "DeleteBackupBody" must "validate args" in {
    DeleteBackupBody(optStatus, optError)

    intercept[IllegalArgumentException] {
      DeleteBackupBody(null, optError)
    }

    intercept[IllegalArgumentException] {
      DeleteBackupBody(Some(null), optError)
    }

    intercept[IllegalArgumentException] {
      DeleteBackupBody(Some("  "), optError)
    }

    intercept[IllegalArgumentException] {
      DeleteBackupBody(optStatus, null)
    }

    intercept[IllegalArgumentException] {
      DeleteBackupBody(optStatus, Some(null))
    }

  }

  "DeleteBackupBody.fromJson" must "validate args" in {
    intercept[IllegalArgumentException] {
      DeleteBackupBody.fromJson(null)
    }
  }

  it must "parse valid JSON" in {
    val e = ("msg" -> "mes") ~ ("code" -> 345)

    val s = ("status" -> optStatus.get) ~ ("exception" -> e)
    assertResult(DeleteBackupBody(optStatus, optError)) {
      DeleteBackupBody.fromJson(s)
    }

    val s1 = ("status" -> optStatus.get)
    assertResult(DeleteBackupBody(optStatus, None)) {
      DeleteBackupBody.fromJson(s1)
    }

    val s2 = ("exception" -> e)
    assertResult(DeleteBackupBody(None, optError)) {
      DeleteBackupBody.fromJson(s2)
    }

  }

  val errorMessage = "message1"
  val errorCode = 895

  "Error" must "validate args" in {
    intercept[IllegalArgumentException] {
      Error(null, errorCode)
    }
  }

  "Error.fromJson" must "validate args" in {
    intercept[IllegalArgumentException] {
      Error.fromJson(null)
    }
  }

  it must "parse valid JSON" in {
    val s = ("msg" -> errorMessage) ~ ("code" -> errorCode)

    assertResult(Error(errorMessage, errorCode)) {
      Error.fromJson(s)
    }
  }

  // ---------- SlaveReplicationManager ----------

  val optMessage = Some("Message")

  "FetchIndexBody" must "validate args" in {
    intercept[IllegalArgumentException] {
      FetchIndexBody(null, optMessage)
    }

    intercept[IllegalArgumentException] {
      FetchIndexBody(status, null)
    }

    intercept[IllegalArgumentException] {
      FetchIndexBody(status, Some(null))
    }
  }

  "FetchIndexBody.fromJson" must "validate args" in {
    intercept[IllegalArgumentException] {
      FetchIndexBody.fromJson(null)
    }
  }

  it must "parse valid JSON" in {
    val s = ("status" -> status) ~ ("message" -> optMessage)
    assertResult(FetchIndexBody(status, optMessage)) {
      FetchIndexBody.fromJson(s)
    }

    val s1 = ("status" -> status)
    assertResult(FetchIndexBody(status, None)) {
      FetchIndexBody.fromJson(s1)
    }
  }

  "AbortFetchBody" must "validate args" in {
    intercept[IllegalArgumentException] {
      AbortFetchBody(null, optMessage)
    }

    intercept[IllegalArgumentException] {
      AbortFetchBody(status, null)
    }

    intercept[IllegalArgumentException] {
      AbortFetchBody(status, Some(null))
    }
  }

  "AbortFetchBody.fromJson" must "validate args" in {
    intercept[IllegalArgumentException] {
      AbortFetchBody.fromJson(null)
    }
  }

  it must "parse valid JSON" in {
    val s = ("status" -> status) ~ ("message" -> optMessage)
    assertResult(AbortFetchBody(status, optMessage)) {
      AbortFetchBody.fromJson(s)
    }

    val s1 = ("status" -> status)
    assertResult(AbortFetchBody(status, None)) {
      AbortFetchBody.fromJson(s1)
    }
  }

  "PollBody.fromJson" must "validate args" in {
    intercept[IllegalArgumentException] {
      PollBody.fromJson(null)
    }
  }

  it must "parse valid JSON" in {
    val s = ("status" -> status) ~ ("message" -> optMessage)
    assertResult(PollBody(status, optMessage)) {
      PollBody.fromJson(s)
    }

    val s1 = ("status" -> status)
    assertResult(PollBody(status, None)) {
      PollBody.fromJson(s1)
    }
  }

  "DetailsBody" must "validate args" in {

  }

  it must "extract JSON values" in {

  }

  "Commit" must "validate args" in {

  }

  it must "extract JSON values" in {

  }

  "Master" must "validate args" in {

  }

  it must "extract JSON values" in {

  }

  "Slave" must "validate args" in {

  }

  it must "extract JSON values" in {

  }

}
