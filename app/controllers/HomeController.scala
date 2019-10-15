package controllers

import javax.inject._
import play.api.db._
import play.api.mvc._
import play.api.libs.json.{Format, Json}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(db: Database, cc: ControllerComponents)
  extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  case class Nested(one: Int, two: String)

  case class Test(hi: String, there: String, times: Double, n: Nested)

  def index() = Action { implicit request: Request[AnyContent] =>
    implicit val nestedReads: Format[Nested] = Json.format[Nested]
    implicit val residentReads: Format[Test] = Json.format[Test]

    println("hi it me the guy")
    println("oh no")
    var outString = "Number is "
    val conn = db.getConnection()

    try {
      val stmt = conn.createStatement
      val col = "da_count"
      val rs = stmt.executeQuery(s"SELECT count(*) as $col from pg_stat_activity")

      while (rs.next()) {
        outString += rs.getString(col)
      }
    } finally {
      conn.close()
    }
    val nested = Nested(6, outString)
    val t = Test("first", "second", 3.8, nested)
    Ok(Json.toJson(t))
  }

//  def index = Action {
//    var outString = "Number is "
//    println("hi it me the guy")
//    println("oh no")
//    val conn = db.getConnection()
//
//    try {
//      val stmt = conn.createStatement
//      val rs = stmt.executeQuery("SELECT 9 as testkey ")
//
//      while (rs.next()) {
//        outString += rs.getString("testkey")
//      }
//    } finally {
//      conn.close()
//    }
//    Ok(outString)
//  }
}
