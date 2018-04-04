package ars.solr.client

import ars.solr.AbstractBaseTest

/** Tests for [[ars.solr.client]] package object.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
class packageTest extends AbstractBaseTest {

  "SolrDocumentMap" must "be of type Map[String, Any]" in {
    assert(classOf[Map[String, Any]] == classOf[SolrDocumentMap])
  }
}
