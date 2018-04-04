package ars.solr.client.request

import ars.precondition.RequireUtils._
import ars.solr.client.request.common.{Nameable, Switchable}
import org.apache.solr.client.solrj.SolrQuery

/** Apache Solr Scala group request builder.
  *
  * @param solrQuery the Apache Solr query
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.5
  */
class GroupRequestBuilder(solrQuery: SolrQuery) extends Switchable[GroupRequestBuilder] {
  import GroupRequestBuilder._

  /** @inheritdoc */
  override def switch(isOn: Boolean): GroupRequestBuilder = {
    solrQuery.set(Group, isOn)
    this
  }

  /**
    *
    * @param fields
    * @return
    */
  def fields(fields: String*): GroupRequestBuilder = {
    addStrings(GroupField, fields, "fields")
  }

  /**
    *
    * @param queries
    * @return
    */
  def functions(queries: String*): GroupRequestBuilder = {
    addStrings(GroupFunction, queries, "queries")
  }

  def queries(queries: String*): GroupRequestBuilder = {
    addStrings(GroupQuery, queries, "queries")
  }

  def limit(limit: Int): GroupRequestBuilder = {
    requireNonNegative(limit, "limit")

    solrQuery.set(GroupLimit, limit)
    this
  }

  def offset(offset: Int): GroupRequestBuilder = {
    requireNonNegative(offset, "offset")

    solrQuery.set(GroupOffset, offset)
    this
  }

  def format(format: GroupFormatValue): GroupRequestBuilder = {
    requireNotNull(format, "format")

    solrQuery.set(GroupFormat, format.name)
    this
  }

  def main(isMain: Boolean): GroupRequestBuilder = {
    solrQuery.set(GroupMain, isMain)
    this
  }

  def numberOfGroups(isInclude: Boolean): GroupRequestBuilder = {
    solrQuery.set(GroupNgroups, isInclude)
    this
  }

  def truncate(isTruncate: Boolean): GroupRequestBuilder = {
    solrQuery.set(GroupTruncate, isTruncate)
    this
  }

  def percent(percent: Int): GroupRequestBuilder = {
    requireNumberSegment(percent, "percent")(0, 100)

    solrQuery.set(GroupCachePercent, percent)
    this
  }

  def addStrings(field: String, values: Seq[String], name: String): GroupRequestBuilder = {
    requireNotBlank(values, name)
    requireAllNotBlank(values, name)

    solrQuery.add(field, values :_*)
    this
  }

}

object GroupRequestBuilder {
  private val Group = "group"
  private val GroupField = "group.field"
  private val GroupFunction = "group.func"
  private val GroupQuery = "group.query"

  private val GroupLimit = "group.limit"
  private val GroupOffset = "group.offset"
  private val GroupSort = "group.sort"
  private val GroupFormat = "group.format"
  private val GroupMain = "group.main"
  private val GroupNgroups = "group.ngroups"
  private val GroupTruncate = "group.truncate"
  private val GroupFacet = "group.facet"
  private val GroupCachePercent = "group.cache.percent"


  private val GroupRows = "rows"
  private val GroupStart = "start"
  private val GroupSort1 = "sort"
}


abstract class GroupFormatValue(name: String) extends Nameable(name)

object GroupFormatValue {
  object Simple extends GroupFormatValue("simple")
  object Grouped extends GroupFormatValue("grouped")
}
