package ars.solr.util

import scalaj.http.HttpRequest

/**
  * Created by ars on 08/04/2017.
  */
object HttpUtils {

  implicit def convertToReachHttpRequest(request: HttpRequest): RichHttpRequest = {
    new RichHttpRequest(request)
  }

  class RichHttpRequest(request: HttpRequest) {
    def addOptParam[T](name: String, value: Option[T]): HttpRequest = {
      value match {
        case Some(v) => request.param(name, v.toString)
        case None => request
      }
    }
  }
}
