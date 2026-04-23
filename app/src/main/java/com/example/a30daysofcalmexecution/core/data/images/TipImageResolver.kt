package com.example.a30daysofcalmexecution.core.data.images

interface TipImageResolver {
    fun resolve(imageKey: String): Int?
}