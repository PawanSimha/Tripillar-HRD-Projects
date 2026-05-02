# Project Two: Smart Timetable Generator

This Java program generates a weekly timetable using backtracking.

## Files
- `Main.java` - contains the timetable generation algorithm and prints the weekly schedule.

## How to compile
```bash
javac "Project 2\Main.java"
```

## How to run
```bash
java -cp "Project 2" Main
```

## Features
- Backtracking-based timetable generation
- Assigns subjects to time slots across a 5-day week
- Avoids consecutive same-subject periods in a day
- Uses subject, teacher, and room data for schedule output
- Uses a constraint satisfaction approach with `isSafe`, `assignSlot`, and `backtrack`
