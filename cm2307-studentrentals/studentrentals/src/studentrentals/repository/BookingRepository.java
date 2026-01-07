package studentrentals.repository;

import java.util.ArrayList;
import java.util.List;

import studentrentals.model.Booking;
import studentrentals.model.BookingStatus;

public class BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    public void save(Booking b) {
        bookings.add(b);
    }

    public List<Booking> findAll() {
        return new ArrayList<>(bookings);
    }

    public List<Booking> findByStudent(String email) {
        List<Booking> out = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getStudentEmail().equalsIgnoreCase(email)) out.add(b);
        }
        return out;
    }

    public List<Booking> findPendingByOwner(String ownerEmail) {
        List<Booking> out = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getOwnerEmail().equalsIgnoreCase(ownerEmail) && b.getStatus() == BookingStatus.PENDING) {
                out.add(b);
            }
        }
        return out;
    }
}
