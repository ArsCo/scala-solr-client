package ars.solr.client

import java.lang.{Float => JFloat}

import ars.precondition.RequireUtils.requireNotNull
import ars.solr.client.QueryCallback._
import org.apache.solr.client.solrj.StreamingResponseCallback
import org.apache.solr.common.SolrDocument

/** Query callback object. This class is a Scala wrapper for [[StreamingResponseCallback]]
  * but with more concise Scala syntax.
  *
  * '''Examples:'''
  *
  * -$ For `DocumentCall` method only you can use syntax:
  * {{{
  *   QueryCallBack { s: SolrDocument =>
  *     ...
  *   }
  * }}}
  * or implicit conversion
  * {{{
  *   val qc: QueryCallBack = (d: SolrDocument) => {}
  * }}}
  *
  * -$ For `InfoCall` method only you can use syntax:
  * {{{
  *   QueryCallBack { (num: Long, start: Long, maxScore: Float) =>
  *     ...
  *   }
  * }}}
  * or implicit conversion
  * {{{
  *   val qc: QueryCallBack = (num: Long, start: Long, maxScore: Float) => {}
  * }}}
  *
  * -$ The most general case:
  * {{{
  *   QueryCallBack(
  *     (s: SolrDocument) => {
  *       ...
  *     },
  *     (num: Long, start: Long, maxScore: Float) => {
  *       ...
  *     })
  *
  * }}}
  * or
  * {{{
  *   QueryCallBack(
  *     document = (s: SolrDocument) => {
  *       ...
  *     },
  *     info = (num: Long, start: Long, maxScore: Float) => {
  *       ...
  *     })
  *
  * }}}
  * or
  * {{{
  *   QueryCallBack(document = docFunction, info = infoFunction)
  * }}}
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
trait QueryCallback {

  /** The document callback function. */
  def document: DocumentCall

  /** The info callback function. */
  def info: InfoCall

  /**
    * Converts this instance to native Apache SolrJ [[StreamingResponseCallback]].
    *
    * @return the instance of [[StreamingResponseCallback]]
    */
  def toNativeCallback: StreamingResponseCallback = QueryCallback.toNativeCallback(this)
}

object QueryCallback {

  /** Document callback type. */
  type DocumentCall = (SolrDocument) => Unit

  /** Information callback type. */
  type InfoCall = (Long, Long, Option[Float]) => Unit

  /** Noop [[DocumentCall]] instance. */
  val NoopDocumentCall: DocumentCall = (d: SolrDocument) => {}

  /** Noop [[InfoCall]] instance. */
  val NoopInfoCall: InfoCall = (_: Long, _: Long, _: Option[Float]) => {}

  /**
    * @param document the document callback function
    * @param info the info callback function
    */
  def apply(document: DocumentCall = NoopDocumentCall, info: InfoCall = NoopInfoCall): QueryCallback =
    DefaultQueryCallback(document, info)

  /**
    * @param call the document callback function
    *
    * @return the callback
    */
  def apply(call: DocumentCall): QueryCallback = DefaultQueryCallback(document = call)

  /**
    * @param call the info callback function
    *
    * @return the callback
    */
  def apply(call: InfoCall): QueryCallback = DefaultQueryCallback(info = call)

  /**
    * Converts from [[StreamingResponseCallback]] to [[QueryCallback]].
    *
    * @param nativeCallback the native Apache SolrJ callback.
    *
    * @return the [[QueryCallback]] instance
    */
  def fromNativeCallback(nativeCallback: StreamingResponseCallback): QueryCallback = {
    requireNotNull(nativeCallback, "nativeCallback")

    nativeCallback match {
      case w: StreamingResponseCallbackWrapper => w.callback // unwrap from StreamingResponseCallbackWrapper
      case _ => new QueryCallbackWrapper(nativeCallback)
    }
  }

  /**
    * Converts from [[QueryCallback]] to [[StreamingResponseCallback]].
    *
    * @param callback the callback
    * @return the [[StreamingResponseCallback]] callback
    */
  def toNativeCallback(callback: QueryCallback): StreamingResponseCallback = {
    requireNotNull(callback, "callback")

    callback match {
      case w: QueryCallbackWrapper => w.nativeCallback // unwrap from QueryCallback
      case _ => new StreamingResponseCallbackWrapper(callback)
    }
  }
}

/** Default implementation of [[QueryCallback]].
  *
  * @param document the document callback function
  * @param info the info callback function
  */
@throws[IllegalArgumentException]("if any argument isn't valid")
private case class DefaultQueryCallback(document: DocumentCall = NoopDocumentCall,
                                        info: InfoCall = NoopInfoCall) extends QueryCallback {
  requireNotNull(document, "document")
  requireNotNull(info, "info")
}

private[client] class StreamingResponseCallbackWrapper(val callback: QueryCallback)
  extends StreamingResponseCallback {

  override def streamSolrDocument(d: SolrDocument): Unit = callback.document(d)
  override def streamDocListInfo(numFound: Long, start: Long, maxScore: JFloat): Unit = {
    callback.info(numFound, start, optionMaxScore(maxScore))
  }

  private[client] def optionMaxScore(maxScore: JFloat) = {
    if (maxScore == null) None
    else Some(maxScore.floatValue())
  }
}

private[client] class QueryCallbackWrapper(val nativeCallback: StreamingResponseCallback)
  extends QueryCallback {

  override def document: DocumentCall = nativeCallback.streamSolrDocument
  override def info: InfoCall = { case (num, start, maxScore) =>
    nativeCallback.streamDocListInfo(num, start, maxScore.orNull)
  }
}