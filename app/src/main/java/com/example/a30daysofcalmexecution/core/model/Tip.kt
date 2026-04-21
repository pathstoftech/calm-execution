package com.example.a30daysofcalmexecution.core.model

data class Tip(
    val id: TipId,
    val dayNumber: Int,
    val sectionKey: SectionKey,
    val categoryKey: TipCategoryKey,
    val title: String,
    val previewText: String,
    val body: TipBody,
    val image: TipImageRef
) {
    init{
        require(dayNumber in 1..30) { "Tip.dayNumber must be between 1 and 30." }
        require(title.isNotBlank()) { "Tip.title must not be blank." }
        require(previewText.isNotBlank()) { "Tip.previewText must not be blank." }
    }
}
