package ars.solr.cloud.api.collection.router

/** Router.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
trait Router {

  /**
    * Gets router request params.
    *
    * @return the map of params
    */
  def params: Map[String, String]
}
