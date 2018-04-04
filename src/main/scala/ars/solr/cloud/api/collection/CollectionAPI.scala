package ars.solr.cloud.api.collection

import scalaj.http.{Http, HttpResponse}
import CollectionAPI._
import ars.precondition.RequireUtils
import ars.precondition.RequireUtils._
import ars.solr.cloud.api.collection.router.AbstractRouter

//
//abstract sealed class RouterName(val name: String)
//object RouterName {
//  case object Implicit extends RouterName("implicit")
//  case object CompositeId extends RouterName("compositeId")
//}



case class Nodes(nodes: Set[String], isShuffle: Boolean)

case class BasicAuth(username: String, password: String)


abstract sealed class Role(val name: String)
object Role {
  case object OverseerRole extends Role("overseer")
}

/**
  * Created by ars on 15/04/2017.
  *
  * @see https://cwiki.apache.org/confluence/display/solr/Collections+API#CollectionsAPI-splitshard
  */
class CollectionAPI(solrUrl: String,
                    auth: Option[BasicAuth]) {

  val s = s"$solrUrl/$BaseUrl"




  /**
    * Creates the collection.
    *
    * @param name the name of the collection to be created
    * @param router the router
    * @param replicationFactor
    * @param maxShardsPerNode
    * @param nodes
    * @param configName
    * @param routerField
    * @param coreProperties
    * @param autoAddReplicas
    * @param asyncRequestId
    * @param rule
    * @param snitch
    */
  def createCollection(name: String,
                       router: AbstractRouter,
                       replicationFactor: Option[Int] = None,
                       maxShardsPerNode: Option[Int] = None,
                       nodes: Option[Nodes] = None,
                       configName: Option[String] = None,
                       routerField: Option[String] = None,
                       coreProperties: Option[Map[String, String]] = None,
                       autoAddReplicas: Option[Boolean] = None,
                       asyncRequestId: Option[String] = None,
                       rule: Option[String] = None,
                       snitch: Option[String] = None): Unit = {

    requireNotBlank(name, "name")
    requireNotNull(router, "router")
    optional(replicationFactor) { requirePositive(_, "replicationFactor") }
    optional(maxShardsPerNode) { requirePositive(_, "maxShardsPerNode") }
    optional(nodes) { requireNotNull(_, "nodes") }
    optional(configName) { requireNotBlank(_, "configName") }
    optional(routerField) { requireNotBlank(_, "routerField") }
//    optional(coreProperties) {} TODO Map not empty to LIb
    optional(asyncRequestId) { requireNotBlank(_, "asyncRequestId") }
    optional(rule) { requireNotBlank(_, "rule") }
    optional(snitch) { requireNotBlank(_, "snitch") }

    val params = Map("name" -> name) ++
      router.params ++
      opt1("replicationFactor", replicationFactor)
      opt1("maxShardsPerNode", maxShardsPerNode)
      //nodes.map("maxShardsPerNode" -> _) ++
      opt1("collection.configName", configName)
      opt1("router.field", routerField) ++
      opt1("autoAddReplicas", autoAddReplicas) ++
      opt1("async", asyncRequestId) ++
      opt1("rule", rule) ++
      opt1("snitch", snitch)




    request("CREATE", params)
  }

  /**
    * Modifies attributes of the collection.
    *
    * It's possible to edit multiple attributes at a time.
    * Changing these values only updates the z-node on Zookeeper,
    * they do not change the topology of the collection.
    * For instance, increasing `replicationFactor` will not automatically add
    * more replicas to the collection but will allow more [[addReplica()]] commands to succeed.
    *
    * @param collection the name of the collection to be modified
    * @param replicationFactor
    * @param maxShardsPerNode
    * @param configName
    * @param autoAddReplicas
    * @param rule
    * @param snitch
    */
  def modifyCollection(collection: String,
                       replicationFactor: Option[Int] = None,
                       maxShardsPerNode: Option[Int] = None,
                       configName: Option[String] = None,
                       autoAddReplicas: Option[Boolean] = None,
                       rule: Option[String] = None,
                       snitch: Option[String] = None): Unit ={

    val params = Map("collection" -> collection)

    request("MODIFYCOLLECTION", params)
  }

  /**
    * Reloads the collection.
    *
    * @param collection the name of the collection
    * @param asyncRequestId the asynchronous request ID
    */
  def reloadCollection(collection: String,
                       asyncRequestId: Option[String]): Unit = {

    val params = Map("name" -> collection) ++ opt1("async", asyncRequestId)
    request("RELOAD", params)
  }

  /**
    * Splits the shard into two pieces which are written to disk as two (new) shards.
    *
    * The original shard will continue to contain the same data as-is but it will start
    * re-routing requests to the new shards. The new shards will have as many replicas as
    * the original shard.
    *
    * This command allows for seamless splitting and requires no downtime.
    * A shard being split will continue to accept query and indexing requests and will
    * automatically start routing them to the new shards once this operation is complete.
    * This command can only be used for SolrCloud collections created with `numShards`
    * parameter, meaning collections which rely on Solr's hash-based routing mechanism.
    *
    * @param collection the name of the collection
    * @param shard the name of the shard
    * @param ranges the hash ranges
    * @param splitKey
    * @param coreProperties
    * @param asyncRequestId
    */
  def splitShard(collection: String,
                 shard: String,
                 ranges: Option[(Long, Long)] = None,
                 splitKey: Option[String] = None,
                 coreProperties: Option[Map[String, String]] = None,
                 asyncRequestId: Option[String] = None): Unit = {

    // val rangeParam = ranges.map { case (l, h) => s"${l.toHexString}-${h.toHexString}" }.mkString(",")

    val params = Map("collection" -> collection, "shard" -> shard) ++
      //rangeParam ++
      opt1("split.key", splitKey) ++
      opt1("async", asyncRequestId)

    request("SPLITSHARD", params)

  }

  /**
    * Creates the shard.
    *
    * Shards can only created with this API for collections that use the `implicit` router.
    * Use [[splitShard()]] for collections using the `compositeId` router.
    * A new shard with a name can be created for an existing `implicit` collection.
    *
    * @param collection the name of the collection
    * @param shard the name of the shard
    * @param nodes the name of the shard
    * @param coreProperties
    * @param asyncRequestId
    */
  def createShard(collection: String,
                  shard: String,
                  nodes: Option[Set[String]] = None,
                  coreProperties: Option[Map[String, String]] = None,
                  asyncRequestId: Option[String] = None): Unit = {

    val params = Map("collection" -> collection, "shard" -> shard)



  }

  /**
    * Deletes the shard.
    *
    * Deleting a shard will unload all replicas of the shard, remove them from `clusterstate.json`,
    * and (by default) delete the `instanceDir` and `dataDir` for each replica.
    * It will only remove shards that are inactive, or which have no range given for custom sharding.
    *
    * @param collection the name of the collection
    * @param shard
    * @param deleteInstanceDir
    * @param deleteDataDir
    * @param deleteIndex
    * @param asyncRequestId
    */
  def deleteShard(collection: String,
                  shard: String,
                  deleteInstanceDir: Option[Boolean] = None,
                  deleteDataDir: Option[Boolean] = None,
                  deleteIndex: Option[Boolean] = None,
                  asyncRequestId: Option[String] = None): Unit = {

    requireNotBlank(collection, "collection")
    requireNotBlank(shard, "shard")
    optional(asyncRequestId) { requireNotBlank(_, "asyncRequestId") }

    val params = Map("collection" -> collection, "shard" -> shard) ++
      opt1("deleteInstanceDir", deleteDataDir) ++
      opt1("deleteDataDir", deleteDataDir) ++
      opt1("deleteIndex", deleteIndex) ++
      opt1("async", asyncRequestId)

    request("DELETESHARD", params)

  }

  /**
    * Create or modifies the alias for the collection.
    *
    * If an alias by the same name already exists, this action will replace the existing alias,
    * effectively acting like an atomic "MOVE" command.
    *
    * @param alias the alias name
    * @param collections the sequence of collections to be aliased, separated by commas
    *                    (they must already exist in the cluster)
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    */
  def createAlias(alias: String,
                  collections: Seq[String],
                  asyncRequestId: Option[String] = None): Unit = {

    requireNotBlank(alias, "alias")
//    optional(collections) { requireNotBlank(_, "collections") }
//    optional(collections) { requireAllNotBlank(_, "collections") }

    val collectionsString = collections.mkString(",")

    val params = Map("name" -> alias, "collections" -> collectionsString) ++ opt1("async", asyncRequestId)

    request("CREATEALIAS", params)
  }

  /**
    * Deletes the collection alias.
    *
    * @param alias the alias name
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    */
  def deleteAlias(alias: String,
                  asyncRequestId: Option[String] = None): Unit = {

    requireNotBlank(alias, "alias")
    optional(asyncRequestId) { requireNotBlank(_, "asyncRequestId") }

    val params = Map("name" -> alias) ++ opt1("async", asyncRequestId)
    request("DELETEALIAS", params)
  }

  /**
    * Deletes the collection.
    *
    * @param collection the name of the collection
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    */
  def deleteCollection(collection: String,
                       asyncRequestId: Option[String] = None): Unit = {

    requireNotBlank(collection, "collection")
    optional(asyncRequestId) { requireNotBlank(_, "asyncRequestId") }

    val params = Map("name" -> collection) ++ opt1("async", asyncRequestId)
    request("DELETE", params)
  }


  /**
    * Delete a named replica from the specified collection and shard.
    *
    * If the corresponding core is up and running the core is unloaded, the entry is removed from
    * the clusterstate, and (by default) delete the `instanceDir` and `dataDir`.
    * If the node/core is down, the entry is taken off the clusterstate and
    * if the core comes up later it is automatically unregistered.
    *
    * @param collection the name of the collection
    * @param shard the name of the shard
    * @param replica the name of the replica to remove
    * @param deleteInstanceDir by default Solr will delete the entire `instanceDir` of the replica that is deleted.
    *                          Set this to `false` to prevent the instance directory from being deleted
    * @param deleteDataDir by default Solr will delete the `dataDir` of the replica that is deleted.
    *                      Set this to `false` to prevent the data directory from being deleted
    * @param deleteIndex by default Solr will delete the index of the replica that is deleted.
    *                    Set this to false to prevent the index directory from being deleted
    * @param onlyIfDown when set to `true` will not take any action if the replica is active. Default `false`
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    */
  def deleteReplica(collection: String,
                            shard: String,
                            replica: String,
                            deleteInstanceDir: Option[Boolean],
                            deleteDataDir: Option[Boolean],
                            deleteIndex: Option[Boolean],
                            onlyIfDown: Option[Boolean],
                            asyncRequestId: Option[String]): Unit = {

    _deleteReplica(collection, shard, Some(replica), None,
      deleteInstanceDir, deleteDataDir, deleteIndex, onlyIfDown, asyncRequestId)
  }

  /**
    *
    * Delete a named replica from the specified collection and shard.
    *
    * If the corresponding core is up and running the core is unloaded, the entry is removed from
    * the clusterstate, and (by default) delete the `instanceDir` and `dataDir`.
    * If the node/core is down, the entry is taken off the clusterstate and
    * if the core comes up later it is automatically unregistered.
    *
    * @param collection the name of the collection
    * @param shard the name of the shard
    * @param count the number of replicas to remove. If the requested number exceeds the number of replicas,
    *              no replicas will be deleted. If there is only one replica, it will not be removed.
    * @param deleteInstanceDir by default Solr will delete the entire `instanceDir` of the replica that is deleted.
    *                          Set this to `false` to prevent the instance directory from being deleted
    * @param deleteDataDir by default Solr will delete the `dataDir` of the replica that is deleted.
    *                      Set this to `false` to prevent the data directory from being deleted
    * @param deleteIndex by default Solr will delete the index of the replica that is deleted.
    *                    Set this to false to prevent the index directory from being deleted
    * @param onlyIfDown when set to `true` will not take any action if the replica is active. Default `false`
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    */
  def deleteReplica(collection: String,
                            shard: String,
                            count: Int,
                            deleteInstanceDir: Option[Boolean],
                            deleteDataDir: Option[Boolean],
                            deleteIndex: Option[Boolean],
                            onlyIfDown: Option[Boolean],
                            asyncRequestId: Option[String]): Unit = {

    _deleteReplica(collection, shard, None, Some(count),
      deleteInstanceDir, deleteDataDir, deleteIndex, onlyIfDown, asyncRequestId)
  }

  private def _deleteReplica(collection: String,
                    shard: String,
                    replica: Option[String],
                    count: Option[Int],
                    deleteInstanceDir: Option[Boolean],
                    deleteDataDir: Option[Boolean],
                    deleteIndex: Option[Boolean],
                    onlyIfDown: Option[Boolean],
                    asyncRequestId: Option[String]): Unit = {

    requireNotBlank(collection, "collection")
    requireNotBlank(shard, "shard")
    optional(replica) { requireNotBlank(_, "replica") }
    optional(count) { requirePositive(_, "count") }
    optional(asyncRequestId) { requireNotBlank(_, "asyncRequestId") }


  }

  /**
    * Add the replica to the shard in the collection.
    *
    * The node name can be specified if the replica is to be created in a specific node.
    *
    * @param collection the name of the collection
    * @param shard the name of the shard
    * @param node the name of the node where the replica should be created
    * @param instanceDir the instanceDir for the core that will be created
    * @param dataDir the directory in which the core should be created
    * @param coreProperties
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    */
  def addShardReplica(collection: String,
                 shard: String,
                 node: Option[String],
                 instanceDir: Option[String],
                 dataDir: Option[String],
                 coreProperties: Option[Map[String, String]],
                 asyncRequestId: Option[String]): Unit = {

    _addReplica(collection, Some(shard), None, node, instanceDir, dataDir, coreProperties, asyncRequestId)
  }

  /**
    * Add the replica to the shard in the collection.
    *
    * The node name can be specified if the replica is to be created in a specific node.
    *
    * @param collection the name of the collection
    * @param route
    * @param node the name of the node where the replica should be created
    * @param instanceDir the instanceDir for the core that will be created
    * @param dataDir the directory in which the core should be created
    * @param coreProperties
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    */
  def addRouteReplica(collection: String,
                 route: String,
                 node: Option[String],
                 instanceDir: Option[String],
                 dataDir: Option[String],
                 coreProperties: Option[Map[String, String]],
                 asyncRequestId: Option[String]): Unit = {
    _addReplica(collection, None, Some(route), node, instanceDir, dataDir, coreProperties, asyncRequestId)
  }

  private def _addReplica(collection: String,
                 shard: Option[String],
                 route: Option[String],
                 node: Option[String],
                 instanceDir: Option[String],
                 dataDir: Option[String],
                 coreProperties: Option[Map[String, String]],
                 asyncRequestId: Option[String]): Unit = {

    requireNotBlank(collection, "collection")
    optional(shard) { requireNotBlank(_, "shard") }
    optional(route) { requireNotBlank(_, "route") }
    optional(node) { requireNotBlank(_, "node") }
    optional(instanceDir) { requireNotBlank(_, "instanceDir") }
    optional(dataDir) { requireNotBlank(_, "dataDir") }
    //
    optional(asyncRequestId) { requireNotBlank(_, "asyncRequestId") }

  }

  /**
    * Adds, edits or deletes a cluster-wide property.
    *
    * @param property the property
    * @param value The value of the property. If the value is empty or null, the property is unset.
    */
  def clusterProperties(property: ClusterProperty, value: String): Unit = {
    requireNotNull(property, "property")
    requireNotBlank(value, "value")



  }


  /**
    * Migrates all documents having the given routing key to another collection.
    *
    * The source collection will continue to have the same data as-is but it will
    * start re-routing write requests to the target collection for the number of
    * seconds specified by the `forwardTimeout` parameter.
    * It is the responsibility of the user to switch to the target collection for
    * reads and writes after this command completes.
    *
    * The routing key specified by the `splitKey` parameter may span multiple
    * shards on both the source and the target collections.
    * The migration is performed shard-by-shard in a single thread.
    * One or more temporary collections may be created by this command during the
    * migrate process but they are cleaned up at the end automatically.
    *
    * This is a long running operation and therefore using the `asyncRequestId` parameter
    * is highly recommended. If the async parameter is not specified then the operation is
    * synchronous by default and keeping a large read timeout on the invocation is advised.
    * Even with a large read timeout, the request may still timeout due to inherent limitations
    * of the Collection APIs but that doesn’t necessarily mean that the operation has failed.
    * Users should check logs, cluster state, source and target collections before invoking
    * the operation again.
    *
    * This command works only with collections having the `compositeId` router.
    * The target collection must not receive any writes during the time the migrate command
    * is running otherwise some writes may be lost.
    *
    * Please note that the migrate API does not perform any de-duplication on the documents so
    * if the target collection contains documents with the same `uniqueKey` as the documents
    * being migrated then the target collection will end up with duplicate documents.
    *
    * @param source the name of the source collection from which documents will be split
    * @param target the name of the target collection to which documents will be migrated
    * @param splitKey the routing key prefix. For example, if `uniqueKey` is `a!123`, then
    *                 you would use `splitKey=a!`
    * @param forwardTimeout the timeout, in seconds, until which write requests made to the source
    *                       collection for the given `splitKey` will be forwarded to the target shard.
    *                       The default is 60 seconds.
    * @param coreProperties
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    */
  def migrate(source: String,
              target: String,
              splitKey: String,
              forwardTimeout: Option[Int],
              coreProperties: Option[Map[String, String]],
              asyncRequestId: Option[String]): Unit = {

    requireNotBlank(source, "source")
    requireNotBlank(target, "target")
    requireNotBlank(splitKey, "splitKey")
    optional(forwardTimeout) { requirePositive(_, "forwardTimeout") }
    //
    optional(asyncRequestId) { requireNotBlank(_, "asyncRequestId") }
  }

  /**
    * Assign a role to a given node in the cluster.
    *
    * The only supported role as of 4.7 is 'overseer'.
    * Use this API to dedicate a particular node as Overseer. Invoke it multiple times to add more nodes.
    * This is useful in large clusters where an Overseer is likely to get overloaded.
    * If available, one among the list of nodes which are assigned the 'overseer' role would become the overseer.
    * The system would  assign the role to any other node if none of the designated nodes are up and running.
    *
    * @param role the role. The only supported role as of now is [[Role.OverseerRole]].
    * @param node the name of the node. It is possible to assign a role even before that node is started.
    */
  def addRole(role: Role, node: String): Unit = {
    requireNotNull(role, "role")
    requireNotBlank(node, "node")

    val params = Map("role" -> role.name, "node" -> node)

    request("ADDROLE", params)
  }

  /**
    * Remove an assigned role.
    *
    * @param role the role. The only supported role as of now is [[Role.OverseerRole]].
    * @param node the name of the node. It is possible to assign a role even before that node is started.
    */
  def removeRole(role: Role, node: String): Unit = {
    requireNotNull(role, "role")
    requireNotBlank(node, "node")

    val params = Map("role" -> role.name, "node" -> node)

    request("REMOVEROLE", params)


  }

  /**
    * Returns the current status of the overseer, performance statistics of various overseer APIs,
    * and the last 10 failures per operation type.
    */
  def overseerStatus(): Unit = {
    request("OVERSEERSTATUS")


  }

  /**
    *
    */
  def clusterAllCollectionStatus(): Unit = {

  }

  /**
    * Requests cluster status.
    *
    * Fetch the cluster status including collections, shards, replicas, configuration name as well as
    * collection aliases and cluster properties.
    *
    * @param collection the collection name
    * @param shard the name of the shard
    * @param route
    */
  // TODO All variants of params + SCALADOC
  def _clusterStatus(collection: String, shard: Seq[String], route: String): Unit = {

//    request("CLUSTERSTATUS", )

  }

  /**
    * Request the status and response of an already submitted asynchronous call.
    * This call is also used to clear up the stored statuses.
    *
    * @param requestId the user defined `requestId` for the request.
    *                  This can be used to track the status of the submitted asynchronous task.
    */
  def requestStatus(requestId: String): Unit = {
    requireNotBlank(requestId, "requestId")

    request("REQUESTSTATUS", Map("requestid" -> requestId))
  }

  /**
    * Delete the stored response of an already failed or completed asynchronous call.
    *
    * @param requestId the `requestId` of the asynchronous call to clear the stored response for.
    * @param isFlush `true` to clear all stored completed and failed async request responses.
    */
  def deleteStatus(requestId: String, isFlush: Boolean): Unit = {
    requireNotBlank(requestId, "requestId")

    request("DELETESTATUS", Map("requestid" -> requestId))
  }

  /**
    * Fetch the names of the collections in the cluster.
    */
  def collectionList(): Unit = {

    request("LIST", Map())

  }

  /**
    * Assign an arbitrary property to a particular replica and give it the value specified.
    * If the property already exists, it will be overwritten with the new value.
    *
    * @param collection the name of the collection
    * @param shard the name of the shard
    * @param replica the name of the replica
    * @param propertyName the property to add.
    *                     __Note:__ this will have the literal `property.` prepended to distinguish it from
    *                     system-maintained properties. So these two forms are equivalent: `property=special` and
    *                     `property=property.special`
    * @param propertyValue the value to assign to the property
    * @param shardUnique if `true`, then setting this property in one replica will remove the property from all
    *                    other replicas in that shard. Default: `false`
    */
  def addReplicaProperty(collection: String,
                           shard: String,
                           replica: String,
                           propertyName: String,
                           propertyValue: String,
                           shardUnique: Option[Boolean] = None): Unit = {

    requireNotBlank(collection, "collection")
    requireNotBlank(shard, "shard")
    requireNotBlank(replica, "replica")
    requireNotBlank(propertyName, "propertyName")



    request("ADDREPLICAPROP", Map())
  }

  /**
    * Deletes an arbitrary property from a particular replica.
    *
    * @param collection the name of the collection
    * @param shard the name of the shard
    * @param replica the name of the replica
    * @param propertyName the property to add.
    *                     __Note:__ this will have the literal `property.` prepended to distinguish it from
    *                     system-maintained properties. So these two forms are equivalent: `property=special` and
    *                     `property=property.special`
    */
  def deleteReplicaProperty(collection: String,
                            shard: String,
                            replica: String,
                            propertyName: String): Unit = {

    requireNotBlank(collection, "collection")
    requireNotBlank(shard, "shard")
    requireNotBlank(propertyName, "propertyName")

    val params = Map("collection" -> collection, "shard" -> shard,
      "replica" -> replica, "property" -> propertyName)


    val response = request("DELETEREPLICAPROP", params)

  }

  /**
    * Insures that a particular property is distributed evenly amongst the physical nodes
    * that make up a collection.
    *
    * If the property already exists on a replica, every effort is made to leave it there.
    * If the property is not on any replica on a shard, one is chosen and the property is added.
    *
    * @param collection the name of the collection
    * @param propertyName the property
    * @param onlyActiveNodes normally, the property is instantiated on active nodes only.
    *                        If this parameter is specified as `false`, then inactive nodes are also included
    *                        for distribution. Defaults: `true`.
    * @param shardUnique Something of a safety valve.
    *                    There is one pre-defined property (preferredLeader) that defaults this value to `true`.
    *                    For all other properties that are balanced, this must be set to `true` or an error
    *                    message is returned.
    */
  def balanceShardUniq(collection: String,
                       propertyName: String,
                       onlyActiveNodes: Option[String] = None,
                       shardUnique: Option[Boolean] = None): Unit = {

    requireNotBlank(collection, "collection")
    requireNotBlank(propertyName, "propertyName")
    optional(onlyActiveNodes) { requireNotBlank(_, "onlyActiveNodes") }

    val params = Map("collection" -> collection, "property" -> propertyName) ++
      opt1("onlyactivenodes", onlyActiveNodes) ++
      opt1("shardUnique", shardUnique)

    val response = request("BALANCESHARDUNIQUE", params)
  }

  /**
    * Reassign leaders in a collection according to the `preferredLeader` property across active nodes.
    *
    * Assigns leaders in a collection according to the `preferredLeader` property on active nodes.
    * This command should be run after the `preferredLeader` property has been assigned via the
    * [[balanceShardUniq()]] or [[addReplicaProperty()]] commands.
    *
    * __NOTE:__ it is not required that all shards in a collection have a `preferredLeader` property.
    * Rebalancing will only attempt to reassign leadership to those replicas that have the `preferredLeader`
    * property set to `true` and are not currently the shard leader and are currently active.
    *
    * @param collection the name of the collection to rebalance preferredLeaders on
    * @param maxAtOnce the maximum number of reassignments to have queue up at once.
    *                  Values `<=0` are use the default value `Integer.MAX_VALUE`.
    *                  When this number is reached, the process waits for one or more leaders
    *                  to be successfully assigned before adding more to the queue.
    * @param maxWaitSeconds Defaults to 60.
    *                       This is the timeout value when waiting for leaders to be reassigned.
    *                       __NOTE:__ if `maxAtOnce` is less than the number of reassignments that
    *                       will take place, this is the maximum interval that any single wait for
    *                       at least one reassignment. For example, if 10 reassignments are to take
    *                       place and `maxAtOnce` is 1 and `maxWaitSeconds` is 60, the upper bound on
    *                       the time that the command may wait is 10 minutes.
    */
  def rebalanceLeaders(collection: String,
                       maxAtOnce: Option[String] = None,
                       maxWaitSeconds: Option[String] = None): Unit = {

    requireNotBlank(collection, "collection")
    optional(maxAtOnce) { requireNotBlank(_, "maxAtOnce") }
    optional(maxWaitSeconds) { requireNotBlank(_, "maxWaitSeconds") }

    val params = Map("collection" -> collection) ++
      opt1("maxAtOnce", maxAtOnce) ++
      opt1("maxWaitSeconds", maxWaitSeconds)

    val response = request("REBALANCELEADERS", params)
  }

  /**
    * Forces shard leader.
    *
    * In the unlikely event of a shard losing its leader,
    * this command can be invoked to force the election of a new leader
    *
    * __ATTENTION:__ This is an expert level command, and should be invoked only when regular
    * leader election is not working. This may potentially lead to loss of data in the event
    * that the new leader doesn't have certain updates, possibly recent ones, which were acknowledged
    * by the old leader before going down.
    *
    * @param collection the name of the collection
    * @param shard the name of the shard
    */
  def forceLeader(collection: String, shard: String): Unit = {
    request("FORCELEADER", Map())
  }

  /**
    * Moves a collection from shared `clusterstate.json` zookeeper node (created with `stateFormat=1`,
    * the default in all Solr releases prior to 5.0) to the per-collection `state.json` stored in ZooKeeper
    * (created with `stateFormat=2`, the current default) seamlessly without any application down-time.
    *
    * This API is useful in migrating any collections created prior to Solr 5.0 to the more scalable cluster
    * state format now used by default. If a collection was created in any Solr 5.x version or higher, then
    * executing this command is not necessary.
    *
    * __ATTENTION:__ This is an expert level command, and should be invoked only when regular
    * leader election is not working. This may potentially lead to loss of data in the event
    * that the new leader doesn't have certain updates, possibly recent ones, which were acknowledged
    * by the old leader before going down.
    *
    * @param collection the name of the collection to be migrated from `clusterstate.json` to its
    *                   own `state.json` zookeeper node
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    */
  def migrateStateFormat(collection: String,
                         asyncRequestId: Option[String]): Unit = {

    requireNotBlank(collection, "collection")
    optional(asyncRequestId) { requireNotBlank(_, "asyncRequestId") }

    val params = Map("collection" -> collection) ++ opt1("async", asyncRequestId)

    val response = request("MIGRATESTATEFORMAT", params)

  }

  /**
    * Backups the collection collection and it's associated configurations to a shared filesystem -
    * for example a Network File System
    *
    * @param collection the name of the collection that needs to be backed up
    * @param location the location on the shared drive for the backup command to write to
    * @param repository the name of the repository to be used for the backup. If no repository is specified
    *                   then the local filesystem repository will be used automatically.
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    */
  def backup(collection: String,
             location: Option[String] = None,
             repository: Option[String] = None,
             asyncRequestId: Option[String] = None): Unit = {

    requireNotBlank(collection, "collection")
    optional(location) { requireNotBlank(_, "location") }
    optional(repository) { requireNotBlank(_, "repository") }
    optional(asyncRequestId) { requireNotBlank(_, "asyncRequestId") }

    val params = Map("collection" -> collection) ++
      opt1("location", location) ++
      opt1("repository", repository) ++
      opt1("async", asyncRequestId)

    val response = request("BACKUP", params)
  }

  /**
    * Restores indexes and associated configurations.
    *
    * The restore operation will create a collection with the specified name in the collection parameter.
    * You cannot restore into the same collection the backup was taken from and the target collection should
    * not be present at the time the API is called as Solr will create it for you.
    *
    * The collection created will be of the same number of shards and replicas as the original collection,
    * preserving routing information, etc. Optionally, you can override some parameters documented below.
    * While restoring, if a configSet with the same name exists in ZooKeeper then Solr will reuse that, or
    * else it will upload the backed up configSet in ZooKeeper and use that.
    *
    * You can use the collection alias API to make sure client's don't need to change the endpoint to query
    * or index against the newly restored collection.
    *
    * @param collection the collection where the indexes will be restored into.
    * @param location the location on the shared drive for the restore command to read from
    * @param repository the name of the repository to be used for the backup. If no repository is specified
    *                   then the local filesystem repository will be used automatically.
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    * @param replicationFactor
    * @param maxShardsPerNode
    * @param configName
    * @param coreProperties
    * @param autoAddReplicas
    */
  def restore(collection: String,
              location: Option[String],
              repository: Option[String],
              asyncRequestId: Option[String],

              replicationFactor: Option[Int] = None,
              maxShardsPerNode: Option[Int] = None,
              configName: Option[String] = None,
              coreProperties: Option[Map[String, String]] = None,
              autoAddReplicas: Option[Boolean] = None): Unit = {

    requireNotBlank(collection, "collection")
    optional(location) { requireNotBlank(_, "location") }
    optional(repository) { requireNotBlank(_, "repository") }
    optional(asyncRequestId) { requireNotBlank(_, "asyncRequestId") }

    optional(replicationFactor) { requirePositive(_, "replicationFactor") }
    optional(maxShardsPerNode) { requirePositive(_, "maxShardsPerNode") }
    optional(configName) { requireNotBlank(_, "configName") }


    request("RESTORE", Map())
  }

  /**
    * Deletes all replicas of all collections in that node.
    * Please note that the node itself will remain as a live node after this operation.
    *
    * @param node the node to be cleaned up
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    */
  def deleteNode(node: String, asyncRequestId: Option[String]): Unit = {
    requireNotBlank(node, "node")
    requireAsyncRequest(asyncRequestId)

    val params = Map("node" -> node) ++ opt1("async", asyncRequestId)
    val response = request("DELETENODE", params)



  }

  /**
    * Move all replicas in a node to another
    *
    * This command recreates replicas in the source node to the target node. After each replica is copied,
    * the replicas in the source node are deleted.
    *
    * @param source the source node from which the replicas need to be copied from
    * @param target the target node
    * @param parallel if `true`, all replicas are created in separate threads.
    *                 Keep in mind that this can lead to very high network and disk I/O
    *                 if the replicas have very large indices. Default: `false`.
    * @param asyncRequestId the request ID to track this action which will be processed asynchronously
    */
  def replaceNode(source: String,
                  target: String,
                  parallel: Option[Boolean],
                  asyncRequestId: Option[String]): Unit = {

    requireNotBlank(source, "source")
    requireNotBlank(target, "target")
    requireAsyncRequest(asyncRequestId)

    val params = Map("source" -> source, "target" -> target) ++
      opt1("parallel", parallel) ++
      opt1("async", asyncRequestId)

    val response = request("REPLACENODE", params)


  }


  def request(url: String, action: String, params: Map[String, String]): HttpResponse[String] = {
    Http(url)
      .param("action", action)
      .params(params)
      .asString
  }

  def request(action: String): HttpResponse[String] = {
    Http(s)
      .param("action", action)
      .asString
  }

  def request(action: String, params: Map[String, String]): HttpResponse[String] = {
    request(s, action, params)
  }

  // ---------- VALIDATION ----------

  def requireAsyncRequest(asyncRequestId: Option[String]): Unit ={
    optional(asyncRequestId){ requireNotBlank(_, "asyncRequestId")}
  }

}






object CollectionAPI {
  val BaseUrl = "/admin/collections"

  def opt1[T](name: String, value: Option[T]): Option[(String, String)] = {
    opt2(name, value)(_.toString)
  }

  def opt2[T](name: String, value: Option[T])(f: => T => String): Option[(String, String)] = {

    value.map(name -> f(_))
  }

}
