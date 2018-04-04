package ars.solr.client

import ars.solr.AbstractBaseTest

/** Tests for [[MappingErrorMode]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
class MappingErrorModeTest extends AbstractBaseTest {

  "MappingErrorMode" must "have all values" in {
    MappingErrorModes.Abort
    MappingErrorModes.DiscardFailed
  }
}
