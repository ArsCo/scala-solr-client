package ars.solr.client.conversion

import org.scalatest.FlatSpec

import scala.util.{Success, Try}

/** Test for [[AbstractCanConvertTo]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
class AbstractCanConvertToTest extends FlatSpec {

  type JInt = java.lang.Integer

  class IntCanConvertTo extends AbstractCanConvertTo[Int] {
    override def tryConvert(from: Any): Try[Int] = {
      from match {
        case v: JInt => Success(v.intValue())
        case _ => fail();
      }
    }
  }

  class StringCanConvertTo extends AbstractCanConvertTo[String] {
    override def tryConvert(from: Any): Try[String] = Success(from.asInstanceOf[String])
  }

  "convert()" must "validates `from` args" in {
    intercept[IllegalArgumentException] {
      new StringCanConvertTo().convert(null)
    }

  }

  it must "if `from` is valid and is type is `To` returns `from`" in {
    assertResult(Success(7)) {
      new IntCanConvertTo().convert(7)
    }
  }

  it must "if `from` is valid and is type is not `To` then invokes tryConvert()" in {
    assertResult(Success(53)) {
      new IntCanConvertTo().convert(new JInt(53))
    }
  }
}
