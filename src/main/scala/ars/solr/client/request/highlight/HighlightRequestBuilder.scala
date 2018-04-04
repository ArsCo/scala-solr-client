package ars.solr.client.request.highlight

import ars.precondition.RequireUtils.{requireAllNotBlank, requireNotBlank, requireNotNull, requirePositive}
import ars.solr.meta.experimental
import ars.solr.client.request.common.Switchable
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.common.params.HighlightParams._

/** Apache Solr Scala highlights request builder.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
class HighlightRequestBuilder(solrQuery: SolrQuery) extends Switchable[HighlightRequestBuilder] {
  import HighlightRequestBuilder._

  /** @inheritdoc */
  def switch(isOn: Boolean): HighlightRequestBuilder = {
    solrQuery.setHighlight(isOn)
    this
  }

//  /**
//    * Sets the highlighting implementation to use.
//    * The Apache Solr request parameter `hl.meethod` will be set.
//    *
//    * @param method the method
//    * @return the `this` instance
//    */
//  @throws[IllegalArgumentException]("if any argument isn't valid")
//  def method(method: HighlightMethod): HighlightRequestBuilder = {
//    requireNotNull(method, "method")
//
//    solrQuery.set(HighlightMethod, method.name) // TODO Rewrite when it'll be ni SolrJ API
//    this
//  }

  /**
    * Sets the a list of fields to highlight. Accepts a comma- or space-delimited list of fields for which
    * Solr should generate highlighted snippets. A wildcard of `*` (asterisk) can be used to match field
    * globs, such as 'text_*' or even '*' to highlight on all fields where highlighting is possible.
    * When using `*`, consider adding `hl.requireFieldMatch=true` [[requireFieldMatch()]].
    * The Apache Solr request parameter `hl.fl` will be set.
    *
    * @param fields the fields
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def fields(fields: String*): HighlightRequestBuilder = {
    requireNotNull(fields, "fields")
    requireAllNotBlank(fields, "fields")

    fields.foreach(solrQuery.addHighlightField)
    this
  }


  @experimental
  def noFields(): HighlightRequestBuilder = {
    val fields = solrQuery.getHighlightFields
    fields.foreach (solrQuery.removeHighlightField)

    this
  }

  /**
    * Sets the query to use for highlighting.
    * This parameter allows you to highlight different terms than those being used to retrieve documents.
    * The Apache Solr request parameter `hl.q` will be set.
    *
    * @param query the query
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def query(query: String): HighlightRequestBuilder = {
    requireNotBlank(query, "query")

    solrQuery.set(Q, query) // TODO Change when it'll be in SolrJ API
    this
  }

  /**
    * Sets the query parser to use for `the hl.q` query.
    * The Apache Solr request parameter `hl.qparser` will be set.
    *
    * @param parser the query
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def parser(parser: String): HighlightRequestBuilder = {
    requireNotBlank(parser, "parser")

    solrQuery.set(QPARSER, parser) // TODO Change when it'll be in SolrJ API
    this
  }

  /**
    * If `false`, all query terms will be highlighted for each field to be highlighted `hl.fl` no matter
    * what fields the parsed query refer to. If set to `true`, only query terms aligning with the field
    * being highlighted will in turn be highlighted.
    * The Apache Solr request parameter `hl.requireFieldMatch` will be set.
    *
    * @param isSet weather set or not
    * @return the `this` instance
    */
  def requireFieldMatch(isSet: Boolean): HighlightRequestBuilder = {
    solrQuery.setHighlightRequireFieldMatch(isSet)
    this
  }

  /**
    *
    * @param isSet
    * @return
    */
  def usePhraseHighlighter(isSet: Boolean): HighlightRequestBuilder = {
    setBoolean(USE_PHRASE_HIGHLIGHTER, isSet)
    this
  }

  def highlightMultiTerm(isSet: Boolean): HighlightRequestBuilder = {
    setBoolean(HIGHLIGHT_MULTI_TERM, isSet)
    this
  }

  /**
    * Specifies maximum number of highlighted snippets to generate per field.
    * It is possible for any number of snippets from zero to this value to be generated.
    * The Apache Solr request parameter `hl.snippets` will be set.
    *
    * @param number the number
    * @return the `this` instance
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def snippets(number: Int): HighlightRequestBuilder = {
    requirePositive(number, "number")

    solrQuery.setHighlightSnippets(number)
    this
  }






  /**
    * Specifies the approximate size, in characters, of fragments to consider for highlighting.
    * 0 indicates that no fragmenting should be considered and the whole field value should be used.
    *
    * @param size
    * @return
    */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def fragmentSize(size: Int): HighlightRequestBuilder = {
    requirePositive(size, "size")

    solrQuery.setHighlightFragsize(size)
    this
  }


  /* hl.tag.pre	<em>	(hl.simple.pre for the Original Highlighter)
  Specifies the “tag” to use before a highlighted term. This can be any string,
   but is most often an HTML or XML tag.
hl.tag.post */

  @throws[IllegalArgumentException]("if any argument isn't valid")
  def simplePre(pre: String): HighlightRequestBuilder = {
    requireNotNull(pre, "pre")

    solrQuery.setHighlightSimplePre(pre)
    this
  }

  @throws[IllegalArgumentException]("if any argument isn't valid")
  def simplePost(post: String): HighlightRequestBuilder = {
    requireNotNull(post, "post")

    solrQuery.setHighlightSimplePost(post)
    this
  }

  @throws[IllegalArgumentException]("if any argument isn't valid")
  def encoder(encoder: HighlightEncoder): HighlightRequestBuilder = {
    requireNotNull(encoder, "encoder")

    solrQuery.set(ENCODER, encoder.name)
    this
  }

  @throws[IllegalArgumentException]("if any argument isn't valid")
  def maxAnalyzedChars(limit: Int): HighlightRequestBuilder = {
    requirePositive(limit, "limit")

    solrQuery.set(MAX_CHARS, limit)
    this
  }



  private def setBoolean(param: String, isSet: Boolean) {
    if (isSet) {
      solrQuery.set(param)
    } else {
      solrQuery.set(param, null)
    }
  }


}

object HighlightRequestBuilder {
  private val HighlightMethod = "hl.method"

}

sealed case class HighlightEncoder(name: String)

object HighlightEncoder {
  object Default extends HighlightEncoder(null)
  object Html extends HighlightEncoder("html")
}

abstract case class UnifiedOffsetSource(name: String)

object UnifiedOffsetSource {
  object Analysis extends UnifiedOffsetSource("ANALYSIS")
  object Posting extends  UnifiedOffsetSource("POSTINGS")
  object PostingWithTermVectors extends UnifiedOffsetSource("POSTINGS_WITH_TERM_VECTORS")
  object TermVectors extends UnifiedOffsetSource("TERM_VECTORS")
}


class UnifiedHighlightRequestBuilder(solrQuery: SolrQuery) {
  import UnifiedHighlightRequestBuilder._

  /**
    *
    * @param source
    * @return the `this` instance
    */
  def offsetSource(source: UnifiedOffsetSource): UnifiedHighlightRequestBuilder = {
    requireNotNull(source, "source")

    solrQuery.set(OffsetSource, source.name)
    this
  }

  /**
    *
    * @param isDefaultSummary
    * @return the `this` instance
    */
  def defaultSummary(isDefaultSummary: Boolean): UnifiedHighlightRequestBuilder = {
    solrQuery.set(DefaultSummary, isDefaultSummary)
    this
  }

  /**
    *
    * @param k1
    * @param b
    * @param pivot
    * @return the `this` instance
    */
  def score(k1: Float = 1.2f, b: Float = 0.75f, pivot: Int = 87): UnifiedHighlightRequestBuilder = {
    solrQuery.set(SCORE_K1, k1.toString)
    solrQuery.set(SCORE_B, b.toString)
    solrQuery.set(SCORE_PIVOT, pivot)
    this
  }
}

object UnifiedHighlightRequestBuilder {
  private val OffsetSource = "hl.offsetSource"
  private val DefaultSummary = "hl.defaultSummary"
}


