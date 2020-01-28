package fi.matiaspaavilainen.masuiteportals.core.services;

import fi.matiaspaavilainen.masuitecore.core.utils.HibernateUtil;
import fi.matiaspaavilainen.masuiteportals.bungee.MaSuitePortals;
import fi.matiaspaavilainen.masuiteportals.core.models.Portal;

import javax.persistence.EntityManager;
import java.util.*;

public class PortalService {

    private EntityManager entityManager = HibernateUtil.addClasses(Portal.class).getEntityManager();
    public HashMap<String, Portal> portals = new HashMap<>();

    private MaSuitePortals plugin;

    public PortalService(MaSuitePortals plugin) {
        this.plugin = plugin;
    }


    /**
     * Creates a new {@link Portal}
     *
     * @param portal portal to create
     * @return created portal
     */
    public boolean createPortal(Portal portal) {
        entityManager.getTransaction().begin();
        entityManager.persist(portal);
        entityManager.getTransaction().commit();
        portals.put(portal.getName(), portal);
        return true;
    }

    /**
     * Updates existing {@link Portal}
     *
     * @param portal portal to updated
     * @return updated portal
     */
    public boolean updatePortal(Portal portal) {
        entityManager.getTransaction().begin();
        entityManager.merge(portal);
        entityManager.getTransaction().commit();
        portals.put(portal.getName(), portal);
        return true;
    }

    /**
     * Removes existing {@link Portal}
     *
     * @param portal portal to remove
     */
    public boolean removePortal(Portal portal) {
        entityManager.getTransaction().begin();
        entityManager.remove(portal);
        entityManager.getTransaction().commit();
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
    public void initializePortals() {
        List<Portal> portalList = entityManager.createQuery("SELECT p FROM Portal p", Portal.class).getResultList();
        portalList.forEach(portal -> portals.put(portal.getName(), portal));
    }

    /**
     * Get {@link Portal} from database or cache
     *
     * @param name name of the portal
     * @return returns {@link Portal} or null
     */
    private Portal loadPortal(String name) {
        if (portals.containsKey(name)) {
            return portals.get(name);
        }

        // Search home from database
        Portal portal = entityManager.createNamedQuery("findPortalByName", Portal.class)
                .setParameter("name", name)
                .getResultList().stream().findFirst().orElse(null);

        // Add home into cache if not null
        if (portal != null) {
            portals.put(name, portal);
        }
        return portal;
    }
}
