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
package dev.cptlobster.cta_tracker.models

/**
 * Enum representation of CTA routes.
 * @param s CTA representation of line names
 */
enum RouteId(s: String):
  case RED    extends RouteId("red")
  case BLUE   extends RouteId("blue")
  case BROWN  extends RouteId("brn")
  case GREEN  extends RouteId("g")
  case ORANGE extends RouteId("org")
  case PURPLE extends RouteId("p")
  case PINK   extends RouteId("pink")
  case YELLOW extends RouteId("y")

  override def toString: String = s

object RouteId:
  def apply(s: String): RouteId = s.toLowerCase match
    case "red" => RouteId.RED
    case "blue" => RouteId.BLUE
    case "brn" => RouteId.BROWN
    case "g" => RouteId.GREEN
    case "org" => RouteId.ORANGE
    case "p" => RouteId.PURPLE
    case "pink" => RouteId.PINK
    case "y" => RouteId.YELLOW
    case _ => throw IllegalArgumentException("Must be a valid route ID (see https://www.transitchicago.com/developers/ttdocs/#_Toc296199908 for reference)")