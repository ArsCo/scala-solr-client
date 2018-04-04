package ars.solr.auth

import ars.solr.AbstractBaseTest

/** Tests for [[AuthUser]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
class AuthUserTest extends AbstractBaseTest {

  class MockAuthUser(username: String) extends AuthUser(username)

  "AuthUser" must "validate args" in {
    new MockAuthUser("Mock user name")

    intercept[IllegalArgumentException] {
      new MockAuthUser(null)
    }

    intercept[IllegalArgumentException] {
      new MockAuthUser("")
    }

    intercept[IllegalArgumentException] {
      new MockAuthUser("   ")
    }
  }

  it must "have persistent username" in {
    assertResult("Mock user name") {
      new MockAuthUser("Mock user name").username
    }
  }
}
