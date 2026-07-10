# Week 05 Reflection

**Name:** Abdurahman Jigre 
**Date:** 2026-06-18

---

## Commits This Week

<!-- Paste a link to your commits for this week. The easiest way: go to your repo on GitHub,
     click "commits", and copy the URL after filtering by your name or branch. -->

**Link:**
https://github.com/abduramanjigre/media-tracker-android/pull/6
---

## Code Review

<!-- Every week you leave a review on a pod mate's pull request. Fill in both parts below.
     Part 1 is the link — I will verify the review exists on GitHub.
     Part 2 is your written assessment — what you actually looked at and what you found. -->

**Reviewed:** *(Brian Williams)*
**Link to my review:** https://github.com/BrianWill2026/media-tracker-android/pull/6/changes/485505affe95b7faebca301bb67d1800a59bc4c4



### What I Looked At

This pull request focused on adding networking and repository components for user authentication and registration. 
I reviewed the new files related to Retrofit, API requests, repositories, and session management. 
I specifically looked at DefaultUserRepository, UserRepository, UserApiService, RegisterRequest, LoginRequest, 
and the session storage implementation using DataStore.

### What I Noticed

One thing I noticed was that the repository pattern was used to separate the networking logic from the rest of the application. 
This makes the code easier to maintain and test. I also noticed that different registration outcomes such as network errors, 
conflicts, and unknown errors were handled through sealed interfaces, which provides a cleaner way to manage application states. 
Another positive aspect was the use of DataStore for storing session information instead of older approaches such as SharedPreferences.

### Comments I Left

I left positive feedback about the organization of the networking code and how the repository pattern helped keep responsibilities separated.

---

## One Thing I Understood More Deeply

This week I gained a better understanding of how Retrofit, repositories, and API services work together in an Android application. 
Before, I knew Retrofit was used for network requests, but I did not fully understand how it al came together. 

---

## One Thing I'm Still Confused About

I am still somewhat confused about how authentication tokens are managed throughout the entire application lifecycle. 
I understand that access tokens and refresh tokens can be stored using DataStore, 
but I am still learning when refresh tokens should be used and how applications automatically refresh expired sessions without requiring the user to log in again.

---

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
