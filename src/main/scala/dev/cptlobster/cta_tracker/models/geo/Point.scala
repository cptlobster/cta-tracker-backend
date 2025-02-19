package dev.cptlobster.cta_tracker.models.geo

case class Point(x: Double, y: Double):
  def distance(other: Point): Double = math.sqrt(math.pow(x - other.x, 2) + math.pow(y - other.y, 2))