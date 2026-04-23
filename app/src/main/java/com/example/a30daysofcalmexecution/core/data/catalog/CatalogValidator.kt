package com.example.a30daysofcalmexecution.core.data.catalog

import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.TipCategoryKey
import javax.inject.Inject

class CatalogValidator @Inject constructor() {

    fun validate(
        dto: CatalogDto,
        imageKeyExists: ((String) -> Boolean)? = null
    ): CatalogValidationResult {
        val errors = mutableListOf<String>()

        // Top-level catalog shape
        if (dto.title.isBlank()) {
            errors += ("Catalog title must not be blank.")
        }

        if (dto.sections.isEmpty()) {
            errors += ("Catalog must contain at least one section.")
        }

        if (dto.tips.size != 30) {
            errors += ("Catalog must contain exactly 30 tips, but found ${dto.tips.size}.")
        }

        // Unique IDs
        val duplicateIds = dto.tips
            .groupBy { it.id }
            .filter { (_, tips) -> tips.size > 1 }
            .keys

        duplicateIds.forEach { id ->
            errors += "Duplicate tip id: $id"
        }

        // Unique day numbers
        val duplicateDayNumbers = dto.tips
            .groupBy { it.dayNumber }
            .filter { (_, tips) -> tips.size > 1 }
            .keys
            .sorted()

        duplicateDayNumbers.forEach { dayNumber ->
            errors += "Duplicate dayNumber: $dayNumber"
        }

        // Day numbers 1..30
        dto.tips.forEach { tip ->
            if (tip.dayNumber !in 1..30) {
                errors += "Tip ${tip.id} has invalid dayNumber: ${tip.dayNumber}; expected 1..30."
            }
        }

        // Valid section keys in section definitions
        dto.sections.forEach { section ->
            if (SectionKey.fromWireValue(section.key) == null) {
                errors += "Unknown section key in sections list: ${section.key}."
            }
        }

        // Valid section/category keys and non-blank editorial fields
        dto.tips.forEach { tip ->
            if (SectionKey.fromWireValue(tip.section) == null) {
                errors += "Tip ${tip.id} uses unknown section key: ${tip.section}."
            }

            if (TipCategoryKey.fromWireValue(tip.category) == null) {
                errors += "Tip ${tip.id} uses unknown category key: ${tip.category}."
            }

            if (tip.id.isBlank()) {
                errors += "A tip has a blank id."
            }

            if (tip.title.isBlank()) {
                errors += "Tip ${tip.id} has a blank title."
            }

            if (tip.previewText.isBlank()) {
                errors += "Tip ${tip.id} has a blank previewText."
            }

            if (tip.problem.isBlank()) {
                errors += "Tip ${tip.id} has a blank problem."
            }

            if (tip.tip.isBlank()) {
                errors += "Tip ${tip.id} has a blank tip."
            }

            if (tip.whyItHelps.isBlank()) {
                errors += "Tip ${tip.id} has a blank whyItHelps."
            }

            if (tip.tryToday.isBlank()) {
                errors += "Tip ${tip.id} has a blank tryToday."
            }

            if (tip.imageKey.isBlank()) {
                errors += "Tip ${tip.id} has a blank imageKey."
            }

            if (imageKeyExists != null && !imageKeyExists(tip.imageKey)) {
                errors += "Tip ${tip.id} references unresolved imageKey: ${tip.imageKey}."
            }

            if (tip.imageDecorative) {
                if (!tip.imageContentDescription.isNullOrBlank()) {
                    errors += "Tip ${tip.id} is decorative but still provides imageContentDescription."
                }
            } else {
                if (tip.imageContentDescription.isNullOrBlank()) {
                    errors += "Tip ${tip.id} is non-decorative and must provide imageContentDescription."
                }
            }
        }

        // Section ranges must match actual contained tips
        val tipsBySection = dto.tips.groupBy { it.section }

        dto.sections.forEach { section ->
            val sectionTips = tipsBySection[section.key].orEmpty()
            val actualDays = sectionTips.map { it.dayNumber }.sorted()

            if (sectionTips.isEmpty()) {
                errors += "Section ${section.key} contains no tips."
            } else {
                val expectedDays = (section.startDay..section.endDay).toList()
                if (actualDays != expectedDays) {
                    errors += "Section ${section.key} day range mismatch. Expected $expectedDays, found $actualDays"
                }
            }

            if (section.startDay > section.endDay) {
                errors += "Section ${section.key} has invalid range ${section.startDay}..${section.endDay}"
            }
        }

        return CatalogValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
}