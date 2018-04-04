package ars.solr.query

import ars.precondition.RequireUtils.{requireNotBlank, _}
import ars.solr.query.SolrBasicQueryFactory._
import org.apache.lucene.search._

/** Apache Solr complex queries factory.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
object SolrComplexQueryFactory {

  /**
    * Creates an `and` query.
    *
    * If this method was invoked with `firstQuery` only then it returns `firstQuery`,
    * otherwise it returns [[BooleanQuery]] with occur [[BooleanClause.Occur.MUST]] for the sequence
    * (`firstQuery`, `otherWords.head`, ...)
    *
    * @param firstQuery the first query
    * @param otherQueries the queries following the first
    * @return the new instance of `and` query
    */
  def and(firstQuery: Query, otherQueries: Query*): Query = {
    requireNotNull(firstQuery, "firstQuery")
    requireNotNull(otherQueries, "otherQueries")
    requireAllNotNull(otherQueries, "otherQueries")

    if (otherQueries.isEmpty) firstQuery
    else {
      val allQueries = firstQuery +: otherQueries
      boolean(allQueries.map(must) :_*)
    }
  }

  /**
    * Creates an `or` query.
    *
    * If this method was invoked with `firstQuery` only then it returns `firstQuery`,
    * otherwise it returns [[BooleanQuery]] with occur [[BooleanClause.Occur.SHOULD]] for the sequence
    * (`firstQuery`, `otherWords.head`, ...)
    *
    * @param firstQuery the first query
    * @param otherQueries the queries following the first
    * @return the new instance of `or` query
    */
  def or(firstQuery: Query, otherQueries: Query*): Query = {
    requireNotNull(firstQuery, "firstQuery")
    requireNotNull(otherQueries, "otherQueries")
    requireAllNotNull(otherQueries, "otherQueries")

    if (otherQueries.isEmpty) firstQuery
    else {
      val allQueries = firstQuery +: otherQueries
      boolean(allQueries.map(should) :_*)
    }
  }

  /**
    * Creates an `not` query.
    *
    * @param query the query
    * @return the new instance of `not` query
    */
  def not(query: Query): Query = {
  // "query" validates by mustNot()

    boolean(must(allDocs), mustNot(query))
  }

  /**
    * Creates a sequence of words query.
    *
    * If this method invokes with `firstWord` only then it returns [[TermQuery]],
    * otherwise it returns [[PhraseQuery]] with the sequence of words
    * (`firstWord`, `otherWords.head`, ...).
    *
    * @param firstWord the first word
    * @param otherWords the words following the first
    * @return the new query instance
    */
  def complexPhrase(field: String, firstWord: String, otherWords: String*): Query = {
    requireNotBlank(field, "field")
    requireNotBlank(firstWord, "firstWord")
    requireAllNotBlank(otherWords, "otherWords")

    _complexPhrase(field)(firstWord, otherWords :_*)
  }

  /**
    * Creates a multiphrase sequence of words query.
    *
    * If each part of `parts` have only one term, then it returns [[TermQuery]] or
    * [[PhraseQuery]], otherwise it returns [[MultiPhraseQuery]].
    *
    * @param firstPart the first part
    * @param otherParts the sequence of query parts following the first
    * @return the new query instance
    */
  def complexPhrase(field: String, firstPart: MultiPhrasePart, otherParts: MultiPhrasePart*): Query = {
    requireNotBlank(field, "field")
    requireNotNull(firstPart, "firstPart")
    requireAllNotNull(otherParts, "otherParts")

    val allParts = firstPart +: otherParts
    val isMulti = allParts.exists(_.terms.size > 1)

    if (isMulti) multiPhrase(field)(allParts :_*)
    else {
      val firstTerm = firstPart.terms.head
      val otherTerms = otherParts.map(_.terms.head)
      _complexPhrase(field)(firstTerm, otherTerms :_*)
    }
  }


  // TODO ==================== EXPERIMENTAL API = BEGIN
//  def in[T](field: String)(firstValue: T, otherValues: T*): Query = {
  //    requireNotBlank(field, "field")
  //    requireNotNull(firstValue, "firstValue")
  //    requireAllNotNull(otherValues, "otherValues")
  //
  //    _in(field)(firstValue, otherValues)
  //  }
  //
  //  def in(field: String)(firstValue: String, otherValues: String*): Query = {
  //    requireNotBlank(field, "field")
  //    requireNotNull(firstValue, "firstValue")
  //    requireAllNotBlank(otherValues, "otherValues")
  //
  //    _in(field)(firstValue, otherValues)
  //  }
  //
  //  private def _in[T](field: String)(firstValue: T, otherValues: T*): Query = {
  //    if (otherValues.isEmpty) {
  //      term(field, firstValue)
  //    } else {
  //      val firstTerm = term(field, firstValue)
  //      val otherTerms = otherValues.map(v => term(field, v))
  //      or(firstTerm, otherTerms)
  //    }
  //  }


  // TODO ==================== EXPERIMENTAL API = END

  private def _complexPhrase(field: String)(firstWord: String, otherWords: String*): Query = {
    if (otherWords.isEmpty) term(field, firstWord)
    else {
      val words = firstWord +: otherWords
      phrase(field)(words :_*)
    }
  }
}
