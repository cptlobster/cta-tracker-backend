package dev.cptlobster.cta_tracker.models

import dev.cptlobster.cta_tracker.models.geo.Ray
import java.util.Date

case class Train(run: Int,
                 destinationName: String,
                 direction: String,
                 nextStation: Station,
                 predictionTime: Date,
                 arrivalTime: Date,
                 approaching: Boolean,
                 delayed: Boolean,
                 position: Ray)
