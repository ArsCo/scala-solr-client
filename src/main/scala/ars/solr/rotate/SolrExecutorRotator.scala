//package ars.solr.rotate
//
//import java.util.concurrent.atomic.AtomicInteger
//
//import ars.precondition.RequireUtils.requireNotBlank
//import ars.solr.pool.SolrExecutor
//
///**
//  * Created by ars on 06/02/2017.
//  */
//trait SolrExecutorRotator[Client <: AnyRef] {
//
//  def allExecutors: Seq[SolrExecutor[Client]]
//
//  def executor: SolrExecutor[Client]
//
//}
//
//abstract class AbstractSolrExecutorRotator[Client <: AnyRef](override val allExecutors: SolrExecutor*)
//  extends SolrExecutorRotator[Client] {
//
//  requireNotBlank(allExecutors, "allExecutors")
//}
//
//class SolrExecutorRoundRobinRotator[Client <: AnyRef](allExecutors: SolrExecutor[Client]*)
//  extends AbstractSolrExecutorRotator[Client](allExecutors :_*) {
//
//  private[this] val size = allExecutors.size
//  private[this] val list = allExecutors.toList
//
//  private[this] val current = new AtomicInteger()
//
//  override def executor: SolrExecutor[Client] = {
//    val c = current.getAndIncrement() % size
//    list(c)
//  }
//}
//
//class SolrExecutorRandomRotator[Client <: AnyRef](allExecutors: SolrExecutor[Client]*)
//  extends AbstractSolrExecutorRotator[Client](allExecutors :_*) {
//
//  private[this] val r = scala.util.Random
//  private[this] val size = allExecutors.size
//  private[this] val list = allExecutors.toList
//
//  override def executor: SolrExecutor[Client] = {
//    val nextNumber = r.nextInt(size)
//    list(nextNumber)
//  }
//}
