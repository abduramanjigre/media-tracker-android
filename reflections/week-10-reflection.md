# Week 10 Reflection

**Name:** Abdurahman Jigre 
**Date:** 2026-07-23

---

## Commits This Week

<!-- Paste a link to your commits for this week. The easiest way: go to your repo on GitHub,
     click "commits", and copy the URL after filtering by your name or branch. -->

**Link:** https://github.com/abduramanjigre/media-tracker-android/pull/11

## Code Review

<!-- Every week you leave a review on a pod mate's pull request. Fill in both parts below.
     Part 1 is the link — I will verify the review exists on GitHub.
     Part 2 is your written assessment — what you actually looked at and what you found. -->

**Reviewed:** *(Brian Willems)* 
**Link to my review: https://github.com/BrianWill2026/media-tracker-android/pull/9 **

### What I Looked At
I reviewed the MediaDetailUiState sealed interface, the load(mediaId) function, and the addToLibrary() function. 
I also looked at how the ViewModel loads the media detail, library status, and reviews.
### What I Noticed
In MediaDetailViewModel.load() Brian starts three requests using async getMediaDetail(), getLibraryItem(), and getReviews(). 
The media detail request is treated as required, while the library and review requests use runCatching.
I thought this was a useful design because a failure to load reviews does not prevent the user from seeing the media details. 
However, if getMediaDetail() throws MediaNotFoundException, the ViewModel cancels the other requests and changes the screen to MediaDetailUiState.NotFound.
### Comments I Left
i left a comment that the isAddingToLibrary check prevents duplicate requests while the first add request is still running. which is a good thing. 
## One Thing I Understood More Deeply
This week I understood more clearly how one screen can load several pieces of data without treating every failure the same way. 
In MediaDetailViewModel.load(), the media detail is required, but reviews and library status are secondary information.
Before this week, I thought that if one network request failed, the entire screen should enter an error state.
## One Thing I'm Still Confused About
I understand why load(mediaId) starts the three requests concurrently, but I am still unsure about what should happen when the user leaves the screen before all of the requests finish.
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
