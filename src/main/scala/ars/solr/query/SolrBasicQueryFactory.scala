package ars.solr.query

import ars.precondition.RequireUtils._
import ars.solr.query.util.TermUtils
import ars.solr.query.util.TermUtils.toTerm
import org.apache.lucene.search.{Query, _}
import org.apache.lucene.util.BytesRef
import org.apache.lucene.util.automaton.{LevenshteinAutomata, RegExp}

import scala.collection.JavaConverters._

/** Apache Solr basic queries factory.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
object SolrBasicQueryFactory {

  /**
    * Creates a term query ([[TermQuery]] instance). The search value `word` must
    * be a single word without some spaces and wildcard symbols.
    *
    * @param field the field name
    * @param word the search word
    * @return the new term query instance
    */
  def term(field: String, word: String): TermQuery = {
    requireNotBlank(field, "field")
    requireNotBlank(word, "word")

    new TermQuery(toTerm(field, word))
  }

  /**
    * Creates a wildcard query ([[WildcardQuery]] instance). The search value `term` may
    * contains `'?'` and `'*'` symbols as wildcards and `'\'` as escape symbol.
    *
    * @param field the field name
    * @param term the search term
    * @return the new wildcard query instance
    */
  def wildcard(field: String, term: String): WildcardQuery = {
    requireNotBlank(field, "field")
    requireNotBlank(term, "term")

    new WildcardQuery(toTerm(field, term))
  }

  /**
    * Creates a phrase query ([[PhraseQuery]] instance).
    * The search value `words` must be a sequence of words.
    *
    * @param slop the slop (@see [[PhraseQuery#getSlop()]])
    * @param field the field name
    * @param words the search words
    * @return the new phrase query instance
    */
  def phrase(field: String, slop: Int = 0)(words: String*): PhraseQuery = {
    requireNotBlank(field, "field")
    requireNonNegative(slop, "slop")
    requireAllNotBlank(words, "words")

    // ==================================================
    // TODO NEW CODE!!! FOR 6.x Solr!!! DON'T REMOVE ====
    // new PhraseQuery(slop, field, words :_ *)
    // ==================================================
    // TODO LEGACY CODE!!! REMOVE WHEN POSSIBLE!!!! =====
    val query = new PhraseQuery
    query.setSlop(slop)
    words.foreach { word =>
      query.add(toTerm(field, word))
    }
    query
    // ==================================================
  }

  /**
    * Creates a prefix query ([[PrefixQuery]] instance). The search value `prefix`
    * must be a prefix of single word term.
    *
    * @param field the field name
    * @param prefix the prefix
    * @param isEscapeSpecials if `true` then each special Sorl query symbol will be escaped with `\`
    * @return the new prefix query instance
    */
  def prefix(field: String, prefix: String, isEscapeSpecials: Boolean = true): PrefixQuery = {
    requireNotBlank(field, "field")
    requireNotBlank(prefix, "prefix")

    new PrefixQuery(toTerm(field, escapeSpecial(prefix, isEscapeSpecials)))
  }

  /**
    * Creates a multiphrase query ([[MultiPhraseQuery]] instance). It's a generalized
    * version of a phrase query with the possibility to use more than one term
    * at the same position.
    *
    * @param field the field name
    * @param phrases the sequence of terms in order
    * @return the new multiphrase query instance
    */
  def multiPhrase(field: String)(phrases: MultiPhrasePart*): MultiPhraseQuery = {
    requireNotBlank(field, "field")
    requireNotBlank(phrases, "phrases")
    requireAllNotNull(phrases, "phrases")

    // ==================================================
    // TODO NEW CODE!!! FOR 6.x Solr!!! DON'T REMOVE ====
    //    val builder = new MultiPhraseQuery.Builder()
    //
    //    phrases.foreach { phrase =>
    //      builder.add(toTermsArray(field, phrase.terms))
    //    }
    //
    //    builder.build()
    // ==================================================
    // TODO LEGACY CODE!!! REMOVE WHEN POSSIBLE!!!! =====
    val query = new MultiPhraseQuery
    phrases.foreach { phrase =>
      query.add(TermUtils.toTermsArray(field, phrase.terms))
    }
    query
    // ==================================================
  }

  /** Iterable of terms at the same position in multiphrase query. The `terms` must not be
    * blank or contains `null` values.
    *
    * @param terms the new terms
    */
  case class MultiPhrasePart(terms: Iterable[String]) {
    requireNotBlank(terms, "terms")
    requireAllNotBlank(terms, "terms")

    def this(term: String) = this(Seq(term))
  }

  // ==================================================
  // TODO LEGACY CODE!!! REMOVE WHEN POSSIBLE!!!! =====
  def field(field: String): Query = { // TODO Tests
    wildcard(field, "*")
  }
  // ==================================================
  // TODO NEW CODE!!! FOR 6.x Solr!!! DON'T REMOVE ====
  //  /**
  //    * Creates a field value query ([[FieldValueQuery]] instance). It matches documents
  //    * that have a value for a given field.
  //    *
  //    * @param field the field name
  //    * @return the field value query instance
  //    */
  //  def field(field: String): FieldValueQuery = {
  //    requireNotBlank(field, "field")
  //
  //    new FieldValueQuery(field)
  //  }
  // ==================================================

  /**
    * Creates a fuzzy query ([[FuzzyQuery]] instance). The similarity measurement
    * is based on the Damerau-Levenshtein (optimal string alignment) algorithm,
    * though you can explicitly choose classic Levenshtein by passing `false`
    * to the `transpositions` parameter.
    *
    * @param field the field name
    * @param term the term to search for
    * @param maxEdits must be >= 0 and <= [[LevenshteinAutomata.MAXIMUM_SUPPORTED_DISTANCE]].
    * @param prefixLength length of common (non-fuzzy) prefix
    * @param maxExpansions the maximum number of terms to match. If this number is
    *                      greater than [[BooleanQuery.getMaxClauseCount()]] when the
    *                      query is rewritten, then the maxClauseCount will be used instead.
    * @param transpositions `true` if transpositions should be treated as a primitive
    *                       edit operation. If this is false, comparisons will implement
    *                       the classic Levenshtein algorithm.
    * @return the new fuzzy query instance
    */
  def fuzzy(field: String, term: String,
            maxEdits: Int = FuzzyQuery.defaultMaxEdits,
            prefixLength: Int = FuzzyQuery.defaultPrefixLength,
            maxExpansions: Int = FuzzyQuery.defaultMaxExpansions,
            transpositions: Boolean = FuzzyQuery.defaultTranspositions): FuzzyQuery = {
    requireNotBlank(field, "field")
    requireNotBlank(term, "term")
    requirePositive(maxEdits, "maxEdits")
    requireNonNegative(prefixLength, "prefixLength")
    requirePositive(maxExpansions, "maxExpansions")

    new FuzzyQuery(toTerm(field, term), maxEdits, prefixLength, maxExpansions, transpositions)

  }

  /**
    * Creates a disjunction query ([[DisjunctionMaxQuery]] instance).
    *
    * @param tieBreakerMultiplier the weight to give to each matching non-maximum disjunct
    * @param disjuncts sequence of all the disjuncts to add
    * @return the new disjunction query instance
    */
  def disjunction(tieBreakerMultiplier: Float, disjuncts: Query*): DisjunctionMaxQuery = {
    requireNotBlank(disjuncts, "disjuncts")
    requireAllNotNull(disjuncts, "disjuncts")

    new DisjunctionMaxQuery(disjuncts.asJavaCollection, tieBreakerMultiplier)
  }

  /**
    * Creates a constant score query ([[ConstantScoreQuery]] instance).
    *
    * @param query the query
    * @return the new constant score query instance
    */
  def constantScore(query: Query): ConstantScoreQuery = {
    requireNotNull(query, "query")

    new ConstantScoreQuery(query)
  }

  // ==================================================
  // TODO NEW CODE!!! FOR 6.x Solr!!! DON'T REMOVE ====
  //  def boost(query: Query, boost: Float): BoostQuery = {
  //    new BoostQuery(query, boost)
  //  }
  //
  //  // TODO scaladoc
  //  def blended(parts: BelendedPart*): BlendedTermQuery = {
  //    val builder = new BlendedTermQuery.Builder
  //    parts.foreach { part =>
  //      builder.add(toTerm(part), part.boost)
  //    }
  //    builder.build()
  //  }
  //
  //  case class BlendedPart(field: String, term: String, boost: Float)
  // ==================================================

  /**
    * Creates a boolean query ([[BooleanQuery]] instance).
    *
    * @param clauses the boolean clauses
    * @return the new boolean query instance
    */
  def boolean(clauses: BooleanClause*): Query = {
    requireNotBlank(clauses, "clauses")
    requireAllNotNull(clauses, "clauses")
    // ==================================================
    // TODO NEW CODE!!! FOR 6.x Solr!!! DON'T REMOVE ====
    //    val builder = new BooleanQuery.Builder
    //    booleanClause.foreach {
    //      builder.add(_)
    //    }
    //    builder.build()
    // ==================================================
    // TODO LEGACY CODE!!! REMOVE WHEN POSSIBLE!!!! =====
    val query = new BooleanQuery()
    clauses.foreach(query.add)
    query
    // ==================================================
  }

  /**
    * Creates a must (`and`) clause for [[BooleanQuery]].
    *
    * @param query the query
    * @return the new must clause instance
    */
  def must(query: Query): BooleanClause = {
    requireNotNull(query, "query")
    new BooleanClause(query, BooleanClause.Occur.MUST)
  }

  // ==================================================
  // TODO NEW CODE!!! FOR 6.x Solr!!! DON'T REMOVE ====
  //  def filter(query: Query): BooleanClause = {
  //  requireNotNull(query, "query")
  //    new BooleanClause(query, BooleanClause.Occur.FILTER)
  //  }
  // ==================================================

  /**
    * Creates a should (`or`) clause for [[BooleanQuery]].
    *
    * @param query the query
    * @return the new should clause instance
    */
  def should(query: Query): BooleanClause = {
    requireNotNull(query, "query")
    new BooleanClause(query, BooleanClause.Occur.SHOULD)
  }

  /**
    * Creates a mustNot (`not`) clause for [[BooleanQuery]].
    *
    * @param query the query
    * @return the new mustNot clause instance
    */
  def mustNot(query: Query): BooleanClause = {
    requireNotNull(query, "query")
    new BooleanClause(query, BooleanClause.Occur.MUST_NOT)
  }

  /**
    * Create an all documents query ([[MatchAllDocsQuery]] instance). It matches all documents.
    *
    * @return the match all docs query instance
    */
  def allDocs: MatchAllDocsQuery = new MatchAllDocsQuery

  // ==================================================
  // TODO NEW CODE!!! FOR 6.x Solr!!! DON'T REMOVE ====
  //  /**
  //    * Create a no documents query ([[MatchNoDocsQuery]] instance). It matches no documents.
  //    *
  //    * @return the match no docs query instance
  //    */
  //  def noDocs: MatchNoDocsQuery = new MatchNoDocsQuery
  //
  //  /**
  //    * Creates a synonyms query ([[SynonymQuery]] instance).
  //    *
  //    * @param field the field name
  //    * @param synonyms the iterable of synonyms
  //    * @return the synonyms query instance
  //    */
  //  def synonyms(field: String, synonyms: String*): SynonymQuery = {
  //    requireNotBlank(field, "field")
  //    requireNotBlank(synonyms, "syns")
  //    requireAllNotBlank(synonyms, "syns")
  //
  //    val syns = synonyms.toSet
  //
  //    new SynonymQuery(toTermsArray(field, syns) :_*)
  //  }
  // ==================================================

  /**
    * Creates an n-gram phrase query ([[NGramPhraseQuery]] instance).
    *
    * @param size the size of n-gram
    * @param phrase the phrase
    * @return the n-gram query instance
    */
  def ngram(size: Int, phrase: PhraseQuery): NGramPhraseQuery = {
    requirePositive(size, "size")
    requireNotNull(phrase, "phrase")

    // ==================================================
    // TODO NEW CODE!!! FOR 6.x Solr!!! DON'T REMOVE ====
    //    new NGramPhraseQuery(size, phrase)
    // ==================================================
    // TODO LEGACY CODE!!! REMOVE WHEN POSSIBLE!!!! =====
    val query = new NGramPhraseQuery(size)
    phrase.getTerms.foreach(query.add)
    query
    // ==================================================
  }

  /**
    * Creates a regular expression query ([[RegexpQuery]] instance).
    *
    * @param field the field name
    * @param expr the regular expression
    * @param flags the flags
    * @return the new regular expression query
    */
  def regExp(field: String, expr: String, flags: Int = RegExp.NONE): RegexpQuery = {
    requireNotBlank(field, "field")
    requireNotBlank(expr, "expr")

    new RegexpQuery(toTerm(field, expr), flags)
  }

  /**
    * Creates a term range query ([[TermRangeQuery]] instance).
    *
    * @param field the field name
    * @param lower the lower bound
    * @param includeLower the lower bound include flag
    * @param upper the upper bound
    * @param includeUpper the upper bound include flag
    * @return the new term range query
    */
  def range(field: String)
           (lower: String, includeLower: Boolean = true)
           (upper: String, includeUpper: Boolean = true): TermRangeQuery = {

    requireNotBlank(field, "field")
    requireNotBlank(lower, "lower")
    requireNotBlank(upper, "upper")

    new TermRangeQuery(field, new BytesRef(lower), new BytesRef(upper), includeLower, includeUpper)
  }

  /**
    * Set a boost of query.
    *
    * @param query the query
    * @param boostValue the boost value
    * @tparam T the type of query
    * @return the same query with boost
    */
  def boost[T <: Query](query: T, boostValue: Float): T = {
    requireNotNull(query, "query")

    query.setBoost(boostValue)
    query
  }

  // ==================================================
  // TODO NEW CODE!!! FOR 6.x Solr!!! DON'T REMOVE ====
  //private def toTerm(part: BlendedPart) = new Term(part.field, part.term)
  // ==================================================

//  private def escapeSpaces(string: String, isEscape: Boolean): String = {
//    if(isEscape) escapeSpaces(string) else string
//  }

  private val SpecialCharacters = Seq(
    "+", "-", "&&", "||", "!", "(", ")", "{", "}", "[", "]",
    "^", "\"", "~", "*", "?", ":", "\\")

  private val SpecialCharactersEscapePairsList = Seq(
    ("\\+", "\\\\+"),
    ("\\-", "\\\\-"),
    ("\\(", "\\\\("),
    ("\\)", "\\\\)"),
    ("\\{", "\\\\{"),
    ("\\}", "\\\\}"),
    ("\\[", "\\\\["),
    ("\\]", "\\\\]"),
    ("\\*", "\\\\*"),
    ("\\?", "\\\\?"),
    ("\\s", "\\\\ ")




      //replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)")
  )

//  private def formEscapeList(chars: String*): List[(String, String)] = {
//    chars.map(char => (s"", s""))
//  }

  private def escapeSpecial(string: String, isEscape: Boolean): String = {
    if(isEscape) escapeSpecial(string) else string
  }

  private def escapeSpecial(string: String): String = {
    SpecialCharactersEscapePairsList.foldLeft(string)(es)
  }

  private def es(string: String, esc: (String, String)): String = {
    string.replaceAll(esc._1, esc._2)
  }

  private def escapeSpaces(string: String): String = string.replaceAll("\\s", "\\\\ ")
}




