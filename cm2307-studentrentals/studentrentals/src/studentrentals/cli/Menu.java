package studentrentals.cli;

import java.util.Scanner;
import studentrentals.model.Booking;
import studentrentals.model.Homeowner;
import studentrentals.model.Property;
import studentrentals.model.Room;
import studentrentals.model.Student;
import studentrentals.model.User;
import studentrentals.repository.BookingRepository;
import studentrentals.repository.PropertyRepository;
import studentrentals.repository.UserRepository;
import studentrentals.service.AuthService;
import studentrentals.service.BookingService;
import studentrentals.service.PropertyService;


public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    private final UserRepository userRepo = new UserRepository();
    private final AuthService auth = new AuthService(userRepo);

    private final PropertyRepository propertyRepo = new PropertyRepository();
    private final PropertyService propertyService = new PropertyService(propertyRepo);

    private final BookingRepository bookingRepo = new BookingRepository();
    private final BookingService bookingService = new BookingService(bookingRepo);


    public void start() {
        System.out.println("StudentRentals CLI - running...");

        while (true) {
            System.out.println();
            User cu = auth.getCurrentUser();
            System.out.println("Current user: " + (cu == null ? "(none)" : cu.getEmail()));

            if (cu == null) {
                showGuestMenu();
                continue;
            }

            if (cu instanceof Homeowner) {
                showHomeownerMenu();
                continue;
            }

            if (cu instanceof Student) {
                showStudentMenu();
                continue;
            }

            System.out.println("Unknown user type. Logging out for safety.");
            auth.logout();
        }
    }

    // Guest menu

    private void showGuestMenu() {
        System.out.println("1) Register Student");
        System.out.println("2) Register Homeowner");
        System.out.println("3) Login");
        System.out.println("5) Exit");
        System.out.print("Choose: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> registerStudent();
            case "2" -> registerHomeowner();
            case "3" -> login();
            case "5" -> {
                System.out.println("Bye.");
                return;
            }
            default -> System.out.println("Invalid option.");
        }
    }


    // Homeowner menu
  
    private void showHomeownerMenu() {
    System.out.println("1) Create Property");
    System.out.println("2) My Properties");
    System.out.println("3) Add Room to My Property");
    System.out.println("4) Logout");
    System.out.println("5) Exit");
    System.out.println("6) View Pending Bookings");
    System.out.println("7) Process Booking (Accept/Reject)");
   
    System.out.print("Choose: ");

    String choice = scanner.nextLine().trim();
    switch (choice) {
        case "1" -> createProperty();
        case "2" -> listMyProperties();
        case "3" -> addRoomToMyProperty();
        case "6" -> viewPendingBookings();
        case "7" -> processBooking();
        case "4" -> {
            auth.logout();
            System.out.println("Logged out.");
        }
        case "5" -> {
            System.out.println("Bye.");
            System.exit(0);
        }
        default -> System.out.println("Invalid option.");
    }
}



    // Student menu

    private void showStudentMenu() {
    System.out.println("1) Browse All Properties");
    System.out.println("2) Search Properties by City");
    System.out.println("3) Request Booking");
    System.out.println("6) My Bookings");
    System.out.println("4) Logout");
    System.out.println("5) Exit");
    System.out.print("Choose: ");

    String choice = scanner.nextLine().trim();
    switch (choice) {
        case "1" -> browseAllProperties();
        case "2" -> searchByCity();
        case "3" -> requestBooking();
        case "6" -> myBookings();
        case "4" -> {
            auth.logout();
            System.out.println("Logged out.");
        }
        case "5" -> {
            System.out.println("Bye.");
            System.exit(0);
        }
        default -> System.out.println("Invalid option.");
    }
}



    // Auth flows

    private void registerStudent() {
        if (auth.getCurrentUser() != null) {
            System.out.println("You are already logged in. Logout first to register a new user.");
            return;
        }

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        if (userRepo.emailExists(email)) {
            System.out.println("Registration failed: email already exists.");
            return;
        }

        System.out.print("Password: ");
        String pw = scanner.nextLine();

        System.out.print("University: ");
        String uni = scanner.nextLine().trim();

        System.out.print("Student ID: ");
        String sid = scanner.nextLine().trim();

        boolean ok = auth.registerStudent(name, email, pw, uni, sid);
        System.out.println(ok ? "Student registered." : "Registration failed.");
    }

    private void registerHomeowner() {
        if (auth.getCurrentUser() != null) {
            System.out.println("You are already logged in. Logout first to register a new user.");
            return;
        }

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        if (userRepo.emailExists(email)) {
            System.out.println("Registration failed: email already exists.");
            return;
        }

        System.out.print("Password: ");
        String pw = scanner.nextLine();

        boolean ok = auth.registerHomeowner(name, email, pw);
        System.out.println(ok ? "Homeowner registered." : "Registration failed.");
    }

    private void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String pw = scanner.nextLine();

        boolean ok = auth.login(email, pw);
        System.out.println(ok ? "Login success." : "Login failed.");
    }

    // Homeowner actions

    private void createProperty() {
        User cu = auth.getCurrentUser();
        if (!(cu instanceof Homeowner)) {
            System.out.println("Only homeowners can create properties.");
            return;
        }

        System.out.print("City: ");
        String city = scanner.nextLine().trim();
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();
        System.out.print("Description: ");
        String desc = scanner.nextLine().trim();

        propertyService.createProperty(cu.getEmail(), city, address, desc);
        System.out.println("Property created.");
    }

    private void listMyProperties() {
        User cu = auth.getCurrentUser();
        if (!(cu instanceof Homeowner)) {
            System.out.println("Only homeowners can view this.");
            return;
        }

        var list = propertyService.propertiesByOwner(cu.getEmail());
        if (list.isEmpty()) {
            System.out.println("No properties yet.");
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ") " + list.get(i));
            Property p = list.get(i);
            if (p.getRooms().isEmpty()) {
                System.out.println("   (no rooms)");
            } else {
                for (Room r : p.getRooms()) {
                    System.out.println("   - " + r);
                }
            }
        }
    }

    private void addRoomToMyProperty() {
        User cu = auth.getCurrentUser();
        if (!(cu instanceof Homeowner)) {
            System.out.println("Only homeowners can add rooms.");
            return;
        }

        var list = propertyService.propertiesByOwner(cu.getEmail());
        if (list.isEmpty()) {
            System.out.println("No properties yet. Create one first.");
            return;
        }

        System.out.println("Select property:");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ") " + list.get(i));
        }

        System.out.print("Choose number: ");
        String s = scanner.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(s) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return;
        }
        if (idx < 0 || idx >= list.size()) {
            System.out.println("Out of range.");
            return;
        }

        Property p = list.get(idx);

        System.out.print("Room name (e.g., Room A): ");
        String roomName = scanner.nextLine().trim();
        System.out.print("Type (Single/Double/Studio): ");
        String type = scanner.nextLine().trim();
        System.out.print("Weekly price: ");
        String priceStr = scanner.nextLine().trim();

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price.");
            return;
        }

        boolean ok = propertyService.addRoomToProperty(p, roomName, type, price);
        System.out.println(ok ? "Room added." : "Failed to add room.");
    }


    // Student actions

    private void browseAllProperties() {
        var props = propertyService.allProperties();
        if (props.isEmpty()) {
            System.out.println("No properties available.");
            return;
        }

        for (Property p : props) {
            System.out.println(p);
            if (p.getRooms().isEmpty()) {
                System.out.println("  (no rooms)");
            } else {
                for (Room r : p.getRooms()) {
                    System.out.println("  - " + r);
                }
            }
        }
    }

    private void searchByCity() {
        System.out.print("City: ");
        String city = scanner.nextLine().trim();

        var props = propertyService.allProperties();
        boolean found = false;

        for (Property p : props) {
            if (p.getCity().equalsIgnoreCase(city)) {
                System.out.println(p);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matches.");
        }
    }

    private void requestBooking() {
    User cu = auth.getCurrentUser();
    if (!(cu instanceof Student)) {
        System.out.println("Only students can request bookings.");
        return;
    }

    var props = propertyService.allProperties();
    if (props.isEmpty()) {
        System.out.println("No properties available.");
        return;
    }

    System.out.println("Select property:");
    for (int i = 0; i < props.size(); i++) {
        System.out.println((i + 1) + ") " + props.get(i));
    }
    System.out.print("Choose number: ");
    String s = scanner.nextLine().trim();
    int pIdx;
    try {
        pIdx = Integer.parseInt(s) - 1;
    } catch (NumberFormatException e) {
        System.out.println("Invalid number.");
        return;
    }
    if (pIdx < 0 || pIdx >= props.size()) {
        System.out.println("Out of range.");
        return;
    }

    Property p = props.get(pIdx);
    if (p.getRooms().isEmpty()) {
        System.out.println("This property has no rooms.");
        return;
    }

    System.out.println("Select room:");
    for (int i = 0; i < p.getRooms().size(); i++) {
        System.out.println((i + 1) + ") " + p.getRooms().get(i));
    }
    System.out.print("Choose number: ");
    String r = scanner.nextLine().trim();
    int rIdx;
    try {
        rIdx = Integer.parseInt(r) - 1;
    } catch (NumberFormatException e) {
        System.out.println("Invalid number.");
        return;
    }
    if (rIdx < 0 || rIdx >= p.getRooms().size()) {
        System.out.println("Out of range.");
        return;
    }

    Room room = p.getRooms().get(rIdx);
    Booking b = bookingService.requestBooking(cu.getEmail(), p, room);
    if (b == null) {
        System.out.println("Booking failed: room not available.");
    } else {
        System.out.println("Booking requested (PENDING).");
    }
}

private void myBookings() {
    User cu = auth.getCurrentUser();
    if (!(cu instanceof Student)) {
        System.out.println("Only students can view bookings.");
        return;
    }

    var list = bookingService.studentBookings(cu.getEmail());
    if (list.isEmpty()) {
        System.out.println("No bookings yet.");
        return;
    }
    for (int i = 0; i < list.size(); i++) {
        System.out.println((i + 1) + ") " + list.get(i));
    }
}

private void viewPendingBookings() {
    User cu = auth.getCurrentUser();
    if (!(cu instanceof Homeowner)) {
        System.out.println("Only homeowners can view pending bookings.");
        return;
    }

    var list = bookingService.pendingForOwner(cu.getEmail());
    if (list.isEmpty()) {
        System.out.println("No pending bookings.");
        return;
    }

    for (int i = 0; i < list.size(); i++) {
        System.out.println((i + 1) + ") " + list.get(i));
    }
}

private void processBooking() {
    User cu = auth.getCurrentUser();
    if (!(cu instanceof Homeowner)) {
        System.out.println("Only homeowners can process bookings.");
        return;
    }

    var list = bookingService.pendingForOwner(cu.getEmail());
    if (list.isEmpty()) {
        System.out.println("No pending bookings.");
        return;
    }

    System.out.println("Select booking:");
    for (int i = 0; i < list.size(); i++) {
        System.out.println((i + 1) + ") " + list.get(i));
    }

    System.out.print("Choose number: ");
    String s = scanner.nextLine().trim();
    int idx;
    try {
        idx = Integer.parseInt(s) - 1;
    } catch (NumberFormatException e) {
        System.out.println("Invalid number.");
        return;
    }
    if (idx < 0 || idx >= list.size()) {
        System.out.println("Out of range.");
        return;
    }

    Booking b = list.get(idx);

    System.out.print("Accept (A) or Reject (R): ");
    String decision = scanner.nextLine().trim().toUpperCase();

    if ("A".equals(decision)) {
        bookingService.accept(b);
        System.out.println("Booking ACCEPTED. Room is now NOT AVAILABLE.");
    } else if ("R".equals(decision)) {
        bookingService.reject(b);
        System.out.println("Booking REJECTED.");
    } else {
        System.out.println("Invalid choice.");
    }
}

}
