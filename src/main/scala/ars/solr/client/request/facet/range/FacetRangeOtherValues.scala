package ars.solr.client.request.facet.range

import ars.solr.client.request.common.Nameable

/** The values for `facet.range.other` request parameter.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
abstract sealed class FacetRangeOtherValue(name: String) extends Nameable(name)

object FacetRangeOtherValue {

  /** Before. */
  object Before extends FacetRangeOtherValue("before")

  /** After. */
  object After extends FacetRangeOtherValue("after")

  /** Between. */
  object Between extends FacetRangeOtherValue("between")

  /** None. */
  object None extends FacetRangeOtherValue("none")

  /** All. */
  object All extends FacetRangeOtherValue("all")
}
