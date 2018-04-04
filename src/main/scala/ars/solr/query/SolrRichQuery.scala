package ars.solr.query

import org.apache.lucene.search.Query

/** Apache Solr query DSL.
  *
  * This objects contains methods that provides awesome syntax for some complex queries.
  *
  * For `and` complex query you can write the code:
  * {{{
  *   val q1: Query = ...
  *   val q2: Query = ...
  *   val q3: Query = ...
  *
  *   q1 AND q2
  *   q1 AND q2 AND q3
  *   q1 AND (q1, q2)
  *
  *   q1 && q2
  *   q1 && q2 & q3
  *   q1 && (q1, q2)
  * }}}
  *
  * For `or` complex query you can write the code:
  * {{{
  *   val q1: Query = ...
  *   val q2: Query = ...
  *   val q3: Query = ...
  *
  *   q1 OR q2
  *   q1 OR q2 OR q3
  *   q1 OR (q1, q2)
  *
  *   q1 || q2
  *   q1 || q2 || q3
  *   q1 || (q1, q2)
  * }}}
  *
  * For `not` query you can write the code:
  * {{{
  *   val q1: Query = ...
  *
  *   q1.not
  *   !q1
  * }}}
  *
  * You can create complex queries to Apache Solr with awesome syntax. For example:
  * {{{
  *   val q1: Query = ...
  *   val q2: Query = ...
  *   val q3: Query = ...
  *   val q4: Query = ...
  *
  *   q1 || !q2*0.7f && q3*1.3f || q4.boost(0.65f)
  *
  * }}}
  *
  * '''NOTE:''' You could not use parentheses with `'&&'`, `'||'` operators because of Scala priority rules,
  * but you must use it for `'AND'` and `'OR'` operators for the right priority.
  *
  * For example:
  * {{{
  *   val q1: Query = ...
  *   val q2: Query = ...
  *   val q3: Query = ...
  *
  *   q1 OR q2 AND q3 // is (q1 OR q2) AND q3
  *   q1 || q2 && q3  // is q1 || (q2 && q3)
  * }}}
  *
  *
  * @param query the query
  * @tparam T the query type
  */
class SolrRichQuery[T <: Query](val query: T) {

  /**
    * Creates an `and` query. This method is a syntax sugar for [[SolrComplexQueryFactory.and()]].
    *
    * It provides the syntax:
    * {{{
    *   val q1: Query = ...
    *   val q2: Query = ...
    *   val q3: Query = ...
    *
    *   q1 AND q2
    *   q1 AND q2 AND q3
    *   q1 AND (q1, q2)
    * }}}
    *
    * @param queries the queries
    * @return the new instance of `and` query
    */
  def AND(queries: Query*): Query = SolrComplexQueryFactory.and(query, queries: _*)

  /**
    * Creates an `or` query. This method is a syntax sugar for [[SolrComplexQueryFactory.or()]].
    *
    * It provides the syntax:
    * {{{
    *   val q1: Query = ...
    *   val q2: Query = ...
    *   val q3: Query = ...
    *
    *   q1 OR q2
    *   q1 OR q2 OR q3
    *   q1 OR (q1, q2)
    * }}}
    *
    * @param queries the words following the first
    * @return the new instance of `or` query
    */
  def OR(queries: Query*): Query = SolrComplexQueryFactory.or(query, queries: _*)

  /**
    * Creates a `not` query. This method is a syntax sugar for [[SolrComplexQueryFactory.not()]].
    *
    * It provides the syntax:
    * {{{
    *   val q1: Query = ...
    *   q1.not
    * }}}
    *
    * @return the new instance of `not` query
    */
  def not: Query = SolrComplexQueryFactory.not(query)

  /**
    * Creates a boosted query. This method is a syntax sugar for
    * [[SolrComplexQueryFactory.boost[T<:Query](query:T*]].
    *
    * It provides the postfix boost syntax:
    * {{{
    *   val q1: Query = ...
    *   q1.boost(1.6)
    * }}}
    *
    * @return the new instance of `not` query
    */
  def boost(b: Float): T = SolrBasicQueryFactory.boost(query, b)

  /**
    * Creates an `and` query. This method is a syntax sugar for [[SolrComplexQueryFactory.and()]].
    *
    * It provides the syntax:
    * {{{
    *   val q1: Query = ...
    *   val q2: Query = ...
    *   val q3: Query = ...
    *
    *   q1 && q2
    *   q1 && q2 && q3
    *   q1 && (q1, q2)
    * }}}
    *
    * @param queries the queries
    * @return the new instance of `and` query
    */
  def &&(queries: Query*): Query = AND(queries: _*)

  /**
    * Creates an `or` query. This method is a syntax sugar for [[SolrComplexQueryFactory.or()]].
    *
    * It provides the syntax:
    * {{{
    *   val q1: Query = ...
    *   val q2: Query = ...
    *   val q3: Query = ...
    *
    *   q1 || q2
    *   q1 || q2 || q3
    *   q1 || (q1, q2)
    * }}}
    *
    * @param queries the words following the first
    * @return the new instance of `or` query
    */
  def ||(queries: Query*): Query = OR(queries: _*)

//  /**
//    * Creates a `not` query. This method is a syntax sugar for [[SolrComplexQueryFactory.not()]].
//    *
//    * It provides the prefix syntax:
//    * {{{
//    *   val q1: Query = ...
//    *   !q1
//    * }}}
//    *
//    * @return the new instance of `not` query
//    */
//  def unary_! : Query = query.not

  /**
    * Creates a boosted query. This method is a syntax sugar for
    * [[SolrComplexQueryFactory.boost[T<:Query](query:T*]].
    *
    * It provides the postfix boost syntax:
    * {{{
    *   val q1: Query = ...
    *   q1 * 1.6
    * }}}
    *
    * @return the new instance of `not` query
    */
  def *(boost: Float): T = SolrBasicQueryFactory.boost(query, boost)
}



