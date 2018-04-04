package ars.solr.client.conversion

import ars.solr.client.SolrDocumentMap
import ars.solr.client.conversion.CanConvertTo.{get, opt}
import ars.solr.exception.SolrException
import org.scalatest.FreeSpec

import scala.util.Success

/** Test for [[CanConvertTo]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
class CanConvertToTest extends FreeSpec {

  implicit val m: SolrDocumentMap = Map(
    "null value" -> null,
    "boolean value" -> true,

    "byte value" -> 8.asInstanceOf[Byte],
    "short value" -> 3.asInstanceOf[Short],
    "int value" -> 5,
    "long value" -> 22L,

    "float value" -> 4.5F,
    "double value" -> 6.7,

    "string value" -> "good string",

    "java.lang.Integer value" -> new java.lang.Integer(55)
  )

  "trait CanConvertTo" - {
    "must have correct signature" in {
      class A extends CanConvertTo[Int] {
        override def convert(from: Any) = Success(5)
      }
    }
  }

  "get[T](...)" - {

    "must validate args" in {
      intercept[IllegalArgumentException] {
        get[Int](null)
      }

      intercept[IllegalArgumentException] {
        get[Int]("int value", null)
      }

      intercept[IllegalArgumentException] {
        get[Int]("int value")(null, null)
      }

      intercept[IllegalArgumentException] {
        get[Int]("int value")(Map(), null)
      }
    }

    "if there's `field` in the `document`" - {
      "if `field` value is `null`" - {
        "`NullPointerException` will be thrown" in {
          intercept[NullPointerException] {
            get[Int]("null value")
          }
        }
      }

      "if `field` value type is `T` " - {
        "it will be returned" in {
          assertResult(m("int value")) {
            get[Int]("int value")
          }

          assertResult(m("java.lang.Integer value")) {
            get[java.lang.Integer]("java.lang.Integer value")
          }
        }
      }

      "if `field` value type isn't `T`" - {
        "it will be converted to `T` with `canConvertTo`" - {

          import ars.solr.client.conversion.converters.NumericConverters.DefaultIntConverter

          "if conversion to `T` is successful" - {
            "converted value will be returned" in {
              assertResult(55) {
                get[Int]("java.lang.Integer value")
              }
            }
          }

          "if conversion to `T` is failed" - {
            "`SolrException` will be thrown" in {
              intercept[SolrException] {
                get[Int]("string value")
              }
            }
          }
        }
      }
    }

    "if there's no `field` in the `document`" - {
      "if `default` is `Some(value)` then " - {
        "`value` will be returned" in {

          import implicits._

          assertResult(50) {
            get[Int]("no value", 50)
          }
        }
      }

      "otherwise `SolrException` will be thrown" in {
        intercept[SolrException] {
          get[Int]("no value")
        }
      }
    }
  }

  "opt[T](...)" - {

    "must validate args" in {
      intercept[IllegalArgumentException] {
        opt[Int](null)
      }

      intercept[IllegalArgumentException] {
        opt[Int]("int value")(null, null)
      }

      intercept[IllegalArgumentException] {
        opt[Int]("int value")(Map(), null)
      }
    }

    "if there's `field` in the `document`" - {
      "if `field` value is `null`" - {
        "`NullPointerException` will be thrown" in {
          intercept[NullPointerException] {
            opt[String]("null value")
          }
        }
      }

      "if `field` value type is `T` " - {
        "`Some` it will be returned" in {
          assertResult(Some(m("string value"))) {
            opt[String]("string value")
          }
        }
      }

      "if `field` value type isn't `T`" - {
        "it will be converted to `T` with `canConvertTo`" - {

          import ars.solr.client.conversion.converters.NumericConverters.DefaultIntConverter

          "if conversion to `T` is successful" - {
            "`Some` of converted value will be returned" in {
              assertResult(Some(55)) {
                opt[Int]("java.lang.Integer value")
              }
            }
          }

          "if conversion to `T` is failed" - {
            "`SolrException` will be thrown" in {
              intercept[SolrException] {
                opt[Int]("string value")
              }
            }
          }
        }
      }
    }

    "if there's no `field` in the `document`" - {
      "`None` will be returned" in {
        assertResult(None) {
          opt[Int]("no value")
        }
      }
    }
  }
}
