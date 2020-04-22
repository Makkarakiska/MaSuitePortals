package dev.masa.masuiteportals.core.services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import dev.masa.masuiteportals.bungee.MaSuitePortals;
import dev.masa.masuiteportals.core.models.Portal;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;

public class PortalService {

    @Getter
    private HashMap<String, Portal> portals = new HashMap<>();
    private Dao<Portal, Integer> portalDao;
    private MaSuitePortals plugin;

    @SneakyThrows
    public PortalService(MaSuitePortals plugin) {
        this.plugin = plugin;
        this.portalDao = DaoManager.createDao(plugin.getApi().getDatabaseService().getConnection(), Portal.class);
        TableUtils.createTableIfNotExists(plugin.getApi().getDatabaseService().getConnection(), Portal.class);
    }


    /**
     * Creates a new {@link Portal}
     *
     * @param portal portal to create
     * @return created portal
     */
    @SneakyThrows
    public boolean createPortal(Portal portal) {
        portalDao.create(portal);
        portals.put(portal.getName(), portal);
        return true;
    }

    /**
     * Updates existing {@link Portal}
     *
     * @param portal portal to updated
     * @return updated portal
     */
    @SneakyThrows
    public boolean updatePortal(Portal portal) {
        portalDao.update(portal);
        portals.put(portal.getName(), portal);
        return true;
    }

    /**
     * Removes existing {@link Portal}
     *
     * @param portal portal to remove
     */
    @SneakyThrows
    public boolean removePortal(Portal portal) {
        portalDao.delete(portal);
        portals.remove(portal.getName());
        return true;
    }

    /**
     * Get {@link Portal} from database or cache
     *
     * @param name name of the portal
     * @return returns {@link Portal} or null
     */
    public Portal getPortal(String name) {
        return this.loadPortal(name);
    }

    /**
     * Get all {@link Portal} from cache
     *
     * @return returns a list of {@link Portal}
     */
    public List<Portal> getAllPortals() {
        return new ArrayList<>(portals.values());
    }


    /**
     * Initialize warps for use
     */
    @SneakyThrows
    public void initializePortals() {
        portalDao.queryForAll().forEach(portal -> portals.put(portal.getName(), portal));
    }

    /**
     * Get {@link Portal} from database or cache
     *
     * @param name name of the portal
     * @return returns {@link Portal} or null
     */
    @SneakyThrows
    private Portal loadPortal(String name) {
        if (portals.containsKey(name)) {
            return portals.get(name);
        }

        // Search home from database
        Portal portal = portalDao.queryForEq("name", name).stream().findFirst().orElse(null);

        // Add home into cache if not null
        if (portal != null) {
            portals.put(name, portal);
        }
        return portal;
    }
}
