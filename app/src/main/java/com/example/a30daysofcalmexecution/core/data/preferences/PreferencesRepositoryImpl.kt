package com.example.a30daysofcalmexecution.core.data.preferences

import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.ThemeMode
import com.example.a30daysofcalmexecution.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataSource: PreferencesDataSource,
    private val mapper: PreferencesMapper
) : PreferencesRepository {

    override fun observePreferences(): Flow<UserPreferences> {
        return dataSource.userPreferences
            .map(mapper::toDomain)
            .distinctUntilChanged()
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        updateDomainPreferences { current ->
            current.copy(
                themeMode = themeMode
            )
        }
    }

    override suspend fun setDynamicColorEnabled(enabled: Boolean) {
        updateDomainPreferences { current ->
            current.copy(
                dynamicColorEnabled = enabled
            )
        }
    }

    override suspend fun setReducedMotionEnabled(enabled: Boolean) {
        updateDomainPreferences { current ->
            current.copy(
                reducedMotionEnabled = enabled
            )
        }
    }

    override suspend fun setLastSelectedSection(sectionKey: SectionKey?) {
        updateDomainPreferences { current ->
            current.copy(
                lastSelectedSectionKey = sectionKey
            )
        }
    }

    override suspend fun setHasSeenIntro(seen: Boolean) {
        updateDomainPreferences { current ->
            current.copy(
                hasSeenIntro = seen
            )
        }
    }

    private suspend fun updateDomainPreferences(
        transform: suspend (UserPreferences) -> UserPreferences
    ): UserPreferences {
        val updatedProto = dataSource.updateUserPreferences { currentProto ->
            val currentDomain = mapper.toDomain(currentProto)
            val updatedDomain = transform(currentDomain)

            mapper.toProto(updatedDomain)
        }

        return mapper.toDomain(updatedProto)
    }

}