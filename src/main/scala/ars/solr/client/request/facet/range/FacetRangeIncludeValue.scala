package ars.solr.client.request.facet.range

import ars.solr.client.request.common.Nameable

/** The values for `facet.range.include` request parameter.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
abstract sealed class FacetRangeIncludeValue(name: String) extends Nameable(name)

object FacetRangeIncludeValue {

  /** Lower. */
  object Lower extends FacetRangeIncludeValue("lower")

  /** Upper. */
  object Upper extends FacetRangeIncludeValue("upper")

  /** Edge. */
  object Edge extends FacetRangeIncludeValue("edge")

  /** Outer. */
  object Outer extends FacetRangeIncludeValue("outer")

  /** All. */
  object All extends FacetRangeIncludeValue("all")
}
