package controllers

import javax.inject._
import play.api.libs.json.{Json, Format}
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

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

    val nested = Nested(6, "oh boy")
    val t = Test("first", "second", 3.8, nested)
    Ok(Json.toJson(t))
  }
}
