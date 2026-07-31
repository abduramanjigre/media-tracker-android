# Week 11 Reflection — Bonus Feature Sprint (Week 1 of 2)

*This week's reflection is different from the standard template. We're not doing Profile this week — instead, this is the first of two weeks building your assigned bonus feature (Write Review, Quotes, or Priorities). See `reflection-instructions.md` for naming/submission rules, which are unchanged; only the content below differs.*

**Name:Abdurahman Jigre**
**Date: 07-30-2026**
**My assigned bonus feature:**Priorities*(Write Review / Quotes / Priorities)*

---

## Commits This Week

<!-- Paste a link to your commits for this week. -->

**Link: https://github.com/abduramanjigre/media-tracker-android/pull/12 **

---

## Code Review

<!-- Code review continues as normal — same pod rotation, regardless of which bonus feature you or your pod mate are building. -->

**Reviewed:** *(Brian Willems)*
**Link to my review: https://github.com/BrianWill2026/media-tracker-android/pull/10**

### What I Looked At
I reviewed WriteReviewScreen.kt and WriteReviewViewModel.kt. I focused on how the screen collects media, rating, reviewText, and isSubmitting from the ViewModel. 
I also looked at loadMedia(), onRatingChange(), onReviewTextChange(), and submitReview().
### What I Noticed
One thing I noticed was that submitReview() sets _isSubmitting to true and then immediately calls onSuccess(). 
It does not send the rating or review text to a repository or API, and it does not reset _isSubmitting.
This matters because the screen navigates back as if the review was saved even though no save request was made. 
It would also be difficult to show an error if the request failed. I think the function should perform the request inside viewModelScope, 
call onSuccess() only after a successful response, and reset the submitting state when the request finishes.
### Comments I Left

I left a comment that several labels in WriteReviewScreen.kt are hardcoded instead of using stringResource, which is inconsistent with the other text on the screen.

## Bonus Feature Progress

<!-- This is the most important section this week. Be concrete: which endpoint(s) did you wire?
     What's actually showing on screen with real data? What's still stubbed or fake?
     "I worked on my bonus feature" is not an answer. "I got POST /quotes working from Media Detail
     and quotes show up in a list on my profile, but I haven't wired edit or delete yet" is. -->

**What's working:**
- I have wired `GET /priorities` and `PUT /priorities` (via sequential updates) to the `DefaultMediaRepository` to manage the top 5 list.
- The dedicated Priorities screen is fully implemented with real data, including a custom design with urgency-colored chips (High, Medium, Low) and specialized media cards matching the required aesthetic.
- "Want To" items can be marked as priorities through a new dialog that allows selecting an urgency level (1-3).
- Client-side enforcement of the 5-item maximum is working within the ViewModel.
- Optimistic UI updates ensure adding and removing items feels instant, with proper rollback logic on network failure.

**What's still stubbed, fake, or not started:**
- Drag-to-reorder gesture logic (the handles are visible in the UI, but the logic to update `orderIndex` on drop isn't wired yet).
- Filtering logic for the urgency chips on the Priorities screen.
- UI inputs for `notes` and `estimatedTimeHours` have not been added to the "Set Priority" dialog yet.

**What I'm blocked on, if anything:**
None currently;

## One Thing I Understood More Deeply

I understood more deeply the importance of structured concurrency and error handling with Kotlin Coroutines. While fetching media details, 
I learned that an unhandled exception in one `async` block can cancel the entire parent scope. 
I had to implement a more robust loading pattern to ensure that if a secondary request (like fetching library status) fails, it doesn't prevent the main media details from loading or crash the app.

## One Thing I'm Still Confused About

I'm still looking for the most "Compose-native" way to handle drag-to-reorder for a `LazyColumn`. 
I'm curious about how to efficiently synchronize the temporary visual state during a drag with the underlying `orderIndex` data without triggering excessive recompositions or jumpy UI.

## Anything Else *(optional)*

---

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Concrete progress report (what's wired, what's not) plus specific, honest "Understood More Deeply" and "Still Confused" sections. | Present but vague — "I worked on my feature" with no specifics on what's actually working. | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match.
