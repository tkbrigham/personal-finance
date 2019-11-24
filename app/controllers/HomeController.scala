package controllers

import javax.inject._
import play.api.Logging
import play.api.db.Database
import play.api.mvc._
import play.api.libs.json.{Format, Json}
import models.User

@Singleton
class HomeController @Inject()(db: Database, cc: ControllerComponents)
  extends AbstractController(cc)
    with Logging {

  case class Nested(one: Int, two: String)
  case class Test(hi: String, there: String, times: Double, n: Nested)

  def index() = Action { implicit request: Request[AnyContent] =>
    implicit val nestedReads: Format[Nested] = Json.format[Nested]
    implicit val residentReads: Format[Test] = Json.format[Test]

    logger.error("error")
    logger.warn("warn")
    logger.info("info")
    logger.debug("debug")
    logger.trace("trace")

//    val outString = new UserTable(db).all()

    val outString = User.findAll.head.email

    val nested = Nested(6, outString)
    val t = Test("first", "second", 3.8, nested)
    Ok(Json.toJson(t))
  }
}
