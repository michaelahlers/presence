package ahlers.presence.web.client

import ahlers.presence.web.Message
import slogging.{ HttpLoggerFactory, LazyLogging, LoggerConfig }

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 05, 2020
 */
object WebApplication extends LazyLogging {
  LoggerConfig.factory = HttpLoggerFactory("/logging")

  def main(arguments: Array[String]): Unit =
    logger.info(Message.text)

}
