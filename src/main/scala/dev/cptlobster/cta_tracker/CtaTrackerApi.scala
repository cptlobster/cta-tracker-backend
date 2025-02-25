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
import dev.cptlobster.cta_tracker.models.api.{ArrivalETA, FollowETA, Route, TTArrival, TTFollow, TTPosition, Train}
import dev.cptlobster.cta_tracker.utils.{BoolSerializer, DoubleSerializer, IntSerializer, LocalInstantSerializer, RouteIdSerializer, ShortSerializer}
import sttp.client3.*
import org.json4s.*
import org.json4s.jackson.JsonMethods.*

import scala.collection.mutable
import org.slf4j.LoggerFactory

/**
 * Interaction with CTA API.
 * @param key Your unique API key, assigned to you after agreeing to DLA and requesting a key be generated for you.
 */
case class CtaTrackerApi(key: String):
  private val baseUrl: String = "http://lapi.transitchicago.com/api/1.0"
  private val backend = HttpClientSyncBackend()

  private val logger = LoggerFactory.getLogger(getClass)

  implicit val formats: Formats = DefaultFormats
    + LocalInstantSerializer
    + DoubleSerializer
    + IntSerializer
    + ShortSerializer
    + BoolSerializer
    + RouteIdSerializer


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
      case Right(body) =>
        logger.info(body)
        body

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
    val responseJson = parse(request("ttarrivals.aspx", params)) \ "ctatt"
    handleArrResponse(responseJson)

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
    val responseJson = parse(request("ttarrivals.aspx", params)) \ "ctatt"
    handleArrResponse(responseJson)

  private def handleArrResponse(responseJson: JValue): TTArrival =
    // normalize the list of responses; if it has one item not in an array, wrap it in one
    val normalizedJson = responseJson.transformField {
      case ("eta", value) => ("eta", value match {
        case arr: JArray => arr
        case other => JArray(List(other))
      })
    }
    val result = normalizedJson.extract[TTArrival]
    result.throwError()
    result

  /**
   * Predict arrivals for a given train at all subsequent stations for which that train is estimated to arrive.
   * @param run The run number to generate predictions for.
   * @return
   */
  def follow(run: Int): TTFollow =
    val params: mutable.Map[String, Any] = mutable.Map("run" -> run)
    val responseJson = parse(request("ttfollow.aspx", params)) \ "ctatt"
    // normalize the list of responses; if it has one item not in an array, wrap it in one
    val normalizedJson = responseJson.transformField {
      case ("eta", value) => ("eta", value match {
        case arr: JArray => arr
        case other => JArray(List(other))
      })
    }
    val result = normalizedJson.extract[TTFollow]
    result.throwError()
    result

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
    val responseJson = parse(request("ttpositions.aspx", params)) \ "ctatt"
    handleLocResponse(responseJson)

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
    val responseJson = parse(request("ttpositions.aspx", params)) \ "ctatt"
    handleLocResponse(responseJson)

  private def handleLocResponse(responseJson: JValue): TTPosition =
    // normalize the list of responses; if it has one item not in an array, wrap it in one
    val normalizedJson = responseJson.transformField {
      case ("route", value) => ("route", value match {
        case arr: JArray =>
          arr.map(item => {
            item.transformField {
              case ("train", value) => ("train", value match {
                case arr: JArray => arr
                case other => JArray(List(other))
              })
            }
          })
        case other: JValue => JArray(List(
          other.transformField {
            case ("train", value) => ("train", value match {
              case arr: JArray => arr
              case other => JArray(List(other))
            })
          }
        ))
      })
    }
    val result = normalizedJson.extract[TTPosition]
    result.throwError()
    result