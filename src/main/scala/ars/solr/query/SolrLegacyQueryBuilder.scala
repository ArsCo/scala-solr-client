package ars.solr.query

import ars.precondition.RequireUtils._

/** Solr query builder.
  *
  * @author ars (Ibragimov Arsen)
  */
@deprecated
class SolrLegacyQueryBuilder

@deprecated
object SolrLegacyQueryBuilder {

  /** Solr query base class. */
  trait Query

  class OperatorQuery(val name: String)(val args: Query*) extends Query {
    requireNotBlank(name, "name")
    requireNotBlank(args, "args")
    requireAllNotNull(args, "args")

    override def toString = {
      val query = args.map(arg => arg.toString).mkString(s" $name ")
      if (args.size >= 2) s"($query)" else query
    }
  }

  class FieldQuery(val name: String, val string: String) extends Query {
    requireNotBlank(name, "name")
    requireNotBlank(string, "pattern")

    override def toString = s"$name:${escape(string)}"

    private def escape(string: String) = "\"" + string + "\"" // Bug in scala compiler
  }

  class PatternFieldQuery(val name: String, val pattern: String = "*") extends Query {
    requireNotBlank(name, "name")
    requireNotBlank(pattern, "pattern")
    //require(!pattern.contains(" "), "The 'pattern' must not contain a space.")


    override def toString = s"$name:$pattern" // TODO pattern with space ?
  }

  class InFieldQuery(val name: String, val values: List[String]) extends Query {
    requireNotBlank(name, "name")
    requireNotNull(values, "values")
    values.foreach(v => requireNotBlank(v, "value"))

    val in = values.mkString(",")

    override def toString = s"$name:($in)"
  }

  class UnaryOperatorQuery(override val name: String, arg: Query) extends OperatorQuery(name)(arg) {
    override def toString = s"$name($args)"
  }

  class BinaryOperatorQuery(override val name: String, arg1: Query, arg2: Query)
    extends OperatorQuery(name)(arg1, arg2)

  class BinaryPlusOperatorQuery(override val name: String, arg1: Query, arg2: Query, otherArgs: Query*)
    extends OperatorQuery(name)(arg1 +: arg2 +: otherArgs :_ *) {
  }

  class And(arg1: Query, arg2: Query, otherArgs: Query*)
    extends BinaryPlusOperatorQuery("AND", arg1, arg2, otherArgs :_ *)

  class Or(arg1: Query, arg2: Query, otherArgs: Query*)
    extends BinaryPlusOperatorQuery("OR", arg1, arg2, otherArgs :_ *)

  class Not(arg: Query) extends UnaryOperatorQuery("!", arg)

  class TrueQuery extends Query {
    override def toString: String = "*:*"
  }

  def query(query: Query): String = query.toString

  def operator(name: String, args: Query*): Query = new OperatorQuery(name)(args :_ *)
  def and(arg1: Query, arg2: Query, otherArgs: Query*): Query = new And(arg1, arg2, otherArgs :_ *)
  def or(arg1: Query, arg2: Query, otherArgs: Query*): Query = new Or(arg1, arg2, otherArgs :_ *)
  def not(arg: Query): Query = new Not(arg)

  def field(name: String, string: String): Query = new FieldQuery(name, string)
//  def rangeField(): Query =
  def patternField(name: String, pattern: String): Query = new PatternFieldQuery(name, pattern)
  def in(name: String, values: List[String]): Query = new InFieldQuery(name, values)

  val True = new TrueQuery

  def cre(f: (Seq[Query]) => Query, args: Query*): Query = {
    if (args.isEmpty) True
    else if (args.length == 1) args.head
    else f(args)
  }

  def nnn(arg: Query*): Query = cre(nnn, arg :_ *)

}
