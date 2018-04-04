package ars.solr.meta

import scala.annotation.StaticAnnotation

/** Marks experimental (unstable) features or APIs that can be changed or removed.
  *
  * These features have been recently added but have not been thoroughly tested in production yet.
  * Experimental features '''may undergo API changes''' in future releases, so production
  * code should not rely on them.
  *
  * @param cause the cause
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
class experimental(val cause: String = "") extends StaticAnnotation
