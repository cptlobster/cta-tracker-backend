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

object GtfsRouteType extends Enumeration:
  type GtfsRouteType = Value

  val LIGHT_RAIL: GtfsRouteType = Value(0)
  val METRO: GtfsRouteType = Value(1)
  val RAIL: GtfsRouteType = Value(2)
  val BUS: GtfsRouteType = Value(3)
  val FERRY: GtfsRouteType = Value(4)
  val CABLE_TRAM: GtfsRouteType = Value(5)
  val AERIAL_LIFT: GtfsRouteType = Value(6)
  val FUNICULAR: GtfsRouteType = Value(7)
  val TROLLEYBUS: GtfsRouteType = Value(11)
  val MONORAIL: GtfsRouteType = Value(12)