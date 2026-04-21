package com.example.a30daysofcalmexecution.core.model

data class TipSection(
    val key: SectionKey,
    val title: String,
    val tips: List<Tip>
) {
    init {
        require(title.isNotBlank()) { "TipSection.title must not be blank." }
        require(tips.isNotEmpty()) { "TipSection.tips must not be empty." }
    }
}
