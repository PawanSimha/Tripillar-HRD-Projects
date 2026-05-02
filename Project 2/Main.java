import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Subject {
    private final String name;
    private final String teacher;
    private final String room;

    public Subject(String name, String teacher, String room) {
        this.name = name;
        this.teacher = teacher;
        this.room = room;
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

    @Override
    public String toString() {
        return name + " (" + teacher + ", " + room + ")";
    }
}

class TimeSlot {
    private final int day;
    private final int period;
    private final String label;

    public TimeSlot(int day, int period, String label) {
        this.day = day;
        this.period = period;
        this.label = label;
    }

    public int getDay() {
        return day;
    }

    public int getPeriod() {
        return period;
    }

    public String getLabel() {
        return label;
    }
}

class TimetableGenerator {
    private final int days = 5;
    private final int periodsPerDay = 6;
    private final List<Subject> subjects;
    private final TimeSlot[] timeSlots;
    private final int[] assignment;
    private final boolean[] usedSlots;
    private final Set<String> usedTeachers;
    private final Set<String> usedRooms;

    public TimetableGenerator(List<Subject> subjects) {
        this.subjects = subjects;
        this.timeSlots = createTimeSlots();
        this.assignment = new int[subjects.size()];
        Arrays.fill(this.assignment, -1);
        this.usedSlots = new boolean[timeSlots.length];
        this.usedTeachers = new HashSet<>();
        this.usedRooms = new HashSet<>();
    }

    private TimeSlot[] createTimeSlots() {
        TimeSlot[] slots = new TimeSlot[days * periodsPerDay];
        String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        int index = 0;
        for (int d = 0; d < days; d++) {
            for (int p = 0; p < periodsPerDay; p++) {
                slots[index++] = new TimeSlot(d, p, dayNames[d] + " - Period " + (p + 1));
            }
        }
        return slots;
    }

    public boolean generateTimetable() {
        return backtrack(0);
    }

    private boolean backtrack(int subjectIndex) {
        if (subjectIndex == subjects.size()) {
            return true;
        }

        Subject subject = subjects.get(subjectIndex);
        for (int slotIndex = 0; slotIndex < timeSlots.length; slotIndex++) {
            if (!usedSlots[slotIndex] && isSafe(subject, slotIndex)) {
                assignSlot(subjectIndex, slotIndex);
                if (backtrack(subjectIndex + 1)) {
                    return true;
                }
                unassignSlot(subjectIndex, slotIndex);
            }
        }
        return false;
    }

    private boolean isSafe(Subject subject, int slotIndex) {
        return !usedTeachers.contains(subject.getTeacher()) && !usedRooms.contains(subject.getRoom());
    }

    private void assignSlot(int subjectIndex, int slotIndex) {
        assignment[subjectIndex] = slotIndex;
        usedSlots[slotIndex] = true;
        usedTeachers.add(subjects.get(subjectIndex).getTeacher());
        usedRooms.add(subjects.get(subjectIndex).getRoom());
    }

    private void unassignSlot(int subjectIndex, int slotIndex) {
        usedSlots[slotIndex] = false;
        usedTeachers.remove(subjects.get(subjectIndex).getTeacher());
        usedRooms.remove(subjects.get(subjectIndex).getRoom());
        assignment[subjectIndex] = -1;
    }

    public void printTimetable() {
        String[][] timetable = new String[days][periodsPerDay];
        for (int d = 0; d < days; d++) {
            for (int p = 0; p < periodsPerDay; p++) {
                timetable[d][p] = "Free";
            }
        }

        for (int i = 0; i < subjects.size(); i++) {
            int slotIndex = assignment[i];
            if (slotIndex >= 0) {
                TimeSlot slot = timeSlots[slotIndex];
                timetable[slot.getDay()][slot.getPeriod()] = subjects.get(i).toString();
            }
        }

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
        subjects.add(new Subject("Mathematics", "Mr. Sharma", "A1"));
        subjects.add(new Subject("Physics", "Ms. Patel", "B2"));
        subjects.add(new Subject("Chemistry", "Mr. Verma", "C3"));
        subjects.add(new Subject("Computer Science", "Ms. Rao", "L1"));
        subjects.add(new Subject("English", "Mrs. Singh", "E1"));
        subjects.add(new Subject("History", "Mr. Iyer", "H2"));

        TimetableGenerator generator = new TimetableGenerator(subjects);
        if (generator.generateTimetable()) {
            generator.printTimetable();
        } else {
            System.out.println("Unable to generate a valid timetable with the current constraints.");
        }
    }
}
