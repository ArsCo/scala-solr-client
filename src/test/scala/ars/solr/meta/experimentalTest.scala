package ars.solr.meta

import ars.solr.AbstractBaseTest

/** Tests for [[experimental]] package.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
class experimentalTest extends AbstractBaseTest {

  "experimental" must "have valid interface" in {
    val e = new experimental("cause value")
    assert(e != null)
    assertResult("cause value") { e.cause }
  }

  it must "have empty default cause" in {
    val e = new experimental()
    assert(e != null)
    assertResult("") { e.cause }
  }
}
