package ars.solr.util

import ars.solr.exception.SolrException
import ars.logging.LoggingUtils

import scala.reflect.ClassTag

/**
  *
  * @param classTag the class tag
  * @tparam L
  */
class LogUtils[L](implicit classTag: ClassTag[L]) extends LoggingUtils[L] {

  val loggingUtils = new LoggingUtils[L]

  def protect[T](errorMessage: String)(operation: => T): T = {

    loggingUtils.protect[T, SolrException](errorMessage,
      (e: Exception) => new SolrException(cause = e))(operation)
  }

  def protect[T](operation: => T): T = {
    protect("") {
      operation
    }
  }
}
