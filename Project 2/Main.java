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
        return name + " (" + teacher + ", " + room + ")";
    }
}

class TimetableGenerator {
    private final int days = 5;
    private final int periodsPerDay = 6;
    private final String[][] timetable = new String[days][periodsPerDay];
    private final List<Subject> subjects;
    private final int[] remainingPeriods;

    public TimetableGenerator(List<Subject> subjects) {
        this.subjects = subjects;
        this.remainingPeriods = new int[subjects.size()];
        for (int i = 0; i < subjects.size(); i++) {
            remainingPeriods[i] = subjects.get(i).getRequiredPeriods();
        }
        for (int day = 0; day < days; day++) {
            for (int period = 0; period < periodsPerDay; period++) {
                timetable[day][period] = "Free";
            }
        }
    }

    public boolean generateTimetable() {
        return backtrack(0, 0);
    }

    private boolean backtrack(int day, int period) {
        if (day == days) {
            return allPeriodsAssigned();
        }

        int nextDay = day;
        int nextPeriod = period + 1;
        if (nextPeriod == periodsPerDay) {
            nextPeriod = 0;
            nextDay++;
        }

        for (int i = 0; i < subjects.size(); i++) {
            Subject subject = subjects.get(i);
            if (remainingPeriods[i] > 0 && isSafe(day, period, subject)) {
                assignSlot(day, period, subject);
                remainingPeriods[i]--;

                if (backtrack(nextDay, nextPeriod)) {
                    return true;
                }

                remainingPeriods[i]++;
                timetable[day][period] = "Free";
            }
        }

        // permit free slot if total required periods are lower than available slots
        if (backtrack(nextDay, nextPeriod)) {
            return true;
        }

        return false;
    }

    private boolean allPeriodsAssigned() {
        for (int count : remainingPeriods) {
            if (count != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isSafe(int day, int period, Subject subject) {
        if (!"Free".equals(timetable[day][period])) {
            return false;
        }

        if (period > 0 && timetable[day][period - 1].startsWith(subject.getName())) {
            return false;
        }

        return true;
    }

    private void assignSlot(int day, int period, Subject subject) {
        timetable[day][period] = subject.toString();
    }

    public void printTimetable() {
        System.out.println("Smart Timetable Generator (Backtracking)");
        System.out.println("Generated weekly timetable:");
        System.out.println("-----------------------------------------------------------");
        String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (int day = 0; day < days; day++) {
            System.out.printf("%s:\n", dayNames[day]);
            for (int period = 0; period < periodsPerDay; period++) {
                System.out.printf(" Period %d: %s\n", period + 1, timetable[day][period]);
            }
            System.out.println("-----------------------------------------------------------");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Mathematics", "Mr. Sharma", "A1", 5));
        subjects.add(new Subject("Physics", "Ms. Patel", "B2", 5));
        subjects.add(new Subject("Chemistry", "Mr. Verma", "C3", 5));
        subjects.add(new Subject("Computer Science", "Ms. Rao", "L1", 5));
        subjects.add(new Subject("English", "Mrs. Singh", "E1", 5));
        subjects.add(new Subject("History", "Mr. Iyer", "H2", 5));

        TimetableGenerator generator = new TimetableGenerator(subjects);
        if (generator.generateTimetable()) {
            generator.printTimetable();
        } else {
            System.out.println("Unable to generate a valid timetable with the current constraints.");
        }
    }
}
