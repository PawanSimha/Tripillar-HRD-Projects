import java.util.ArrayList;
import java.util.List;

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
    public static void main(String[] args) {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Mathematics", "Mr. Sharma", "A1", 5));
        subjects.add(new Subject("Physics", "Ms. Patel", "B2", 5));
        subjects.add(new Subject("Java", "Ms. Rao", "L1", 5));
        subjects.add(new Subject("Chemistry", "Mr. Verma", "C3", 5));
        subjects.add(new Subject("English", "Mrs. Singh", "E1", 5));
        subjects.add(new Subject("History", "Mr. Iyer", "H2", 5));

        TimetableGenerator generator = new TimetableGenerator(subjects);
        if (generator.generateTimetable()) {
            generator.printTimetable();
        } else {
            System.out.println("No valid solution found.");
        }
    }
}
