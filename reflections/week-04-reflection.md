# Week 4 Reflection

**Name:Abdurahman Jigre**
**Date:06-11-2026**

---

## Commits This Week
**Link:**
https://github.com/dmarsh31/media-tracker-android/pull/6
---

## Code Review

**Reviewed:** *(Dustin Marsh)*
**Link to my review:https://github.com/dmarsh31/media-tracker-android/pull/6#pullrequestreview-4481986116**

### What I Looked At

I reviewed the RegisterScreen changes from this week. 
I focused on the text fields, button actions, and the validation logic that checks for empty fields and matching passwords.

### What I Noticed
I noticed that the registration screen uses multiple state variables to track user input and displays an error message when validation fails. 
This helps provide immediate feedback to the user.


### Comments I Left

I left a positve comment.

## One Thing I Understood More Deeply

This week I gained a better understanding of how form validation works in Jetpack Compose. 
Before, I could see the code checking for empty fields or matching passwords, but I did not fully understand 
how the UI responded to those checks. After working through the RegisterScreen implementation, it clicked that user input 
is stored in state variables and that the screen can immediately react when those values change.
---
## One Thing I'm Still Confused About

I am still somewhat confused about how larger projects are organized when multiple layers are involved. 
I understand how the UI works and how a ViewModel can store data, but I am still learning when data should stay in the screen, 
when it should move to a ViewModel, and when it should be handled by a repository. 

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
