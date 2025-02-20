package dev.cptlobster.cta_tracker.models.api

import dev.cptlobster.cta_tracker.models.{Position, TTResponse}

import java.util.Date

case class TTFollow(tmst: Date,
                    errCd: Int,
                    errNm: String,
                    position: Position) extends TTResponse

case class Position()