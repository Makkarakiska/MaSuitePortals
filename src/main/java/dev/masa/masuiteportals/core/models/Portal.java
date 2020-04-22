package dev.masa.masuiteportals.core.models;


import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import dev.masa.masuitecore.core.objects.Location;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Table(name = "masuite_portals")
public class Portal {

    @DatabaseField(generatedId = true)
    private int id;

    @NonNull
    @DatabaseField(unique = true, canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String type;

    @DatabaseField(canBeNull = false)
    private String destination;

    @DatabaseField(canBeNull = false)
    private String fillType;

    @DatabaseField
    private String server;
    @DatabaseField
    private String world;
    @DatabaseField
    private Double minX;
    @DatabaseField
    private Double minY;
    @DatabaseField
    private Double minZ;
    @DatabaseField
    private Double maxX;
    @DatabaseField
    private Double maxY;
    @DatabaseField
    private Double maxZ;

    public Location getMinLocation() {
        return new Location(server, world, minX, minY, minZ);
    }

    public void setMinLocation(Location loc) {
        this.server = loc.getServer();
        this.world = loc.getWorld();
        this.minX = loc.getX();
        this.minY = loc.getY();
        this.minZ = loc.getZ();
    }

    public Location getMaxLocation() {
        return new Location(server, world, maxX, maxY, maxZ);
    }

    public void setMaxLocation(Location loc) {
        this.server = loc.getServer();
        this.world = loc.getWorld();
        this.maxX = loc.getX();
        this.maxY = loc.getY();
        this.maxZ = loc.getZ();
    }

    public String serialize() {
        return new Gson().toJson(this);
    }

    public Portal deserialize(String json) {
        return new Gson().fromJson(json, Portal.class);
    }
}
