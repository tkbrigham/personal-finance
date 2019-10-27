package models

import scala.reflect._
import scala.reflect.runtime.universe._

import java.sql.ResultSet

object Model {
  def fromParams[T: TypeTag: ClassTag](m: scala.collection.mutable.Map[String,AnyRef]): T = {
    val rm = runtimeMirror(classTag[T].runtimeClass.getClassLoader)
    val classTest = typeOf[T].typeSymbol.asClass
    val classMirror = rm.reflectClass(classTest)
    val constructor = typeOf[T].decl(termNames.CONSTRUCTOR).asMethod
    val constructorMirror = classMirror.reflectConstructor(constructor)

    val constructorArgs = constructor.paramLists.flatten.map( (param: Symbol) => {
      val paramName = param.name.toString
      if(param.typeSignature <:< typeOf[Option[Any]])
        m.get(paramName)
      else
        m.getOrElse(paramName, throw new IllegalArgumentException("Map is missing required parameter named " + paramName))
    })

    constructorMirror(constructorArgs:_*).asInstanceOf[T]
  }

  def rsToParamList(rs: ResultSet): Stream[Map[String, AnyRef]] = {
    val md = rs.getMetaData
    val numCols = md.getColumnCount

    def loop(rs: ResultSet): Stream[Map[String,AnyRef]] = {
      rs.next()
//      val map = scala.collection.mutable.Map.empty[String,AnyRef]
//      for (i <- 1 to numCols) {
//        map.put(md.getColumnName(i), rs.getObject(i))
//      }

      val map = (1 to numCols)
        .foldLeft(Map[String,AnyRef]())((m, idx) => m + (md.getColumnName(idx) -> rs.getObject(idx)))

      map #:: loop(rs)
    }

    loop(rs)
  }
}

trait Model[B <: Model[B]] {
  val tableName: String = s"${snakify(getClass.getSimpleName)}s"

  def rsToParamList(rs: ResultSet): Stream[scala.collection.mutable.Map[String, AnyRef]] = {
    val md = rs.getMetaData
    val numCols = md.getColumnCount

    def loop(rs: ResultSet): Stream[scala.collection.mutable.Map[String,AnyRef]] = {
      rs.next()
      val map = scala.collection.mutable.Map.empty[String,AnyRef]
      for (i <- 1 to numCols) {
        map.put(md.getColumnName(i), rs.getObject(i))
      }

      map #:: loop(rs)
    }

    loop(rs)
  }

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
