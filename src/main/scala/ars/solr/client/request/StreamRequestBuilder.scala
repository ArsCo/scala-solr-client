package ars.solr.client.request

import java.util.concurrent.Executors.{newCachedThreadPool, newFixedThreadPool, newSingleThreadExecutor}
import java.util.concurrent.{ExecutorService, ThreadFactory}

import ars.precondition.RequireUtils.requireNotNull
import ars.solr.client.QueryCallback.{DocumentCall, InfoCall}
import ars.solr.client.{QueryCallback, ScalaSolrClient}
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.common.SolrDocument

/**
  * Created by ars on 27/03/2017.
  */
class StreamRequestBuilder(client: ScalaSolrClient, solrQuery: SolrQuery) {
  import StreamRequestBuilder._
  import ars.solr.client.implicits._

  requireNotNull(client, "client")
  requireNotNull(solrQuery, "solrQuery")

  private var _executors: ExecutorService = newSingleThreadExecutor()

  /**
    * Sets the thread pool for stream query execution.
    *
    * @param executors the executor (thread pool)
    * @return the `this` instance
    */
  def threadPool(executors: ExecutorService): StreamRequestBuilder = {
    requireNotNull(executors, "executors")
    requireThreadPool(executors)
    _executors = executors
    this
  }

  /**
    * Sets the fixed thread pool of `numThreads` size.
    *
    * @param numThreads the number of threads
    * @return the `this` instance
    */
  def fixedThreadPool(numThreads: Int): StreamRequestBuilder =
    threadPool(newFixedThreadPool(numThreads))

  /**
    * Sets the fixed thread pool of `numThreads` size.
    *
    * @param numThreads the number of threads
    * @param threadFactory the thread factory
    * @return the `this` instance
    */
  def fixedThreadPool(numThreads: Int, threadFactory: ThreadFactory): StreamRequestBuilder =
    threadPool(newFixedThreadPool(numThreads, threadFactory))

  /**
    * Sets the fixed thread pool of `numThreads` size.
    *
    * @param numThreads the number of threads
    * @param threadName the thread name prefix
    * @return the `this` instance
    */
  def fixedThreadPool(numThreads: Int, threadName: String): StreamRequestBuilder =
    fixedThreadPool(numThreads, new CounterNamedThreadFactory(threadName))

  /**
    * Sets the cached thread pool.
    *
    * @return the `this` instance
    */
  def cachedThreadPool(): StreamRequestBuilder =
    threadPool(newCachedThreadPool())

  /**
    * Sets the cached thread pool.
    *
    * @param threadFactory the thread factory
    * @return the `this` instance
    */
  def cachedThreadPool(threadFactory: ThreadFactory): StreamRequestBuilder =
    threadPool(newCachedThreadPool(threadFactory))

  /**
    * Sets the cached thread pool.
    *
    * @param threadName the thread name prefix
    * @return the `this` instance
    */
  def cachedThreadPool(threadName: String): StreamRequestBuilder =
    cachedThreadPool(new CounterNamedThreadFactory(threadName))

  /**
    * Sets the single thread pool.
    *
    * @return the `this` instance
    */
  def singleThreadPool(): StreamRequestBuilder =
    threadPool(newSingleThreadExecutor())

  /**
    * Sets the single thread pool.
    *
    * @param threadFactory the thread factory
    * @return the `this` instance
    */
  def singleThreadPool(threadFactory: ThreadFactory): StreamRequestBuilder =
    threadPool(newSingleThreadExecutor(threadFactory))

  /**
    * Sets the single thread pool.
    *
    * @param threadName the thread name prefix
    * @return the `this` instance
    */
  def singleThreadPool(threadName: String): StreamRequestBuilder =
    singleThreadPool(new CounterNamedThreadFactory(threadName))

  /**
    * Executes query and process results with thread pool (single thread pool by default).
    * This is the terminating operation of the query builder.
    *
    * @param callback the callback
    * @return the result of query
    */
  def execute(callback: QueryCallback): Unit = {
    val parallelCallback = new QueryCallback {
      override def document: DocumentCall = {
        (d: SolrDocument) => {
          _executors.submit(new Runnable {
            override def run(): Unit = callback.document(d)
          })
        }
      }
      override def info: InfoCall = callback.info
    }

    client.nativeClient.queryAndStreamResponse(solrQuery, parallelCallback)
  }

  private[client] class NamedThreadFactory(threadName: String) extends ThreadFactory {

    override def newThread(r: Runnable): Thread = new Thread(r, nextThreadName)
    protected def nextThreadName: String = threadName
  }

  private[client] class CounterNamedThreadFactory(threadName: String) extends NamedThreadFactory(threadName) {
    private var _count = -1

    override protected def nextThreadName: String = {
      _count += 1
      s"$threadName-${_count}"
    }
  }
}

object StreamRequestBuilder {
  private def requireThreadPool(executors: ExecutorService): Unit = {
    if (executors.isTerminated || executors.isShutdown) {
      throw new IllegalArgumentException("The thread pool 'executor' was terminated or shutdowned.")
    }
  }
}
