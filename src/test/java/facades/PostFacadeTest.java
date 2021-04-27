
package facades;

import dtos.PostDTO;
import entities.Post;
import entities.Role;
import entities.User;
import errorhandling.MissingInput;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import utils.EMF_Creator;


@Disabled

public class PostFacadeTest {
    
   private static EntityManagerFactory emf;
   private static PostFacade facade;
   private static User user, admin;
   
   public PostFacadeTest (){}

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = PostFacade.getPostFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }
    
    
    
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Post").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            
            
        user = new User("user1", "pass1");
        admin = new User("admin1", "pass1");
        
        Role r1 = new Role("user");
        Role r2 = new Role("admin");
        Post p1 = new Post("First Post", "Hello World!", new Date(System.currentTimeMillis()));
        Post p2 = new Post("Got a cat!", "My cat is so sweet.", new Date(System.currentTimeMillis()));
        
        user.addRole(r1);
        admin.addRole(r2);
        
        p1.setUser(user);
        p2.setUser(admin);
        
            em.persist(user);
            em.persist(admin);
            em.persist(p1);
            em.persist(p2);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testAddPost() throws MissingInput {
       Post testPost = new Post("Im adding this post", "My username is Username1", new Date(System.currentTimeMillis()));
       PostDTO postDTO = new PostDTO(testPost);
       PostDTO addedPost = facade.addPost(postDTO, user.getUserName());
       assertTrue(addedPost.getId() != testPost.getId());
    }
    
}
