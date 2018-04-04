package ars.solr.cloud.api.collection

/** Cluster property.
  *
  * @param name the property name
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
abstract sealed class ClusterProperty(val name: String)

object ClusterProperty {
  final case object UrlSchema extends ClusterProperty("urlScheme")
  final case object AutoAddReplicas extends ClusterProperty("autoAddReplicas")
  final case object Location extends ClusterProperty("location")
}
