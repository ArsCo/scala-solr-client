package ars.solr.client.request

import ars.precondition.RequireUtils.{requireAllNotBlank, requireNotBlank, requirePositive}
import ars.solr.client.request.common.Switchable
import org.apache.solr.client.solrj.SolrQuery

/** Apache Solr Scala terms request builder.
  *
  * @param solrQuery the solr native query
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
class TermsRequestBuilder(solrQuery: SolrQuery) extends Switchable[TermsRequestBuilder] {

  /**
    * @param isOn `true` to switch on and `false` to switch off group.
    * @return the `this` instance
    */
  override def switch(isOn: Boolean): TermsRequestBuilder = {
    solrQuery.setTerms(isOn)
    this
  }

  /**
    * Sets query parameter `terms.limit`.
    *
    * @param limit the limit
    * @return the `this` instance
    */
  def termsLimit(limit: Int): TermsRequestBuilder = {
    requirePositive(limit, "limit")

    solrQuery.setTermsLimit(limit)
    this
  }

  /**
    * Sets query parameters `terms.lower` and `terms.lower.incl`.
    *
    * @param lower the lower
    * @param isInclusive is inclusive
    * @return the `this` instance
    */
  def termsLower(lower: String, isInclusive: Boolean): TermsRequestBuilder = {
    requireNotBlank(lower, "lower")

    solrQuery.setTermsLower(lower)
    solrQuery.setTermsLowerInclusive(isInclusive)
    this
  }

  /**
    * Sets query parameters `terms.upper` and `terms.upper.incl`.
    *
    * @param upper the upper
    * @param isInclusive is inclusive
    * @return the `this` instance
    */
  def termsUpper(upper: String, isInclusive: Boolean): TermsRequestBuilder = {
    requireNotBlank(upper, "upper")

    solrQuery.setTermsUpper(upper)
    solrQuery.setTermsUpperInclusive(isInclusive)
    this
  }

  /**
    * Sets query parameter `terms.mincount`.
    *
    * @param minCount the maximum count
    * @return the `this` instance
    */
  def termsMinCount(minCount: Int): TermsRequestBuilder = {
    requirePositive(minCount, "minCount") // TODO 0???

    solrQuery.setTermsMinCount(minCount)
    this
  }

  /**
    * Sets query parameter `terms.maxcount`.
    *
    * @param maxCount the maximum count
    * @return the `this` instance
    */
  def termsMaxCount(maxCount: Int): TermsRequestBuilder = {
    requirePositive(maxCount, "maxCount")

    solrQuery.setTermsMaxCount(maxCount)
    this
  }

  /**
    * Sets query parameters `terms.mincount` and `terms.maxcount`.
    *
    * @param minCount the minimum count
    * @param maxCount the maximum count
    * @return the `this` instance
    */
  def termsCount(minCount: Int, maxCount: Int): TermsRequestBuilder = {
    requirePositive(minCount, "minCount") // TODO 0???
    requirePositive(maxCount, "maxCount")

    termsMinCount(minCount)
    termsMaxCount(maxCount)
  }

  /**
    * Sets query parameter `terms.prefix`.
    *
    * @param prefix the prefix
    * @return the `this` instance
    */
  def termsPrefix(prefix: String): TermsRequestBuilder = {
    requireNotBlank(prefix, "prefix")

    solrQuery.setTermsPrefix(prefix)
    this
  }

  /**
    * Sets query parameter `terms.raw`.
    *
    * @param isRaw is raw
    * @return the `this` instance
    */
  def termsRaw(isRaw: Boolean): TermsRequestBuilder = {
    solrQuery.setTermsRaw(isRaw)
    this
  }

  /**
    *
    * @param regExp
    * @param flags
    * @return
    */
  def termsRegexp(regExp: String, flags: String): TermsRequestBuilder = {
    requireNotBlank(regExp, "regExp")
    requireNotBlank(flags, "flags") // TODO ??? Empty

    solrQuery.setTermsRegex(regExp)
    solrQuery.setTermsRegexFlag(flags)
    this
  }

  /**
    *
    * @param string
    * @return
    */
  def termsSort(string: String): TermsRequestBuilder = {
    requireNotBlank(string, "string")

    solrQuery.setTermsSortString(string)
    this
  }

  /**
    *
    * @param fields
    * @return
    */
  def termsFields(fields: String*): TermsRequestBuilder = {
    requireNotBlank(fields, "fields")
    requireAllNotBlank(fields, "fields")

    fields.foreach(solrQuery.addTermsField)
    this
  }
}
