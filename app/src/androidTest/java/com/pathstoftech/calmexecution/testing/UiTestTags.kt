package com.pathstoftech.calmexecution.testing

import com.pathstoftech.calmexecution.core.model.TipId

object UiTestTags {
    const val COMPACT_BACK_ACTION = "compact_back_action"
    const val COMPACT_SETTINGS_ACTION = "compact_settings_action"

    const val TIP_DETAIL_LOADING_STATE = "tip_detail_loading_state"
    const val TIP_DETAIL_ERROR_STATE = "tip_detail_error_state"
    const val TIP_DETAIL_READY_STATE = "tip_detail_ready_state"

    const val EXPANDED_APP_SCAFFOLD = "expanded_app_scaffold"
    const val EXPANDED_JOURNEY_ROUTE = "expanded_journey_route"
    const val EXPANDED_LIST_DETAIL_LAYOUT = "expanded_list_detail_layout"
    const val EXPANDED_LIST_PANE = "expanded_list_pane"
    const val EXPANDED_DETAIL_PANE = "expanded_detail_pane"
    const val EXPANDED_EMPTY_DETAIL_PLACEHOLDER = "expanded_empty_detail_placeholder"

    fun tipCard(
        tipId: TipId,
    ): String =
        "tip_card_${tipId.value}"
}