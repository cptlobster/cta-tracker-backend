package dev.cptlobster.cta_tracker.models.api

import dev.cptlobster.cta_tracker.models.RouteId

import java.util.Date

case class TTPosition(tmst: Date,
                      errCd: Int,
                      errNm: String,
                      route: List[Route]) extends TTResponse

case class Route(name: RouteId,
                 train: List[Train])

case class Train(rn: Int,
                 destSt: Int,
                 destNm: String,
                 trDr: Int,
                 nextStaId: Int,
                 nextStpId: Int,
                 prdt: Date,
                 arrT: Date,
                 isApp: Boolean,
                 isDly: Boolean,
                 lat: Double,
                 long: Double,
                 heading: Short)