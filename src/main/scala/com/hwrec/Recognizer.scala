package com.hwrec

import akka.actor.ActorSystem
import com.hwrec.Data.DataEntry

import scala.concurrent.{ ExecutionContext, Future }

trait Recognizer extends Knn {
  val referenceData: Seq[DataEntry]
  implicit val system: ActorSystem

  def recognize(inputData: Seq[Byte], k: Int): Future[String] = {
    recognizeByFanout(inputData, k)
  }

  private def recognizeByFanout(inputData: Seq[Byte], k: Int): Future[String] = {
    recognize(referenceData, inputData, k)
  }

}
