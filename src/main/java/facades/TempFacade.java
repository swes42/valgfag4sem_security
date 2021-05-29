/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import security.errorhandling.AuthenticationException;

/**
 *
 * @author miade
 */
public class TempFacade {
    
    private static EntityManagerFactory emf;
    private static TempFacade instance; 
    
    private TempFacade() {
    }
    
    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    public static TempFacade getTempFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TempFacade();
        }
        return instance;
    }
    
    
    public User temp(String username) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null) {
                throw new AuthenticationException("This didn't work...");
            }
        } finally {
            em.close();
        }
        return user;
    }
}
