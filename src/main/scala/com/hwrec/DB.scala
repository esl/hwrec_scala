package com.hwrec
import java.sql.{ Connection, DriverManager, ResultSet }

trait DB {

  def write_me(inputData: Seq[Byte]) {

    //    classOf[org.postgresql.Driver]
    val conn = DriverManager.getConnection("jdbc:postgresql://postgres-mim-elixir-vs-scala-1-eu-central-1a.cdv2zhdgkwuf.eu-central-1.rds.amazonaws.com/knndb", "postgres", "i2t6JVgCkTtq3jzBVBtzER2hgqKKxR")
    try {
      //      val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

      val mylist = inputData.toList
      val statement = conn.prepareStatement("insert into numbers(data) values ('" + mylist + "');")
      statement.executeUpdate()
      statement.close()
      //      val rs = stm.executeQuery("SELECT * from numbers")

    } finally {
      conn.close()
    }
  }

  def return_last(last: Integer): List[String] = {
    val conn = DriverManager.getConnection("jdbc:postgresql://localhost/admin", "admin", "admin")
    try {
      val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      val rs = stm.executeQuery("select * from numbers limit " + last)
      Iterator.from(0).takeWhile(_ => rs.next()).map(_ =>
        {
          rs.getString(1)
        }).toList

    } finally {
      conn.close()
    }
  }
}