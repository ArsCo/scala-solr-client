package ars.solr.client.request.facet

import ars.solr.client.request.common.Nameable
import org.apache.solr.common.params.FacetParams.{FACET_SORT_COUNT, FACET_SORT_INDEX}

/** The values for `facet.sort` request parameter.
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.4
  */
abstract sealed class FacetSortType(name: String) extends Nameable(name)

object FacetSortTypes {

  /** Count. */
  object Count extends FacetSortType(FACET_SORT_COUNT)

  /** Index. */
  object Index extends FacetSortType(FACET_SORT_INDEX)
}
