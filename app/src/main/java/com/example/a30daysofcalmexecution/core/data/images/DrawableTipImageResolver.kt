package com.example.a30daysofcalmexecution.core.data.images

import com.example.a30daysofcalmexecution.R
import javax.inject.Inject

class DrawableTipImageResolver @Inject constructor() : TipImageResolver {
    override fun resolve(imageKey: String): Int? {
        return when (imageKey) {
            "tip_01_define_real_priority" -> R.drawable.ic_launcher_background
            // Add the rest when assets exist.
            else -> null
        }
    }
}