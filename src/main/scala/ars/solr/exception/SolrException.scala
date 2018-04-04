package ars.solr.exception

/** Solr exception.
  *
  * @param message the message
  * @param cause the cause exception object
  * @param enableSuppression is enable suppersions
  * @param writableStackTrace is writeable stack trace
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.1
  */
class SolrException(
    message: String = null,
    cause: Throwable = null,
    enableSuppression: Boolean = true,
    writableStackTrace: Boolean = false
  ) extends Exception(message, cause, enableSuppression, writableStackTrace)
