# Week 07 Reflection

**Name:** Abdurahman Jigre 
**Date:** 2026-07-02

---

## Commits This Week

<!-- Paste a link to your commits for this week. The easiest way: go to your repo on GitHub,
     click "commits", and copy the URL after filtering by your name or branch. -->

**Link:**
https://github.com/abduramanjigre/media-tracker-android/pull/8
---

## Code Review

<!-- Every week you leave a review on a pod mate's pull request. Fill in both parts below.
     Part 1 is the link — I will verify the review exists on GitHub.
     Part 2 is your written assessment — what you actually looked at and what you found. -->

**Reviewed:** *()* My pod mate was not in the zoom with me he left early. Reason unknown.
**Link to my review:**

### What I Looked At
N/A pod mate left early
### What I Noticed
N/A pod mate left early
### Comments I Left
N/A pod mate left early
## One Thing I Understood More Deeply
I finally understood why the app won't build if you have the same class name in two different files. 
I had a 'Redeclaration' error because I accidentally had the same code in MediaApiService.kt and DefaultMediaRepository.kt. 
I learned that you can't have two things with the same name in the same package. It helped me realize that an Interface is just a 'list of rules' for the API, 
while the Repository is the one that actually does the work.
## One Thing I'm Still Confused About
I am still a bit confused about when to use a Column versus a LazyColumn. In the Media Detail screen, 
we used a Column with a verticalScroll, but in the Library, we used a LazyColumn. 
I'm not 100% sure which one is better to use when the list of items (like the reviews) might get really long.
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
