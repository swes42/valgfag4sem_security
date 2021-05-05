package facades;

import dtos.PostDTO;
import dtos.PostsDTO;
import entities.Post;
import entities.User;
import errorhandling.MissingInput;
import errorhandling.PostNotFound;
import errorhandling.UserNotFound;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

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
    
    
 // Skal have testet om lastEdited virker - evt. med application test
    @Override
    public PostDTO editPost(PostDTO pDTO) throws PostNotFound, MissingInput {
    
        checkFormMissingInput(pDTO.getTitle(), pDTO.getText()); 
        
        EntityManager em = getEntityManager();
        Post post = em.find(Post.class, pDTO.getId());
        
        
        if (post == null) {
                throw new PostNotFound(String.format("No post with provided id found", pDTO.getId()));
        } else {
            post.setTitle(pDTO.getTitle());
            post.setText(pDTO.getText());
            post.setLastEdited();
            //post.setUser(pDTO.getUsername());
            System.out.println("Post: " + post.getTitle() + ", " + post.getText());
            try {
                em.getTransaction().begin();
                    em.merge(post);
                em.getTransaction().commit();
                
                return new PostDTO(post);
               
            } finally {  
            em.close();
          }
        }    
    }
    
    
    @Override
    public PostsDTO getPostsByUser(String username){
        
        EntityManager em = getEntityManager();
        
        Query q = em.createNamedQuery("Post.getAllRowsByUser");
        q.setParameter("username", username);
        
        // Laver en liste med alle posts:
        List<Post> posts = q.getResultList();
        
        try {
            
            if (posts.size() == 0) {
                 throw new PostNotFound("No posts posted yet!");
             } 
            
        } catch (PostNotFound ex) {
            Logger.getLogger(PostFacade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            em.close();
        }
        return new PostsDTO(posts);
    }

}
