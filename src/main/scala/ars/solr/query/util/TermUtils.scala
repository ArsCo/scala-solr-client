package ars.solr.query.util

import ars.precondition.RequireUtils._
import org.apache.lucene.index.Term

/** Query utility methods.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
object TermUtils {

  /**
    * Creates a new [[Term]] instance.
    *
    * @param field the field name
    * @param text the text
    * @return the new [[Term]] instance
    */
  def toTerm(field: String, text: String): Term = {
    requireNotBlank(field, "field")
    requireNotBlank(text, "test")

    _toTerm(field, text)
  }

  /**
    * Creates a terms array instance. The `field` of all terms in the array are equals.
    *
    * @param field the field name
    * @param texts the texts
    * @return
    */
  def toTermsArray(field: String, texts: Iterable[String]): Array[Term] = {
    requireNotBlank(field, "field")
    requireNotBlank(texts, "test")
    requireAllNotBlank(texts, "texts")

    texts.map(_toTerm(field, _)).toArray
  }

  private def _toTerm(field: String, text: String): Term = new Term(field, text)
}
