package ars.solr.client.request.facet

import ars.solr.client.request.common.Nameable
import org.apache.solr.common.params.FacetParams._

/** The values for `facet.missing` request parameter.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
abstract sealed class FacetMethod(name: String) extends Nameable(name)

object FacetMethod {

  /** Enum. */
  object Enum extends FacetMethod(FACET_METHOD_enum)

  /** Facet counts. */
  object FacetCounts extends FacetMethod(FACET_METHOD_fc)

  /** Per-segment field faceting for single-valued string fields. */
  object SegmentFacetCounts extends FacetMethod(FACET_METHOD_fcs)
}
