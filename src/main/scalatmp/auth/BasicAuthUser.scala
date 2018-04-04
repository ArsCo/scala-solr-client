package auth

import ars.precondition.RequireUtils.requireNotBlank

/** User with password.
  *
  * @param username the username. Must be non-blank.
  * @param password the password. Must be non-black.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
final case class BasicAuthUser(override val username: String, password: String) extends AuthUser(username) {
  requireNotBlank(password, "password")
}