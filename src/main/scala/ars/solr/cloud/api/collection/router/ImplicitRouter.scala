package ars.solr.cloud.api.collection.router

import ars.precondition.RequireUtils.{requireAllNotBlank, requireNotBlank}

/** Implicit router.
  *
  * @param shards the sequence of shards
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
@throws[IllegalArgumentException]("if any argument is invalid")
case class ImplicitRouter(shards: Set[String]) extends AbstractRouter("implicit") {
  requireNotBlank(shards, "shards")
  requireAllNotBlank(shards, "shards")

  override protected def otherParams: Map[String, String] = {
    val s = shards.mkString(",")
    Map("shards" -> s)
  }
}
