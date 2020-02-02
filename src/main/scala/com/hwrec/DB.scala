package com.hwrec
import java.sql.{ Connection, DriverManager, ResultSet }

trait DB {

  val hostname = "postgres-mim-elixir-vs-scala-1-eu-central-1a.cdv2zhdgkwuf.eu-central-1.rds.amazonaws.com"
  val db = "knndb"
  val username = "postgres"
  val password = "i2t6JVgCkTtq3jzBVBtzER2hgqKKxR"
  def write_me(inputData: Seq[Byte]) {
    val conn = DriverManager.getConnection("jdbadc:postgresql://" + hostname + "/" + db, username, password)

    //    classOf[org.postgresql.Driver]

    try {
      //      val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

      val value = convertBytesToHex(inputData)
      val statement = conn.prepareStatement("insert into numbers(data) values ('" + value + "');")
      statement.executeUpdate()
      statement.close()
      //      val rs = stm.executeQuery("SELECT * from numbers")

    } finally {
      conn.close()
    }
  }

  def convertBytesToHex(bytes: Seq[Byte]): String = {
    val sb = new StringBuilder
    for (b <- bytes) {
      sb.append(String.format("%02x", Byte.box(b)))
    }
    sb.toString
  }

  def return_last(last: Integer): List[String] = {
    val conn = DriverManager.getConnection("jdbadc:postgresql://" + hostname + "/" + db, username, password)
    try {
      val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      val rs = stm.executeQuery("select * from numbers limit " + last)
      val result = Iterator.from(0).takeWhile(_ => rs.next()).map(_ =>
        {
          val f = rs.getBytes(1)
          (f.map(_.toChar)).mkString
        }).toList
      stm.close()

      result
    } finally {
      conn.close()
    }
  }
}