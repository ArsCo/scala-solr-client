package ars.solr.client.request.highlight

/** The values for `hl.method` request parameter.
  *
  * @param name the name
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
abstract sealed case class HighlightMethod(name: String)

object HighlightMethod {

  /** Unified. */
  object Unified extends HighlightMethod("unified")

  /** Original. */
  object Original extends HighlightMethod("original")

  /** FastVector. */
  object FastVector extends HighlightMethod("fastVector")

  /** Postings. */
  object Postings extends HighlightMethod("postings")
}
