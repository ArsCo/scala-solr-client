package ars.solr.pool

import java.util.concurrent.TimeUnit

import ars.solr.AbstractBaseTest
import ars.solr.client.ScalaSolrClient
import ars.solr.config.SolrCoreConfig
import ars.solr.pool.SolrConnectionPoolFactory.createScalaPool
import ars.solr.pool.SolrExecutor._

import scala.concurrent.duration.Duration

/**
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
class SolrExecutorTest extends AbstractBaseTest {

  "SolrExecutor" must "have awesome syntax :)" in {

    val pool = createScalaPool(SolrCoreConfig("http://aaa.aa", "core-name"), 100)
    val executor = new SolrExecutor(pool)

    val duration = Duration(5, TimeUnit.MINUTES)

    // ========================================
    // From executor
    // ========================================
    executor { client =>
      assert(client.isInstanceOf[ScalaSolrClient])
    }

    executor ~ { client =>
      assert(client.isInstanceOf[ScalaSolrClient])
    }

    executor(duration) { client =>
      assert(client.isInstanceOf[ScalaSolrClient])
    }

    // ========================================
    // From pool
    // ========================================
    pool { client =>
      assert(client.isInstanceOf[ScalaSolrClient])
    }

    pool ~ { client =>
      assert(client.isInstanceOf[ScalaSolrClient])
    }

    pool(duration) { client =>
      assert(client.isInstanceOf[ScalaSolrClient])
    }

    // ========================================
    // From executor
    // ========================================

    withSolr(executor) { client =>
      assert(client.isInstanceOf[ScalaSolrClient])
    }

    tryWithSolr(executor) { client =>
      assert(client.isInstanceOf[ScalaSolrClient])
    }

    tryWithSolr(executor, duration) { client =>
      assert(client.isInstanceOf[ScalaSolrClient])
    }

    // ========================================
    // From pool
    // ========================================

    withSolr(pool) { client =>
      assert(client.isInstanceOf[ScalaSolrClient])
    }

    tryWithSolr(pool) { client =>
      assert(client.isInstanceOf[ScalaSolrClient])
    }

    tryWithSolr(pool, duration) { client =>
      assert(client.isInstanceOf[ScalaSolrClient])
    }
  }

}
