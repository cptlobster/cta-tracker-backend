package dev.cptlobster.cta_tracker.models.api

import java.util.Date

case class TTFollow(tmst: Date,
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
                     prdt: Date,
                     arrT: Date,
                     isApp: Boolean,
                     isSch: Boolean,
                     isFlt: Boolean,
                     isDly: Boolean)