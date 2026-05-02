import java.util.ArrayList;
import java.util.List;

class Subject {
    private final String name;
    private final String teacher;
    private final int requiredPeriods;

    public Subject(String name, String teacher, int requiredPeriods) {
        this.name = name;
        this.teacher = teacher;
        this.requiredPeriods = requiredPeriods;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getRequiredPeriods() {
        return requiredPeriods;
    }
}

class TimetableGenerator {
    private final int days = 5;
    private final int periodsPerDay = 6;
    private final String[][] timetable = new String[days][periodsPerDay];
    private final List<Subject> subjects;
    private final int[] assignedPeriods;

    public TimetableGenerator(List<Subject> subjects) {
        this.subjects = subjects;
        this.assignedPeriods = new int[subjects.size()];
        for (int i = 0; i < days; i++) {
            for (int j = 0; j < periodsPerDay; j++) {
                timetable[i][j] = "Free";
            }
        }
    }

    public boolean buildTimetable() {
        return assignPeriod(0, 0);
    }

    private boolean assignPeriod(int day, int period) {
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
            if (assignedPeriods[i] < subject.getRequiredPeriods() && isValidAssignment(day, subject)) {
                timetable[day][period] = subject.getName() + " (" + subject.getTeacher() + ")";
                assignedPeriods[i]++;

                if (assignPeriod(nextDay, nextPeriod)) {
                    return true;
                }

                assignedPeriods[i]--;
                timetable[day][period] = "Free";
            }
        }

        // Allow free period if no subject can be assigned
        if (assignPeriod(nextDay, nextPeriod)) {
            return true;
        }

        return false;
    }

    private boolean allPeriodsAssigned() {
        for (int i = 0; i < subjects.size(); i++) {
            if (assignedPeriods[i] != subjects.get(i).getRequiredPeriods()) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidAssignment(int day, Subject subject) {
        for (int p = 0; p < periodsPerDay; p++) {
            if (timetable[day][p].contains(subject.getTeacher())) {
                return false;
            }
        }
        return true;
    }

    public void printTimetable() {
        System.out.println("Smart Timetable Generator (Backtracking)");
        System.out.println("Generated weekly timetable:");
        System.out.println("-----------------------------------------------------------");
        String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (int d = 0; d < days; d++) {
            System.out.printf("%s:\n", dayNames[d]);
            for (int p = 0; p < periodsPerDay; p++) {
                System.out.printf(" Period %d: %s\n", p + 1, timetable[d][p]);
            }
            System.out.println("-----------------------------------------------------------");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Mathematics", "Mr. Sharma", 7));
        subjects.add(new Subject("Physics", "Ms. Patel", 6));
        subjects.add(new Subject("Chemistry", "Mr. Verma", 6));
        subjects.add(new Subject("Computer Science", "Ms. Rao", 6));
        subjects.add(new Subject("English", "Mrs. Singh", 5));

        TimetableGenerator generator = new TimetableGenerator(subjects);
        if (generator.buildTimetable()) {
            generator.printTimetable();
        } else {
            System.out.println("Unable to generate a valid timetable with the current constraints.");
        }
    }
}
