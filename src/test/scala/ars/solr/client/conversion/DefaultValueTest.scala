package ars.solr.client.conversion

import org.scalatest.FlatSpec

/** Tests for [[DefaultValue]].
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.5
  */
class DefaultValueTest extends FlatSpec {

  private val DefaultValueClassName = classOf[DefaultValue[_]].getSimpleName
  private val DefValClassName = classOf[DefVal[_]].getSimpleName
  private val NoDefValClassName = NoDefVal.getClass.getSimpleName

  DefaultValueClassName must s"have $DefValClassName and $NoDefValClassName subclasses" in {
    assert(DefVal(5).isInstanceOf[DefaultValue[_]])
    assert(DefVal("String").isInstanceOf[DefaultValue[_]])
    assert(NoDefVal.isInstanceOf[DefaultValue[_]])
  }

  DefValClassName must "validate args" in {
    DefVal(5)
    DefVal("String")

    intercept[IllegalArgumentException] {
      DefVal(null)
    }
  }
}
