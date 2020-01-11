package fi.matiaspaavilainen.masuiteportals.core.models;


import fi.matiaspaavilainen.masuitecore.core.objects.Location;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "masuite_portals")
@NamedQuery(
        name = "findPortalByName",
        query = "SELECT p FROM Portal p WHERE p.name = :name"
)
public class Portal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "destination")
    private String destination;

    @Column(name = "filltpye")
    private String fillType;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "x", column = @Column(name = "minX")),
            @AttributeOverride(name = "y", column = @Column(name = "minY")),
            @AttributeOverride(name = "z", column = @Column(name = "minZ")),
            @AttributeOverride(name = "yaw", column = @Column(name = "yaw", insertable = false, updatable = false)),
            @AttributeOverride(name = "pitch", column = @Column(name = "pitch", insertable = false, updatable = false)),
            @AttributeOverride(name = "server", column = @Column(name = "server"))
    })
    private Location minLoc;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "x", column = @Column(name = "maxX")),
            @AttributeOverride(name = "y", column = @Column(name = "maxY")),
            @AttributeOverride(name = "z", column = @Column(name = "maxZ")),
            @AttributeOverride(name = "yaw", column = @Column(name = "yaw", insertable = false, updatable = false)),
            @AttributeOverride(name = "pitch", column = @Column(name = "pitch", insertable = false, updatable = false)),
            @AttributeOverride(name = "server", column = @Column(name = "server", insertable = false, updatable = false))
    })
    private Location maxLoc;

    public String toString() {
        String minLoc = getMinLoc().getWorld() + ":" + getMinLoc().getX() + ":" + getMinLoc().getY() + ":" + getMinLoc().getZ();
        String maxLoc = getMaxLoc().getX() + ":" + getMaxLoc().getY() + ":" + getMaxLoc().getZ();
        return getName() + ":" + getType() + ":" + getDestination() + ":" + getFillType() + ":" + minLoc + ":" + maxLoc;
    }
}