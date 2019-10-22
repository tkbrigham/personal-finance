package routers

import controllers.HomeController
import javax.inject.Inject
import play.api.routing.SimpleRouter
import play.api.routing.sird._


class HomeRouter @Inject()(controller: HomeController) extends SimpleRouter {
  override def routes: play.api.routing.Router.Routes = {
    case GET(p"/") => controller.index()
  }
}
