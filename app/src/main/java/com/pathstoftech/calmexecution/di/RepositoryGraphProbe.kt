package com.pathstoftech.calmexecution.di

import com.pathstoftech.calmexecution.core.data.catalog.CatalogRepository
import com.pathstoftech.calmexecution.core.data.journey.JourneyRepository
import com.pathstoftech.calmexecution.core.data.preferences.PreferencesRepository
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