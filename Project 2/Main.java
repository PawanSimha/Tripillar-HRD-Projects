import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Subject {
    private final String name;
    private final String teacher;
    private final String room;
    private final int requiredPeriods;

    public Subject(String name, String teacher, String room, int requiredPeriods) {
        this.name = name;
        this.teacher = teacher;
        this.room = room;
        this.requiredPeriods = requiredPeriods;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getRoom() {
        return room;
    }

    public int getRequiredPeriods() {
        return requiredPeriods;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s)", name, teacher, room);
    }
}

class Slot {
    private final String timeLabel;

    public Slot(String timeLabel) {
        this.timeLabel = timeLabel;
    }

    public String getTimeLabel() {
        return timeLabel;
    }
}

class TimetableGenerator {
    private static final int DAYS = 5;
    private static final int PERIODS_PER_DAY = 6;
    private static final String[] DAY_NAMES = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static final Slot[] SLOTS = {
        new Slot("09:00"),
        new Slot("10:00"),
        new Slot("11:00"),
        new Slot("13:00"),
        new Slot("14:00"),
        new Slot("15:00")
    };

    private final Subject[][] timetable = new Subject[DAYS][PERIODS_PER_DAY];
    private final List<Subject> subjects;
    private final int[] remainingPeriods;
    private final boolean[][] subjectUsedInDay;

    public TimetableGenerator(List<Subject> subjects) {
        this.subjects = subjects;
        this.remainingPeriods = new int[subjects.size()];
        this.subjectUsedInDay = new boolean[DAYS][subjects.size()];

        for (int i = 0; i < subjects.size(); i++) {
            remainingPeriods[i] = subjects.get(i).getRequiredPeriods();
        }
    }

    public boolean generateTimetable() {
        return backtrack(0, 0);
    }

    private boolean backtrack(int day, int period) {
        if (day == DAYS) {
            return allPeriodsAssigned();
        }

        int nextDay = day;
        int nextPeriod = period + 1;
        if (nextPeriod == PERIODS_PER_DAY) {
            nextPeriod = 0;
            nextDay++;
        }

        int startIndex = day % subjects.size();
        for (int offset = 0; offset < subjects.size(); offset++) {
            int subjectIndex = (startIndex + offset) % subjects.size();
            Subject subject = subjects.get(subjectIndex);
            if (remainingPeriods[subjectIndex] > 0 && isSafe(day, period, subjectIndex, subject)) {
                assignSlot(day, period, subjectIndex, subject);

                if (backtrack(nextDay, nextPeriod)) {
                    return true;
                }

                unassignSlot(day, period, subjectIndex);
            }
        }

        return false;
    }

    private boolean isSafe(int day, int period, int subjectIndex, Subject subject) {
        if (subjectUsedInDay[day][subjectIndex]) {
            return false;
        }

        if (period > 0 && timetable[day][period - 1] == subject) {
            return false;
        }

        int daysRemaining = DAYS - day;
        if (remainingPeriods[subjectIndex] > daysRemaining) {
            return false;
        }

        return true;
    }

    private void assignSlot(int day, int period, int subjectIndex, Subject subject) {
        timetable[day][period] = subject;
        remainingPeriods[subjectIndex]--;
        subjectUsedInDay[day][subjectIndex] = true;
    }

    private void unassignSlot(int day, int period, int subjectIndex) {
        timetable[day][period] = null;
        remainingPeriods[subjectIndex]++;
        subjectUsedInDay[day][subjectIndex] = false;
    }

    private boolean allPeriodsAssigned() {
        for (int remaining : remainingPeriods) {
            if (remaining != 0) {
                return false;
            }
        }
        return true;
    }

    public void printTimetable() {
        System.out.println("Smart Timetable Generator (Backtracking)");
        System.out.println("Final valid timetable:");

        for (int day = 0; day < DAYS; day++) {
            System.out.println();
            System.out.println("+--------+----------------------+----------------------+--------+");
            System.out.printf("| %-6s | %-20s | %-20s | %-6s |%n", "Time", "Subject", "Teacher", "Room");
            System.out.println("+--------+----------------------+----------------------+--------+");
            System.out.println(DAY_NAMES[day] + ":");
            for (int period = 0; period < PERIODS_PER_DAY; period++) {
                Subject subject = timetable[day][period];
                if (subject != null) {
                    System.out.printf("| %-6s | %-20s | %-20s | %-6s |%n",
                            SLOTS[period].getTimeLabel(), subject.getName(), subject.getTeacher(), subject.getRoom());
                } else {
                    System.out.printf("| %-6s | %-20s | %-20s | %-6s |%n",
                            SLOTS[period].getTimeLabel(), "Free", "-", "-");
                }
            }
            System.out.println("+--------+----------------------+----------------------+--------+");
        }
    }
}

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static List<Subject> subjects = new ArrayList<>();
    private static TimetableGenerator generator;

    public static void main(String[] args) {
        displayWelcome();
        mainMenu();
        scanner.close();
    }

    private static void displayWelcome() {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║       Smart Timetable Generator (Interactive)         ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private static void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n┌─ Main Menu ────────────────────────────────────────┐");
            System.out.println("│ 1. Add Subject                                     │");
            System.out.println("│ 2. View All Subjects                               │");
            System.out.println("│ 3. Remove Subject                                  │");
            System.out.println("│ 4. Generate Timetable                              │");
            System.out.println("│ 5. Load Sample Data                                │");
            System.out.println("│ 6. Clear All Subjects                              │");
            System.out.println("│ 0. Exit                                            │");
            System.out.println("└────────────────────────────────────────────────────┘");
            System.out.print("\nEnter your choice: ");
            
            int choice = getIntInput();
            System.out.println();

            switch (choice) {
                case 1:
                    addSubject();
                    break;
                case 2:
                    viewSubjects();
                    break;
                case 3:
                    removeSubject();
                    break;
                case 4:
                    generateTimetable();
                    break;
                case 5:
                    loadSampleData();
                    break;
                case 6:
                    clearAllSubjects();
                    break;
                case 0:
                    System.out.println("Thank you for using Smart Timetable Generator. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }

    private static void addSubject() {
        System.out.println("┌─ Add New Subject ──────────────────────────────────┐");
        
        System.out.print("Enter subject name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("❌ Subject name cannot be empty!");
            return;
        }

        System.out.print("Enter teacher name: ");
        String teacher = scanner.nextLine().trim();
        if (teacher.isEmpty()) {
            System.out.println("❌ Teacher name cannot be empty!");
            return;
        }

        System.out.print("Enter room number: ");
        String room = scanner.nextLine().trim();
        if (room.isEmpty()) {
            System.out.println("❌ Room number cannot be empty!");
            return;
        }

        System.out.print("Enter required periods per week: ");
        int periods = getIntInput();
        if (periods <= 0 || periods > 30) {
            System.out.println("❌ Periods must be between 1 and 30!");
            return;
        }

        subjects.add(new Subject(name, teacher, room, periods));
        System.out.println("✅ Subject added successfully!");
        System.out.println("└────────────────────────────────────────────────────┘");
    }

    private static void viewSubjects() {
        System.out.println("┌─ All Subjects ─────────────────────────────────────┐");
        if (subjects.isEmpty()) {
            System.out.println("│ No subjects added yet!                             │");
        } else {
            System.out.println(String.format("│ Total subjects: %d", subjects.size()));
            System.out.println("└────────────────────────────────────────────────────┘");
            System.out.println("+────+──────────────────+──────────────────+───────+────────+");
            System.out.printf("| %-2s | %-16s | %-16s | %-5s | %-6s |%n", "No", "Subject", "Teacher", "Room", "Period");
            System.out.println("+────+──────────────────+──────────────────+───────+────────+");
            
            for (int i = 0; i < subjects.size(); i++) {
                Subject s = subjects.get(i);
                System.out.printf("| %-2d | %-16s | %-16s | %-5s | %-6d |%n", 
                    i + 1, s.getName(), s.getTeacher(), s.getRoom(), s.getRequiredPeriods());
            }
            System.out.println("+────+──────────────────+──────────────────+───────+────────+");
        }
    }

    private static void removeSubject() {
        if (subjects.isEmpty()) {
            System.out.println("❌ No subjects to remove!");
            return;
        }

        viewSubjects();
        System.out.print("\nEnter subject number to remove (0 to cancel): ");
        int choice = getIntInput();

        if (choice == 0) {
            return;
        }

        if (choice > 0 && choice <= subjects.size()) {
            Subject removed = subjects.remove(choice - 1);
            System.out.println("✅ Subject '" + removed.getName() + "' removed successfully!");
        } else {
            System.out.println("❌ Invalid choice!");
        }
    }

    private static void generateTimetable() {
        if (subjects.isEmpty()) {
            System.out.println("❌ Please add at least one subject before generating timetable!");
            return;
        }

        System.out.println("⏳ Generating timetable...\n");
        generator = new TimetableGenerator(subjects);

        if (generator.generateTimetable()) {
            System.out.println("✅ Timetable generated successfully!\n");
            generator.printTimetable();
        } else {
            System.out.println("❌ Could not generate a valid timetable with the given constraints.");
            System.out.println("   Try adjusting the number of periods for each subject.");
        }
    }

    private static void loadSampleData() {
        subjects.clear();
        subjects.add(new Subject("Mathematics", "Mr. Sharma", "A1", 5));
        subjects.add(new Subject("Physics", "Ms. Patel", "B2", 5));
        subjects.add(new Subject("Java", "Ms. Rao", "L1", 5));
        subjects.add(new Subject("Chemistry", "Mr. Verma", "C3", 5));
        subjects.add(new Subject("English", "Mrs. Singh", "E1", 5));
        subjects.add(new Subject("History", "Mr. Iyer", "H2", 5));

        System.out.println("✅ Sample data loaded successfully!");
        viewSubjects();
    }

    private static void clearAllSubjects() {
        if (subjects.isEmpty()) {
            System.out.println("ℹ️  No subjects to clear!");
            return;
        }

        System.out.print("Are you sure you want to clear all subjects? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("yes")) {
            subjects.clear();
            generator = null;
            System.out.println("✅ All subjects cleared!");
        } else {
            System.out.println("❌ Operation cancelled.");
        }
    }

    private static int getIntInput() {
        try {
            int input = Integer.parseInt(scanner.nextLine().trim());
            return input;
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid number!");
            return -1;
        }
    }
}
