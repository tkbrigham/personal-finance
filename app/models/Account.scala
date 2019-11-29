package models

import scalikejdbc._

case class Account(id: Int, name: String, accountType: String) extends Model[Account]

//object Account extends SQLSyntaxSupport[Account] with AutoSyntax[Account] {
//  override val tableName = "accounts"
//  implicit val session: DBSession = autoSession
//  val a: Syntax = Account.syntax("a")
//  def apply(u: SyntaxProvider[Account])(rs: WrappedResultSet): Account = apply(u.resultName)(rs)
//  def apply(u: ResultName[Account])(rs: WrappedResultSet): Account =
//    Account(rs.int(u.id), rs.string(u.name), rs.string(u.accountType))
//
//  def findAll: List[Account] = withSQL {
//    select.from(Account as a).orderBy(a.id)
//  }.map(Account(a)).list().apply()
//}

object Account extends DBClass[Account]
