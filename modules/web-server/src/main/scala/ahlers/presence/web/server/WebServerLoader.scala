package ahlers.presence.web.server

import play.api.ApplicationLoader.Context
import play.api.Mode.Dev
import play.api.{ ApplicationLoader, LoggerConfigurator }

/**
 * @author <a href="mailto:michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 06, 2020
 */
class WebServerLoader extends ApplicationLoader {
  override def load(context: Context) = {

    /** @see [[https://playframework.com/documentation/latest/ScalaCompileTimeDependencyInjection#Configuring-Logging Compile-Time Dependency Injection: Configure Logging]] */
    LoggerConfigurator(context.environment.classLoader)
      .foreach(_.configure(
        context.environment,
        context.initialConfiguration,
        Map.empty))

    context.environment.mode match {
      case Dev =>
        (new WebServerModule(context))
          .application
      case _ =>
        (new WebServerModule(context))
          .application
    }

  }
}
