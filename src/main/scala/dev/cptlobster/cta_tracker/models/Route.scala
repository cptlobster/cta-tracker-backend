package dev.cptlobster.cta_tracker.models

enum Route(s: String):
  case RED    extends Route("red")
  case BLUE   extends Route("blue")
  case BROWN  extends Route("brn")
  case GREEN  extends Route("g")
  case ORANGE extends Route("org")
  case PURPLE extends Route("p")
  case PINK   extends Route("pink")
  case YELLOW extends Route("y")

  def toString: String = s