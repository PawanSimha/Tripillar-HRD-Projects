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
- Assigns 6 subjects to 6 daily time slots: 09:00, 10:00, 11:00, 13:00, 14:00, 15:00
- Lunch break at 12:00 is excluded from assignment
- Avoids teacher and room conflicts for each time slot
- Implements `generateTimetable()`, `isSafe()`, and `backtrack()`
- Displays a clean ASCII timetable grid with Time, Subject, Teacher, and Room
