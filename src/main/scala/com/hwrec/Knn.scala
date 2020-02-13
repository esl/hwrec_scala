package com.hwrec

import java.net.URI
import java.util.concurrent.{ Callable, Executors }
import java.nio.ByteBuffer
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.model.{ HttpMethod, HttpMethods, HttpRequest, Uri }
//import com.hwrec.Data.DataEntry
import com.typesafe.config.{ Config, ConfigFactory }

import scala.concurrent.{ Await, ExecutionContext, Future }

trait Knn extends RecognizedWithFutures {

  def distance(refData: Seq[Byte], inputData: Seq[Byte]): Double = {
    //    println(s"distancing: ${refData.size}, ${inputData.size}")
    refData
      .zip(inputData.toStream #::: Stream.continually(0: Byte))
      .map { case (a, b) => Math.pow(b - a, 2) }
      .sum
  }

  def makeSelections(distances: Seq[(String, Int)], k: Int): String = {
    distances
      .sortBy { case (_, distance) => distance }
      .take(k)
      .foldLeft(Map[String, Int]()) {
        case (map, (digit, _)) =>
          map
            .get(digit)
            .map(count => map + (digit -> (count + 1)))
            .getOrElse(map + (digit -> 1))
      }
      .toList
      .sortWith { case ((_, c1), (_, c2)) => !(c1 < c2) }
      .head._1
  }

  def recognize(refDigits: Array[Array[Byte]], inputData: Seq[Byte], k: Int): Future[String] = {
    doRecognize(refDigits, inputData, k)
  }

  //  def differentiate(refData: Seq[Byte], inputData: Seq[Byte]): Future[Double] = {
  //    import scala.concurrent.ExecutionContext.Implicits.global
  //    Future(distance(refData, inputData))
  //  }
}

trait KnnTrait {
  val calc = new JavaNativeCalculator
  def distance(refData: Seq[Byte], inputData: Seq[Byte]): Double
  def makeSelections(distances: Seq[(String, Int)], k: Int): String
}

trait RecognizedWithFutures extends KnnTrait {
  import scala.concurrent.ExecutionContext.Implicits.global

  def doRecognize(refDigits: Array[Array[Byte]], inputData: Seq[Byte], k: Int): Future[String] = {

    //    Future.sequence(refDigits
    //      .map {
    //        case DataEntry(_, digit, _, data) =>
    // wypisz to

    println("printing digits")
    for (el <- refDigits) {
      print(el(0) + ", ")
    }

    val res = calc.distance(refDigits, inputData.toArray)
    println("printing return digits")
    for (el <- res) {
      print(el(0) + ", ")
    }

    Future.sequence(
      res
        .toSeq
        .map { arr => Future((arr(0).toString, ByteBuffer.wrap((Array(0, 0, arr(1), arr(2)))).getInt)) })
      .map(makeSelections(_, k))
  }
}

//trait RecognizedWithThreadPool extends KnnTrait {
//
//  import scala.concurrent.ExecutionContext.Implicits.global
//
//  //  private val threads = Executors.newCachedThreadPool()
//  private val threads = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors())
//  //  private val threads = Executors.newFixedThreadPool(30)
//
//  def doRecognize(refDigits: Seq[DataEntry], inputData: Seq[Byte], k: Int): Future[String] = {
//    withFuture(refDigits, inputData, k)
//  }
//}

//  private def withoutFuture(refDigits: Seq[DataEntry], inputData: Seq[Byte], k: Int): Future[String] = {
//    try {
//      val distances = refDigits
//        .map {
//          case DataEntry(_, digit, _, data) =>
//            threads.submit({ () => (digit, distance(data, inputData)) }: Callable[(String, Double)])
//        }
//        .map(_.get())
//      val result = makeSelections(distances, k)
//      Future.successful(result)
//    } catch {
//      case throwable: Throwable => Future.failed(throwable)
//    }
//  }

//  private def withFuture(refDigits: Seq[DataEntry], inputData: Seq[Byte], k: Int): Future[String] = {
//    Future {
//      val distances = refDigits
//        .map {
//          case DataEntry(_, digit, _, data) =>
//            threads.submit({ () => (digit, distance(data, inputData)) }: Callable[(String, Double)])
//        }
//        .map(_.get())
//      makeSelections(distances, k)
//    }
//  }
//}

//trait RecognizedWithService extends KnnTrait {
//  import scala.concurrent.ExecutionContext.Implicits.global
//  implicit val system: ActorSystem
//
//  val http = Http()
//  val diffUri = Uri(system.settings.config.getString("diffserver.uri"))
//
//  def doRecognize(refDigits: Seq[DataEntry], inputData: Seq[Byte], k: Int)(implicit system: ActorSystem): Future[String] = {
//    Future.sequence(refDigits.zip(Stream.from(0))
//      .map {
//        case (DataEntry(id, digit, _, data), index) =>
//          if (index % 2 == 0) {
//            Future((digit, distance(data, inputData)))
//          } else {
//            val targetUri = diffUri.copy(path = diffUri.path / id)
//            val request = HttpRequest(method = HttpMethods.POST, uri = targetUri)
//            Future.successful(("1", 5.0))
//          }
//      })
//      .map(makeSelections(_, k))
//  }
//}

