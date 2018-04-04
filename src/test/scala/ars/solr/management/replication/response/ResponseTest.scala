package ars.solr.management.replication.response

import ars.solr.AbstractBaseTest

/** Test for [[Response]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
class ResponseTest extends AbstractBaseTest {

  val header = Header(1L, 2L)
  val body = new AnyRef

  "ResponseTest" must "validate args" in {
    Response(header, body)

    intercept[IllegalArgumentException] {
      Response(null, body)
    }

    intercept[IllegalArgumentException] {
      Response(header, null)
    }
  }
}
