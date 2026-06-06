package com.pathstoftech.calmexecution.testing

import androidx.annotation.DrawableRes
import com.pathstoftech.calmexecution.core.data.images.TipImageResolver

class FakeTipImageResolver(
    @param:DrawableRes private val imageResId: Int? = null,
) : TipImageResolver {

    override fun resolve(imageKey: String): Int? =
        imageResId
}