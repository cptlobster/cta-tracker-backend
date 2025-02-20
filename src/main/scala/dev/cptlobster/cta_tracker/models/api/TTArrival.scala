package dev.cptlobster.cta_tracker.models.api

import dev.cptlobster.cta_tracker.models.RouteId

import java.util.Date

case class TTArrival(tmst: Date,
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
                      prdt: Date,
                      arrT: Date,
                      isApp: Boolean,
                      isSch: Boolean,
                      isFlt: Boolean,
                      isDly: Boolean,
                      lat: Double,
                      long: Double,
                      heading: Short)
