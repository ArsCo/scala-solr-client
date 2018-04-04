//package ars.solr.client.conversion
//
//import ars.solr.AbstractBaseTest
//import ars.solr.client.SolrDocumentMap
//import ars.solr.client.implicits.CanConvertMapToClass
//import ars.solr.exception.SolrException
//
///** Tests for [[CanConvertMapToClass]].
//  *
//  * @author ars (Ibragimov Arsen)
//  * @since 0.0.5
//  */
//class CanConvertMapToClassTest extends AbstractBaseTest {
//
//  "CanConvertMapToCaseClass" must "have correct interface" in {
//    class ResultType
//
//    class ResultTypeConverter extends CanConvertMapToClass[ResultType] {
//      override def convert(document: SolrDocumentMap): ResultType = {
//        new ResultType
//      }
//    }
//  }
//
//  "get[T](field: String)" must "gets required value from SolrDocumentMap" in {
//    import CanConvertMapToClass._
//
//    implicit case class FakeCustomType(result: String)
//
//    class ResultTypeConverter extends CanConvertMapToClass[FakeCustomType] {
//      override def convert(document: SolrDocumentMap): FakeCustomType = {
//        implicit val d: SolrDocumentMap = document
//        FakeCustomType(get[String]("fake custom object"))
//      }
//    }
//
//    implicit val doc: SolrDocumentMap = Map(
//      "intField" -> 5,
//      "floatField" -> 89.6f,
//      "stringField" -> "String",
//      "optionInt" -> Some(34),
//      "option null" -> Some(null),
//      "option none" -> None,
//      "fake custom" -> FakeCustomType("fake result")
//    )
//
//    assertResult(5){
//      get[Int]("intField")
//    }
//
//    assertResult(89.6f){
//      get[Float]("floatField")
//    }
//
//    assertResult(Some(34)){
//      get[Option[Int]]("optionInt")
//    }
//
//    assertResult(Some(null)){
//      get[Option[String]]("option null")
//    }
//
//    assertResult(None){
//      get[Option[Int]]("option none")
//    }
//
//    assertResult("String"){
//      get[String]("stringField")
//    }
//
//    assertResult(FakeCustomType("fake result")) {
//      get[FakeCustomType]("fake custom")
//    }
//  }
//
//  it must "throws SolrConversionException if there's no field or it's of bad type" in {
//    import CanConvertMapToClass._
//
//    implicit val doc: SolrDocumentMap = Map(
//      "floatField" -> 89.6f,
//      "stringField" -> "String"
//    )
//
//    intercept[SolrConversionException] {
//      get[Int]("no field")
//    }
//
//    intercept[SolrConversionException] {
//      get[Float]("stringField")
//    }
//  }
//
//  "get[T](field: String, default: T)" must "gets existing value from SolrDocumentMap" in {
//
//    implicit case class FakeCustomType(result: String)
//
//    class ResultTypeConverter extends CanConvertMapToClass[FakeCustomType] {
//      override def convert(document: SolrDocumentMap): FakeCustomType = {
//        implicit val d: SolrDocumentMap = document
//        FakeCustomType(get[String]("fake custom object"))
//      }
//    }
//
//    implicit val doc: SolrDocumentMap = Map(
//      "intField" -> 5,
//      "floatField" -> 89.6f,
//      "stringField" -> "String",
//      "optionInt" -> Some(34),
//      "option null" -> Some(null),
//      "option none" -> None,
//      "fake custom" -> FakeCustomType("fake result")
//    )
//
//    assertResult(5){
//      get[Int]("intField", 123)
//    }
//
//    assertResult(89.6f){
//      get[Float]("floatField", 333.6f)
//    }
//
//    assertResult(Some(34)){
//      get[Option[Int]]("optionInt", Some(123))
//    }
//
//    assertResult(Some(null)){
//      get[Option[String]]("option null", Some("123"))
//    }
//
//    assertResult(None){
//      get[Option[Int]]("option none", Some(123))
//    }
//
//    assertResult("String"){
//      get[String]("stringField", "defaultString")
//    }
//
//    assertResult(FakeCustomType("fake result")) {
//      get[FakeCustomType]("fake custom", FakeCustomType("123"))
//    }
//  }
//
//  it must "returns default value if there's no in SolrDocumentMap" in {
//
//    implicit case class FakeCustomType(result: String)
//
//    class ResultTypeConverter extends CanConvertMapToClass[FakeCustomType] {
//      override def convert(document: SolrDocumentMap): FakeCustomType = {
//        implicit val d: SolrDocumentMap = document
//        FakeCustomType(get[String]("fake custom object"))
//      }
//    }
//
//    implicit val doc: SolrDocumentMap = Map()
//
//    assertResult(5){
//      get[Int]("intField", 5)
//    }
//
//    assertResult(89.6f){
//      get[Float]("floatField", 89.6f)
//    }
//
//    assertResult(Some(34)){
//      get[Option[Int]]("optionInt", Some(34))
//    }
//
//    assertResult(Some(null)){
//      get[Option[String]]("option null", Some(null))
//    }
//
//    assertResult(None){
//      get[Option[Int]]("option none", None)
//    }
//
//    assertResult("String"){
//      get[String]("stringField", "String")
//    }
//
//    assertResult(FakeCustomType("123")) {
//      get[FakeCustomType]("fake custom", FakeCustomType("123"))
//    }
//  }
//
//  it must "throws SolrException if field has bad type" in {
//    implicit val doc: SolrDocumentMap = Map(
//      "intField" -> 5,
//      "floatField" -> 89.6f,
//      "option none" -> None
//    )
//
//    intercept[SolrException] {
//      get[String]("intField", "str")
//    }
//
//    intercept[SolrException] {
//      get[Int]("floatField", 666)
//    }
//
//    intercept[SolrException] {
//      get[Float]("option none", 6.7f)
//    }
//  }
//
//  "opt[T](field: String)" must "gets optional value from SolrDocumentMap" in {
//
//    implicit case class FakeCustomType(result: String)
//
//    class ResultTypeConverter extends CanConvertMapToClass[FakeCustomType] {
//      override def convert(document: SolrDocumentMap): FakeCustomType = {
//        implicit val d: SolrDocumentMap = document
//        FakeCustomType(get[String]("fake custom object"))
//      }
//    }
//
//    implicit val doc: SolrDocumentMap = Map(
//      "intField" -> 5,
//      "floatField" -> 89.6f,
//      "optionInt" -> Some(555),
//      "option none" -> None,
//      "fake custom" -> FakeCustomType("fake result")
//    )
//
//    assertResult(Some(5)){
//      opt[Int]("intField")
//    }
//
//    assertResult(Some(89.6f)){
//      opt[Float]("floatField")
//    }
//
//    assertResult(Some(Some(555))){
//      opt[Option[Int]]("optionInt")
//    }
//
//    assertResult(Some(None)){
//      opt[Option[Int]]("option none")
//    }
//
//    assertResult(Some(FakeCustomType("123"))) {
//      opt[FakeCustomType]("fake custom")
//    }
//  }
//
//  it must "returns None if there's no field in SolrDocumentMap" in {
//
//    implicit case class FakeCustomType(result: String)
//
//    class ResultTypeConverter extends CanConvertMapToClass[FakeCustomType] {
//      override def convert(document: SolrDocumentMap): FakeCustomType = {
//        implicit val d: SolrDocumentMap = document
//        FakeCustomType(get[String]("fake custom object"))
//      }
//    }
//
//    implicit val doc: SolrDocumentMap = Map()
//
//    assertResult(None){
//      opt[Int]("intField")
//    }
//
//    assertResult(None){
//      opt[Float]("floatField")
//    }
//
//    assertResult(None){
//      opt[Option[Int]]("optionInt")
//    }
//
//    assertResult(None){
//      opt[Option[Int]]("option none")
//    }
//
//    assertResult(None) {
//      opt[FakeCustomType]("fake custom")
//    }
//  }
//
//  it must "throws SolrException if field has bad type" in {
//    implicit val doc: SolrDocumentMap = Map(
//      "intField" -> 5,
//      "floatField" -> 89.6f,
//      "option none" -> None
//    )
//
//    intercept[SolrException] {
//      opt[String]("intField")
//    }
//
//    intercept[SolrException] {
//      opt[Int]("floatField")
//    }
//
//    intercept[SolrException] {
//      opt[Float]("option none")
//    }
//  }
//}
