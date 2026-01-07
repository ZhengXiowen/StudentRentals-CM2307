package studentrentals.service;

import java.util.List;

import studentrentals.model.Property;
import studentrentals.model.Room;
import studentrentals.repository.PropertyRepository;

public class PropertyService {
    private final PropertyRepository repo;

    public PropertyService(PropertyRepository repo) {
        this.repo = repo;
    }

    public Property createProperty(String ownerEmail, String city, String address, String description) {
        Property p = new Property(ownerEmail, city, address, description);
        repo.save(p);
        return p;
    }

    public boolean addRoomToProperty(Property property, String roomName, String type, double weeklyPrice) {
        if (weeklyPrice <= 0) return false;
        property.addRoom(new Room(roomName, type, weeklyPrice));
        return true;
    }

    public List<Property> allProperties() {
        return repo.findAll();
    }

    public List<Property> propertiesByOwner(String ownerEmail) {
        return repo.findByOwnerEmail(ownerEmail);
    }
}
