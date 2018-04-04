package ars.solr.client

import ars.solr.AbstractBaseTest
import ars.solr.client.mapping.CanMapTo
import org.apache.solr.client.solrj.response.QueryResponse

/** Tests for [[QueryResult]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.4
  */
class QueryResultTest extends AbstractBaseTest {

  "QueryResult" must "have correct interface" in {
    new QueryResult {
      override def documents: Seq[SolrDocumentMap] = ???
      override def documents[Result](mode: MappingErrorMode)
                                    (implicit mapper: CanMapTo[Result]): Seq[Result] = ???
      override def start: Long = ???
      override def maxScore: Option[Float] = ???
      override def numFound: Long = ???
    }
  }

  it must "have method apply(QueryResponse) returning DefaultQueryResult" in {
    val queryResponseMock = new QueryResponse
    val r = QueryResult.apply(queryResponseMock)
    assert(r != null)
    assert(r.isInstanceOf[QueryResult])
  }
}
