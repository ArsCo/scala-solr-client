package ars.solr.client.request

/**
  *
  *
  * @author ars (Arsen Ibragimov)
  * @since 0.0.0
  */
trait Functions {

  type Val = String
  type FieldName = String

  type Vec2 = (Float, Float)
  type Vec3 = (Float, Float, Float)
  type Vec4 = (Float, Float, Float, Float)
  type Vec5 = (Float, Float, Float, Float, Float)
  type Vec6 = (Float, Float, Float, Float, Float, Float)
  type Vec7 = (Float, Float, Float, Float, Float, Float, Float)
  type Vec8 = (Float, Float, Float, Float, Float, Float, Float, Float)


  def abs(value: Val)

  def childfield(value: Val) // ???

  def concat(value: Val*)

  def `def`(value: Val)

  def div(value1: Val, value2: Val)

  def dist(power: Int, v1: Vec2, v2: Vec2)
  def dist(power: Int, v1: Vec3, v2: Vec3)
  def dist(power: Int, v1: Vec4, v2: Vec4)
  def dist(power: Int, v1: Vec5, v2: Vec5)
  def dist(power: Int, v1: Vec6, v2: Vec6)
  def dist(power: Int, v1: Vec7, v2: Vec7)


  def docfreq(field: FieldName, value: String)
  def field(field: FieldName)

  def hsin() // ???

  def idf(field: FieldName, value: String)

  def `if`(condition: Boolean, trueValue: Val, falseValue: Val)

  def linear()

}

object Func {

  case class element()
  case class function() extends element()

  case class field() extends function()
  case class linear() extends function()
  case class log() extends function()
  case class max() extends function()
  case class map() extends function()
  case class maxdoc() extends function()

  case class ms() extends function()
  case class norm() extends function()

  case class numdocs() extends function()

  case class ord() extends function()
  case class payload() extends function()
  case class pow() extends function()
  case class product() extends function()
  case class query() extends function()
  case class recip() extends function()
  case class rord() extends function()
  case class scale() extends function()

  case class sqedist() extends function()
  case class sqrt() extends function()
  case class strdist() extends function()
  case class sub() extends function()
  case class sum() extends function()
  case class sumtotaltermfreq() extends function()
  case class termfreq() extends function()
  case class tf() extends function()
  case class top() extends function()
  case class totaltermfreq() extends function()


  case class and(value1: Val, value2: Val) extends function()
  case class or(value1: Val, value2: Val) extends function()
  case class xor(value1: Val, value2: Val) extends function()
  case class not(value1: Val, value2: Val) extends function()
  case class exists(value1: Val, value2: Val) extends function()

  case class gt() extends function()
  case class gte() extends function()
  case class lt() extends function()
  case class lte() extends function()
  case class eq() extends function()

  case class

}
