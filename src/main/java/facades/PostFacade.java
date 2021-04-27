package facades;

import dtos.PostDTO;
import entities.Post;
import entities.User;
import errorhandling.MissingInput;
import errorhandling.UserNotFound;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class PostFacade {

    private static PostFacade instance;
    private static EntityManagerFactory emf;
    
    
    private PostFacade() {}
    
    
    public static PostFacade getPostFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PostFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    
    public PostDTO addPost(PostDTO pDTO, String username) throws MissingInput{
        EntityManager em = getEntityManager();
        //checkFormMissingInput(title, text); // Jeg ved ikke hvorfor jeg ikke kan få lov at nævne metoden.
        User user = em.find(User.class, username);
        
        Post post = new Post(user, pDTO.getText(), pDTO.getTitle());
        
        try {
            
            em.getTransaction().begin();
            em.persist(post);
            em.getTransaction();
            return new PostDTO(post);
       
        } finally {
            em.close();
        }
    }
    
    /*
    private void getUserFromDB(EntityManager em, String username) throws UserNotFound {
        User user = em.find(User.class, username);

        if (user == null) {
             throw new UserNotFound(String.format("No user with provided username found", username));
         } else {
             return user;
        }
    }*/

    public void checkFormMissingInput(String title, String text) throws MissingInput {
        if ((title.length() == 0) || (text.length() == 0)){
            throw new MissingInput("If you are gonne post a new post you need to "
                    + "give it a title and write something.");
        }
    }


    
    

}
