//package ars.solr.query
//
//import ars.solr.AbstractBaseTest
//import ars.solr.query.SolrBasicQueryFactory._
//import org.apache.lucene.search._
//import org.apache.lucene.util.automaton.RegExp
//
//import scala.collection.JavaConversions._
//
///** Tests for [[SolrBasicQueryFactory]].
//  *
//  * @author ars (Ibragimov Arsen)
//  * @since 0.0.3
//  */
//class SolrBasicQueryFactoryTest extends AbstractBaseTest {
//
//  "term" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      term(null, "value")
//    }
//
//    intercept[IllegalArgumentException] {
//      term("", "value")
//    }
//
//    intercept[IllegalArgumentException] {
//      term("  ", "value")
//    }
//
//    intercept[IllegalArgumentException] {
//      term("name", null)
//    }
//
//    intercept[IllegalArgumentException] {
//      term("name", "")
//    }
//
//    intercept[IllegalArgumentException] {
//      term("name", "  ")
//    }
//  }
//
//  it must "be valid TermQuery" in {
//    val t = term("name", "value")
//    assert(t != null)
//    assert(t.isInstanceOf[TermQuery])
//
//    val t1 = t.getTerm
//    assert(t1 != null)
//    assertResult("name")(t1.field())
//    assertResult("value")(t1.text())
//  }
//
//  "wildcard" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      wildcard(null, "value")
//    }
//
//    intercept[IllegalArgumentException] {
//      wildcard("", "value")
//    }
//
//    intercept[IllegalArgumentException] {
//      wildcard("  ", "value")
//    }
//
//    intercept[IllegalArgumentException] {
//      wildcard("name", null)
//    }
//
//    intercept[IllegalArgumentException] {
//      wildcard("name", "")
//    }
//
//    intercept[IllegalArgumentException] {
//      wildcard("name", "  ")
//    }
//  }
//
//  it must "be valid WildcardQuery" in {
//    val t = wildcard("name", "value")
//    assert(t != null)
//    assert(t.isInstanceOf[WildcardQuery])
//
//    val t1 = t.getTerm
//    assert(t1 != null)
//    assertResult("name")(t1.field())
//    assertResult("value")(t1.text())
//  }
//
//  "phrase" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      phrase(null)()
//    }
//
//    intercept[IllegalArgumentException] {
//      phrase("")()
//    }
//
//    intercept[IllegalArgumentException] {
//      phrase("   ")()
//    }
//
//    intercept[IllegalArgumentException] {
//      phrase("field", -10)()
//    }
//
//    intercept[IllegalArgumentException] {
//      phrase("field")(null)
//    }
//
//    intercept[IllegalArgumentException] {
//      phrase("field")("")
//    }
//
//    intercept[IllegalArgumentException] {
//      phrase("field")("  ")
//    }
//  }
//
//  it must "be valid PhraseQuery" in {
//    val p = phrase("field", 5)("v1", "v2", "v3")
//    assert(p != null)
//    assert(p.isInstanceOf[PhraseQuery])
//    assertResult(5)(p.getSlop)
//
//    val p0 = p.getTerms
//    assertResult(3)(p0.length)
//
//    val p1 = p0(0)
//    assertResult("field")(p1.field())
//    assertResult("v1")(p1.text())
//
//    val p2 = p0(1)
//    assertResult("field")(p2.field())
//    assertResult("v2")(p2.text())
//
//    val p3 = p0(2)
//    assertResult("field")(p3.field())
//    assertResult("v3")(p3.text())
//  }
//
//  it must "have valid default params" in {
//    val p = phrase("field")()
//    assert(p != null)
//    assert(p.isInstanceOf[PhraseQuery])
//    assertResult(0)(p.getSlop)
//    assertResult(0)(p.getTerms.length)
//  }
//
//  "prefix" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      prefix(null, "value")
//    }
//
//    intercept[IllegalArgumentException] {
//      prefix("", "value")
//    }
//
//    intercept[IllegalArgumentException] {
//      prefix("  ", "value")
//    }
//
//    intercept[IllegalArgumentException] {
//      prefix("name", null)
//    }
//
//    intercept[IllegalArgumentException] {
//      prefix("name", "")
//    }
//
//    intercept[IllegalArgumentException] {
//      prefix("name", "  ")
//    }
//  }
//
//  it must "be valid PrefixQuery" in {
//    val p = prefix("field", "prefix")
//    assert(p != null)
//    assert(p.isInstanceOf[PrefixQuery])
//
//    val p0 = p.getPrefix
//    assertResult("field")(p0.field())
//    assertResult("prefix")(p0.text())
//  }
//
//  "multiPhrase" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      multiPhrase(null)()
//    }
//
//    intercept[IllegalArgumentException] {
//      multiPhrase("")()
//    }
//
//    intercept[IllegalArgumentException] {
//      multiPhrase("  ")()
//    }
//
//    intercept[IllegalArgumentException] {
//      multiPhrase("name")(Nil)
//    }
//
//    intercept[IllegalArgumentException] {
//      multiPhrase("name")(null)
//    }
//  }
//
//  it must "be valid MultiPhraseQuery" in {
//    val m = multiPhrase("name")("val1", Seq("val2", "val3"), "val4")
//    assert(m != null)
//    assert(m.isInstanceOf[MultiPhraseQuery])
//
//    val t = m.getTermArrays
//    assertResult(3)(t.length)
//
//    val arr1 = t(0)
//    assertResult(1)(arr1.length)
//    assertResult("name")(arr1(0).field())
//    assertResult("val1")(arr1(0).text())
//
//    val arr2 = t(1)
//    assertResult(2)(arr2.length)
//    assertResult("name")(arr2(0).field())
//    assertResult("val2")(arr2(0).text())
//    assertResult("name")(arr2(1).field())
//    assertResult("val3")(arr2(1).text())
//
//    val arr3 = t(2)
//    assertResult(1)(arr3.length)
//    assertResult("name")(arr3(0).field())
//    assertResult("val4")(arr3(0).text())
//
//  }
//
//  "MultiPhrasePart" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      val terms: Iterable[String] = null
//      MultiPhrasePart(terms)
//    }
//
//    intercept[IllegalArgumentException] {
//      MultiPhrasePart(Nil)
//    }
//
//    intercept[IllegalArgumentException] {
//      new MultiPhrasePart(Seq(null))
//    }
//
//    intercept[IllegalArgumentException] {
//      MultiPhrasePart(Seq(""))
//    }
//
//    intercept[IllegalArgumentException] {
//      MultiPhrasePart(Seq(" "))
//    }
//  }
//
//  it must "be valid" in {
//    val vals = Seq("val1", "val2", "val3")
//    val m = MultiPhrasePart(vals)
//    assertResult(vals)(m.terms)
//  }
//
//  it must "have implicit convertions" in {
//    val v: MultiPhrasePart = "value"
//    val t = v.terms
//    assertResult(1)(t.size)
//    assertResult("value")(t.iterator.next())
//
//    val v1: MultiPhrasePart = Seq("value1", "value2")
//    val t1 = v1.terms
//    assertResult(2)(t1.size)
//    val i = t1.toList
//    assertResult("value1")(i.head)
//    assertResult("value2")(i.last)
//  }
//
//  "field" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      field(null)
//    }
//
//    intercept[IllegalArgumentException] {
//      field("")
//    }
//
//    intercept[IllegalArgumentException] {
//      field("")
//    }
//  }
//
//  it must "be valid FieldQuery" in {
//    val q = field("field")
//    assert(q != null)
//    // TODO ==== LEGACY CODE ======
//    q match {
//      case w: WildcardQuery =>
//        val t = w.getTerm
//        assertResult("field")(t.field())
//        assertResult("*")(t.text())
//
//      case _ => fail()
//    }
//
//    // TODO ========= NEW CODE =========
////    q match {
////      case w: FieldQuery =>
////        val t = w.getTerm
////        assertResult("field")(t.field())
////        assertResult("*")(t.text())
////
////      case _ => fail()
////    }
//  }
//
//  "fuzzy" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      fuzzy(null, "term")
//    }
//
//    intercept[IllegalArgumentException] {
//      fuzzy("", "term")
//    }
//
//    intercept[IllegalArgumentException] {
//      fuzzy("  ", "term")
//    }
//
//    intercept[IllegalArgumentException] {
//      fuzzy("field", null)
//    }
//
//    intercept[IllegalArgumentException] {
//      fuzzy("field", "")
//    }
//
//    intercept[IllegalArgumentException] {
//      fuzzy("field", "  ")
//    }
//
//    intercept[IllegalArgumentException] {
//      fuzzy("field", "term" , maxEdits = -1)
//    }
//
//    intercept[IllegalArgumentException] {
//      fuzzy("field", "term", maxEdits = 0)
//    }
//    fuzzy("field", "term", maxEdits = 1)
//
//    intercept[IllegalArgumentException] {
//      fuzzy("field", "term", prefixLength = -1)
//    }
//    fuzzy("field", "term", prefixLength = 0)
//    fuzzy("field", "term", prefixLength = 1)
//
//    intercept[IllegalArgumentException] {
//      fuzzy("field", "term", maxExpansions = -1)
//    }
//
//    intercept[IllegalArgumentException] {
//      fuzzy("field", "term", maxExpansions = 0)
//    }
//    fuzzy("field", "term", maxExpansions = 1)
//  }
//
//  it must "be valid FuzzyQuery" in {
//    val f = fuzzy("field", "term", maxEdits = 2, prefixLength = 6, maxExpansions = 7, false)
//    assert(f != null)
//    val t = f.getTerm
//    assertResult("field")(t.field())
//    assertResult("term")(t.text())
//
//    assertResult(2)(f.getMaxEdits)
//    assertResult(6)(f.getPrefixLength)
//    //assertResult(7)(f.getMaxExpansions) TODO No getter
//    assertResult(false)(f.getTranspositions)
//  }
//
//  it must "have valid default params" in {
//    val f = fuzzy("field", "term")
//
//    assertResult(2)(f.getMaxEdits)
//    assertResult(0)(f.getPrefixLength)
////    assertResult(50)(f.) TODO No getter
//    assertResult(true)(f.getTranspositions)
//  }
//
//  "disjunction" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      disjunction(0.6f, null)
//    }
//
//    intercept[IllegalArgumentException] {
//      disjunction(0.6f)
//    }
//  }
//
//  it must "be valid DisjunctionMaxQuery" in {
//    val v = disjunction(1.5f, term("f1", "v1"), term("f2", "v2"))
//    assert(v != null)
//
//    val s = v.getDisjuncts
//    val s1 = s(0).asInstanceOf[TermQuery].getTerm
//    assert(s1 != null)
//    assertResult("f1")(s1.field())
//    assertResult("v1")(s1.text())
//
//    val s2 = s(1).asInstanceOf[TermQuery].getTerm
//    assert(s2 != null)
//    assertResult("f2")(s2.field())
//    assertResult("v2")(s2.text())
//
//    assertResult(1.5f)(v.getTieBreakerMultiplier)
//  }
//
//  "constantScore" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      constantScore(null)
//    }
//  }
//
//  it must "be valid ConstantScoreQuery" in {
//    val c = constantScore(term("field", "value"))
//    assert(c != null)
//    assert(c.isInstanceOf[ConstantScoreQuery])
//
//    val q = c.getQuery
//    assert(q != null)
//    assert(q.isInstanceOf[TermQuery])
//
//    val t = q.asInstanceOf[TermQuery].getTerm
//    assertResult("field")(t.field())
//    assertResult("value")(t.text())
//  }
//
//  "boolean" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      boolean(null)
//    }
//
//    intercept[IllegalArgumentException] {
//      boolean(Seq() :_*)
//    }
//
//    intercept[IllegalArgumentException] {
//      boolean(Seq(null) :_*)
//    }
//  }
//
//  it must "be valid BooleanQuery" in {
//    val b = boolean(must(term("field", "value")))
//    assert(b != null)
//    assert(b.isInstanceOf[BooleanQuery])
//
//    val q = b.asInstanceOf[BooleanQuery]
//    val c = q.clauses()
//    assertResult(1)(c.size())
//    val t = c.head
//    assertResult(BooleanClause.Occur.MUST)(t.getOccur)
//    val term1 = t.getQuery.asInstanceOf[TermQuery].getTerm
//    assertResult("field")(term1.field())
//    assertResult("value")(term1.text())
//  }
//
//  "must" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      must(null)
//    }
//  }
//
//  it must "be valid must BooleanClause" in {
//    val b = must(term("field", "value"))
//    assert(b != null)
//    assert(b.isInstanceOf[BooleanClause])
//
//    val q = b.asInstanceOf[BooleanClause]
//    assertResult(BooleanClause.Occur.MUST)(q.getOccur)
//    val term1 = q.getQuery.asInstanceOf[TermQuery].getTerm
//    assertResult("field")(term1.field())
//    assertResult("value")(term1.text())
//  }
//
//  "should" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      should(null)
//    }
//  }
//
//  it must "be valid should BooleanClause" in {
//    val b = should(term("field", "value"))
//
//    val q = b.asInstanceOf[BooleanClause]
//    assertResult(BooleanClause.Occur.SHOULD)(q.getOccur)
//    val term1 = q.getQuery.asInstanceOf[TermQuery].getTerm
//    assertResult("field")(term1.field())
//    assertResult("value")(term1.text())
//  }
//
//  "mustNot" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      mustNot(null)
//    }
//  }
//
//  it must "be valid mustNot BooleanClause" in {
//    val b = mustNot(term("field", "value"))
//
//    val q = b.asInstanceOf[BooleanClause]
//    assertResult(BooleanClause.Occur.MUST_NOT)(q.getOccur)
//    val term1 = q.getQuery.asInstanceOf[TermQuery].getTerm
//    assertResult("field")(term1.field())
//    assertResult("value")(term1.text())
//  }
//
//  "allDocs" must "be valid MatchAllDocsQuery" in {
//    val a = allDocs
//    assert(a != null)
//    assert(a.isInstanceOf[MatchAllDocsQuery])
//  }
//
//  "ngram" must "validate args" in {
//    val p = phrase("field")("a", "b", "c")
//
//    intercept[IllegalArgumentException] {
//      ngram(-1, p)
//    }
//
//    intercept[IllegalArgumentException] {
//      ngram(0, p)
//    }
//
//    intercept[IllegalArgumentException] {
//      ngram(1, null)
//    }
//  }
//
//  it must "be valid NGramPhraseQuery" in {
//    val p = phrase("field")("a", "b", "c")
//    val q = ngram(1, p)
//    assert(q != null)
//    assert(q.isInstanceOf[NGramPhraseQuery])
//
//    val ng = q.asInstanceOf[NGramPhraseQuery]
//    assertResult(3)(ng.getTerms.length)
//    assertResult(p.getTerms)(ng.getTerms)
//
////    ng.getSize() TODO No getters!
//  }
//
//  "regExp" must "validate args" in {
//    val field = "field"
//    val expr = "[expr]?(11){2,3}"
//
//    intercept[IllegalArgumentException] {
//      regExp(null, expr)
//    }
//
//    intercept[IllegalArgumentException] {
//      regExp("  ", expr)
//    }
//
//    intercept[IllegalArgumentException] {
//      regExp(field, null)
//    }
//
//    intercept[IllegalArgumentException] {
//      regExp(field, "  ")
//    }
//  }
//
//  it must "be valid RegexpQuery" in {
//    val r = regExp("field", "[expr]?(11){2,3}", RegExp.ALL)
//    assert(r != null)
//    assert(r.isInstanceOf[RegexpQuery])
//
//    val q = r.asInstanceOf[RegexpQuery]
//    // TODO No getters!
//  }
//
//  it must "have valid default params" in {
//    var r = regExp("field", "[expr]?(11){2,3}")
//    // TODO No getters!
//  }
//
//  "range" must "validate args" in {
//    val field = "field"
//    val lower = "low"
//    val isIncLower = false
//    val upper = "up"
//    val isIncUpper = true
//
//    intercept[IllegalArgumentException] {
//      range(null)(lower, isIncLower)(upper, isIncUpper)
//    }
//
//    intercept[IllegalArgumentException] {
//      range("   ")(lower, isIncLower)(upper, isIncUpper)
//    }
//
//    intercept[IllegalArgumentException] {
//      range(field)(null, isIncLower)(upper, isIncUpper)
//    }
//
//    intercept[IllegalArgumentException] {
//      range(field)("   ", isIncLower)(upper, isIncUpper)
//    }
//
//    intercept[IllegalArgumentException] {
//      range(field)(lower, isIncLower)(null, isIncUpper)
//    }
//
//    intercept[IllegalArgumentException] {
//      range(field)(lower, isIncLower)("   ", isIncUpper)
//    }
//  }
//
//  it must "have valid default params" in {
//    val field = "field"
//    val lower = "low"
//    val upper = "up"
//
//    val q = range(field)(lower)(upper)
//    assert(q != null)
//    q match {
//      case r: TermRangeQuery =>
//        assertResult(true)(r.includesLower())
//        assertResult(true)(r.includesUpper())
//      case _ => fail()
//    }
//
//  }
//
//  it must "be valid TermRangeQuery" in {
//    val field = "field"
//    val lower = "low"
//    val isIncLower = false
//    val upper = "up"
//    val isIncUpper = true
//
//    val q = range(field)(lower, isIncLower)(upper, isIncUpper)
//    assert(q != null)
//    assert(q.isInstanceOf[TermRangeQuery])
//
//    val r = q.asInstanceOf[TermRangeQuery]
//    assertResult(field)(r.getField)
//    assertResult(lower)(r.getLowerTerm.utf8ToString())
//
//    assertResult(isIncLower)(r.includesLower())
//    assertResult(upper)(r.getUpperTerm.utf8ToString())
//    assertResult(isIncUpper)(r.includesUpper())
//  }
//
//  "boost" must "validate args" in {
//    intercept[IllegalArgumentException] {
//      boost(null, 0.6f)
//    }
//  }
//
//  it must "set query boost" in {
//    val t = term("field", "value")
//    val b = boost(t, 0.6f)
//
//    assert(b.getTerm eq t.getTerm)
//    assertResult(0.6f)(b.getBoost)
//  }
//}
