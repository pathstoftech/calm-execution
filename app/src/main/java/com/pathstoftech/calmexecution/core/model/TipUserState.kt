package com.pathstoftech.calmexecution.core.model

data class TipUserState(
    val tipId: TipId,
    val isBookmarked: Boolean = false,
    val completionStatus: TipCompletionStatus = TipCompletionStatus.NOT_STARTED,
    val lastViewedAtEpochMillis: Long? = null,
    val completedAtEpochMillis: Long? = null
) {
    init {
        require(lastViewedAtEpochMillis == null || lastViewedAtEpochMillis > 0L) {
            "TipUserState.lastViewedAtEpochMillis must be a positive number when present"
        }
        require(completedAtEpochMillis == null || completedAtEpochMillis > 0L) {
            "TipUserState.completedAtEpochMillis must be a positive number when present"
        }
        require(
            completedAtEpochMillis == null || completionStatus == TipCompletionStatus.COMPLETED
        ) {
           "TipUserState.completedAtEpochMillis can only be set when " +
                   "TipUserState.completionStatus is COMPLETED."
        }
    }
}
