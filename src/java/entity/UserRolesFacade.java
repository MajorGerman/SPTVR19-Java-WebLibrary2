/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Georg
 */
@Stateless
public class UserRolesFacade extends AbstractFacade<UserRoles> {

    @PersistenceContext(unitName = "SPTVR19-Laabe-Java-WebShopPU")
    private EntityManager em;

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
}
