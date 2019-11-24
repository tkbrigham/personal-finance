package models

import scalikejdbc._

case class User(id: Int, email: String, password: Option[String]) extends Model[User]

trait AutoSyntax[A] {
  type Syntax = scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[A], A]
}

object User extends SQLSyntaxSupport[User] with AutoSyntax[User] {
  override val tableName = "users"
  implicit val session: DBSession = autoSession
  val u: Syntax = User.syntax("u")

  def apply(u: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(u.resultName)(rs)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User =
    User(rs.int(u.id), rs.string(u.email), rs.stringOpt(u.password))

  def findAll(): List[User] = withSQL {
    select.from(User as u).orderBy(u.id)
  }.map(User(u)).list().apply()
}
