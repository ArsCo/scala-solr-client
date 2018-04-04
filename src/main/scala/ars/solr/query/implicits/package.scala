package ars.solr.query

import scala.language.implicitConversions
import ars.solr.query.SolrBasicQueryFactory.MultiPhrasePart
import org.apache.lucene.search.Query

/** Provides implicit conversions.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
package object implicits {

  /**
    * Implicit conversion of [[Seq[String] ]] value to [[MultiPhrasePart]]. It provides syntax:
    * {{{
    *   multiPhrase("field")(Seq("value_1", "value_2"))
    * }}}
    *
    * @param terms the terms at the same position
    * @return the multiphrase query part
    */
  implicit def stringToMultiPhrasePart(terms: Seq[String]): MultiPhrasePart = MultiPhrasePart(terms)

  /**
    * Implicit conversion of [[String]] value to [[MultiPhrasePart]]. It provides syntax:
    * {{{
    *   multiPhrase("field")("value_1", "value_2")
    * }}}
    *
    * @param term the term
    * @return the multiphrase query part
    */
  implicit def stringToMultiPhrasePart(term: String): MultiPhrasePart = new MultiPhrasePart(term)

  /**
    * Implicit conversion from [[String]] to [[SolrRichFieldQuery]].
    *
    * @param string the field name
    * @return the new instance of [[SolrRichFieldQuery]]
    */
  implicit def stringToRichQuery(string: String): SolrRichFieldQuery = new SolrRichFieldQuery(string)

  /**
    * Implicit conversion from [[Query]] to [[SolrRichFieldQuery]].
    *
    * @param query the query
    * @return the new instance of [[SolrRichQuery]]
    */
  implicit def queryToRichQuery[T <: Query](query: T): SolrRichQuery[T] = new SolrRichQuery(query)

}
