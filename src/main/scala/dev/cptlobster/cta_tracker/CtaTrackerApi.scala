package dev.cptlobster.cta_tracker

import dev.cptlobster.cta_tracker.models.Route
import sttp.client3._

import scala.collection.mutable

case class CtaTrackerApi(key: String):
  val baseUrl: String = "http://lapi.transitchicago.com/api/1.0"
  private val backend = HttpClientSyncBackend()

  def request(endpoint: String, params: mutable.Map[String, Any]): String =
    params += ("key" -> key)
    params += ("outputFormat" -> "JSON")
    val paramStr = params.map((k, v) => s"$k=$v").mkString("&")
    val response = basicRequest
      .get(uri"$baseUrl/$endpoint?$paramStr").send(backend)

    response.body match
      case Left(err) => throw Exception(err)
      case Right(body) => body

  def stationArrivals(station: Int, max: Int = 0, route: Route = null): String =
    val params: mutable.Map[String, Any] = mutable.Map("mapid" -> station)
    if max > 0 then params += ("max" -> max)
    if route != null then params += ("rt" -> route)
    request("ttarrivals.aspx", params)

  def stopArrivals(stop: Int, max: Int = 0, route: Route = null): String =
    val params: mutable.Map[String, Any] = mutable.Map("stpid" -> stop)
    if max > 0 then params += ("max" -> max)
    if route != null then params += ("rt" -> route)
    request("ttarrivals.aspx", params)

  def follow(run: Int): String =
    val params: mutable.Map[String, Any] = mutable.Map("run" -> run)
    request("ttfollow.aspx", params)

  def locations(route: Route): String =
    val params: mutable.Map[String, Any] = mutable.Map("rt" -> route)
    request("ttpositions.aspx", params)

  def locations(routes: List[Route]): String =
    val params: mutable.Map[String, Any] = mutable.Map("rt" -> routes.mkString(","))