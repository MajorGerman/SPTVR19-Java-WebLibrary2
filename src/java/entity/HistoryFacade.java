package entity;

import entity.History;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Melnikov
 */
@Stateless
public class HistoryFacade extends AbstractFacade<History> {

    @PersistenceContext(unitName = "SPTVR19-Laabe-Java-WebShopPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HistoryFacade() {
        super(History.class);
    }

    public List<History> findBoughtProducts(Person pers) {
        try {
            return em.createQuery("SELECT h FROM History h WHERE h.returnDate = NULL AND h.pers = :pers")
                    .setParameter("pers", pers)
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
}