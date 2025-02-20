package dev.cptlobster.cta_tracker

import org.scalatra._

class CtaTrackerServlet extends ScalatraServlet {
  get("/") {
    "transfer to escalator at O'Hare\n"
  }
}
