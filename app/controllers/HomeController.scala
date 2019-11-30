package controllers

import javax.inject._
import play.api.Logging
import play.api.db.Database
import play.api.mvc._
import play.api.libs.json.{Format, Json}
import models.{Account, User}

@Singleton
class HomeController @Inject()(db: Database, cc: ControllerComponents)
  extends AbstractController(cc)
    with Logging {

  case class Nested(one: Int, two: String)
  case class Test(user: String, account: String, times: Double, n: Nested)

  def index() = Action { implicit request: Request[AnyContent] =>
    implicit val nestedReads: Format[Nested] = Json.format[Nested]
    implicit val residentReads: Format[Test] = Json.format[Test]

    logger.error("error")
    logger.warn("warn")
    logger.info("info")
    logger.debug("debug")
    logger.trace("trace")

    val user = User.findAll.head

    val user3 = User.findAll3.head
    println("found user3")
    println(user3)

    val user4 = User.findById(3)
    println("found user4")
    println(user4)

    Account.printReflectionStuff
    Account.printResult

    val userString = user.email

    val nested = Nested(6, "nested")
    val t = Test(userString, "second", 3.8, nested)
    Ok(Json.toJson(t))
  }
}
