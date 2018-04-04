package ars.solr.client.request

import ars.precondition.RequireUtils._
import ars.solr.client.request.common.{Nameable, Switchable}
import org.apache.solr.client.solrj.SolrQuery

/** Apache Solr Scala spatial request builder.
  *
  * @param solrQuery the Apache Solr query
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.5
  */
class MoreLikeThisRequestBuilder(solrQuery: SolrQuery) extends Switchable[MoreLikeThisRequestBuilder] {
  import MoreLikeThisRequestBuilder._

  /** @inheritdoc */
  override def switch(isOn: Boolean): MoreLikeThisRequestBuilder = {
    solrQuery.set(MoreLikeThis, isOn)
    this
  }

  def fields(fields: String*): MoreLikeThisRequestBuilder = {
    requireNotBlank(fields, "fields")
    requireAllNotBlank(fields, "fields")

    solrQuery.add(Fields, fields :_*)
    this
  }

  def minTermFrequency(frequency: Int): MoreLikeThisRequestBuilder = {
    setPositiveInt(MinTermFrequency, frequency, "frequency")
  }

  def minDocFrequency(frequency: Int): MoreLikeThisRequestBuilder = {
    setPositiveInt(MinDocFrequency, frequency, "frequency")
  }

  def maxDocFrequency(frequency: Int): MoreLikeThisRequestBuilder = {
    setPositiveInt(MaxDocumentFrequency, frequency, "frequency")
  }

  def minWordLength(length: Int): MoreLikeThisRequestBuilder = {
    setNonNegative(MinWordLength, length, "length")
  }

  def maxWordLength(length: Int): MoreLikeThisRequestBuilder = {
    setNonNegative(MaxWordLength, length, "length")
  }

  def maxQueryTerms(max: Int): MoreLikeThisRequestBuilder = {
    setPositiveInt(MaxQueryTerms, max, "max")
  }

  def maxTokens(max: Int): MoreLikeThisRequestBuilder = {
    setPositiveInt(MaxTokens, max, "max")
  }

  def queryBoost(isBoost: Boolean): MoreLikeThisRequestBuilder = {
    solrQuery.set(QueryBoost, isBoost)
    this
  }

  def queryFields(fields: String*): MoreLikeThisRequestBuilder = {
    requireNotBlank(fields, "fields")
    requireAllNotBlank(fields, "fields")

    solrQuery.add(QueryFields, fields :_*)
    this
  }

  def maxCount(count: Int): MoreLikeThisRequestBuilder = {
    setPositiveInt(MaxCount, count, "count")
  }

  def matchInclude(isInclude: Boolean): MoreLikeThisRequestBuilder = {
    solrQuery.set(MatchInclude, isInclude)
    this
  }

  def matchOffset(offset: Int): MoreLikeThisRequestBuilder = {
    setNonNegative(MatchOffset, offset, "offset")
  }

  def interestingTerms(value: InterestingTermsValue): MoreLikeThisRequestBuilder = {
    requireNotNull(value, "value")

    solrQuery.set(InterestingTerms, value.name)
    this
  }

  private def setPositiveInt(field: String, value: Int, name: String) = {
    requirePositive(value, name)

    solrQuery.set(field, value)
    this
  }

  private def setNonNegative(field: String, value: Int, name: String) = {
    requireNonNegative(value, name)

    solrQuery.set(field, value)
    this
  }
}

object MoreLikeThisRequestBuilder {
  val MoreLikeThis = "mlt"

  val Fields = "mlt.fl"

  val MinTermFrequency = "mlt.mintf"

  val MinDocFrequency = "mlt.mindf"
  val MaxDocumentFrequency = "mlt.maxdf"

  val MinWordLength = "mlt.minwl"
  val MaxWordLength = "mlt.maxwl"

  val MaxQueryTerms = "mlt.maxqt"
  val MaxTokens = "mlt.maxntp"

  val QueryBoost = "mlt.boost"
  val QueryFields = "mlt.qf"

  val MaxCount = "mlt.count"

  val MatchInclude = "mlt.match.include"
  val MatchOffset = "mlt.match.offset"

  val InterestingTerms = "mlt.interestingTerms"


}

abstract sealed class InterestingTermsValue(name: String) extends Nameable(name)

object InterestingTermsValue {
  object List extends InterestingTermsValue("list")
  object None extends InterestingTermsValue("none")
  object Details extends InterestingTermsValue("details")
}
