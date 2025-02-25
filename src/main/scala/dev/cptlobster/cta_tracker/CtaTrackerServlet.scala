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

import dev.cptlobster.cta_tracker.models.RouteId
import dev.cptlobster.cta_tracker.models.api.{CtaApiException, CtaRateLimitException, CtaUserErrorException}
import dev.cptlobster.cta_tracker.utils.*
import org.scalatra.*
import org.slf4j.LoggerFactory

// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.*

class CtaTrackerServlet extends ScalatraServlet with JacksonJsonSupport {
  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected lazy implicit val jsonFormats: Formats = DefaultFormats
    + LocalInstantSerializer
    + DoubleSerializer
    + IntSerializer
    + ShortSerializer
    + BoolSerializer
    + RouteIdSerializer

  private val logger = LoggerFactory.getLogger(getClass)

  private val api = CtaTrackerApi(sys.env("CTA_TRACKER_KEY"))

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
      case Some(s) =>
        try
          api.stationArrivals(s)
        catch
          case e: CtaUserErrorException => halt(status = 400, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
          case e: CtaRateLimitException => halt(status = 503, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
          case e: CtaApiException => halt(status = 500, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
          case e: Exception => halt(status = 500, body = s"{\"msg\":\"${e.getMessage}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
      case None => halt(status = 400,
                        body = "station ID must be an integer")
  }

  get("/arrivals/stop/:stop") {
    logger.info(s"Getting arrivals for stop ${params("stop")}")
    params("stop").toIntOption match
      case Some(s) =>
        try
          api.stopArrivals(s)
        catch
          case e: CtaUserErrorException => halt(status = 400, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
          case e: CtaRateLimitException => halt(status = 503, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
          case e: CtaApiException => halt(status = 500, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
          case e: Exception => halt(status = 500, body = s"{\"msg\":\"${e.getMessage}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
      case None => halt(status = 400,
        body = "stop ID must be an integer")
  }

  get("/follow/:run") {
    logger.info(s"Following run ${params("run")}")
    params("run").toIntOption match
      case Some(s) =>
        try
          api.follow(s)
        catch
          case e: CtaUserErrorException => halt(status = 400, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
          case e: CtaRateLimitException => halt(status = 503, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
          case e: CtaApiException => halt(status = 500, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
          case e: Exception => halt(status = 500, body = s"{\"msg\":\"${e.getMessage}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
      case None => halt(status = 400,
        body = "run ID must be an integer")
  }

  get("/positions/:line") {
    logger.info(s"Getting all positions for ${params("line")}")
    try
      val l: RouteId = RouteId(params("line"))
      api.locations(l)
    catch
      case e: CtaUserErrorException => halt(status = 400, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
      case e: CtaRateLimitException => halt(status = 503, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
      case e: CtaApiException => halt(status = 500, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
      case e: Exception => halt(status = 500, body = s"{\"msg\":\"${e.getMessage}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
  }
  get("/positions") {
    logger.info("Getting all positions for all lines")
    try
      api.locations(List(RouteId.RED, RouteId.BLUE, RouteId.BROWN, RouteId.GREEN, RouteId.ORANGE, RouteId.PURPLE, RouteId.PINK, RouteId.YELLOW))
    catch
      case e: CtaUserErrorException => halt(status = 400, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
      case e: CtaRateLimitException => halt(status = 503, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
      case e: CtaApiException => halt(status = 500, body = s"{\"errCd\":${e.getErrCd},\"errNm\":\"${e.getErrNm}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
      case e: Exception => halt(status = 500, body = s"{\"msg\":\"${e.getMessage}\",\"stacktrace\":${e.getStackTrace.mkString("[\"", "\",\"", "\"]")}}")
  }
}
