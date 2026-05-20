package com.example.a30daysofcalmexecution.testing

import androidx.annotation.DrawableRes
import com.example.a30daysofcalmexecution.core.data.images.TipImageResolver

class FakeTipImageResolver(
    @param:DrawableRes private val imageResId: Int? = null,
) : TipImageResolver {

    override fun resolve(imageKey: String): Int? =
        imageResId
}