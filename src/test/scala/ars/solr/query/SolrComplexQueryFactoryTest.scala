//package ars.solr.query
//
//import ars.solr.AbstractBaseTest
//import ars.solr.query.SolrBasicQueryFactory.{MultiPhrasePart, term}
//import ars.solr.query.SolrComplexQueryFactory.{complexPhrase, _}
//import org.apache.lucene.search._
//
//import scala.collection.JavaConversions._
//
///** Tests for [[SolrComplexQueryFactory]].
//  *
//  * @author ars (Ibragimov Arsen)
//  * @since 0.0.3
//  */
//class SolrComplexQueryFactoryTest extends AbstractBaseTest {
//
//  val q1 = term("field1", "word1")
//  val q2 = term("field2", "word2")
//  val q3 = term("field3", "word3")
//
//  "and()" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      and(null)
//    }
//
//    intercept[IllegalArgumentException] {
//      and(q1, otherQueries = null)
//    }
//
//    intercept[IllegalArgumentException] {
//      and(q1, null)
//    }
//  }
//
//  it must "return first query if other queries is empty" in {
//    val q = and(q1)
//    assert(q != null)
//    assert(q.isInstanceOf[TermQuery])
//    assert(q eq q1)
//  }
//
//  it must "create boolean must query for all args if other queries is not empty" in {
//    val q = and(q1, q2, q3)
//    assert(q != null)
//    assert(q.isInstanceOf[BooleanQuery])
//
//    val bq = q.asInstanceOf[BooleanQuery]
//    val cl = bq.clauses()
//    assertResult(3)(cl.size())
//
//    assert(cl.toList.forall(c => c.getOccur == BooleanClause.Occur.MUST))
//    val qs = cl.toList.map(c => c.getQuery)
//    assertResult(q1)(qs(0))
//    assertResult(q2)(qs(1))
//    assertResult(q3)(qs(2))
//  }
//
//  "or()" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      or(null)
//    }
//
//    intercept[IllegalArgumentException] {
//      or(q1, otherQueries = null)
//    }
//
//    intercept[IllegalArgumentException] {
//      or(q1, null)
//    }
//  }
//
//  it must "return first query if other queries is empty" in {
//    val q = or(q1)
//    assert(q != null)
//    assert(q.isInstanceOf[TermQuery])
//    assert(q eq q1)
//  }
//
//  it must "create boolean must query for all args if other queries is not empty" in {
//    val q = or(q1, q2, q3)
//    assert(q != null)
//    assert(q.isInstanceOf[BooleanQuery])
//
//    val bq = q.asInstanceOf[BooleanQuery]
//    val cl = bq.clauses()
//    assertResult(3)(cl.size())
//
//    assert(cl.toList.forall(c => c.getOccur == BooleanClause.Occur.SHOULD))
//    val qs = cl.toList.map(c => c.getQuery)
//    assertResult(q1)(qs(0))
//    assertResult(q2)(qs(1))
//    assertResult(q3)(qs(2))
//  }
//
//  "not()" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      SolrComplexQueryFactory.not(null)
//    }
//  }
//
//  it must "return not query" in {
//    val q = SolrComplexQueryFactory.not(q1)
//    assert(q != null)
//    assert(q.isInstanceOf[BooleanQuery])
//
//    val bq = q.asInstanceOf[BooleanQuery]
//    val cl = bq.clauses()
//    assertResult(2)(cl.size())
//
//    val cl1 = cl(0)
//    assertResult(BooleanClause.Occur.MUST)(cl1.getOccur)
//    assert(cl1.getQuery.isInstanceOf[MatchAllDocsQuery])
//
//    val cl2 = cl(1)
//    assertResult(BooleanClause.Occur.MUST_NOT)(cl2.getOccur)
//    assertResult(q1)(cl2.getQuery)
//  }
//
//  "complexPhrase(String, String, ...)" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      complexPhrase(null, "value")
//    }
//
//    intercept[IllegalArgumentException] {
//      complexPhrase("  ", "value")
//    }
//
//    intercept[IllegalArgumentException] {
//      complexPhrase("field", null)
//    }
//
//    intercept[IllegalArgumentException] {
//      complexPhrase("field", "   ")
//    }
//
//    intercept[IllegalArgumentException] {
//      complexPhrase("field", "value", null)
//    }
//
//    intercept[IllegalArgumentException] {
//      complexPhrase("field", "value", "  ")
//    }
//  }
//
//  "complexPhrase(String, MultiPhrase, ...)" must "validate args" in {
//    val mNull: MultiPhrasePart = null
//    val mValue: MultiPhrasePart = "value"
//
//    intercept[IllegalArgumentException] { // Can't be blank
//      val mBlank: MultiPhrasePart = "   "
//    }
//
//    intercept[IllegalArgumentException] {
//      complexPhrase(null, mValue)
//    }
//
//    intercept[IllegalArgumentException] {
//      complexPhrase("  ", mValue)
//    }
//
//    intercept[IllegalArgumentException] {
//      complexPhrase("field", mNull)
//    }
//
//    intercept[IllegalArgumentException] {
//      complexPhrase("field", mValue, mNull)
//    }
//  }
//
//  "complexPhrase" must "select from TermQuery, PhraseQuery or MultipartQuery (TermQuery case)" in {
//
//    val term = complexPhrase("field", "value")
//    assert(term != null)
//    assert(term.isInstanceOf[TermQuery])
//    val t1 = term.asInstanceOf[TermQuery]
//    assertResult("field")(t1.getTerm.field())
//    assertResult("value")(t1.getTerm.text())
//
//    val term2 = complexPhrase("field2", Seq("value2"))
//    assert(term2 != null)
//    assert(term2.isInstanceOf[TermQuery])
//    val t2 = term2.asInstanceOf[TermQuery]
//    assertResult("field2")(t2.getTerm.field())
//    assertResult("value2")(t2.getTerm.text())
//  }
//
//  it must "select from TermQuery, PhraseQuery or MultipartQuery (PhraseQuery case)" in {
//    val phrase = complexPhrase("field", "value1", "value2", "value3")
//    assert(phrase != null)
//    assert(phrase.isInstanceOf[PhraseQuery])
//    val p = phrase.asInstanceOf[PhraseQuery]
//    val terms = p.getTerms
//    assertResult(3)(terms.length)
//    val p1 = terms(0)
//    assertResult("field")(p1.field())
//    assertResult("value1")(p1.text())
//    val p2 = terms(1)
//    assertResult("field")(p2.field())
//    assertResult("value2")(p2.text())
//    val p3 = terms(2)
//    assertResult("field")(p3.field())
//    assertResult("value3")(p3.text())
//
//    val phraseFromMulti = complexPhrase("field", Seq("value1"), Seq("value2"), Seq("value3"))
//    assert(phraseFromMulti != null)
//    assert(phraseFromMulti.isInstanceOf[PhraseQuery])
//    val ph = phrase.asInstanceOf[PhraseQuery]
//    val terms2 = ph.getTerms
//    assertResult(3)(terms2.length)
//    val ph1 = terms2(0)
//    assertResult("field")(ph1.field())
//    assertResult("value1")(ph1.text())
//    val ph2 = terms2(1)
//    assertResult("field")(ph2.field())
//    assertResult("value2")(ph2.text())
//    val ph3 = terms2(2)
//    assertResult("field")(ph3.field())
//    assertResult("value3")(ph3.text())
//
//    val phraseFromMulti2 = complexPhrase("field", "value1", Seq("value2"), Seq("value3"))
//    assert(phraseFromMulti2 != null)
//    assert(phraseFromMulti2.isInstanceOf[PhraseQuery])
//    val pr = phrase.asInstanceOf[PhraseQuery]
//    val terms3 = pr.getTerms
//    assertResult(3)(terms3.length)
//    val pr1 = terms2(0)
//    assertResult("field")(pr1.field())
//    assertResult("value1")(pr1.text())
//    val pr2 = terms2(1)
//    assertResult("field")(pr2.field())
//    assertResult("value2")(pr2.text())
//    val pr3 = terms2(2)
//    assertResult("field")(pr3.field())
//    assertResult("value3")(pr3.text())
//  }
//
//  it must "select from TermQuery, PhraseQuery or MultipartQuery (MultipartQuery case)" in {
//    val multiPhrase = complexPhrase("field", "value1", Seq("value2", "value3"), "value4")
//    assert(multiPhrase != null)
//    assert(multiPhrase.isInstanceOf[MultiPhraseQuery])
//
//    val m = multiPhrase.asInstanceOf[MultiPhraseQuery]
//    val ta = m.getTermArrays
//
//    assert(ta.toList.flatten.forall(t => t.field() == "field"))
//    val ta1 = ta(0)
//    assertResult(1)(ta1.length)
//    assertResult("value1")(ta1.head.text())
//
//    val ta2 = ta(1)
//    assertResult(2)(ta2.length)
//    assertResult("value2")(ta2.head.text())
//    assertResult("value3")(ta2.last.text())
//
//    val ta3 = ta(2)
//    assertResult(1)(ta3.length)
//    assertResult("value4")(ta3.head.text())
//  }
//}
