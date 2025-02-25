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

trait TTResponse:
  val tmst: Instant
  val errCd: Int
  val errNm: String

  def isOk: Boolean = errCd == 0
  def throwError(): Unit =
    errCd match
      // everything's okay, don't throw an error
      case 0 => ()
      // Required parameter is missing, could be user or backend error
      case 100 => CtaUserErrorException(errCd, errNm)
      // Invalid API key, backend error since backend provides key
      case 101 => CtaRequestErrorException(errCd, errNm)
      // rate limited by CTA
      case 102 => CtaRateLimitException(errCd, errNm)
      // no train found, problem's on CTA's end
      case 501 => CtaInternalException(errCd, errNm)
      // unexpected exit station ID, problem's on CTA's end
      case 502 => CtaInternalException(errCd, errNm)
      // unable to find predictions, problem's on CTA's end
      case 503 => CtaInternalException(errCd, errNm)
      // server error, problem's on CTA's end
      case 900 => CtaInternalException(errCd, errNm)
      case _ => throw CtaApiException(errCd, errNm)

class CtaApiException(errCd: Int, errNm: String) extends Exception:
  private val message: String = s"CTA API error $errCd: $errNm"
  def getErrCd: Int = errCd
  def getErrNm: String = errNm

class CtaUserErrorException(errCd: Int, errNm: String) extends CtaApiException(errCd, errNm)

class CtaRequestErrorException(errCd: Int, errNm: String) extends CtaApiException(errCd, errNm)

class CtaInternalException(errCd: Int, errNm: String) extends CtaApiException(errCd, errNm)

class CtaRateLimitException(errCd: Int, errNm: String) extends CtaRequestErrorException(errCd, errNm)