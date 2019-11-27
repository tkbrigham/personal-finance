package models

import scalikejdbc._

trait AutoSyntax[A] {
  type Syntax = QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[A], A]
}

trait DBModel[A >: User] extends SQLSyntaxSupport[A] with AutoSyntax[A] {
  implicit val session: DBSession = autoSession
  override val tableName: String = this.getClass.getSimpleName.toLowerCase().replace('$', 's')
  val tbl: Syntax = this.syntax(tableName)

  def apply(model: SyntaxProvider[A])(rs: WrappedResultSet): A = apply(model.resultName)(rs)
  def apply(model: ResultName[A])(rs: WrappedResultSet): A = {
    User(rs.int(model.id), rs.string(model.email), rs.stringOpt(model.password))
  }

  def list(q: SQLBuilder[A]): List[A] = withSQL(q)
    .map(apply(tbl))
    .list
    .apply

  def find(q: SQLBuilder[A]): Option[A] = withSQL(q)
    .map(apply(tbl))
    .single
    .apply

  lazy val sel: scalikejdbc.SelectSQLBuilder[A] = select.from(this as tbl)
  lazy val ins: scalikejdbc.InsertSQLBuilder = insert.into(this)
  lazy val upd: scalikejdbc.UpdateSQLBuilder = update(this)
  lazy val del: scalikejdbc.DeleteSQLBuilder = delete.from(this)
  def findById(id: Int): Option[A] = find(sel.where.eq(tbl.id, id))
}
