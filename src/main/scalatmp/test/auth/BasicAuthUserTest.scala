package ars.solr.auth

import ars.solr.AbstractBaseTest

/** Tests for [[BasicAuthUser]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
class BasicAuthUserTest extends AbstractBaseTest {

  "AuthUser" must "validate args" in {
    BasicAuthUser("Mock user name", "Password%555")

    intercept[IllegalArgumentException] {
      BasicAuthUser(null, "Password%555")
    }

    intercept[IllegalArgumentException] {
      BasicAuthUser("", "Password%555")
    }

    intercept[IllegalArgumentException] {
      BasicAuthUser("   ", "Password%555")
    }

    intercept[IllegalArgumentException] {
      BasicAuthUser("Mock user name", null)
    }

    intercept[IllegalArgumentException] {
      BasicAuthUser("Mock user name", "")
    }

    intercept[IllegalArgumentException] {
      BasicAuthUser("Mock user name", "   ")
    }
  }

  it must "have persistent username" in {
    assertResult("Mock user name") {
      BasicAuthUser("Mock user name", "Password%555").username
    }
    assertResult("Password%555") {
      BasicAuthUser("Mock user name", "Password%555").password
    }
  }
}
