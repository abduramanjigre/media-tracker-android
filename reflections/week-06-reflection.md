# Week 06 Reflection

**Name:** Abdurahman Jigre 
**Date:** 2026-06-25

---

## Commits This Week

<!-- Paste a link to your commits for this week. The easiest way: go to your repo on GitHub,
     click "commits", and copy the URL after filtering by your name or branch. -->

**Link:**
https://github.com/abduramanjigre/media-tracker-android/pull/7
---

## Code Review

<!-- Every week you leave a review on a pod mate's pull request. Fill in both parts below.
     Part 1 is the link — I will verify the review exists on GitHub.
     Part 2 is your written assessment — what you actually looked at and what you found. -->

**Reviewed:** *(Brian Williams)*
**Link to my review:** https://github.com/BrianWill2026/media-tracker-android/pull/7

### What I Looked At
I reviewed Brian's Week 06 pull request. I focused on the authentication and networking changes, especially 
`DefaultUserRepository.kt`, `UserApiService.kt`, `RetrofitInstance.kt`, `MediaApiService.kt`, and `ApiConstants.kt`.

The main parts I looked at were `DefaultUserRepository.login()`, `UserApiService.login()`, and 
the new `mediaApiService` function in `RetrofitInstance`.

### What I Noticed
In `DefaultUserRepository.login()`, Brian calls `service.login()` with a `LoginRequest`, then converts the HTTP response into a `LoginResult`. 
A 200 response becomes `LoginResult.Success`, a 401 response becomes `LoginResult.InvalidCredentials`, an `IOException` becomes `LoginResult.NetworkError`, 
and anything else becomes `LoginResult.UnknownError`.

I thought this was a good design because the ViewModel does not need to know about HTTP status codes like 200 or 401. 
The repository handles the API details and returns clearer app-level states.

I also noticed that `RetrofitInstance.mediaApiService(sessionRepository)` creates a separate Retrofit service for media requests 
and adds `AuthInterceptor(sessionRepository)`. This matters because media requests likely need the saved session token, while login and registration do not need an existing token.

### Comments I Left
I left a comment about `DefaultUserRepository.login()` and `UserApiService.login()`. I said that converting API responses into `LoginResult.Success`, 
`LoginResult.InvalidCredentials`, `LoginResult.NetworkError`, and `LoginResult.UnknownError` keeps networking details inside the repository and 
gives the ViewModel cleaner states to work with.

## One Thing I Understood More Deeply
This week I understood more clearly why the repository layer is useful. In `DefaultUserRepository.login()`, the repository talks directly to `UserApiService.login()`, 
checks the response code, and returns a `LoginResult`. Before, I understood that repositories and API services were connected, but this week I understood the specific flow better: 
`UserApiService` makes the Retrofit request, `DefaultUserRepository` interprets the result, and the ViewModel can respond to a simple result type instead of dealing with raw API details.

## One Thing I'm Still Confused About
I understand that `RetrofitInstance.mediaApiService(sessionRepository)` adds `AuthInterceptor(sessionRepository)` for authenticated media requests, 
but I am still confused about how the app handles an expired access token. I understand how a token can be attached to a request, 
but I am not fully sure where the app would check whether the token is expired, use the refresh token, and retry the request without forcing the user to log in again.

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
