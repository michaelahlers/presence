package ahlers.michael.presence

import javax.inject._

import com.google.inject.AbstractModule
import com.typesafe.scalalogging.LazyLogging

@Singleton
class ApplicationModule
  extends AbstractModule
          with LazyLogging {

  override def configure() = {
    logger.info( s"""Started Presence version ${BuildInfo.version} (revision ${BuildInfo.revision}).""")
  }

}
