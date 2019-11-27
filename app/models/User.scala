package models

import scalikejdbc._

case class User(id: Int, email: String, password: Option[String])

object User extends DBModel[User] {
  def findAll: List[User] = withSQL {
    select.from(User as tbl).orderBy(tbl.id)
  }.map(User(tbl)).list().apply()

  def findAll3: List[User] = list(select.from(this as tbl).orderBy(tbl.id))
}
