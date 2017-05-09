# present
Simple android app logging to adafruit.io whenever my phone is locked or unlocked

### What it is:
This is a very simple, UI-less Android app I built to track every time I look at my phone during the day. Every time the phone is unlocked (Android broadcast `ACTION_USER_PRESENT`) or manually put back to sleep (Android broadcast `ACTION_SCREEN_OFF`), the app will ping the adafruit.io server and log the event.  
I made this because I'm working on some technologies that reduce the amount of distraction+time spent checking your phone throughout the day.

### How to use it:
- visit [adafruit.io](https://adafruit.io) and obtain an API Key
- At adafrtui.io, create two feeds, one called "locked" and one "unlocked" (or w/e you want to call them: awake/asleep, etc)
- Compile this app and open it
- At the prompts, enter the Adafruit API Key (first line), your Adafruit username (second line), and the two feed labels
- Press the "save" icon in the lower right.
- That's it: now, in the background, your Android will log every time you open or close your phone, and you can check it at your adafruit.io feeds
