package facades;

import dtos.PostDTO;
import dtos.PostsDTO;
import entities.Post;
import entities.User;
import errorhandling.MissingInput;
import errorhandling.PostNotFound;
import errorhandling.UserNotFound;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class PostFacade implements IPostFacade{

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
    
    
    @Override
    public PostDTO addPost(String title, String text, String username) throws MissingInput{
        EntityManager em = getEntityManager();
        
        checkFormMissingInput(title, text);
        
        User user = null;
        Post post = null;
        
        try {
            em.getTransaction().begin();
            user = getUserFromDB(em, username);
            post = new Post(title, text, user);
            
            em.persist(post);
            em.getTransaction().commit();
       
        } catch (UserNotFound ex) {
            Logger.getLogger(PostFacade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            em.close();
        }
        return new PostDTO(post);
    }
    
    
    public User getUserFromDB(EntityManager em, String username) throws UserNotFound {
        User user = em.find(User.class, username);

        if (user == null) {
             throw new UserNotFound(String.format("No user with provided username found", username));
         } else {
             return user;
        }
    }

    public void checkFormMissingInput(String title, String text) throws MissingInput {
        if ((title.length() == 0) || (text.length() == 0)){
            throw new MissingInput("If you are gonne post a new post you need to "
                    + "give it a title and write something.");
        }
    }

    
    @Override
    public PostsDTO getAllPosts() {
        EntityManager em = getEntityManager();
        try {
            return new PostsDTO(em.createNamedQuery("Post.getAllRows").getResultList());
        } finally{  
            em.close();
        }   
    }
    
    
    @Override
    public PostDTO deletePost(int p_id) throws PostNotFound {
         EntityManager em = getEntityManager();
         
          Post post = em.find(Post.class, p_id);
          User user = post.getUser();
          
          if (post == null) {
            throw new PostNotFound(String.format("Post with id: (%d) not found", p_id));
          } else {
                try {
                    em.getTransaction().begin();
                        em.remove(post);
                     // Hvis flere har den samme addresse skal addressen ikke slettes
                        user.getPosts().remove(post);
                        if (user.getPosts().size() < 1){
                            em.remove(user);
                        }
                    em.getTransaction().commit();
                } finally {
                    em.close();
            }
            return new PostDTO(post);
          }
    }

}
