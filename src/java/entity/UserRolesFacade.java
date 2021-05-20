package entity;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserRolesFacade extends AbstractFacade<UserRoles> {

    @PersistenceContext(unitName = "SPTVR19-Laabe-Java-WebShopPU")
    private EntityManager em;
    
    @EJB
    private RoleFacade roleFacade;
    @EJB
    private UserRolesFacade userRolesFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserRolesFacade() {
        super(UserRoles.class);
    }

    public boolean isRole(String roleName, User user) {
        try {
            UserRoles userRoles = (UserRoles) em.createQuery("SELECT userroles FROM UserRoles userRoles WHERE userRoles.role.roleName = :roleName AND userRoles.user = :user").setParameter("roleName", roleName).setParameter("user", user).getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public UserRoles findByUserAndRoleName(User user, String roleName) {
        try {
            return (UserRoles) em.createQuery("SELECT u FROM UserRoles u WHERE u.user = :user AND u.role.roleName = :roleName")
                    .setParameter("user", user)
                    .setParameter("roleName", roleName)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public String getTopRoleForUser(User user) {
        if(user == null) return "";
        List<UserRoles> listUserRoles = em.createQuery("SELECT userRoles FROM UserRoles userRoles WHERE userRoles.user = :user")
                .setParameter("user", user)
                .getResultList();
        for(int i=0;i<listUserRoles.size();i++){
            if("admin".equals(listUserRoles.get(i).getRole().getRoleName())){
                return "admin";
            }
        }
        for(int i=0;i<listUserRoles.size();i++){
            if("manager".equals(listUserRoles.get(i).getRole().getRoleName())){
                return "manager";
            }
        }
        for(int i=0;i<listUserRoles.size();i++){
            if("customer".equals(listUserRoles.get(i).getRole().getRoleName())){
                return "customer";
            }
        }
        return "";
    }
    
    public void setRole(String roleName, User user) {
        Role role = roleFacade.findByName(roleName);
        UserRoles userRoles = new UserRoles(user, role);
        userRolesFacade.create(userRoles);
    }
}
