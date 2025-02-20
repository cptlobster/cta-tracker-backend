package dev.cptlobster.cta_tracker.models.api

import dev.cptlobster.cta_tracker.models.{ETA, RouteId, TTResponse}

import java.util.Date

case class TTArrival(tmst: Date,
                     errCd: Int,
                     errNm: Int,
                     eta: List[ETA]) extends TTResponse

case class ETA(staId: Int,
               stpId: Int,
               staNm: String,
               stpDe: String,
               rn: Int,
               rt: RouteId,
               destSt: Int,
               destNm: String,
               trDr: String,
               prdt: Date,
               arrT: Date,
               isApp: Boolean,
               isSch: Boolean,
               isFlt: Boolean,
               isDly: Boolean,
               lat: Double,
               long: Double,
               heading: Short)
