package ahlers.presence.web.client

import ahlers.presence.web.Message
import slogging.{ ConsoleLoggerFactory, HttpLoggerFactory, LazyLogging, LoggerConfig, StrictLogging }

import scala.scalajs.js

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 * @since October 05, 2020
 */
object WebApplication extends LazyLogging {
  LoggerConfig.factory =
    HttpLoggerFactory(
      "/logging",
      "client1",
      (id, level, name, message, cause) =>
        js.Dynamic.literal(
          id = id,
          //level = level,
          name = name,
          message = message
        )
    )

  def main(arguments: Array[String]): Unit =
    logger.info(Message.text)
}
