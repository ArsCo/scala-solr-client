package ars.solr.client.conversion

import org.scalamock.scalatest.MockFactory
import org.scalatest.FreeSpec

/** Test for [[Converter]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
class ConverterTest extends FreeSpec with MockFactory {

  "Converter" - {
    "must have correct signature" in {
      class C extends Converter[Int, String] {
        override def convert(from: Int) = Some(from.toString)
      }
    }
  }

  "~()" - {

    class TestConverter extends Converter[Int, String] {
      override def convert(from: Int) = Some(from.toString)
    }

    "must creates new instance of `Converter`" in {
      val a = new TestConverter
      val b = new TestConverter
      val r = a ~: b

      assert(a ne b)
      assert(a ne r)
      assert(b ne r)
    }

    "must first call arg `converter`" in {
      ???
    }

    "must call `this` converter if `converter` returns `None`" in {
      ???
    }
  }
}
