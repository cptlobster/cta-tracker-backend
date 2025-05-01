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
package dev.cptlobster.cta_tracker.models.gtfs

import dev.cptlobster.cta_tracker.models.geo.Point
import dev.cptlobster.cta_tracker.models.gtfs.GtfsLocationType.GtfsLocationType

/**
 * representation of GTFS station/stop record.
 * @param id The parent station ID
 * @param stopId If describing a stop, the stop ID
 * @param desc The long description of the station
 * @param position The lat/long position of this station
 * @param locationType The GTFS location type (station, stop, entrance, etc.)
 * @param accessible The wheelchair boarding capability of this station or stop. If true, station is accessible; if
 *                   false, station is not accessible; if none, then the accessibility data is not available or is
 *                   inherited from the parent station. If inherited, this should be filled from the parent station
 */
case class GtfsStation(id: Int,
                       stopId: Option[Int],
                       desc: String,
                       position: Point,
                       locationType: GtfsLocationType,
                       accessible: Option[Boolean])