package ars.solr.client.request.facet.range

import ars.solr.client.request.common.Nameable

/** The values for `facet.range.method` request parameter.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
abstract sealed class FacetRangeMethod(name: String) extends Nameable(name)

object FacetRangeMethod {

  /** Filter. */
  object Filter extends FacetRangeMethod("filter")

  /** Document values */
  object DocumentValues extends FacetRangeMethod("dv")
}
