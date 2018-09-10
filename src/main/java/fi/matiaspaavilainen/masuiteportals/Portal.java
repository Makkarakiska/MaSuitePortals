package fi.matiaspaavilainen.masuiteportals;

import fi.matiaspaavilainen.masuitecore.managers.Location;

public class Portal {

    private String name;
    private String type;
    private String destination;
    private String fillType;
    private Location location;

    public Portal(){}

    public Portal(String name, String type, String destination, String fillType, Location location) {
        this.name = name;
        this.type = type;
        this.destination = destination;
        this.fillType = fillType;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFillType() {
        return fillType;
    }

    public void setFillType(String fillType) {
        this.fillType = fillType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
