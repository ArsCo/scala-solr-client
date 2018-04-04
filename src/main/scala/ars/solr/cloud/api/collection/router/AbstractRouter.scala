package ars.solr.cloud.api.collection.router

import ars.precondition.RequireUtils.requireNotBlank

/** Abstract router.
  *
  * @param routerName the router name
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
@throws[IllegalArgumentException]("if any argument is invalid")
abstract class AbstractRouter(val routerName: String) extends Router {
  requireNotBlank(routerName, "routerName")

  private[this] val _params = otherParams + ("router.name" -> routerName)

  /**
    * Gets router request params. It accumulates `router.name` param and
    * params returning by [[otherParams]].
    *
    * @return the map of params
    */
  def params: Map[String, String] = _params

  /**
    * Additional request params. Subclasses must implement this method to add their request params.
    *
    * @return the request params
    */
  protected def otherParams: Map[String, String]
}
