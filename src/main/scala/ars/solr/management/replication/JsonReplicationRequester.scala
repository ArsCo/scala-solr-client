package ars.solr.management.replication

import ars.solr.exception.SolrException
import ars.solr.management.replication.response.{Header, Response}
import com.typesafe.scalalogging.Logger
import org.json4s.JValue
import org.json4s.native.JsonMethods.parse

import scalaj.http.{HttpRequest, HttpResponse}

/** JSON replication requester.
  *
  * @author ars (Ibragimov Arsen)
  * @since 0.0.3
  */
object JsonReplicationRequester {

  /**
    * Executes `request`, checks the response code. If it is 200 then method parses the response string with
    * `toResponse` funstion. Else it logs the error and throw [[SolrException]].
    *
    * @param request the request object
    * @param toResponse the function to parse response string to body object
    * @tparam T the type of body object
    * @return the parsed body object
    * @throws SolrException if response code is not 200 or there is an error
    *                       during parsing of response string to body object
    */
  def execute[T <: AnyRef](request: HttpRequest)
                (toResponse: JValue => Response[T]): Response[T] = {

    debug("Request =>\n" + requestToString(request))

    val response = request.asString
    val code = response.code
    val body = response.body
    if (code == 200) {
      val json = parse(body)
      debug("Response =>\n" + json)

      toResponse(json)
    } else {
      throw new SolrException(responseToString(response))
    }
  }

  /**
    * Executes `request` and parses received response.
    *
    * @param request the request
    * @param bodyParser the JSON body parser function
    * @tparam T the type of body object
    * @return the parsed body object
    * @throws SolrException if response code is not 200 or there is an error
    *                       during parsing of response string to body object
    */
  def executeAsJson[T <: AnyRef](request: HttpRequest)
                      (bodyParser: (JValue) => T): Response[T] = {
    execute(request) { json =>
      Response(Header.fromJson(json), bodyParser(json))
    }
  }

  private def requestToString(request: HttpRequest): String = {
    s"\tURL: ${request.url}\n" +
    s"\tHeaders: ${request.headers}\n" +
    s"\tParams: ${request.params}"
  }

  private def responseToString(response: HttpResponse[String]): String = {
    s"\tCode: ${response.code}\n" +
    s"\tHeaders: ${response.headers}\n" +
    s"\tBody: ${response.body}"
  }

  private def logger = Logger[JsonReplicationRequester.type]

  private def debug(string: String): Unit = {
    logger.error(string)
  }
}
