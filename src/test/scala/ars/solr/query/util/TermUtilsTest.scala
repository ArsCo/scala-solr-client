package ars.solr.query.util

import ars.solr.AbstractBaseTest
import ars.solr.query.util.TermUtils.{toTerm, toTermsArray}
import org.apache.lucene.index.Term

/** Tests for [[TermUtils]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
class TermUtilsTest extends AbstractBaseTest {

  val field = "field"
  val text = "text"
  val text1 = "text1"
  val text2 = "text2"

  "toTerm" must "validate args" in {

    intercept[IllegalArgumentException] {
      toTerm(null, text)
    }

    intercept[IllegalArgumentException] {
      toTerm("   ", text)
    }

    intercept[IllegalArgumentException] {
      toTerm(field, null)
    }

    intercept[IllegalArgumentException] {
      toTerm(field, "   ")
    }
  }

  it must "create the new valid Term object" in {
    val t = toTerm(field, text)
    assert(t != null)
    assert(t.isInstanceOf[Term])
    assertResult(field)(t.field())
    assertResult(text)(t.text())
  }

  "toTermsArray" must "validate args" in {
    intercept[IllegalArgumentException] {
      toTermsArray(null, Seq(text))
    }

    intercept[IllegalArgumentException] {
      toTermsArray("   ", Seq(text))
    }

    intercept[IllegalArgumentException] {
      toTermsArray(field, Seq())
    }

    intercept[IllegalArgumentException] {
      toTermsArray(field, null)
    }

    intercept[IllegalArgumentException] {
      toTermsArray(field, Seq(null))
    }

    intercept[IllegalArgumentException] {
      toTermsArray(field, Seq("  "))
    }
  }

  it must "create the new array of terms with the same field name" in {
    val t = toTermsArray(field, Seq(text, text1, text2))
    assert(t != null)
    assert(t.isInstanceOf[Array[Term]])
    assertResult(3)(t.length)

    val t1 = t(0)
    assertResult(new Term(field, text))(t1)

    val t2 = t(1)
    assertResult(new Term(field, text1))(t2)

    val t3 = t(2)
    assertResult(new Term(field, text2))(t3)
  }

}
