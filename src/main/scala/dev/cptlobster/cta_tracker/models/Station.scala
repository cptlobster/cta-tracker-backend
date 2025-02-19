package dev.cptlobster.cta_tracker.models

import dev.cptlobster.cta_tracker.models.geo.Point

case class Station(StopID: Int,
                   direction: Char,
                   stopName: String,
                   stationName: String,
                   stationDescriptiveName: String,
                   mapId: Int,
                   accessible: Boolean,
                   lines: List[LineId],
                   location: Point)
