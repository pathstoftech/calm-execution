package com.pathstoftech.calmexecution.core.model

enum class TipCategoryKey(
    val wireValue: String,
    val label: String
) {
    AWARENESS(
        wireValue = "awareness",
        label = "Awareness"
    ),
    COMPLETION(
        wireValue = "completion",
        label = "Completion"
    ),
    DECISION_MAKING(
        wireValue = "decision_making",
        label = "Decision-making"
    ),
    ENVIRONMENT(
        wireValue = "environment",
        label = "Environment"
    ),
    EXECUTION(
        wireValue = "execution",
        label = "Execution"
    ),
    FOCUS(
        wireValue = "focus",
        label = "Focus"
    ),
    PLANNING(
        wireValue = "planning",
        label = "Planning"
    ),
    RECOVERY(
        wireValue = "recovery",
        label = "Recovery"
    ),
    BOUNDARIES(
        wireValue = "boundaries",
        label = "Boundaries"
    ),
    ATTENTION(
        wireValue = "attention",
        label = "Attention"
    ),
    WORKLOAD(
        wireValue = "workload",
        label = "Workload"
    ),
    TIME(
        wireValue = "time",
        label = "Time"
    ),
    SUSTAINABILITY(
        wireValue = "sustainability",
        label = "Sustainability"
    ),
    REFLECTION(
        wireValue = "reflection",
        label = "Reflection"
    ),
    IMPROVEMENT(
        wireValue = "improvement",
        label = "Improvement"
    ),
    CONTINUITY(
        wireValue = "continuity",
        label = "Continuity"
    ),
    GROWTH(
        wireValue = "growth",
        label = "Growth"
    ),
    DISCIPLINE(
        wireValue = "discipline",
        label = "Discipline"
    );

    companion object {
        fun fromWireValue(value: String): TipCategoryKey? =
            entries.firstOrNull { it.wireValue == value }
    }
}