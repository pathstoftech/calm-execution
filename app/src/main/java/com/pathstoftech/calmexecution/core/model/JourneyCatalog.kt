package com.pathstoftech.calmexecution.core.model

data class JourneyCatalog(
    val title: String,
    val subtitle: String? = null,
    val sections: List<TipSection>
) {
    init {
        require(title.isNotBlank()) { "JourneyCatalog.title must not be blank." }
        require(sections.isNotEmpty()) { "JourneyCatalog.sections must not be empty." }

        val allTipIds = sections.flatMap { section -> section.tips.map { it.id } }
        require(allTipIds.distinct().size == allTipIds.size) {
            "JourneyCatalog.sections must not contain duplicate TipIds."
        }
    }
    val allTips: List<Tip>
        get() = sections.flatMap { it.tips }
}