package ars.solr.query

import ars.solr.query.SolrComplexQueryFactory._
import ars.solr.query.SolrBasicQueryFactory._
import org.apache.lucene.search._

/** Apache Solr field query DSL.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
class SolrRichFieldQuery(val field: String) {

  /**
    * Creates a sequence of words query. This method is a syntax sugar for
    * [[SolrBasicQueryFactory.complexPhrase(field:String,firstWord:String*)]].
    *
    * @param firstWord the first word
    * @param otherWords the words following the first
    * @return the new query instance
    */
  def -->(firstWord: String, otherWords: String*): Query = complexPhrase(field, firstWord, otherWords :_*)

  /**
    * Creates a multiphrase sequence of words query. This method is a syntax sugar for
    * [[SolrBasicQueryFactory.complexPhrase(field:String,firstPart:MultiPhrasePart*)]].
    *
    * @param firstPart the first part pf query
    * @param otherParts the sequence of query parts following the first
    * @return the new query instance
    */
  def -->(firstPart: MultiPhrasePart, otherParts: MultiPhrasePart*): Query =
    complexPhrase(field, firstPart, otherParts :_*)

  /**
    * Creates a wildcard query. This method is a syntax sugar for
    * [[SolrBasicQueryFactory.wildcard()]].
    *
    * @param term the term
    * @return the new instance of wildcard query
    */
  def *~>(term: String): WildcardQuery = wildcard(field, term)

  /**
    * Creates a prefix query. This method is a syntax sugar for
    * [[SolrBasicQueryFactory.prefix()]].
    *
    * '''NOTE:''' this method escapes spaces in `value` with `\`.
    * If you don't wanna escape spaces then use method [[SolrBasicQueryFactory.prefix()]]
    * with `isEscapeSpaces = true`
    *
    * @param value the prefix value
    * @return the new instance of prefix query
    */
  def -*>(value: String): PrefixQuery = prefix(field, value)

  /**
    * Creates a regular expression query. This method is a syntax
    * sugar for [[SolrBasicQueryFactory.regExp()]].
    *
    * @param expr the regular expression
    * @return the new instance of regular expression query
    */
  def -?>(expr: String): RegexpQuery = regExp(field, expr)

  /**
    * Creates a fuzzy query. This method is a syntax
    * sugar for [[SolrBasicQueryFactory.fuzzy()]].
    *
    * @param term the term
    * @return the new instance of fuzzy query
    */
  def ~~>(term: String,
          maxEdits: Int = FuzzyQuery.defaultMaxEdits,
          prefixLength: Int = FuzzyQuery.defaultPrefixLength,
          maxExpansions: Int = FuzzyQuery.defaultMaxExpansions,
          transpositions: Boolean = FuzzyQuery.defaultTranspositions): FuzzyQuery = fuzzy(field, term)
}

