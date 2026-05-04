# Project Two: Smart Timetable Generator (Interactive)

This Java program is an interactive timetable generator that uses backtracking algorithm to create conflict-free weekly schedules. Users can dynamically add subjects and generate optimized timetables on demand.

## Files
- `Main.java` - contains the timetable generation algorithm, interactive menu system, and subject management features.

## How to compile
```bash
javac "Project 2\Main.java"
```

## How to run
```bash
java -cp "Project 2" Main
```

## Features

### Core Algorithm
- Backtracking-based timetable generation
- Assigns subjects to 6 daily time slots: 09:00, 10:00, 11:00, 13:00, 14:00, 15:00
- Lunch break at 12:00 is excluded from assignment
- Avoids teacher and room conflicts for each time slot
- Implements `generateTimetable()`, `isSafe()`, `backtrack()`, and constraint validation
- Displays a clean ASCII timetable grid with Time, Subject, Teacher, and Room

### Interactive User Interface
- **Menu-Driven System:** Easy navigation through 7 options
- **Add Subject:** Dynamically add subjects with custom teacher, room, and weekly periods
- **View Subjects:** Display all added subjects in a formatted table
- **Remove Subject:** Delete specific subjects by selection number
- **Generate Timetable:** Create optimized weekly schedules based on current subjects
- **Load Sample Data:** Quick-load 6 pre-configured subjects for testing
- **Clear All:** Reset and start with a fresh subject list
- **Input Validation:** Validates all user inputs with helpful error messages
- **Visual Formatting:** Enhanced user experience with box borders, emojis, and color-coded feedback

## Sample Data
- **Sample Subjects:** Mathematics, Physics, Java, Chemistry, English, History
- **Sample Teachers:** Mr. Sharma, Ms. Patel, Ms. Rao, Mr. Verma, Mrs. Singh, Mr. Iyer
- **Sample Rooms:** A1, B2, L1, C3, E1, H2
- **Periods per week:** 5 (configurable per subject)

## Usage Guide

### Running the Program
```bash
cd "Project 2"
javac Main.java
java Main
```

### Menu Options
1. **Add Subject** - Input subject name, teacher, room number, and required periods
2. **View All Subjects** - Display all subjects in a table format
3. **Remove Subject** - Remove a subject from the list
4. **Generate Timetable** - Create a conflict-free timetable for the week
5. **Load Sample Data** - Load 6 pre-configured subjects for demonstration
6. **Clear All Subjects** - Remove all subjects (requires confirmation)
7. **Exit** - Close the program

## Example Workflow
1. Start the program
2. Choose option 5 to load sample data
3. View subjects with option 2
4. Generate a timetable with option 4
5. Add your own subject with option 1
6. Generate a new timetable with updated subjects

## Technical Details
- **Algorithm:** Backtracking with constraint satisfaction
- **Data Structures:** ArrayList for subject management, 2D arrays for timetable grid
- **Constraints:** No subject conflicts, one subject per day per teacher, balanced weekly distribution
- **Time Complexity:** O(n! × d × p) where n=subjects, d=days, p=periods
- **Input Validation:** Checks for empty inputs, valid number ranges (1-30 periods)
