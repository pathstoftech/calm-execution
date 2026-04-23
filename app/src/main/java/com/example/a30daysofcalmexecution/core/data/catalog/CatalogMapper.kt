package com.example.a30daysofcalmexecution.core.data.catalog

import com.example.a30daysofcalmexecution.core.model.JourneyCatalog
import com.example.a30daysofcalmexecution.core.model.SectionKey
import com.example.a30daysofcalmexecution.core.model.Tip
import com.example.a30daysofcalmexecution.core.model.TipBody
import com.example.a30daysofcalmexecution.core.model.TipCategoryKey
import com.example.a30daysofcalmexecution.core.model.TipId
import com.example.a30daysofcalmexecution.core.model.TipImageRef
import com.example.a30daysofcalmexecution.core.model.TipSection
import javax.inject.Inject

class CatalogMapper @Inject constructor() {
    fun map(dto: CatalogDto): JourneyCatalog {
        val tipsBySection: Map<String, List<Tip>> = dto.tips
            .sortedBy { it.dayNumber }
            .map(::mapTip)
            .groupBy { it.sectionKey.wireValue }

        val sections: List<TipSection> = dto.sections.map {sectionDto ->
            val sectionKey = requireNotNull(SectionKey.fromWireValue(sectionDto.key)) {
                "Unknown section key: ${sectionDto.key}"
            }

            val sectionTips = tipsBySection[sectionDto.key].orEmpty().sortedBy { it.dayNumber }

            TipSection(
                key = sectionKey,
                title = sectionDto.title,
                tips = sectionTips
            )
        }

        return JourneyCatalog(
            title = dto.title,
            subtitle = dto.subtitle,
            sections = sections
        )
    }
    fun mapTip(dto: TipDto): Tip {
        val sectionKey = requireNotNull(SectionKey.fromWireValue(dto.section)) {
            "Unknown section key: ${dto.section}"
        }

        val categoryKey = requireNotNull(TipCategoryKey.fromWireValue(dto.category)) {
            "Unknown category key: ${dto.category}"
        }

        return Tip(
            id = TipId(dto.id),
            dayNumber = dto.dayNumber,
            sectionKey = sectionKey,
            categoryKey = categoryKey,
            title = dto.title,
            previewText = dto.previewText,
            body = TipBody(
                problem = dto.problem,
                tip = dto.tip,
                whyItHelps = dto.whyItHelps,
                tryToday = dto.tryToday
            ),
            image = TipImageRef(
                imageKey = dto.imageKey,
                contentDescription = dto.imageContentDescription,
                isDecorative = dto.imageDecorative
            )
        )
    }
}