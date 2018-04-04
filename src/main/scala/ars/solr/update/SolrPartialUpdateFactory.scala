package ars.solr.update

import ars.precondition.RequireUtils.{requireAll, requireNotBlank, requireNotNull, toRequireElementFunction}
import org.apache.solr.common.SolrInputDocument

/** Apache Solr partial update factory.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.2
  */
object SolrPartialUpdateFactory {

  sealed abstract class DocPart[+T]
  case class PartialPart[+T](field: String, map: Map[String, T]) extends DocPart[T]
  case class FieldPart[+T](field: String, value: T, boost: Float = 1.0f) extends DocPart[T]


  def document(documents: SolrInputDocument*)(value: String): SolrInputDocument = {
    val doc = new SolrInputDocument()
    documents foreach doc.addChildDocument
    doc
  }

  def document(parts: FieldPart[Any]*): SolrInputDocument = {
    val doc = new SolrInputDocument()
    parts.foreach { part =>
      doc.addField(part.field, part.value, part.boost)
    }
    doc
  }

  def field[T](field: String, value: T, boost: Float = 1.0f): FieldPart[T] = {
    FieldPart(field, value, boost)
  }

  def partial(parts: PartialPart[Any]*): SolrInputDocument = {
    val doc = new SolrInputDocument()
    parts.foreach { p => doc.addField(p.field, p.map) }

    doc
  }

//  def req() TODO No change id fields

  def set[T](field: String)(value: T): PartialPart[T] = {
    requireNotBlank(field, "field")
    requireNotNullIfRef[T](value, "value")

    PartialPart(field, Map("set" -> value))
  }

  def add[T](field: String)(value: T): PartialPart[T] = {
    requireNotBlank(field, "field")
    requireNotNullIfRef[T](value, "value")

    PartialPart(field, Map("add" -> value))
  }

  def remove(field: String): PartialPart[_] = {
    requireNotBlank(field, "field")

    PartialPart(field, Map("set" -> null))
  }

  def remove[T](field: String)(values: T*): PartialPart[T] = {
    requireNotBlank(field, "field")
    requireAll(values, field)(toRequireElementFunction(requireNotNullIfRef))

    PartialPart(field, values.map("remove" -> _).toMap)
  }

  def removeRegExp[T](field: String)(expr: String): PartialPart[String] = {
    requireNotBlank(field, "field")
    requireNotBlank(expr, "expr")

    PartialPart(field, Map("removeregex" -> expr))
  }

  def inc(field: String)(amount: Int): PartialPart[_] = {
    requireNotBlank(field, "field")

    PartialPart(field, Map(field -> amount))
  }


  def requireNotNullIfRef[T](value: T, field: String) {
    requireIfRef(field, value, requireNotNull)
  }

  def requireIfRef[T](field: String, value: T, f: (AnyRef, String) => Unit) {
    value match { case v: AnyRef => f(v, field) }
  }


//  def ex(): Unit = {
//
//    document(
//      document(
//        field("rr", "dd"),
//        field("rr", "sss")
//      ),
//      document(
//        field("rr", "dd"),
//        field("rr", "sss")
//      ),
//      partial{
//        set("field")(5)
//        set("field")(5)
//        set("field")(5)
//        removeRegExp("field3")("[0-1]?")
//      }
//    )
//  }
}
