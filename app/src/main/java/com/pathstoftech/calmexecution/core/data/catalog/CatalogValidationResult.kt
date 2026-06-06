package com.pathstoftech.calmexecution.core.data.catalog

data class CatalogValidationResult(
    val isValid: Boolean,
    val errors: List<String>
)
