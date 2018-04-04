package ars.solr.client.request.facet

import java.util.Date

import ars.precondition.Predicates.BoundTypes
import ars.precondition.RequireUtils._
import ars.solr.meta.experimental
import ars.solr.client.request.common.Switchable
import ars.solr.client.request.facet.FacetRequestBuilder.FacetContainsIgnoreCase
import ars.solr.client.request.facet.range.FacetFieldRangeRequestBuilder
import org.apache.lucene.search.Query
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.common.params.FacetParams
import org.apache.solr.common.params.FacetParams._


/** Apache Solr Scala facets request builder.
  *
  * @param solrQuery the Apache Solr query
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
class FacetRequestBuilder(solrQuery: SolrQuery) extends Switchable[FacetRequestBuilder] with FacetFieldRequestParams  {
  import FacetRequestBuilder._

  private var _queryStrings: Set[String] = Set()
  private var _queries: Set[Query] = Set()

  private var _fields: Set[String] = Set()
  private var _prefix: Option[String] = None

  private var _contains: Option[(String, Boolean)] = None

  private var _sortType: Option[FacetSortType] = None
  private var _limit: Option[Int] = None
  private var _offset: Option[Int] = None
  private var _minCount: Option[Int] = None
  private var _withMissing: Option[Boolean] = None
  private var _facetMethod: Option[FacetMethod] = None
  private var _frequency: Option[Int] = None
  private var _binary: Option[Boolean] = None
  private var _excludingTerms: Set[String] = Set()
  private var _overrequest: Option[(Int, Float)] = None
  private var _limitThreads: Option[Int] = None

  // ---------- Field params ----------

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `queries` is blank or contains blank string")
  def addQueries(queries: String*): FacetRequestBuilder = {
    requireNotBlank(queries, "queries")
    requireAllNotBlank(queries, "queries")

    _queryStrings ++= queries.toSet
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `queries` is blank or contains null query")
  def addQueries(queries: Query*): FacetRequestBuilder = {
    requireNotBlank(queries, "queries")
    requireAllNotNull(queries, "queries")

    _queries ++= queries
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `fields` is blank or contains blank string")
  def addFields(fields: String*): FacetRequestBuilder = {
    requireNotNull(fields, "fields")
    requireAllNotBlank(fields, "fields")

    _fields ++= fields
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `prefix` is blank")
  def prefix(prefix: String): FacetRequestBuilder = {
    requireNotBlank(prefix, "prefix")

    _prefix = Some(prefix)
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `substring` is blank")
  def contains(substring: String, isCaseSensitive: Boolean = true): FacetRequestBuilder = {
    requireNotBlank(substring, "substring")

    _contains = Some((substring, isCaseSensitive))
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `sortType` is `null`")
  def sortBy(sortType: FacetSortType): FacetRequestBuilder = {
    requireNotNull(sortType, "sortingType")

    _sortType = Some(sortType)
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `limit` is negative")
  def limit(limit: Int): FacetRequestBuilder = {
    requireNonNegative(limit, "limit")

    _limit = Some(limit)
    this
  }

  /** @inheritdoc */
  def unlimited(): FacetRequestBuilder = {
    _limit = Some(-1)
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `offset` is negative")
  def offset(offset: Int): FacetRequestBuilder = {
    requireNonNegative(offset, "offset")

    _offset = Some(offset)
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `count` is negative")
  def minCount(count: Int): FacetRequestBuilder = {
    requireNonNegative(count, "count")

    _minCount = Some(count)
    this
  }

  /** @inheritdoc */
  def withMissing(): FacetRequestBuilder = {
    _withMissing = Some(true)
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `method` is `null`")
  def method(method: FacetMethod): FacetRequestBuilder = {
    requireNotNull(method, "method")

    _facetMethod = Some(method)
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `frequency` is negative")
  def minFrequency(frequency: Int): FacetRequestBuilder = {
    requireNonNegative(frequency, "frequency")

    _frequency = Some(frequency)
    this
  }

  /** @inheritdoc */
  def binary(): FacetRequestBuilder = {
    _binary = Some(true)
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `fields` is blank or contains blank string")
  def addExcludingTerms(terms: String*): FacetRequestBuilder = {
    requireNotBlank(terms, "terms")
    requireAllNotBlank(terms, "terms")

    _excludingTerms ++= terms
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if `count` or `ratio` is non-positive")
  def overrequest(count: Int, ratio: Float): FacetRequestBuilder = {
    requirePositive(count, "count")
    requirePositive(ratio, "ratio")

    _overrequest = Some(count, ratio)
    this
  }

  /** @inheritdoc */
  @throws[IllegalArgumentException]("if any argument isn't valid")
  def limitThreads(number: Int): FacetRequestBuilder = {
    requireNonNegative(number, "number")

    _limitThreads = Some(number)
    this
  }

  /** @inheritdoc */
  def unlimitedThreads(): FacetRequestBuilder = {
    _limitThreads = Some(-1)
    this
  }

  // ---------- Range facets ----------














  // ---------- Pivot facets ----------

  /**
    * Sets pivot (Decision Tree) faceting fields.
    * The Apache Solr request parameter `facet.pivot` will be set.
    *
    * @param fields the pivot fields
    * @return
    */
  def pivot(fields: String*): FacetRequestBuilder = {
    requireNotNull(fields, "fields")
    requireAllNotBlank(fields, "fields")

    solrQuery.addFacetPivotField(fields :_*)
    this
  }
  /**
    * Sets pivot (Decision Tree) faceting fields and minimum number of documents
    * that need to match in order for the facet to be included in results.
    * The Apache Solr request parameters `facet.pivot` and `facet.pivot.mincount` will be set.
    *
    * @param fields the pivot fields
    * @return
    */
  def pivot(minCount: Int, fields: String*): FacetRequestBuilder = {
    requireNonNegative(minCount, "minCount")

    solrQuery.set(FACET_PIVOT_MINCOUNT, minCount)
    pivot(fields :_*)

    this
  }


  def build(solrQuery: SolrQuery): SolrQuery = {
    buildFieldParams()








  }

  def buildFieldParams(): Unit = {
    val allQueries = _queryStrings ++ _queries.map(_.toString)
    allQueries.foreach(solrQuery.addFacetQuery)

    solrQuery.addFacetField(_fields.toSeq :_*)

    _prefix.map(solrQuery.setFacetPrefix)

    _contains.map { case (substring, isCaseSensitive) =>
      solrQuery.set(containsParamName(isCaseSensitive), substring)
    }

    _sortType.map { sortType => solrQuery.setFacetSort(sortType.name) }
    _limit.map(solrQuery.setFacetLimit)
    _offset.map(solrQuery.set(FACET_OFFSET, _))
    _minCount.map(solrQuery.setFacetMinCount)
    _withMissing.map(v => solrQuery.setFacetMissing(v))
    _facetMethod.map(method => solrQuery.set(FACET_METHOD, method.name))
    _frequency.map { f =>
      _facetMethod match {
        case Some(FacetMethod.Enum) =>
          solrQuery.set("facet.enum.cache.minDf", f)

        case _ => throw new IllegalStateException("This method can be called only if facet.method=enum.")
      }
    }
    _binary.map {b =>
      _facetMethod match {
        case None | Some(FacetMethod.Enum) =>
          solrQuery.set(FacetExists, true)

        case _ => throw new IllegalStateException("This method can be called only if facet.method=enum or when it’s omitted.")
      }
    }

    if (_excludingTerms.nonEmpty) {
      val termsString = _excludingTerms.mkString(",")
      solrQuery.set(FacetExcludeTerms, termsString)
    }

    _overrequest.map { case (count, ratio) =>
      solrQuery.set(FacetOverrequestCount, count)
      solrQuery.set(FacetOverrequestRatio, ratio.toString)
    }

    _limitThreads.map(solrQuery.set(FACET_THREADS, _))
  }


  // ---------- Interval facets ----------

  //  def intervalFacets(field: String): FacetRequestBuilder = {
  //    solrQuery.addIntervalFacets(String field, String[] intervals)
  // String[]	removeIntervalFacets(String field)
  //  }


  // ---------- Per field parameters ----------

  def perField(field: String): FacetFieldRequestBuilder = {
    new FacetFieldRequestBuilder(solrQuery, field)
  }


  def dateRange(field: String)
               (builder: (FacetFieldRangeRequestBuilder[Date]) => Unit): Unit = {
    builder(new FacetFieldRangeRequestBuilder(solrQuery, field))
  }

  def intRange[@specialized T <: Numeric[T]](field: String)
                                            (builder: (FacetFieldRangeRequestBuilder[T]) => Unit): Unit = {
    builder(new FacetFieldRangeRequestBuilder(solrQuery, field))
  }


  def requireNotSetBefore[T](value: T, parameter: String): Unit = {
    if (value != null)
      throw new IllegalStateException(s"Parameter `$parameter` already was set before. It cab be set at most once.")
  }






//  def test(): Unit = {
//    this.dateRange("аааа") {
//      _.
//    }
//
//
//    this.intRange("field") {
//      _.dateRange()
//    }
//  }
}


object FacetRequestBuilder {
  private[request] val FacetContains = "facet.contains"
  private[request] val FacetContainsIgnoreCase = "facet.contains.ignoreCase"
  private[request] val FacetExists = "facet.exists"
  private[request] val FacetExcludeTerms = "facet.excludeTerms"
  private[request] val FacetOverrequestCount = "facet.overrequest.count"
  private[request] val FacetOverrequestRatio = "facet.overrequest.ratio"


  private[request] def fieldParam(field: String, param: String) = s"f.$field.$param"


  private[request] def containsParamName(isCaseSensitive: Boolean): String = {
    if (isCaseSensitive) FacetContains else FacetContainsIgnoreCase
  }

  private def example(): Unit = {

  }


}

