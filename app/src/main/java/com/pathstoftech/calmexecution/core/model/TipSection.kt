package com.pathstoftech.calmexecution.core.model

data class TipSection(
    val key: SectionKey,
    val title: String,
    val subtitle: String? = null,
    val startDay: Int,
    val endDay: Int,
    val tips: List<Tip>
) {
    init {
        require(title.isNotBlank()) { "TipSection.title must not be blank." }
        require(startDay in 1..30) { "TipSection.startDay must be between 1 and 30." }
        require(endDay in 1..30) { "TipSection.endDay must be between 1 and 30." }
        require(startDay <= endDay) { "TipSection.startDay must be <= endDay." }
        require(tips.isNotEmpty()) { "TipSection.tips must not be empty." }
    }
}