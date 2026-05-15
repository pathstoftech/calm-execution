# Tip Image Briefs ? 30 Days of Calm Execution

Status: approved for runtime asset production  
Task: D4-01 Finalize image briefs  
Owner: Content designer  
Reviewer: Product/content reviewer  

## Purpose

This document is the controlled source of truth for image production review.

Each tip has one canonical image brief. The image must support both:

- Home editorial card top image
- Detail screen hero image

Runtime asset integration is not part of D4-01. Runtime export naming, WebP conversion, drawable placement, resolver mapping, and validation belong to later D4 tasks.

## Source of truth

Per-tip fields in this document are derived from:

`app/src/main/res/raw/tips_catalog.json`

The catalog remains canonical for:

- day number
- section key
- category key
- tip title
- imageKey
- imageContentDescription
- imageDecorative

This brief document adds review status, global art direction, production guidance, and approval control.

If the catalog and this document disagree, check the catalog first and resolve the mismatch deliberately.

## Global art direction

Images should feel:

- calm
- editorial
- modern
- structured
- focused
- slightly warm but not noisy
- productivity-oriented without hustle-culture energy

Preferred visual language:

- quiet workspaces
- notebooks, planners, laptops, cards, folders, timers, desk objects
- soft daylight or controlled warm light
- restrained composition
- clean negative space
- readable subject at card size
- consistent contrast and crop strength

Avoid:

- frantic office scenes
- exaggerated emotions
- cluttered stock-photo chaos
- motivational poster style
- fake UI text that carries essential meaning
- unreadable tiny typography
- aggressive neon colors
- images that rely on people?s faces for meaning
- inconsistent illustration/photo style across the set

## Runtime assumptions for later D4 tasks

- One approved image per tip
- Runtime format: WebP
- Default aspect ratio: 16:9
- Runtime location: `app/src/main/res/drawable-nodpi/`
- Runtime file name should match `imageKey`
- Catalog `imageKey` remains stable unless the image meaning changes
- Non-decorative images must keep meaningful content descriptions
- Master/source assets stay outside the Android runtime resource tree

## Brief table

| Day | imageKey | Tip title | Section | Category | Catalog image description / approved brief | imageDecorative | Status |
|---:|---|---|---|---|---|---|---|
| 01 | `tip_01_define_real_priority` | Define the real priority | Start with Clarity | `planning` | A clean desk with one open notebook page and a single task written at the top. | `false` | Approved brief |
| 02 | `tip_02_stop_planning_by_panic` | Stop planning by panic | Start with Clarity | `planning` | A morning workspace with coffee, a planner, and neatly written daily goals. | `false` | Approved brief |
| 03 | `tip_03_protect_your_first_work_block` | Protect your first work block | Start with Clarity | `focus` | A laptop with notifications muted and one document open in a quiet morning setting. | `false` | Approved brief |
| 04 | `tip_04_separate_urgent_from_important` | Separate urgent from important | Start with Clarity | `decision_making` | Sticky notes or cards arranged into four neat groups on a desk. | `false` | Approved brief |
| 05 | `tip_05_make_the_next_step_obvious` | Make the next step obvious | Start with Clarity | `execution` | A checklist with one first action highlighted, beside a laptop and pen. | `false` | Approved brief |
| 06 | `tip_06_clear_your_work_surface` | Clear your work surface | Start with Clarity | `environment` | A minimalist desk with one active device and one notebook. | `false` | Approved brief |
| 07 | `tip_07_start_before_you_feel_ready` | Start before you feel ready | Build Focus | `discipline` | A laptop opened to a blank or partially started document, with a timer or notebook beside it. | `false` | Approved brief |
| 08 | `tip_08_do_one_thing_for_one_block` | Do one thing for one block | Build Focus | `focus` | A clean desk with one open document, one notebook, and a visible timer. | `false` | Approved brief |
| 09 | `tip_09_silence_the_invitation_to_react` | Silence the invitation to react | Build Focus | `attention` | A phone face-down beside a laptop in a calm workspace. | `false` | Approved brief |
| 10 | `tip_10_make_distraction_slightly_harder` | Make distraction slightly harder | Build Focus | `environment` | A minimalist desk with a phone placed farther away and a laptop with only one browser tab open. | `false` | Approved brief |
| 11 | `tip_11_finish_the_current_thought_before_switching` | Finish the current thought before switching | Build Focus | `execution` | An open notebook with a short handwritten next-step note beside an active laptop document. | `false` | Approved brief |
| 12 | `tip_12_protect_recovery_between_focus_blocks` | Protect recovery between focus blocks | Build Focus | `recovery` | A calm workspace with a mug, chair pushed back, and a brief pause moment near a window or plant. | `false` | Approved brief |
| 13 | `tip_13_do_not_answer_everything_immediately` | Do not answer everything immediately | Protect Boundaries | `boundaries` | A calm workspace with a laptop partially turned away and a phone set aside in soft daylight. | `false` | Approved brief |
| 14 | `tip_14_say_yes_more_slowly` | Say yes more slowly | Protect Boundaries | `decision_making` | A quiet desk scene with one incoming document placed aside rather than opened immediately. | `false` | Approved brief |
| 15 | `tip_15_keep_fewer_tasks_in_motion` | Keep fewer tasks in motion | Protect Boundaries | `workload` | A clean desk with one or two neatly stacked folders in focus and the rest removed from view. | `false` | Approved brief |
| 16 | `tip_16_protect_your_calendar_from_shallow_clutter` | Protect your calendar from shallow clutter | Protect Boundaries | `time` | A planner or laptop seen from an editorial angle, implying one clearly reserved time block. | `false` | Approved brief |
| 17 | `tip_17_make_interruptions_earn_their_place` | Make interruptions earn their place | Protect Boundaries | `attention` | A minimal workspace with a secondary device at the edge of the scene, visually present but not central. | `false` | Approved brief |
| 18 | `tip_18_end_the_workday_with_a_boundary` | End the workday with a boundary | Protect Boundaries | `recovery` | A calm end-of-day workspace with a notebook closed, laptop nearly shut, and evening light. | `false` | Approved brief |
| 19 | `tip_19_notice_fatigue_before_it_becomes_the_boss` | Notice fatigue before it becomes the boss | Sustain Energy | `awareness` | A quiet workspace with a glass of water, soft daylight, and a chair angled away from the desk, suggesting a pause before continuing. | `false` | Approved brief |
| 20 | `tip_20_change_your_body_to_reset_your_mind` | Change your body to reset your mind | Sustain Energy | `recovery` | A calm workspace with the chair pushed back, a mug or water glass on the desk, and open space near a window. | `false` | Approved brief |
| 21 | `tip_21_protect_lunch_from_becoming_another_task` | Protect lunch from becoming another task | Sustain Energy | `recovery` | A simple lunch setting near natural light, separate from the main desk, with a notebook or laptop absent or closed. | `false` | Approved brief |
| 22 | `tip_22_match_the_task_to_the_energy_you_have` | Match the task to the energy you have | Sustain Energy | `planning` | An editorial desk scene with one focused object in the foreground and lighter secondary materials set aside. | `false` | Approved brief |
| 23 | `tip_23_reduce_the_silent_drains_in_your_workspace` | Reduce the silent drains in your workspace | Sustain Energy | `environment` | A refined workspace detail with an adjusted lamp, clean desk edge, aligned chair, or improved light. | `false` | Approved brief |
| 24 | `tip_24_stop_proving_effort_after_the_useful_part_is_over` | Stop proving effort after the useful part is over | Sustain Energy | `sustainability` | A calm late-day workspace with softer light, a notebook closing, and one active item gently set aside. | `false` | Approved brief |
| 25 | `tip_25_define_what_done_for_now_means` | Define what “done for now” means | Finish and Improve | `completion` | A calm editorial workspace with one closed folder or document stack in focus. | `false` | Approved brief |
| 26 | `tip_26_close_one_loop_before_opening_another` | Close one loop before opening another | Finish and Improve | `execution` | A neat desk with one completed stack or closed notebook in the foreground and one new item waiting off to the side. | `false` | Approved brief |
| 27 | `tip_27_review_what_actually_moved` | Review what actually moved | Finish and Improve | `reflection` | A refined desk scene with a short reviewed list or a neatly aligned stack of completed pages. | `false` | Approved brief |
| 28 | `tip_28_learn_from_friction_instead_of_repeating_it` | Learn from friction instead of repeating it | Finish and Improve | `improvement` | A quiet editorial workspace detail showing one adjusted tool or setup element. | `false` | Approved brief |
| 29 | `tip_29_prepare_the_next_start_before_you_stop` | Prepare the next start before you stop | Finish and Improve | `continuity` | A calm end-of-day desk with one item intentionally left ready in the foreground. | `false` | Approved brief |
| 30 | `tip_30_improve_the_system_not_just_the_effort` | Improve the system, not just the effort | Finish and Improve | `growth` | A calm, slightly elevated editorial scene suggesting review and integration with a closed notebook and neatly stacked papers. | `false` | Approved brief |

## Review checklist for each generated/exported image

Editorial quality:

- Matches the tip?s actual meaning
- Matches Calm Execution tone
- Not generic in a way that weakens the tip
- Not emotionally noisy
- Does not contradict the catalog text

Visual quality:

- Subject is readable at card size
- Crop remains strong in detail view
- Works as a 16:9 editorial image
- No obvious compression artifacts
- Consistent contrast and color temperature across the set

Accessibility quality:

- Content description remains valid
- No essential meaning is only inside unreadable text
- Image is meaningful, not decorative, unless catalog explicitly marks it decorative

Technical quality for later D4 tasks:

- Runtime filename matches `imageKey`
- Runtime format is WebP unless explicitly waived
- Runtime asset goes in `drawable-nodpi`
- Export size stays within project budget
- Resolver mapping is added
- Catalog validation passes

## Change-control rule

Asset changes are controlled changes.

Changing crop, compression, or export quality is an asset revision. Changing the visual meaning may require updating the brief and review status. Changing `imageKey` requires deliberate catalog review.
