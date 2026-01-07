package studentrentals.service;

import java.util.List;

import studentrentals.model.Booking;
import studentrentals.model.BookingStatus;
import studentrentals.model.Property;
import studentrentals.model.Room;
import studentrentals.repository.BookingRepository;

public class BookingService {
    private final BookingRepository repo;

    public BookingService(BookingRepository repo) {
        this.repo = repo;
    }

    public Booking requestBooking(String studentEmail, Property property, Room room) {
        if (!room.isAvailable()) return null;
        Booking b = new Booking(studentEmail, property.getOwnerEmail(), property, room);
        repo.save(b);
        return b;
    }

    public List<Booking> studentBookings(String email) {
        return repo.findByStudent(email);
    }

    public List<Booking> pendingForOwner(String ownerEmail) {
        return repo.findPendingByOwner(ownerEmail);
    }

    public void accept(Booking b) {
        b.setStatus(BookingStatus.ACCEPTED);
        b.getRoom().setAvailable(false);
    }

    public void reject(Booking b) {
        b.setStatus(BookingStatus.REJECTED);
    }
}
