package ars.solr.cloud.api.collection.router

import ars.precondition.RequireUtils.requirePositive

/** Composite ID router.
  *
  * @param numShards the number of shards
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
@throws[IllegalArgumentException]("if any argument is invalid")
case class CompositeIdRouter(numShards: Int) extends AbstractRouter("compositeId") {
  requirePositive(numShards, "numShards")

  override protected def otherParams: Map[String, String] = {
    Map("numShards" -> numShards.toString)
  }
}
