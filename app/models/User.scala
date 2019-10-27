package models

import javax.inject._
import play.api.Logging
import play.api.db._
import play.api.mvc._

import scala.reflect._
import scala.reflect.runtime.universe._

object User {
  def fromParams(m: scala.collection.mutable.Map[String,AnyRef]): User =
    Model.fromParams[User](m)
}

case class User(id: Int, email: String, password: Option[String]) extends Model[User]

class UserTable @Inject()(db: Database) {
  def all(): String = {
    val testUser = new User(2, "test", None)

    db.withTransaction { conn =>
      val stmt = conn.createStatement
      val rs = stmt.executeQuery(s"SELECT * from users")

      val meta = rs.getMetaData

      val paramList = testUser.rsToParamList(rs)
      val userList = paramList.map(params => User.fromParams(params))
      val paramListIt = userList.iterator

      println(paramListIt.next())
      println(paramListIt.next())
      println(paramListIt.next())
      println(paramListIt.next())


//      rs.next()
//
//      for (a <- 1 to meta.getColumnCount) {
//        println("=========================")
//        println("columnClassName", meta.getColumnClassName(a))
//        println("columnDisplaySize", meta.getColumnDisplaySize(a))
//        println("columnLabel", meta.getColumnLabel(a))
//        println("columnName", meta.getColumnName(a))
//        println("columnType", meta.getColumnType(a))
//        println("columnTypeName", meta.getColumnTypeName(a))
//        println("tableName", meta.getTableName(a))
//        println("oooooooooooh", rs.getObject(a))
//      }
    }

    testUser.tableName
  }
}
