//package ars.solr.query
//
//import ars.solr.AbstractBaseTest
//import ars.solr.query.SolrBasicQueryFactory.{boolean, fuzzy, must}
//import org.apache.lucene.search._
//import org.apache.lucene.util.QueryBuilder
//import ars.solr.query._
//
///** Tests for [[SolrRichFieldQuery]].
//  *
//  * @author ars (Ibragimov Arsen)
//  * @since 0.0.2
//  */
//class SolrRichFieldQueryTest extends AbstractBaseTest {
//
//  "RichNativeQuery" must "have implicit coversions" in {
//    val r: SolrRichFieldQuery = "field"
//    assert(r != null)
//    assertResult("field")(r.field)
//  }
//
//  "method '-->'" must "select from TermQuery, PhraseQuery or MultipartQuery (TermQuery case)" in {
//
//    val term = "field" --> "value"
//    assert(term != null)
//    assert(term.isInstanceOf[TermQuery])
//
//    val term2 = "field" --> Seq("value")
//    assert(term2 != null)
//    assert(term2.isInstanceOf[TermQuery])
//  }
//
//  it must "select from TermQuery, PhraseQuery or MultipartQuery (PhraseQuery case)" in {
//
//    val phrase = "field" --> ("value1", "value2", "value3")
//    assert(phrase != null)
//    assert(phrase.isInstanceOf[PhraseQuery])
//
//    val phraseFromMulti = "field" --> (Seq("value1"), Seq("value2"), Seq("value3"))
//    assert(phraseFromMulti != null)
//    assert(phraseFromMulti.isInstanceOf[PhraseQuery])
//  }
//
//  it must "select from TermQuery, PhraseQuery or MultipartQuery (MultipartQuery case)" in {
//    val multiPhrase = "field" --> ("value1", Seq("value2", "value3"), "value4")
//    assert(multiPhrase != null)
//    assert(multiPhrase.isInstanceOf[MultiPhraseQuery])
//  }
//
//  "method '*~>'" must "create wildcard query" in {
//    val wildcard = "field" *~> "value1*-\\ *"
//    assert(wildcard != null)
//    assert(wildcard.isInstanceOf[WildcardQuery])
//  }
//
//  "method '*->'" must "create prefix query" in {
//    val prefix = "field" -*> "value"
//    assert(prefix != null)
//    assert(prefix.isInstanceOf[PrefixQuery])
//  }
//
//  "method '?->'" must "create regexp query" in {
//    val regexp = "field" -?> "[0-9a-z]?[0-9]*"
//    assert(regexp != null)
//    assert(regexp.isInstanceOf[RegexpQuery])
//  }
//
//  "method '~~>'" must "create fuzzy query" in {
//    val fuzzy = "field" ~~> "term"
//    assert(fuzzy != null)
//    assert(fuzzy.isInstanceOf[FuzzyQuery])
//  }
//}
