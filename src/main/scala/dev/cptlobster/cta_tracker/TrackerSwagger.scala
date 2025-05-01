package dev.cptlobster.cta_tracker

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, ContactInfo, LicenseInfo, JacksonSwaggerBase, Swagger}

class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with JacksonSwaggerBase

object TrackerApiInfo extends ApiInfo(
  "CTA Tracker Backend API",
  "Backend API translator for the CTA Train Tracker",
  "https://cptlobster.dev",
  ContactInfo(
    "Dustin Thomas",
    "https://cptlobster.dev",
    "io@cptlobster.dev"
  ),
  LicenseInfo(
    "GPLv3",
    "https://www.gnu.org/licenses/gpl-3.0.en.html"
  )
)

class TrackerSwagger extends Swagger(Swagger.SpecVersion, "1.0.0", TrackerApiInfo)