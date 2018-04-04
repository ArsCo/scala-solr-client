package ars.solr.auth

import ars.precondition.RequireUtils.requireNotBlank

/** Abstract user.
  *
  * @param username the username. Must be non-blank.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
abstract class AuthUser(val username: String) {
  requireNotBlank(username, "username")
}


