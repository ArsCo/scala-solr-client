package ars.solr

import java.util.{Map => JMap}

import ars.precondition.RequireUtils.requireNotBlank
import org.apache.solr.common.{SolrDocument, SolrDocumentList}

import scala.collection.JavaConversions._
import scala.util.Try

/** Package object for `ars.solr.client`.
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
package object client {

  /** Apache Solr document map type. */
  type SolrDocumentMap = Map[String, Any]

  /** Document filter predicate type.
    *
    * @tparam T the document result type
    */
  type Predicate[Result] = (Try[Result]) => Boolean

  /** Predicate failure action type. */
  type PredicateFailureAction = (String) => Unit

  /** Error message formatter type.
    *
    * @tparam T the message type.
    */
  type ErrorMessageFormatter[T] = Try[T] => String

  /** Apache Solr Collection type.
    *
    * @param name the collection name
    */
  case class Collection(name: String) {
    requireNotBlank(name)
  }

  /**
    * Converts the native Solr [[SolrDocumentList]] to the sequence of [[SolrDocumentMap]].
    *
    * @param nativeDocs native Solr documents list
    *
    * @return sequence of scala Solr document map
    */
  def toScalaMapSeq(nativeDocs: SolrDocumentList): Seq[SolrDocumentMap] = {
    for {
      nativeSolrDoc <- nativeDocs
      scalaSolrDoc = nativeDocToScala(nativeSolrDoc)
    } yield scalaSolrDoc
  }

  /**
    * Converts the native Solr [[SolrDocument]] to the [[SolrDocumentMap]].
    *
    * @param doc the native Solr document
    *
    * @return scala Solr document map
    */
  def nativeDocToScala(doc: SolrDocument): SolrDocumentMap = doc.asInstanceOf[JMap[String, Any]].toMap
}
