import java.util.Scanner;

class RideBooking {
    private int bookingId;
    private String customerName;
    private String origin;
    private String destination;
    private String rideDate;
    private double distanceKm;
    private double fare;

    public RideBooking(int bookingId, String customerName, String origin, String destination, String rideDate, double distanceKm, double fare) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.origin = origin;
        this.destination = destination;
        this.rideDate = rideDate;
        this.distanceKm = distanceKm;
        this.fare = fare;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Customer: %s | %s -> %s | Date: %s | Distance: %.2f km | Fare: %.2f",
                bookingId, customerName, origin, destination, rideDate, distanceKm, fare);
    }
}

class RideHistory {
    private static class Node {
        RideBooking booking;
        Node next;

        Node(RideBooking booking) {
            this.booking = booking;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    public RideHistory() {
        this.head = null;
        this.size = 0;
    }

    public void addBooking(RideBooking booking) {
        Node newNode = new Node(booking);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
        System.out.println("Booking added successfully.");
    }

    public RideBooking searchById(int bookingId) {
        Node current = head;
        while (current != null) {
            if (current.booking.getBookingId() == bookingId) {
                return current.booking;
            }
            current = current.next;
        }
        return null;
    }

    public boolean deleteById(int bookingId) {
        Node current = head;
        Node previous = null;
        while (current != null) {
            if (current.booking.getBookingId() == bookingId) {
                if (previous == null) {
                    head = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    public boolean updateBooking(int bookingId, String customerName, String origin, String destination, String rideDate, double distanceKm, double fare) {
        RideBooking booking = searchById(bookingId);
        if (booking == null) {
            return false;
        }
        booking.setCustomerName(customerName);
        booking.setOrigin(origin);
        booking.setDestination(destination);
        booking.setRideDate(rideDate);
        booking.setDistanceKm(distanceKm);
        booking.setFare(fare);
        return true;
    }

    public void displayAllBookings() {
        if (head == null) {
            System.out.println("No ride bookings found.");
            return;
        }
        System.out.println("Ride Booking History:");
        System.out.println("---------------------");
        Node current = head;
        while (current != null) {
            System.out.println(current.booking);
            current = current.next;
        }
        System.out.println("---------------------");
        System.out.println("Total bookings: " + size);
    }

    public int getSize() {
        return size;
    }
}

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RideHistory history = new RideHistory();

    public static void main(String[] args) {
        System.out.println("Ride Booking History System");
        System.out.println("Linked List Based Implementation");
        while (true) {
            displayMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> addBooking();
                case 2 -> history.displayAllBookings();
                case 3 -> searchBooking();
                case 4 -> deleteBooking();
                case 5 -> updateBooking();
                case 6 -> System.out.println("Exiting... Goodbye!") ;
                default -> System.out.println("Invalid choice. Please try again.");
            }
            if (choice == 6) {
                break;
            }
            System.out.println();
        }
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Add a new ride booking");
        System.out.println("2. Display booking history");
        System.out.println("3. Search booking by ID");
        System.out.println("4. Delete booking by ID");
        System.out.println("5. Update booking details");
        System.out.println("6. Exit");
    }

    private static void addBooking() {
        int id = readInt("Enter booking ID: ");
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();
        System.out.print("Enter origin: ");
        String origin = scanner.nextLine();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter ride date (e.g., 2026-05-02): ");
        String rideDate = scanner.nextLine();
        double distanceKm = readDouble("Enter distance in km: ");
        double fare = readDouble("Enter fare amount: ");
        RideBooking booking = new RideBooking(id, customerName, origin, destination, rideDate, distanceKm, fare);
        history.addBooking(booking);
    }

    private static void searchBooking() {
        int id = readInt("Enter booking ID to search: ");
        RideBooking booking = history.searchById(id);
        if (booking != null) {
            System.out.println("Booking found:");
            System.out.println(booking);
        } else {
            System.out.println("Booking with ID " + id + " was not found.");
        }
    }

    private static void deleteBooking() {
        int id = readInt("Enter booking ID to delete: ");
        if (history.deleteById(id)) {
            System.out.println("Booking deleted successfully.");
        } else {
            System.out.println("Booking with ID " + id + " was not found.");
        }
    }

    private static void updateBooking() {
        int id = readInt("Enter booking ID to update: ");
        if (history.searchById(id) == null) {
            System.out.println("Booking with ID " + id + " was not found.");
            return;
        }
        System.out.print("Enter new customer name: ");
        String customerName = scanner.nextLine();
        System.out.print("Enter new origin: ");
        String origin = scanner.nextLine();
        System.out.print("Enter new destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter new ride date: ");
        String rideDate = scanner.nextLine();
        double distanceKm = readDouble("Enter new distance in km: ");
        double fare = readDouble("Enter new fare amount: ");
        boolean updated = history.updateBooking(id, customerName, origin, destination, rideDate, distanceKm, fare);
        if (updated) {
            System.out.println("Booking updated successfully.");
        } else {
            System.out.println("Failed to update booking.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Invalid integer. Please try again.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }
}
