package com.example.a30daysofcalmexecution.di

import com.example.a30daysofcalmexecution.core.data.catalog.CatalogRepository
import com.example.a30daysofcalmexecution.core.data.journey.JourneyRepository
import com.example.a30daysofcalmexecution.core.data.preferences.PreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryGraphProbe @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val journeyRepository: JourneyRepository,
    private val preferencesRepository: PreferencesRepository
) {
    fun verifyGraphIsConstructed() {
        // Intentionally empty.
        // Constructor injection is the verification target for B3-05.
    }
}