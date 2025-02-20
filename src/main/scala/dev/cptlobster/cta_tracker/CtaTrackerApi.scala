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
import dev.cptlobster.cta_tracker.models.api.{TTArrival, TTFollow, TTPosition}
import sttp.client3.*
import org.json4s.*
import org.json4s.jackson.JsonMethods.*

import scala.collection.mutable

import org.slf4j.{Logger, LoggerFactory}

/**
 * Interaction with CTA API.
 * @param key Your unique API key, assigned to you after agreeing to DLA and requesting a key be generated for you.
 */
case class CtaTrackerApi(key: String):
  private val baseUrl: String = "http://lapi.transitchicago.com/api/1.0"
  private val backend = HttpClientSyncBackend()

  private val logger = LoggerFactory.getLogger(getClass)

  implicit val formats: Formats = DefaultFormats

  /**
   * Execute a request against the CTA train tracker API.
   * @param endpoint The API endpoint to request
   * @param params Additional filtering parameters
   * @return The response from the CTA API, in JSON format.
   * @throws Exception if a non-200 status code is returned by the API.
   */
  private def request(endpoint: String, params: mutable.Map[String, Any]): String =
    params += ("key" -> key)
    params += ("outputType" -> "JSON")
    val stringedParams: Map[String, String] = params.map((k, v) => k -> v.toString).toMap

    val queryUrl = uri"$baseUrl/$endpoint?$stringedParams"
    logger.info(queryUrl.toString)

    val response = basicRequest
      .get(queryUrl).send(backend)

    response.body match
      case Left(err) => throw Exception(err)
      case Right(body) => body

  /**
   * Receive predictions for all platforms at a given station.
   *
   * @param station  The station ID
   * @param max      The maximum number of results to receive. If 0 or below, returns all results.
   * @param route    Allows for specifying a single route. If null, returns all routes.
   * @return
   */
  def stationArrivals(station: Int, max: Int = 0, route: RouteId = null): TTArrival =
    val params: mutable.Map[String, Any] = mutable.Map("mapid" -> station)
    if max > 0 then params += ("max" -> max)
    if route != null then params += ("rt" -> route)
    parse(request("ttarrivals.aspx", params)).extract[TTArrival]

  /**
   * Receive predictions for a specific platform at a given station.
   * @param stop The stop ID
   * @param max The maximum number of results to receive. If 0 or below, returns all results.
   * @param route Allows for specifying a single route. If null, returns all routes.
   * @return
   */
  def stopArrivals(stop: Int, max: Int = 0, route: RouteId = null): TTArrival =
    val params: mutable.Map[String, Any] = mutable.Map("stpid" -> stop)
    if max > 0 then params += ("max" -> max)
    if route != null then params += ("rt" -> route)
    parse(request("ttarrivals.aspx", params)).extract[TTArrival]

  /**
   * Predict arrivals for a given train at all subsequent stations for which that train is estimated to arrive.
   * @param run The run number to generate predictions for.
   * @return
   */
  def follow(run: Int): TTFollow =
    val params: mutable.Map[String, Any] = mutable.Map("run" -> run)
    parse(request("ttfollow.aspx", params)).extract[TTFollow]

  /**
   * Produce a list of in-service trains and basic info / locations for one L route.
   *
   * Each separate entry describes a single train and provides coordinate, geospatial heading, certain train attributes
   * and next stop information.
   * @param route The route to read train information for.
   * @return
   */
  def locations(route: RouteId): TTPosition =
    val params: mutable.Map[String, Any] = mutable.Map("rt" -> route)
    parse(request("ttpositions.aspx", params)).extract[TTPosition]

  /**
   * Produce a list of in-service trains and basic info / locations for multiple L routes.
   *
   * Each separate entry describes a single train and provides coordinate, geospatial heading, certain train attributes
   * and next stop information.
   *
   * @param routes The routes to read train information for.
   * @return
   */
  def locations(routes: List[RouteId]): TTPosition =
    val params: mutable.Map[String, Any] = mutable.Map("rt" -> routes.mkString(","))
    parse(request("ttpositions.aspx", params)).extract[TTPosition]