# Runtime Export Set ? 30 Days of Calm Execution

Status: approved export set / runtime assets pending  
Task: D4-02 Finalize runtime export set  
Owner: Content designer  
Reviewer: Product/content reviewer  

## Purpose

This document defines the exact runtime image set that must be exported and added in D4-03.

D4-02 does not add runtime assets. It defines the controlled export target for each asset.

## Source of truth

Runtime export entries are derived from:

- `app/src/main/res/raw/tips_catalog.json`
- `docs/assets/tip_image_briefs.md`

The catalog remains canonical for:

- tip id
- day number
- section key
- category key
- title
- imageKey
- imageContentDescription
- imageDecorative

The runtime export manifest controls:

- runtime filename
- runtime path
- format
- dimensions
- aspect ratio
- file-size budget
- asset status

## Runtime export standard

| Property | Value |
|---|---|
| Runtime format | WebP |
| Runtime size | 1600 ? 900 px |
| Aspect ratio | 16:9 |
| Runtime directory | `app/src/main/res/drawable-nodpi/` |
| Runtime filename rule | `imageKey.webp` |
| Target average file size | 150?350 KB |
| Soft max file size | 500 KB |
| Source/master assets | Outside Android runtime resource tree |
| Runtime integration task | D4-03 |
| Resolver/validation task | D4-04 |

## Export rules

1. Export exactly one runtime image per catalog tip.
2. Runtime filename must equal `imageKey + ".webp"`.
3. Runtime file must be placed in `app/src/main/res/drawable-nodpi/`.
4. Runtime image must be 1600 ? 900 px unless an exception is documented.
5. Runtime image must use WebP unless an exception is documented.
6. The visual subject must remain readable in card and detail contexts.
7. The image must preserve the meaning of `imageContentDescription`.
8. Non-decorative images must not rely on unreadable embedded text.
9. No imageKey changes are allowed in D4-02.
10. No runtime image files are added in D4-02.

## Runtime export table

| Day | imageKey | Tip title | Section | Category | Runtime file | Size | Format | Runtime directory | Content description | Decorative | Status |
|---:|---|---|---|---|---|---|---|---|---|---|---|
| 01 | `tip_01_define_real_priority` | Define the real priority | Start with Clarity | `planning` | `tip_01_define_real_priority.webp` | `1600x900` | `webp` | `drawable-nodpi` | A clean desk with one open notebook page and a single task written at the top. | `false` | Export approved / asset pending |
| 02 | `tip_02_stop_planning_by_panic` | Stop planning by panic | Start with Clarity | `planning` | `tip_02_stop_planning_by_panic.webp` | `1600x900` | `webp` | `drawable-nodpi` | A morning workspace with coffee, a planner, and neatly written daily goals. | `false` | Export approved / asset pending |
| 03 | `tip_03_protect_your_first_work_block` | Protect your first work block | Start with Clarity | `focus` | `tip_03_protect_your_first_work_block.webp` | `1600x900` | `webp` | `drawable-nodpi` | A laptop with notifications muted and one document open in a quiet morning setting. | `false` | Export approved / asset pending |
| 04 | `tip_04_separate_urgent_from_important` | Separate urgent from important | Start with Clarity | `decision_making` | `tip_04_separate_urgent_from_important.webp` | `1600x900` | `webp` | `drawable-nodpi` | Sticky notes or cards arranged into four neat groups on a desk. | `false` | Export approved / asset pending |
| 05 | `tip_05_make_the_next_step_obvious` | Make the next step obvious | Start with Clarity | `execution` | `tip_05_make_the_next_step_obvious.webp` | `1600x900` | `webp` | `drawable-nodpi` | A checklist with one first action highlighted, beside a laptop and pen. | `false` | Export approved / asset pending |
| 06 | `tip_06_clear_your_work_surface` | Clear your work surface | Start with Clarity | `environment` | `tip_06_clear_your_work_surface.webp` | `1600x900` | `webp` | `drawable-nodpi` | A minimalist desk with one active device and one notebook. | `false` | Export approved / asset pending |
| 07 | `tip_07_start_before_you_feel_ready` | Start before you feel ready | Build Focus | `discipline` | `tip_07_start_before_you_feel_ready.webp` | `1600x900` | `webp` | `drawable-nodpi` | A laptop opened to a blank or partially started document, with a timer or notebook beside it. | `false` | Export approved / asset pending |
| 08 | `tip_08_do_one_thing_for_one_block` | Do one thing for one block | Build Focus | `focus` | `tip_08_do_one_thing_for_one_block.webp` | `1600x900` | `webp` | `drawable-nodpi` | A clean desk with one open document, one notebook, and a visible timer. | `false` | Export approved / asset pending |
| 09 | `tip_09_silence_the_invitation_to_react` | Silence the invitation to react | Build Focus | `attention` | `tip_09_silence_the_invitation_to_react.webp` | `1600x900` | `webp` | `drawable-nodpi` | A phone face-down beside a laptop in a calm workspace. | `false` | Export approved / asset pending |
| 10 | `tip_10_make_distraction_slightly_harder` | Make distraction slightly harder | Build Focus | `environment` | `tip_10_make_distraction_slightly_harder.webp` | `1600x900` | `webp` | `drawable-nodpi` | A minimalist desk with a phone placed farther away and a laptop with only one browser tab open. | `false` | Export approved / asset pending |
| 11 | `tip_11_finish_the_current_thought_before_switching` | Finish the current thought before switching | Build Focus | `execution` | `tip_11_finish_the_current_thought_before_switching.webp` | `1600x900` | `webp` | `drawable-nodpi` | An open notebook with a short handwritten next-step note beside an active laptop document. | `false` | Export approved / asset pending |
| 12 | `tip_12_protect_recovery_between_focus_blocks` | Protect recovery between focus blocks | Build Focus | `recovery` | `tip_12_protect_recovery_between_focus_blocks.webp` | `1600x900` | `webp` | `drawable-nodpi` | A calm workspace with a mug, chair pushed back, and a brief pause moment near a window or plant. | `false` | Export approved / asset pending |
| 13 | `tip_13_do_not_answer_everything_immediately` | Do not answer everything immediately | Protect Boundaries | `boundaries` | `tip_13_do_not_answer_everything_immediately.webp` | `1600x900` | `webp` | `drawable-nodpi` | A calm workspace with a laptop partially turned away and a phone set aside in soft daylight. | `false` | Export approved / asset pending |
| 14 | `tip_14_say_yes_more_slowly` | Say yes more slowly | Protect Boundaries | `decision_making` | `tip_14_say_yes_more_slowly.webp` | `1600x900` | `webp` | `drawable-nodpi` | A quiet desk scene with one incoming document placed aside rather than opened immediately. | `false` | Export approved / asset pending |
| 15 | `tip_15_keep_fewer_tasks_in_motion` | Keep fewer tasks in motion | Protect Boundaries | `workload` | `tip_15_keep_fewer_tasks_in_motion.webp` | `1600x900` | `webp` | `drawable-nodpi` | A clean desk with one or two neatly stacked folders in focus and the rest removed from view. | `false` | Export approved / asset pending |
| 16 | `tip_16_protect_your_calendar_from_shallow_clutter` | Protect your calendar from shallow clutter | Protect Boundaries | `time` | `tip_16_protect_your_calendar_from_shallow_clutter.webp` | `1600x900` | `webp` | `drawable-nodpi` | A planner or laptop seen from an editorial angle, implying one clearly reserved time block. | `false` | Export approved / asset pending |
| 17 | `tip_17_make_interruptions_earn_their_place` | Make interruptions earn their place | Protect Boundaries | `attention` | `tip_17_make_interruptions_earn_their_place.webp` | `1600x900` | `webp` | `drawable-nodpi` | A minimal workspace with a secondary device at the edge of the scene, visually present but not central. | `false` | Export approved / asset pending |
| 18 | `tip_18_end_the_workday_with_a_boundary` | End the workday with a boundary | Protect Boundaries | `recovery` | `tip_18_end_the_workday_with_a_boundary.webp` | `1600x900` | `webp` | `drawable-nodpi` | A calm end-of-day workspace with a notebook closed, laptop nearly shut, and evening light. | `false` | Export approved / asset pending |
| 19 | `tip_19_notice_fatigue_before_it_becomes_the_boss` | Notice fatigue before it becomes the boss | Sustain Energy | `awareness` | `tip_19_notice_fatigue_before_it_becomes_the_boss.webp` | `1600x900` | `webp` | `drawable-nodpi` | A quiet workspace with a glass of water, soft daylight, and a chair angled away from the desk, suggesting a pause before continuing. | `false` | Export approved / asset pending |
| 20 | `tip_20_change_your_body_to_reset_your_mind` | Change your body to reset your mind | Sustain Energy | `recovery` | `tip_20_change_your_body_to_reset_your_mind.webp` | `1600x900` | `webp` | `drawable-nodpi` | A calm workspace with the chair pushed back, a mug or water glass on the desk, and open space near a window. | `false` | Export approved / asset pending |
| 21 | `tip_21_protect_lunch_from_becoming_another_task` | Protect lunch from becoming another task | Sustain Energy | `recovery` | `tip_21_protect_lunch_from_becoming_another_task.webp` | `1600x900` | `webp` | `drawable-nodpi` | A simple lunch setting near natural light, separate from the main desk, with a notebook or laptop absent or closed. | `false` | Export approved / asset pending |
| 22 | `tip_22_match_the_task_to_the_energy_you_have` | Match the task to the energy you have | Sustain Energy | `planning` | `tip_22_match_the_task_to_the_energy_you_have.webp` | `1600x900` | `webp` | `drawable-nodpi` | An editorial desk scene with one focused object in the foreground and lighter secondary materials set aside. | `false` | Export approved / asset pending |
| 23 | `tip_23_reduce_the_silent_drains_in_your_workspace` | Reduce the silent drains in your workspace | Sustain Energy | `environment` | `tip_23_reduce_the_silent_drains_in_your_workspace.webp` | `1600x900` | `webp` | `drawable-nodpi` | A refined workspace detail with an adjusted lamp, clean desk edge, aligned chair, or improved light. | `false` | Export approved / asset pending |
| 24 | `tip_24_stop_proving_effort_after_the_useful_part_is_over` | Stop proving effort after the useful part is over | Sustain Energy | `sustainability` | `tip_24_stop_proving_effort_after_the_useful_part_is_over.webp` | `1600x900` | `webp` | `drawable-nodpi` | A calm late-day workspace with softer light, a notebook closing, and one active item gently set aside. | `false` | Export approved / asset pending |
| 25 | `tip_25_define_what_done_for_now_means` | Define what “done for now” means | Finish and Improve | `completion` | `tip_25_define_what_done_for_now_means.webp` | `1600x900` | `webp` | `drawable-nodpi` | A calm editorial workspace with one closed folder or document stack in focus. | `false` | Export approved / asset pending |
| 26 | `tip_26_close_one_loop_before_opening_another` | Close one loop before opening another | Finish and Improve | `execution` | `tip_26_close_one_loop_before_opening_another.webp` | `1600x900` | `webp` | `drawable-nodpi` | A neat desk with one completed stack or closed notebook in the foreground and one new item waiting off to the side. | `false` | Export approved / asset pending |
| 27 | `tip_27_review_what_actually_moved` | Review what actually moved | Finish and Improve | `reflection` | `tip_27_review_what_actually_moved.webp` | `1600x900` | `webp` | `drawable-nodpi` | A refined desk scene with a short reviewed list or a neatly aligned stack of completed pages. | `false` | Export approved / asset pending |
| 28 | `tip_28_learn_from_friction_instead_of_repeating_it` | Learn from friction instead of repeating it | Finish and Improve | `improvement` | `tip_28_learn_from_friction_instead_of_repeating_it.webp` | `1600x900` | `webp` | `drawable-nodpi` | A quiet editorial workspace detail showing one adjusted tool or setup element. | `false` | Export approved / asset pending |
| 29 | `tip_29_prepare_the_next_start_before_you_stop` | Prepare the next start before you stop | Finish and Improve | `continuity` | `tip_29_prepare_the_next_start_before_you_stop.webp` | `1600x900` | `webp` | `drawable-nodpi` | A calm end-of-day desk with one item intentionally left ready in the foreground. | `false` | Export approved / asset pending |
| 30 | `tip_30_improve_the_system_not_just_the_effort` | Improve the system, not just the effort | Finish and Improve | `growth` | `tip_30_improve_the_system_not_just_the_effort.webp` | `1600x900` | `webp` | `drawable-nodpi` | A calm, slightly elevated editorial scene suggesting review and integration with a closed notebook and neatly stacked papers. | `false` | Export approved / asset pending |

## Review checklist before D4-03

Before adding runtime assets, confirm:

- every source image is available
- every source image matches its approved brief
- every export is 1600 ? 900 px
- every export is WebP
- every runtime filename matches `imageKey.webp`
- every file is under the soft 500 KB cap or has a documented exception
- every image remains readable at card size
- every image works as a detail hero image
- no image contradicts catalog text
- no important meaning exists only as tiny baked-in text

## Change-control rule

Changing compression or crop is an asset revision.  
Changing visual meaning requires content review.  
Changing `imageKey` requires catalog review.  
Adding or removing runtime images requires validation updates.
