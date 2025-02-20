/* Copyright (C) 2025  Dustin Thomas <io@cptlobster.dev>
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, see
 * <https://www.gnu.org/licenses/>.
 */
package dev.cptlobster.cta_tracker

import org.scalatra.*
import org.slf4j.{Logger, LoggerFactory}

// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._

class CtaTrackerServlet extends ScalatraServlet with JacksonJsonSupport {
  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit lazy val jsonFormats: Formats = DefaultFormats

  private val logger = LoggerFactory.getLogger(getClass)

  private val api: CtaTrackerApi = CtaTrackerApi("changeme")

  before() {
    contentType = formats("json")
  }

  get("/") {
    logger.info("Transfer to escalator at O'Hare")
    "{\"transfer\": {\"to\": \"escalator\", \"at\": \"O'Hare\"}}"
  }

  get("/arrivals/station/:station") {
    logger.info(s"Getting arrivals for station ${params("station")}")
    params("station").toIntOption match
      case Some(s) => api.stationArrivals(s)
      case None => halt(status = 400,
                        body = "station ID must be an integer")
  }

  get("/arrivals/stop/:stop") {
    logger.info(s"Getting arrivals for stop ${params("stop")}")
    params("stop").toIntOption match
      case Some(s) => api.stopArrivals(s)
      case None => halt(status = 400,
        body = "stop ID must be an integer")
  }

  get("follow/:run") {
    logger.info(s"Following run ${params("run")}")
    params("run").toIntOption match
      case Some(s) => api.follow(s)
      case None => halt(status = 400,
        body = "run ID must be an integer")
  }
}
