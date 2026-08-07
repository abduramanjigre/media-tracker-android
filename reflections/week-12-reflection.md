# Week 12 Reflection — Bonus Feature Sprint (Week 2 of 2, Final)

*Second and last week of bonus feature work. Week 13 has no build time — this is the last chance to get your feature demo-ready before Week 14. This template replaces the standard weekly reflection, same as last week.*

**Name:Abdurahman Jigre**
**Date:08/06/2026**
**My assigned bonus feature:** *(Priorities)*

---

## Commits This Week

**Link: https://github.com/abduramanjigre/media-tracker-android/pull/13/changes**

---

## Code Review

**Reviewed:** *(Brian Willems)*
**Link to my review: https://github.com/BrianWill2026/media-tracker-android/pull/11/changes **

### What I Looked At
I focused mainly on LibraryViewModel.kt and LibraryViewModelTest.kt. I looked at the new LibraryUiState, the loadLibrary() function, and especially the removeItem() and updateStatus() functions.
### What I Noticed
I also noticed one possible issue with the rollback. The code restores the item using latest.items + backup, which would put the item at the end of the list. If it originally appeared somewhere else, 
the list order could change after a failed request. Saving the original index could allow it to be restored to the same position.
### Comments I Left

---I also asked whether the rollback should remember the item’s original position instead of adding the backup item to the end of the list.

## Bonus Feature — Final Status

<!-- Be concrete and honest. This is your last chance to flag something before demos.
     What does your feature actually do, end to end, right now? What's polished vs. rough?
     Is there anything you know is broken or half-done that you want on my radar before Week 14? -->

**What works end-to-end, right now:**
Users can add any "Want To" item to a list of up to 5 priorities. The drag-and-drop reordering however they like. 
**Tests written for this feature:**
I have LibraryViewModelTest.kt which includes 5 passing tests covering the logic for adding, reordering, and removing priorities, as well as ensuring the library and priorities lists stay in sync.
**Known gaps or rough edges going into demos:**
I would like to make the drag and drop to be more visual and exiciting like in a real professional app 
---

## One Thing I Understood More Deeply

---  This week was a huge lesson in how a backend might return data in one format (camelCase) but require updates in another (snake_case). 
Debugging the "missing field" errors taught me exactly how kotlinx.serialization handles these mappings.

## One Thing I'm Still Confused About

--- I'm still a bit confused about the most efficient way to handle "Optimistic Updates" for a list that can be reordered.

## Anything Else *(optional)*

<!-- Anything about the bonus feature sprint as a whole — the two-week format, being assigned a
     feature rather than choosing it, whatever's on your mind — is fair game here. -->

---

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Honest final-status report — what works end-to-end, what's rough, what's tested — plus a specific, genuine "Understood More Deeply" that reflects on the sprint as a whole, not just this week. | Present but vague, or only reports on this week rather than the feature's overall state. | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** same as every other week — I check the link before grading.
