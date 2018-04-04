package ars.solr.client.mapping

import ars.solr.exception.SolrException

/** Solr mapping exception.
  *
  * @param message the message
  * @param cause the cause exception object
  * @param enableSuppression is enable suppersions
  * @param writableStackTrace is writeable stack trace
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.5
  */
class SolrMappingException(
    message: String = null,
    cause: Throwable = null,
    enableSuppression: Boolean = true,
    writableStackTrace: Boolean = false
  ) extends SolrException(message, cause, enableSuppression, writableStackTrace)
