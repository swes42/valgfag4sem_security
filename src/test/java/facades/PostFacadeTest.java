/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PostDTO;
import entities.Post;
import entities.User;
import errorhandling.MissingInput;
import errorhandling.UserNotFound;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.EMF_Creator;

/**
 *
 * @author miade
 */
public class PostFacadeTest {
    
   private static EntityManagerFactory emf;
    private static PostFacade facade;
    private static User u1, u2;

    public PostFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = PostFacade.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        
        u1 = new User("Username1", "password1");
        u2 = new User("Username2", "password2");
        
        Post p1 = new Post("First Post", "Hello World!", new Date(System.currentTimeMillis()));
        Post p2 = new Post("Got a cat!", "My cat is so sweet.", new Date(System.currentTimeMillis()));
        Post p3 = new Post("Moving today", "Carrying furniture is very hard", new Date(System.currentTimeMillis()));
        
        u1.setPosts(p1);
        u1.setPosts(p2);
        u2.setPosts(p3);
        
        p1.setUser(u1);
        p2.setUser(u1);
        p3.setUser(u2);
        
        try {
            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.createQuery("delete from Post").executeUpdate();
            em.persist(u1);
            em.persist(u2);
            
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

 // Denne test skulle blot sikre at vi kunne hente en user fra db
 // den virker ikke da metoden er private   
    /*
    @Test
    public void testFetchingAUser() throws UserNotFound {
        EntityManager em = emf.createEntityManager();
        User u = facade.getUserFromDB(em, "Username1");
        
        String exp = "Username1";
        String result = u.getUserName();
        
        assertEquals(exp, result);
    }
    */
    /*
    @Test
    public void testAddPost() throws MissingInput {
        
        Post p = new Post("Im adding this post", "My username is Username1", new Date(System.currentTimeMillis()));
        User u = new User("Username1", "password1");
        
        facade.addPost(p, u);
        
        String exp = "Username1";
        String result = p.getUser().getUserName();
        
        assertEquals(exp, result);
    }
    
    */
    
    @Test
    public void testAddPost() throws MissingInput {
        System.out.println("Tester addPost");
        
        String title = "Im adding this post";
        String text = "My username is Username1";
        Date dateT = new Date(System.currentTimeMillis());
        
        Post p = new Post(title, text, dateT);
            p.setUser(u1);
        System.out.println("Her: " + p.getUser().getUserName());
        
        PostDTO result = facade.addPost(p);
        
        PostDTO expResult = new PostDTO(title, text, dateT, u1.getUserName());
        
        expResult.setId(expResult.getId());
        assertEquals(expResult.getTitle(), result.getTitle());
    }
    
}
