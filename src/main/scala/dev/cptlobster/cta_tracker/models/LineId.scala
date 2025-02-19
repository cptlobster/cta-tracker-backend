package dev.cptlobster.cta_tracker.models

enum LineId(gtfs: String):
  case RED        extends LineId("red")
  case BLUE       extends LineId("blue")
  case GREEN      extends LineId("g")
  case BROWN      extends LineId("brn")
  case PURPLE     extends LineId("p")
  case PURPLE_EXP extends LineId("p_exp")
  case YELLOW     extends LineId("y")
  case PINK       extends LineId("pnk")
  case ORANGE     extends LineId("o")