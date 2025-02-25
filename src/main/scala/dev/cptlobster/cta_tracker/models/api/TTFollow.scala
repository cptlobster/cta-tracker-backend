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

import java.time.Instant

case class TTFollow(tmst: Instant,
                    errCd: Int,
                    errNm: String,
                    position: Position,
                    eta: List[FollowETA]) extends TTResponse

case class Position(lat: Double,
                    lon: Double,
                    heading: Int)

case class FollowETA(staId: Int,
                     stpId: Int,
                     staNm: String,
                     stpDe: String,
                     rn: Int,
                     rt: Route,
                     destSt: Int,
                     destNm: String,
                     trDr: Int,
                     prdt: Instant,
                     arrT: Instant,
                     isApp: Boolean,
                     isSch: Boolean,
                     isFlt: Boolean,
                     isDly: Boolean,
                     flags: Null)