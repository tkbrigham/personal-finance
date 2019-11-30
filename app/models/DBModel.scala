package models

import scala.reflect._
import scala.reflect.runtime.universe._

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


abstract class DBClass[A : TypeTag : ClassTag] extends SQLSyntaxSupport[A] with AutoSyntax[A] {
  lazy val constructor: MethodSymbol = typeOf[A].decl(termNames.CONSTRUCTOR).asMethod

  lazy val constructorMirror: MethodMirror = {
    val rm = runtimeMirror(classTag[A].runtimeClass.getClassLoader)
    val classTest = typeOf[A].typeSymbol.asClass
    val classMirror = rm.reflectClass(classTest)
    classMirror.reflectConstructor(constructor)
  }

  def fromWRS(rs: WrappedResultSet): A = fromWrappedResultSet(rs)
  def fromWrappedResultSet(rs: WrappedResultSet): A = {
    val resultMap = rs.toMap()
    val constructorArgs: List[Any] = constructor.paramLists.flatten.map((param: Symbol) => {
      val columnName = snakify(param.name.toString)
      val paramName = tbl.resultName.column(columnName).value
      if(param.typeSignature <:< typeOf[Option[Any]])
        resultMap.get(paramName)
      else
        resultMap.getOrElse(paramName, throw new IllegalArgumentException("Map is missing required parameter named " + paramName))
    })

    constructorMirror(constructorArgs:_*).asInstanceOf[A]
  }

  /// Remove plz
  implicit val session: DBSession = autoSession
  override val tableName: String = this.getClass.getSimpleName.toLowerCase().replace('$', 's')
  val tbl: Syntax = this.syntax(tableName)
  lazy val sel: scalikejdbc.SelectSQLBuilder[A] = select.from(this as tbl)
  def printResult = withSQL(sel)
    .map(fromWRS)
    .list
    .apply

  // Lifted from https://github.com/lift/framework/commit/
  //   f56a814c5d452b0be66f7c37e3f253ed2e283a0c#diff-53a9b63fb8b8d594b37f64186ab2b1e4R104-R116
  def snakify(name: String): String = {
    def loop(x : List[Char]) : List[Char] = x match {
      case c :: rest if Character.isUpperCase(c) => '_' :: Character.toLowerCase(c) :: loop(rest)
      case c :: rest => c :: loop(rest)
      case Nil => Nil
    }
    if (name.isEmpty)
      ""
    else
      (Character.toLowerCase(name.charAt(0)) :: loop(name.substring(1).toList)).mkString
  }
}

