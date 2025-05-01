package dev.cptlobster.cta_tracker.utils

import dev.cptlobster.cta_tracker.models.gtfs.{GtfsLocationType, GtfsRoute, GtfsRouteType, GtfsStation}

import java.io.File
import com.github.tototoshi.csv.*
import dev.cptlobster.cta_tracker.models.geo.Point

case class GtfsDatabase():
  val stations: List[GtfsStation] =
    val reader = CSVReader.open(new File("gtfs/stops.csv"))
    val rows = reader.allWithHeaders()
    for (row <- rows) yield {
      val id = if row("parent_station") != "" then row("parent_station").toInt else row("stop_id").toInt
      val stopId = if row("parent_station") != "" then Some(row("stop_id").toInt) else None
      val name = row("stop_name")
      val position = Point(row("stop_lat").toDouble, row("stop_lon").toDouble)
      val locationType = GtfsLocationType(row("location_type").toInt)
      val accessible = row("wheelchair_boarding").toInt match
        case 0 => None
        case 1 => Some(true)
        case 2 => Some(false)
      GtfsStation(id, stopId, name, position, locationType, accessible)
    }

  val routes: List[GtfsRoute] =
    val reader = CSVReader.open(new File("gtfs/routes.csv"))
    val rows = reader.allWithHeaders()
    for (row <- rows) yield {
      val id = row("route_id")
      val name = row("route_long_name")
      val routeType = GtfsRouteType(row("route_type").toInt)
      val url = row("route_url")
      GtfsRoute(id, name, routeType, url)
    }