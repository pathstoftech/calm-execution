package com.example.a30daysofcalmexecution.core.model

data class TipBody(
    val problem: String,
    val tip: String,
    val whyItHelps: String,
    val tryToday: String
) {
    init {
        require(problem.isNotBlank()) { "TipBody.problem must not be blank." }
        require(tip.isNotBlank()) { "TipBody.tip must not be blank." }
        require(whyItHelps.isNotBlank()) { "TipBody.whyItHelps must not be blank" }
        require(tryToday.isNotBlank()) { "TipBody.tryToday must not be blank." }
    }
}
