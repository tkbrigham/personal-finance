//import play.api.ApplicationLoader
//import play.api.Configuration
//import play.api.inject._
//import play.api.inject.guice._
//
//import play.api.ApplicationLoader.Context
//import play.api.BuiltInComponentsFromContext
//import play.api.db.Database
//import play.api.db.DBComponents
//import play.api.db.HikariCPComponents
//import play.api.db.evolutions.EvolutionsComponents
//import play.api.routing.Router
//import play.filters.HttpFiltersComponents
//
//class CustomApplicationLoader extends GuiceApplicationLoader() {
//  override def builder(context: ApplicationLoader.Context): GuiceApplicationBuilder = {
//    val extra = Configuration("a" -> 1)
//    initialBuilder
//      .in(context.environment)
//      .loadConfig(extra ++ context.initialConfiguration)
//      .overrides(overrides(context): _*)
//  }
//}
//
//class AppComponents(cntx: Context)
//  extends BuiltInComponentsFromContext(cntx)
//    with DBComponents
//    with EvolutionsComponents
//    with HikariCPComponents
//    with HttpFiltersComponents {
//  // this will actually run the database migrations on startup
//  applicationEvolutions
//}


import controllers.HomeController
import javax.inject.Inject
import routers.HomeRouter
import play.api.ApplicationLoader.Context
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import play.api.db.{DBComponents, Database, HikariCPComponents}
import play.api.db.evolutions.EvolutionsComponents
import play.api.routing.Router.Routes
import play.api.routing.{Router, SimpleRouter}
import play.filters.HttpFiltersComponents
import play.api.routing.sird._

import scalikejdbc.config._

class MyApplicationLoader extends ApplicationLoader {
  def load(context: ApplicationLoader.Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment, context.initialConfiguration, Map.empty)
    }
    DBs.setupAll()
    new AppComponents(context).application
  }
}

class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
    with DBComponents
    with EvolutionsComponents
    with HikariCPComponents
    with HttpFiltersComponents {
  // this will actually run the database migrations on startup
  applicationEvolutions

  lazy val controller = new HomeController(dbApi.database("default"), controllerComponents)

  lazy val router: Router = Router.from {
    case GET(p"/") => controller.index()
  }
}
