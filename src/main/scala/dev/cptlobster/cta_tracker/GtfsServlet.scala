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

import dev.cptlobster.cta_tracker.models.gtfs.{GtfsRoute, GtfsStation}
import dev.cptlobster.cta_tracker.utils.GtfsDatabase
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.swagger.*
import org.slf4j.LoggerFactory

class GtfsServlet(implicit val swagger: Swagger) extends ScalatraServlet with JacksonJsonSupport with SwaggerSupport {
  protected val applicationDescription = "GTFS database"
  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected lazy implicit val jsonFormats: Formats = DefaultFormats

  private val logger = LoggerFactory.getLogger(getClass)

  private val db = GtfsDatabase()

  before() {
    contentType = formats("json")
  }

  private val getStation =
    (apiOperation[GtfsStation]("getStation")
      summary "Get station info"
      description "Get the GTFS data for a specific station."
      parameter queryParam[Int]("station").description("The ID of the station you are searching for"))

  get("/station/:station", operation(getStation)) {
    logger.info(s"Getting station matching ID ${params("station")}")
    params("station").toIntOption match
      case Some(id) =>
        db.stations.find(_.id.equals(id)) match
          case Some(s) => s
          case None => halt(status = 404,
            body = "station ID not found")
      case None => halt(status = 400,
        body = "station ID must be an integer")
  }

  private val getAllStations =
    (apiOperation[List[GtfsStation]]("getAllStations")
      summary "Get all station info"
      description "Get the GTFS data for all stations.")

  get("/station", operation(getAllStations)) {
    logger.info("Getting list of all stations")
    db.stations
  }

  private val getRoute =
    (apiOperation[GtfsRoute]("getRoute")
      summary "Get route info"
      description "Get the GTFS data for a specific route."
      parameter queryParam[String]("route").description("The ID of the route you are searching for"))

  get("/route/:route", operation(getRoute)) {
    logger.info(s"Getting route ID ${params("route")}")
    db.routes.find(_.id.toLowerCase.equals(params("route").toLowerCase)) match
      case Some(s) => s
      case None => halt(status = 404,
        body = "route ID not found")
  }

  private val getAllRoutes =
    (apiOperation[List[GtfsRoute]]("getAllRoutes")
      summary "Get all route info"
      description "Get the GTFS data for all routes.")

  get("/route", operation(getAllRoutes)) {
    logger.info("Getting list of all routes")
    db.routes
  }
}