package dev.cptlobster.cta_tracker

import dev.cptlobster.cta_tracker.models.Route
import sttp.client3._

import scala.collection.mutable

/**
 * Interaction with CTA API.
 * @param key Your unique API key, assigned to you after agreeing to DLA and requesting a key be generated for you.
 */
case class CtaTrackerApi(key: String):
  val baseUrl: String = "http://lapi.transitchicago.com/api/1.0"
  private val backend = HttpClientSyncBackend()

  /**
   * Execute a request against the CTA train tracker API.
   * @param endpoint The API endpoint to request
   * @param params Additional filtering parameters
   * @return The response from the CTA API, in JSON format.
   * @throws Exception if a non-200 status code is returned by the API.
   */
  private def request(endpoint: String, params: mutable.Map[String, Any]): String =
    params += ("key" -> key)
    params += ("outputFormat" -> "JSON")
    val paramStr = params.map((k, v) => s"$k=$v").mkString("&")
    val response = basicRequest
      .get(uri"$baseUrl/$endpoint?$paramStr").send(backend)

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
  def stationArrivals(station: Int, max: Int = 0, route: Route = null): String =
    val params: mutable.Map[String, Any] = mutable.Map("mapid" -> station)
    if max > 0 then params += ("max" -> max)
    if route != null then params += ("rt" -> route)
    request("ttarrivals.aspx", params)

  /**
   * Receive predictions for a specific platform at a given station.
   * @param stop The stop ID
   * @param max The maximum number of results to receive. If 0 or below, returns all results.
   * @param route Allows for specifying a single route. If null, returns all routes.
   * @return
   */
  def stopArrivals(stop: Int, max: Int = 0, route: Route = null): String =
    val params: mutable.Map[String, Any] = mutable.Map("stpid" -> stop)
    if max > 0 then params += ("max" -> max)
    if route != null then params += ("rt" -> route)
    request("ttarrivals.aspx", params)

  /**
   * Predict arrivals for a given train at all subsequent stations for which that train is estimated to arrive.
   * @param run The run number to generate predictions for.
   * @return
   */
  def follow(run: Int): String =
    val params: mutable.Map[String, Any] = mutable.Map("run" -> run)
    request("ttfollow.aspx", params)

  /**
   * Produce a list of in-service trains and basic info / locations for one L route.
   *
   * Each separate entry describes a single train and provides coordinate, geospatial heading, certain train attributes
   * and next stop information.
   * @param route The route to read train information for.
   * @return
   */
  def locations(route: Route): String =
    val params: mutable.Map[String, Any] = mutable.Map("rt" -> route)
    request("ttpositions.aspx", params)

  /**
   * Produce a list of in-service trains and basic info / locations for multiple L routes.
   *
   * Each separate entry describes a single train and provides coordinate, geospatial heading, certain train attributes
   * and next stop information.
   *
   * @param routes The routes to read train information for.
   * @return
   */
  def locations(routes: List[Route]): String =
    val params: mutable.Map[String, Any] = mutable.Map("rt" -> routes.mkString(","))