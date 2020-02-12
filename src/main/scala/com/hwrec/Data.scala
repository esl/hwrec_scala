package com.hwrec

import java.io.File
import java.net.URI
import java.nio.file.{ FileSystems, Files, Path, Paths }

import scala.io.Source
import scala.collection.JavaConverters._

trait Data {
  import Data._

  val referenceData: Array[Array[Byte]] = load_data("references")
  //  val referenceDataMap: Map[String, DataEntry] = referenceData.map { case d @ DataEntry(id, _, _, _) => id -> d }.toMap
  lazy val testData: Array[Array[Byte]] = load_data("tests")
}

object Data {
  //  case class DataEntry(id: String, digit: String, file: Path, data: Seq[Byte])

  def load_data(path: String): Array[Array[Byte]] = {
    list_files(path)
      .map {
        case (id, digit, file) =>

          Array[Byte](digit.toByte) ++ load_file(file)
        //        DataEntry(id, digit, file, load_file(file))

      }.toArray
  }

  def list_files(path: String): Seq[(String, String, Path)] = {
    val resourceURI = this.getClass.getResource("/digits").toURI
    var files: Iterator[Path] = null
    if (resourceURI.toString.contains("!")) {
      val baseUri = resourceURI.toString.split("\\!/")(0)
      val uri = URI.create(baseUri)
      val filesystem = FileSystems.newFileSystem(uri, Map.empty[String, Nothing].asJava)
      val filepath = filesystem.getPath(s"/digits/$path/")
      files = Files.newDirectoryStream(filepath).iterator().asScala
    } else {
      val dir = FileSystems.getDefault.getPath(resourceURI.getPath, path)
      files = Files.newDirectoryStream(dir).iterator().asScala
    }
    files.flatMap { file =>
      val filename = file.getFileName.toString
      if (filename.endsWith(".txt")) {
        val Array(id, _) = filename.split('.')
        val Array(digit, _) = filename.split('_')
        Some((id, digit, file))
      } else {
        None
      }
    }.toList
  }

  def load_file(file: Path): Array[Byte] = {
    val src = Files.readAllBytes(file)
    src
      .filter { c => c != '\n' && c != '\r' }
      .map { c => (c.toInt - 48).toByte }
      .toArray
  }
}

