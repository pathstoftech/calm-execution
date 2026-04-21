package com.example.a30daysofcalmexecution.core.model

data class TipImageRef(
    val imageKey: String,
    val contentDescription: String? = null,
    val isDecorative: Boolean = false
) {
    init {
        require(imageKey.isNotBlank()) { "TipImageRef.imageKey must not be blank." }

        if (isDecorative) {
            require(contentDescription.isNullOrBlank()) {
                "Decorative images should not carry a content description."
            }
        } else {
            require(!contentDescription.isNullOrBlank()) {
                "Non-decorative images must have a content description. "
            }
        }

    }
}
