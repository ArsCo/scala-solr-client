package ars.solr.config

import ars.precondition.RequireUtils.requireNotBlank

/** Apache Solr core configuration.
  *
  * @param url the Apache Solr server URL
  *            (e.g. `http://mydomain.com/solr` or `http://mydomain.com/solr/`)
  * @param coreName the core name (e.g. `my-core-name`)
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.3
  */
@throws[IllegalArgumentException]("if any argument isn't valid")
final class SolrCoreConfig (url: String, val coreName: String) {
  requireNotBlank(url, "url")
  requireNotBlank(coreName, "coreName")

  /** Solr server url. */
  val serverUrl: String = formatSolrUrl()

  /** Core name. */
  val coreUrl: String = serverUrl + "/" + coreName

  private def formatSolrUrl(): String = stripEnd(url.trim, '/')

  private def countStripEnd(string: String, char: Char): Int = {
    var cutCount = 0
    var isDone = false
    for (c <- string.reverseIterator) {
      if (!isDone) {
        if (c == char) cutCount += 1
        else isDone = true
      }
    }
    cutCount
  }

  private def stripEnd(string: String, char: Char): String = {
    string.substring(0, string.length - countStripEnd(string, char))
  }

  override def hashCode(): Int = coreUrl.hashCode

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case null => false
      case other: SolrCoreConfig => coreUrl == other.coreUrl
      case _ => false
    }
  }

  override def toString: String = coreUrl
}

object SolrCoreConfig {

  /**
    * Creates new instance.
    *
    * @param url the Apache Solr server URL
    *            (e.g. `http://mydomain.com/solr` or `http://mydomain.com/solr/`)
    * @param coreName the core name (e.g. `my-core-name`)
    * @return the new instance
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def apply(url: String, coreName: String): SolrCoreConfig = new SolrCoreConfig(url, coreName)

  /**
    * Creates new instance. It parses `coreUrl` and extracts `serverUrl` and `coreName`.
    *
    * @param coreUrl the
    * @return
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def apply(coreUrl: String): SolrCoreConfig = {
    requireNotBlank(coreUrl, "coreUrl")

    val (serverUrl, coreName) = parseCoreUrl(coreUrl)
    SolrCoreConfig(serverUrl, coreName)

  }

  private[config] def parseCoreUrl(coreUrl: String): (String, String) = {
    val indexOfSlash = coreUrl.lastIndexOf('/')
    if (indexOfSlash == -1) badCoreUrl(coreUrl)
    else {
      val serverUrl = coreUrl.substring(0, indexOfSlash)
      if (serverUrl.trim.isEmpty || isLastIndexOf(coreUrl, indexOfSlash)) badCoreUrl(coreUrl)
      else {
        val coreName = coreUrl.substring(indexOfSlash + 1)
        (serverUrl, coreName)
      }
    }
  }

  private[config] def badCoreUrl(coreUrl: String): Nothing = {
    throw new IllegalArgumentException(s"Bad Apache Solr core URL format: $coreUrl")
  }

  private[config] def isLastIndexOf(string: String, index: Int): Boolean = {
    string.length == (index + 1)
  }
}
