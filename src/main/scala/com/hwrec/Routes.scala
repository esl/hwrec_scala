package com.hwrec

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import spray.json.{ DefaultJsonProtocol, JsArray, JsNumber, JsValue, RootJsonFormat }

import scala.concurrent.{ Await, ExecutionContext, ExecutionContextExecutor, Future }

trait Routes {
  import Routes._
  // we leave these abstract, since they will be provided by the App
  implicit def system: ActorSystem
  implicit val executionContext: ExecutionContext

  lazy val log = Logging(system, classOf[Routes])

  val k: Int
  def recognize(inputData: Seq[Byte], k: Int): Future[String]
  def differentiate(id: String, inputData: Seq[Byte]): Future[Double]

  import scala.concurrent.duration._
  lazy val routes: Route =
    concat(
      pathPrefix("api") {
        concat(
          path("letters") {
            post {
              entity(as[JsValue]) { json =>
                val inputJson = json.asInstanceOf[JsArray]
                val inputData = inputJson.elements.map(_.asInstanceOf[JsNumber].value.byteValue)
                val resultFuture = recognize(inputData, k)
                onSuccess(resultFuture) { digit =>
                  log.info("Recognized: {}", digit)
                  val response = HwrecResponse(true, digit)
                  complete((StatusCodes.OK, response))
                }

                //                val digit = Await.result(resultFuture, 10 seconds)
                //                log.info("Recognized: {}", digit)
                //                val response = HwrecResponse(true, digit)
                //                complete((StatusCodes.OK, response))
              }
            }
          },
          path("diff" / Segment) { id =>
            post {
              entity(as[JsValue]) { json =>
                val inputJson = json.asInstanceOf[JsArray]
                val inputData = inputJson.elements.map(_.asInstanceOf[JsNumber].value.byteValue)
                val resultFuture = differentiate(id, inputData)
                onSuccess(resultFuture) { distance =>
                  log.info("Differentiated: {}, {}", id, distance)
                  val response = DiffResponse(true, distance)
                  complete((StatusCodes.OK, response))
                }
              }
            }
          })
      },
      getFromResourceDirectory("webapp"))
}

object Routes extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)

  import DefaultJsonProtocol._
  System.loadLibrary("JavaNativeCalculator")
  case class HwrecResponse(ok: Boolean, letter: String)
  case class DiffResponse(ok: Boolean, distance: Double)
  implicit val hwrecResponseJsonFormat: RootJsonFormat[HwrecResponse] = jsonFormat2(HwrecResponse)
  implicit val diffResponseJsonFormat: RootJsonFormat[DiffResponse] = jsonFormat2(DiffResponse)
}
