package studentrentals.model;

import java.util.ArrayList;
import java.util.List;

public class Property {
    private final String ownerEmail;
    private final String city;
    private final String address;
    private final String description;
    private final List<Room> rooms = new ArrayList<>();

    public Property(String ownerEmail, String city, String address, String description) {
        this.ownerEmail = ownerEmail;
        this.city = city;
        this.address = address;
        this.description = description;
    }

    public String getOwnerEmail() { return ownerEmail; }
    public String getCity() { return city; }
    public String getAddress() { return address; }
    public String getDescription() { return description; }
    public List<Room> getRooms() { return rooms; }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    @Override
    public String toString() {
        return city + " | " + address + " | " + description + " | rooms=" + rooms.size();
    }
}
