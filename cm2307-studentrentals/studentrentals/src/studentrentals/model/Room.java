package studentrentals.model;

public class Room {
    private final String roomName;
    private final String type;
    private final double weeklyPrice;
    private boolean available;

    public Room(String roomName, String type, double weeklyPrice) {
        this.roomName = roomName;
        this.type = type;
        this.weeklyPrice = weeklyPrice;
        this.available = true;
    }

    public String getRoomName() { return roomName; }
    public String getType() { return type; }
    public double getWeeklyPrice() { return weeklyPrice; }
    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return roomName + " (" + type + "), £" + weeklyPrice + "/week, " + (available ? "AVAILABLE" : "NOT AVAILABLE");
    }
}
