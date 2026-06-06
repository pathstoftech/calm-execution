package com.pathstoftech.calmexecution.core.data.images

import com.pathstoftech.calmexecution.R
import javax.inject.Inject

class DrawableTipImageResolver @Inject constructor() : TipImageResolver {
    private val mapping: Map<String, Int> =
        mapOf(
            "tip_01_define_real_priority" to R.drawable.tip_01_define_real_priority,
            "tip_02_stop_planning_by_panic" to R.drawable.tip_02_stop_planning_by_panic,
            "tip_03_protect_your_first_work_block" to R.drawable.tip_03_protect_your_first_work_block,
            "tip_04_separate_urgent_from_important" to R.drawable.tip_04_separate_urgent_from_important,
            "tip_05_make_the_next_step_obvious" to R.drawable.tip_05_make_the_next_step_obvious,
            "tip_06_clear_your_work_surface" to R.drawable.tip_06_clear_your_work_surface,
            "tip_07_start_before_you_feel_ready" to R.drawable.tip_07_start_before_you_feel_ready,
            "tip_08_do_one_thing_for_one_block" to R.drawable.tip_08_do_one_thing_for_one_block,
            "tip_09_silence_the_invitation_to_react" to R.drawable.tip_09_silence_the_invitation_to_react,
            "tip_10_make_distraction_slightly_harder" to R.drawable.tip_10_make_distraction_slightly_harder,
            "tip_11_finish_the_current_thought_before_switching" to R.drawable.tip_11_finish_the_current_thought_before_switching,
            "tip_12_protect_recovery_between_focus_blocks" to R.drawable.tip_12_protect_recovery_between_focus_blocks,
            "tip_13_do_not_answer_everything_immediately" to R.drawable.tip_13_do_not_answer_everything_immediately,
            "tip_14_say_yes_more_slowly" to R.drawable.tip_14_say_yes_more_slowly,
            "tip_15_keep_fewer_tasks_in_motion" to R.drawable.tip_15_keep_fewer_tasks_in_motion,
            "tip_16_protect_your_calendar_from_shallow_clutter" to R.drawable.tip_16_protect_your_calendar_from_shallow_clutter,
            "tip_17_make_interruptions_earn_their_place" to R.drawable.tip_17_make_interruptions_earn_their_place,
            "tip_18_end_the_workday_with_a_boundary" to R.drawable.tip_18_end_the_workday_with_a_boundary,
            "tip_19_notice_fatigue_before_it_becomes_the_boss" to R.drawable.tip_19_notice_fatigue_before_it_becomes_the_boss,
            "tip_20_change_your_body_to_reset_your_mind" to R.drawable.tip_20_change_your_body_to_reset_your_mind,
            "tip_21_protect_lunch_from_becoming_another_task" to R.drawable.tip_21_protect_lunch_from_becoming_another_task,
            "tip_22_match_the_task_to_the_energy_you_have" to R.drawable.tip_22_match_the_task_to_the_energy_you_have,
            "tip_23_reduce_the_silent_drains_in_your_workspace" to R.drawable.tip_23_reduce_the_silent_drains_in_your_workspace,
            "tip_24_stop_proving_effort_after_the_useful_part_is_over" to R.drawable.tip_24_stop_proving_effort_after_the_useful_part_is_over,
            "tip_25_define_what_done_for_now_means" to R.drawable.tip_25_define_what_done_for_now_means,
            "tip_26_close_one_loop_before_opening_another" to R.drawable.tip_26_close_one_loop_before_opening_another,
            "tip_27_review_what_actually_moved" to R.drawable.tip_27_review_what_actually_moved,
            "tip_28_learn_from_friction_instead_of_repeating_it" to R.drawable.tip_28_learn_from_friction_instead_of_repeating_it,
            "tip_29_prepare_the_next_start_before_you_stop" to R.drawable.tip_29_prepare_the_next_start_before_you_stop,
            "tip_30_improve_the_system_not_just_the_effort" to R.drawable.tip_30_improve_the_system_not_just_the_effort,
        )

    override fun resolve(imageKey: String): Int? =
        mapping[imageKey]

    fun knownImageKeys(): Set<String> =
        mapping.keys
}