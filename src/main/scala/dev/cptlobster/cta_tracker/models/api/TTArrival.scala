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
package dev.cptlobster.cta_tracker.models.api

import dev.cptlobster.cta_tracker.models.RouteId

import java.time.Instant

case class TTArrival(tmst: Instant,
                     errCd: Int,
                     errNm: String,
                     eta: List[ArrivalETA]) extends TTResponse

case class ArrivalETA(staId: Int,
                      stpId: Int,
                      staNm: String,
                      stpDe: String,
                      rn: Int,
                      rt: RouteId,
                      destSt: Int,
                      destNm: String,
                      trDr: String,
                      prdt: Instant,
                      arrT: Instant,
                      isApp: Boolean,
                      isSch: Boolean,
                      isFlt: Boolean,
                      isDly: Boolean,
                      flags: Null,
                      lat: Double,
                      lon: Double,
                      heading: Short)
