# Extra Credit Reflection — Design Alignment

*See `extra-credit-design-alignment.md` for submission requirements and the full assignment description.*

**Name: Abdurahman Jigre**
**Date: 07-09-2026**
---

## The Audit

*Before touching any code, compare your running app to the wireframes screen by screen. List what you found — be specific about which screen, which component, and what was different. "The colors were off" is not specific. "The active chip on the Search screen was using amber instead of primary container (#E0E0FF)" is specific.*

*List at least five concrete differences you found:*

1. Missing Branding: My original screens only had text at the top. The wireframe featured a square rounded icon with a play button inside a light purple container.
2. Typography Weight: The titles "Media Tracker" and "Create Account" were using standard font weights, while the wireframe required them to be Bold.
3. Corner Radius: My text fields and buttons had the default Material 3 shape (slightly rounded). The wireframe required much rounder corners, specifically a 12.dp radius.
4. Button Prominence: My "Log In" and "Sign Up" buttons were the standard height. The wireframe design used taller, more prominent buttons.
5. Two-Tone Links: The bottom navigation links (e.g., "Don't have an account? Sign Up") were plain text in my app. In the wireframe, the buttons were bold and purple to stand out as a link.

---

## What You Changed

*Walk through the changes you made. For each area of the design system, describe what the code looked like before and what you changed it to. Reference specific files and Composables.*


### Color System

<!-- What did your Color.kt look like before? What did you add or change? How did you wire colors into MaterialTheme? -->

Before the audit, my Color.kt was using the default Material 3 colors the standard purple. They didn't match the specific indigo branding shown in the wireframe image.
I manually updated the Primary hex code to #6366F1 to match the indigo branding from the design. I also updated the PrimaryContainer to a lighter version of that indigo to create a consistent "reading app" feel. This gave the app a more professional, custom look rather than just looking like a basic template.
In Theme.kt, I ensured that these new indigo colors were correctly mapped to the lightColorScheme. By assigning my new Primary variable to the primary slot in the color scheme, it allowed all my buttons and icons to automatically pick up the new indigo branding without me having to change the color on every single individual component.

### Typography

<!-- Were weights hardcoded? Did you update Type.kt? What specifically changed? -->

Before: I was using standard MaterialTheme.typography styles like headlineMedium and bodyMedium without extra modifiers.

After: I kept the Material styles but added fontWeight = FontWeight.Bold to the titles and buttons to match the high-fidelity design.
### Buttons

<!-- Which button variants needed work? What was wrong and how did you fix it? -->

Before: Main buttons were using default shapes and heights.

After: I updated the Button composable in both files to include shape = RoundedCornerShape(12.dp) and a modifier = Modifier.height(56.dp). I also wrapped the bottom "Sign Up" and "Log In" prompts in a buildAnnotatedString so I could style the actual link part with a different color and weight.
### Text Fields

<!-- What shape and color changes did you make? -->
I changed the primary color to match what we have on the wire frame.(Primary #6366F1) it was something different for me before for some reason. 

### Other Components

<!-- Chips, cards, bottom nav, status badges — what changed? -->

--- App Icon Box: I added a new Box component at the top of the screens. I used Modifier.size(80.dp), Modifier.clip(RoundedCornerShape(12.dp)), and Modifier.background with a copy of the primaryContainer color at 0.4f alpha to create that soft purple icon background.

## What Was Hard

*Describe the most technically challenging part of this work. Don't write "it was confusing." Explain specifically what confused you, what you tried, and what helped you figure it out. If something in the Jetpack Compose theming system surprised you, describe it.*

--- The most technically challenging part was styling the text at the bottom (the "Don't have an account? Sign Up" part). 
At first, I tried using a Row with two separate Text items, but the alignment and spacing didn't look quite right. I had to learn how to use buildAnnotatedString. 
It was confusing to figure out how withStyle and SpanStyle worked together inside a single string, but once I got it, it allowed me to style the "Sign Up" part differently without needing multiple composables.

## What You Understand Now

*What do you understand about Jetpack Compose theming — `MaterialTheme`, `colorScheme`, `typography`, component defaults — that you didn't fully grasp before this assignment? Be specific enough that you could explain it to a pod mate who hasn't done this yet.*

--- I now have a much better grasp of how to "override" default Material 3 styles. I used to think I had to change everything in Theme.kt, 
but I realized this week that for specific screens like Login/Register, it's often better to apply surgical changes (like specific shape or fontWeight modifiers) directly to the components. 
I also learned that Box is a very powerful tool for creating custom UI elements, like the rounded icon container, by combining clip, background, and contentAlignment.

## Self-Assessment

*Look at the rubric (`extra-credit-design-alignment-rubric.md`) and estimate your own score for each section. Be honest — this does not affect your grade, but it shows me whether you read the rubric carefully.*

| Section | Possible | My Estimate |
|:---|:---:|:---:|
| Color System | 13 | |
| Typography | 5 | |
| Component Styling | 15 | |
| Navigation & Cards | 5 | |
| Reflection | 12 | |
| **Total** | **50** | |

*One thing I think I did well:*

*One thing I know I left incomplete or could have done better:*
