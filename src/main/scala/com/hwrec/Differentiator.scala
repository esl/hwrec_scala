package com.hwrec

import com.hwrec.Data.DataEntry

import scala.concurrent.{ ExecutionContext, Future }

trait Differentiator extends Knn {

  val referenceDataMap: Map[String, DataEntry]

  def differentiate(id: String, inputData: Seq[Byte]): Future[Double] = {
    referenceDataMap
      .get(id)
      .map { case DataEntry(_, _, _, refData) => differentiate(refData, inputData) }
      .getOrElse(Future.failed(new IllegalArgumentException(s"id not found: $id")))
  }

}
