package dev.cptlobster.cta_tracker.models.api

import java.util.Date

trait TTResponse:
  val tmst: Date
  val errCd: Int
  val errNm: String

  def isOk: Boolean = errCd == 0
  def throwError(): Unit = throw CtaApiException(errCd, errNm)

case class CtaApiException(errCd: Int, errNm: String) extends Exception:
  private val message: String = s"CTA API error $errCd: $errNm"