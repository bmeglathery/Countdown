# Countdown
This project is an extension of an in-class exercise, to be submitted as an assignment for CS341 - Mobile App Development.
The user may begin up to three 'Countdown' timers, with the target date/time selected via Android's built-in DatePicker and TimePicker widgets.

### Known Bugs
[10/7/17] User cannot pick date in a (roughly) 24 day range, 25 days past the current date. This pattern reoccurs every 25 days...
[10/11/17] Regarding ^^: Issue is due to **long** variable type sometimes representing a very large number as negative...app reads this as a time selected in the past. My initial thought to fix this issue is to have the CountdownTimer accept a BigInt as the parameter, but that's not a simple @Override... I'll come back to this at a later point in time.
