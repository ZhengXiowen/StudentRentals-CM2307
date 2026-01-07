package studentrentals.model;

public class Booking {
    private final String studentEmail;
    private final String ownerEmail;
    private final Property property;
    private final Room room;
    private BookingStatus status;

    public Booking(String studentEmail, String ownerEmail, Property property, Room room) {
        this.studentEmail = studentEmail;
        this.ownerEmail = ownerEmail;
        this.property = property;
        this.room = room;
        this.status = BookingStatus.PENDING;
    }

    public String getStudentEmail() { return studentEmail; }
    public String getOwnerEmail() { return ownerEmail; }
    public Property getProperty() { return property; }
    public Room getRoom() { return room; }
    public BookingStatus getStatus() { return status; }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "student='" + studentEmail + '\'' +
                ", property='" + property.getAddress() + '\'' +
                ", room='" + room.getRoomName() + '\'' +
                ", status=" + status +
                '}';
    }
}
