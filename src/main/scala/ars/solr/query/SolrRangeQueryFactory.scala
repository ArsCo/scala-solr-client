package ars.solr.query

import org.apache.lucene.search.NumericRangeQuery

/**
  * Created by ars on 10/02/2017.
  */
class SolrRangeQueryFactory {

  def doubleRange(field: String, precision: Int = 16)
                 (min: Double, minInclusive: Boolean)
                 (max: Double, maxInclusive: Boolean): NumericRangeQuery[java.lang.Double] = {
    NumericRangeQuery.newDoubleRange(field, precision, min, max, minInclusive, maxInclusive)
  }

  def floatRange(field: String, precision: Int = 8)
                (min: Float, minInclusive: Boolean)
                (max: Float, maxInclusive: Boolean): NumericRangeQuery[java.lang.Float] = {
    NumericRangeQuery.newFloatRange(field, precision, min, max, minInclusive, maxInclusive)
  }

  def longRange(field: String, precision: Int = 16)
               (min: Long, minInclusive: Boolean)
               (max: Long, maxInclusive: Boolean): NumericRangeQuery[java.lang.Long] = {
    NumericRangeQuery.newLongRange(field, precision, min, max, minInclusive, maxInclusive)
  }

  def intRange(field: String, precision: Int = 8)
              (min: Int, minInclusive: Boolean)
              (max: Int, maxInclusive: Boolean): NumericRangeQuery[java.lang.Integer] = {
    NumericRangeQuery.newIntRange(field, precision, min, max, minInclusive, maxInclusive)
  }


//  val f[T]: (String, Int)(Int, Boolean)(Int, Boolean) => NumericRangeQuery[T] = {
//
//  }




//
//  def range[T](field: String, precision: Int = 8)
//              (min: Int, minInclusive: Boolean)
//              (max: Int, maxInclusive: Boolean)
//              (implicit toT: ()): NumericRangeQuery[T] = {
//
//  }

}
