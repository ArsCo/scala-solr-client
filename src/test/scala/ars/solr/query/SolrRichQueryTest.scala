//package ars.solr.query
//
//import ars.solr.AbstractBaseTest
//import ars.solr.query.SolrBasicQueryFactory._
//import ars.solr.query.SolrComplexQueryFactory._
//
///** Tests for [[SolrBasicQueryFactory]].
//  *
//  * @author ars (Ibragimov Arsen)
//  * @since 0.0.3
//  */
//class SolrRichQueryTest extends AbstractBaseTest {
//
//  private val t1 = term("field1", "text1")
//  private val t2 = term("field2", "text2")
//  private val t3 = term("field3", "text3")
//
//  "method 'AND'" must "provide awesome syntax" in {
//    t1 AND t2
//    t1 AND t2 AND t3
//    t1 AND (t2, t3)
//  }
//
//  "method 'OR'" must "provide awesome syntax" in {
//    t1 OR t2
//    t1 OR t2 OR t3
//    t1 OR (t2, t3)
//  }
//
//  "method 'not'" must "provide awesome syntax" in {
//    t1.not
//  }
//
//  "method 'boost'" must "provide awesome syntax" in {
//    t1.boost(2.6f)
//    t1 boost 0.5f
//  }
//
//  "method '&&'" must "provide awesome syntax" in {
//    t1 && t2
//    t1 && t2 && t3
//    t1 && (t2, t3)
//  }
//
//  "method '||'" must "provide awesome syntax" in {
//    t1 || t2
//    t1 || t2 || t3
//    t1 || (t2, t3)
//  }
//
//  "method 'unary_!'" must "provide awesome syntax" in {
//    !t1
//  }
//
//  "method '^'" must "provide awesome syntax" in {
//    t1 * 0.5f
//  }
//
//  "methods '&&', '||', '^', '!'" must "provide right precedence" in {
//    val q = t1 || !t2 && t3 * 0.6f
//    val q1 = or(
//      t1,
//      and(
//        SolrComplexQueryFactory.not(t2),
//        SolrBasicQueryFactory.boost(t3, 0.6f)
//      )
//    )
//    assertResult(q1.toString)(q.toString)
//  }
//
//  it must "be consistent" in {
//    val q1 = term("field", "word")
//    val q2 = term("field", "word")
//    val q3 = term("field", "word")
//    val q4 = term("field", "word")
//
//    q1 && !q2 * 0.7f && q3 * 1.3f && q4 * 0.65f
//  }
//}
