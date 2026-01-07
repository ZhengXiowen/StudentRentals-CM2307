package studentrentals.repository;

import java.util.ArrayList;
import java.util.List;

import studentrentals.model.Property;

public class PropertyRepository {
    private final List<Property> properties = new ArrayList<>();

    public void save(Property p) {
        properties.add(p);
    }

    public List<Property> findAll() {
        return new ArrayList<>(properties);
    }

    public List<Property> findByOwnerEmail(String ownerEmail) {
        List<Property> out = new ArrayList<>();
        for (Property p : properties) {
            if (p.getOwnerEmail().equalsIgnoreCase(ownerEmail)) {
                out.add(p);
            }
        }
        return out;
    }
}
