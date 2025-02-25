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
package dev.cptlobster.cta_tracker.utils

import dev.cptlobster.cta_tracker.models.RouteId
import org.json4s.{CustomSerializer, Extraction, Formats, JArray, JBool, JDecimal, JInt, JNothing, JNull, JString}

import java.time.{Instant, LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter

// CTA's API returns timestamps in an ISO-8601 format, but in local time. We need to convert this into a Java 8 Instant
object LocalInstantSerializer extends CustomSerializer[Instant](format => (
  {
    // Deserialization: Parse the ISO-8601 local timestamp string.
    case JString(s) =>
      // Parse as LocalDateTime (which has no timezone) then convert using system default.
      val localDateTime = LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
      localDateTime.atZone(ZoneId.systemDefault()).toInstant
    case JNull => null
    case JNothing => null
  },
  {
    // Serialization: Convert the Instant to a UTC ISO-8601 string.
    case instant: Instant =>
      // Return a JString to ensure json4s never uses reflection on Instant.
      JString(DateTimeFormatter.ISO_INSTANT.format(instant))
  }
))

object ShortSerializer extends CustomSerializer[Short](format => (
  {
    case JString(s) => s.toShort
    case JInt(s) => s.toShort
    case JNull => 0
    case JNothing => 0
  },
  {
    case s: Short =>
      JInt(s.toInt)
  }
))

object IntSerializer extends CustomSerializer[Int](format => ( {
  case JString(s) => s.toInt
  case JInt(s) => s.toInt
  case JNull => 0
  case JNothing => 0
}, {
  case s: Int =>
    JInt(s)
}
))

object BoolSerializer extends CustomSerializer[Boolean](format => (
  {
    case JString("0") => false
    case JString("1") => true
    case JInt(0) => false
    case JInt(1) => true
    case JBool(s) => s
    case JNull => false
  },
  {
    case s: Boolean =>
      JBool(s)
  }
))

object DoubleSerializer extends CustomSerializer[Double](format => (
  {
    case JString(s) => s.toDouble
    case JDecimal(s) => s.toDouble
    case JNull => 0
    case JNothing => 0
  },
  {
    case s: Double => JDecimal(s)
  }
))

object RouteIdSerializer extends CustomSerializer[RouteId](format => (
  {
    case JString(s) => RouteId(s)
    case JNull => null
    case JNothing => null
  },
  {
    case r: RouteId => JString(r.toString)
  }
))