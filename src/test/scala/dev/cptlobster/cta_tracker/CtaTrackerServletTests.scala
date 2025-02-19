package dev.cptlobster.cta_tracker

import org.scalatra.test.scalatest._

class CtaTrackerServletTests extends ScalatraFunSuite {

  addServlet(classOf[CtaTrackerServlet], "/*")

  test("GET / on CtaTrackerServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
