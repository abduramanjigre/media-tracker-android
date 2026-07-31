# Week 07 Reflection

**Name:** Abdurahman Jigre 
**Date:** 2026-07-09

---

## Commits This Week

<!-- Paste a link to your commits for this week. The easiest way: go to your repo on GitHub,
     click "commits", and copy the URL after filtering by your name or branch. -->

**Link:** https://github.com/abduramanjigre/media-tracker-android/pull/10

## Code Review

<!-- Every week you leave a review on a pod mate's pull request. Fill in both parts below.
     Part 1 is the link — I will verify the review exists on GitHub.
     Part 2 is your written assessment — what you actually looked at and what you found. -->

**Reviewed:** *(Brian Willems)* 
**Link to my review: https://github.com/BrianWill2026/media-tracker-android/pull/8/changes **

### What I Looked At
Brian’s pull request included changes across many files, so I focused mainly on MediaDetailScreen.kt 
and how it matched the Week 8 requirements. I looked at the MediaDetailScreen() function, its mediaId parameter, 
the use of FakeMediaRepository, and the action for adding an item to the library.
### What I Noticed
In MediaDetailScreen(), the screen receives a mediaId, but that value is not currently used to load the selected media item. 
The screen still assigns FakeMediaRepository.sampleMediaDetail to detail, which means every ID would show the same hardcoded item.
### Comments I Left
I left a positive comment because he added a lot of files i did not go through all of them one by one to leave comments. 
## One Thing I Understood More Deeply
This week I understood more clearly why a screen should not make all of its decisions directly inside the composable. 
Looking at MediaDetailScreen() helped me see that the layout itself is mostly complete, but it still needs a ViewModel to load the correct media item and represent loading, error, and success states.
## One Thing I'm Still Confused About
I understand that GET /media/{id} returning a 404 should be treated as an error, while GET /library/{mediaId} returning a 404 means the item has not been added yet. I am still confused about the cleanest way to represent both requests in one UI state.
For example, I am not sure whether the media details and library status should use separate state variables or be combined into one MediaDetailUiState. I also want to better understand how the retry button should repeat only the request that failed.

## Anything Else *(optional)*

<!-- Did you help a pod mate work through something? Did you discover something cool or frustrating?
     Did something from a previous week finally click? This is a good place to put it. -->

---

## Rubric

*You don't need to self-assess — this is here so you know what I'm looking at.*

| Section | Points | Full Credit | Half Credit | No Credit |
|:---|:---:|:---|:---|:---|
| **Reflection** | 10 | Specific, honest responses to "More Deeply" and "Still Confused" sections. Shows genuine thinking — not just "I learned X." | Responses are present but vague or generic ("I got better at Compose"). | Missing or one-word answers. |
| **Code Review** | 10 | Specific observation about the code with explanation of why it matters (or a substantive positive comment). Link to review present and verified. | A question or comment that shows you read the code, but lacks explanation. | "Looks good!" or equivalent. Missing link. Review not found on GitHub. |
| **Total** | **20** | | | |

**A note on the code review score:** I check that the review actually exists on GitHub before grading. The written summary here and the GitHub comment should match. If the review isn't there, the written summary can't earn credit.
