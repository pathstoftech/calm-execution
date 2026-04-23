package com.example.a30daysofcalmexecution.core.data.catalog

data class CatalogValidationResult(
    val isValid: Boolean,
    val errors: List<String>
)
