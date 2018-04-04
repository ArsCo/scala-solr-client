package ars.solr.client.conversion

import ars.solr.exception.SolrException

/** Conversion exception.
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.5
  */
class SolrConversionException(
    message: String = null,
    cause: Throwable = null,
    enableSuppression: Boolean = true,
    writableStackTrace: Boolean = false
) extends SolrException(message, cause, enableSuppression, writableStackTrace)
