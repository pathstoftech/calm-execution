package com.pathstoftech.calmexecution.core.data.images

interface TipImageResolver {
    fun resolve(imageKey: String): Int?
}