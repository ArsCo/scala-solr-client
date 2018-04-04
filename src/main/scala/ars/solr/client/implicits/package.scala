package ars.solr.client

import ars.solr.client.QueryCallback.{DocumentCall, InfoCall, fromNativeCallback, toNativeCallback}
import org.apache.solr.client.solrj.StreamingResponseCallback

import scala.language.implicitConversions

/** Implicits for package `ars.solr.client`.
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
package object implicits {

  // ---------- QueryCallback ----------

  /** Implicit conversion from [[DocumentCall]] to [[QueryCallback]]. */
  implicit def documentCallToQueryCallback(call: DocumentCall): QueryCallback = QueryCallback(call)

  /** Implicit conversion from [[InfoCall]] to [[QueryCallback]]. */
  implicit def infoCallToQueryCallback(call: InfoCall): QueryCallback = QueryCallback(call)

  /** Implicit conversion from [[StreamingResponseCallback]] to [[QueryCallback]]. */
  implicit def nativeCallbackToQueryCallback: (StreamingResponseCallback) => QueryCallback = fromNativeCallback

  /** Implicit conversion from [[QueryCallback]] to [[StreamingResponseCallback]]. */
  implicit def queryCallbackToNativeCallback: (QueryCallback) => StreamingResponseCallback = toNativeCallback
}
