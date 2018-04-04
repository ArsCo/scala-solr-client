package ars.solr.query

import ars.solr.query.util.TermUtils
import ars.solr.query.util.TermUtils.toTerm
import org.apache.lucene.search.spans._

/** Apache Solr span queries factory.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
class SolrSpanQueryFactory {

  /**
    *
    * @param field
    * @param word
    * @return
    */
  def spanTerm(field: String, word: String): SpanTermQuery = {
    new SpanTermQuery(toTerm(field, word))
  }

  /**
    *
    * @param field
    * @param spanQueries
    * @param slop
    * @param inOrder
    * @param collectPayloads
    * @return
    */
  def spanNear(field: String, spanQueries: SpanQuery*)
              (slop: Int,
               inOrder: Boolean,
               collectPayloads: Boolean = true) : SpanNearQuery = {
    new SpanNearQuery(spanQueries.toArray, slop, inOrder, collectPayloads)
  }

  /**
    *
    * @param spanQueries
    * @return
    */
  def spanOr(spanQueries: SpanQuery*): SpanOrQuery = {
    new SpanOrQuery(spanQueries :_*)
  }

  /**
    *
    * @param include
    * @param exclude
    * @param pre
    * @param post
    * @return
    */
  def spanNot(include: SpanQuery, exclude: SpanQuery,
              pre: Int = 0, post: Int = 0): SpanNotQuery = {
    new SpanNotQuery(include, exclude, pre, post)
  }

  /**
    *
    * @param query
    * @param end
    * @return
    */
  def spanFirst(query: SpanQuery, end: Int): SpanFirstQuery = {
    new SpanFirstQuery(query, end)
  }

  /**
    *
    * @param query
    * @param start
    * @param end
    * @return
    */
  def spanRange(query: SpanQuery, start: Int, end: Int): SpanPositionRangeQuery = {
    new SpanPositionRangeQuery(query, start, end)
  }

}
