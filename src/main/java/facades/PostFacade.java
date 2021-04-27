package facades;

import dtos.PostDTO;
import dtos.UserDTO;
import entities.Post;
import entities.User;
import errorhandling.MissingInput;
import errorhandling.UserNotFound;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PostFacade implements IPostFacade{

    private static PostFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private PostFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PostFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PostFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    
    
    @Override
    public PostDTO addPost(Post p) throws MissingInput{
       
     // Der må ikke mangle noget input i formen i frontend:
        checkFormMissingInput(p.getTitle(), p.getText());
        
        EntityManager em = getEntityManager();
        
        try {
            
            em.getTransaction().begin();
                // Skal finde en eksisterende bruger:
                User user = getUserFromDB(em, p.getUser().getUserName());
                System.out.println(user.getUserName());
             // Sætter brugeren på post:  
                p.setUser(user);
                
                // Fejler her:
                em.persist(p);
                
            em.getTransaction().commit();
        } catch (UserNotFound ex) {
            Logger.getLogger(PostFacade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            em.close();
        }
        return new PostDTO(p);
    }
    
    private User getUserFromDB(EntityManager em, String username) throws UserNotFound {
        User user = em.find(User.class, username);

        if (user == null) {
             throw new UserNotFound(String.format("No user with provided username found", username));
         } else {
             return user;
        }
    }

    private void checkFormMissingInput(String title, String text) throws MissingInput {
        if ((title.length() == 0) || (text.length() == 0)){
            throw new MissingInput("If you are gonne post a new post you need to "
                    + "give it a title and write something.");
        }
    }


    
    

}
