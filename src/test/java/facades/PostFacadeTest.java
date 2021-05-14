
package facades;

import dtos.PostDTO;
import dtos.PostsDTO;
import entities.Post;
import entities.Role;
import entities.User;
import errorhandling.MissingInput;
import errorhandling.PostNotFound;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import utils.EMF_Creator;


//@Disabled

public class PostFacadeTest {
    
   private static EntityManagerFactory emf;
   private static PostFacade facade;
   private static User user, admin;
   private static Post p1, p2;
   
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
           // em.createNativeQuery("alter table Posts AUTO_INCREMENT = 1").executeUpdate();
            
            
        user = new User("user1", "pass1");
        admin = new User("admin1", "pass1");
        
//        Role r1 = new Role("user");
//        Role r2 = new Role("admin");
        p1 = new Post("p1", "p1", new Date(System.currentTimeMillis()));
        p2 = new Post("p2", "p2", new Date(System.currentTimeMillis()));
        
//        user.addRole(r1);
//        admin.addRole(r2);
        
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

//    @AfterEach
//    public void tearDown() {
//    EntityManager em = emf.createEntityManager();
//    em.getTransaction().begin();
//    em.createNativeQuery("truncate table posts").executeUpdate();
//    em.createNativeQuery("truncate table users").executeUpdate();
//    em.createNativeQuery("truncate table roles").executeUpdate();
//    em.createNativeQuery("truncate table user_roles").executeUpdate();
//    em.getTransaction().commit();    
//    }
    
    @Test
    public void testAddPost() throws MissingInput {
       PostDTO addedPost = facade.addPost("This is the title","This is the text", user.getUserName());
       assertNotNull(addedPost.getTitle());
    }
    
    @Test
    public void testAddPost2() throws MissingInput {
        System.out.println("---- Tester addPost ----");
        
        String title = "Im adding this post";
        String text = "My username is Username1";
        Date dateT = new Date(System.currentTimeMillis());
        
        Post p = new Post(title, text, dateT);
            p.setUser(user);
        
        PostDTO result = facade.addPost(title, text, p.getUser().getUserName());
        
        PostDTO expResult = new PostDTO(1, title, text, p.getUser().getUserName());
        
        expResult.setId(expResult.getId());
        assertEquals(expResult.getTitle(), result.getTitle());
    }
    
    
    @Test
    public void testGetAll() {
        System.out.println("---- Tester getAll ----");
        
        EntityManagerFactory _emf = null;
        PostFacade pFac = PostFacade.getPostFacade(_emf);
        
        int expResult = 2;
        PostsDTO result = pFac.getAllPosts();
        
        assertEquals(expResult, result.getAll().size());
    }
    
    @Test
    public void testGetAllContains() {
        System.out.println("---- Tester getAll contains ----");
        
        EntityManagerFactory _emf = null;
        PostFacade pFac = PostFacade.getPostFacade(_emf);
        
        PostsDTO result = pFac.getAllPosts();
        
        PostDTO p1DTO = new PostDTO(p1);
        PostDTO p2DTO = new PostDTO(p2);
        
        List<String> resultTitles = new ArrayList();
        
        for (PostDTO p : result.getAll()){
            resultTitles.add(p.getTitle());
        }
        
        assertThat(resultTitles, containsInAnyOrder(p1DTO.getTitle(), p2DTO.getTitle()));
    }

    
    @Test
    public void testDeletePost() throws PostNotFound {
        System.out.println("---- Tester deletePost ----");
        
        int id = p2.getId();
        
        EntityManagerFactory _emf = null;
        PostFacade pFac = PostFacade.getPostFacade(_emf);
        
        PostDTO expResult = new PostDTO(p2);
        PostDTO result = pFac.deletePost(id);
        
        assertEquals(expResult.getId(), result.getId());
    }
    
    
    @Test
    public void testEditPost() throws PostNotFound, MissingInput {
        System.out.println("---- Tester editPost ----");
        
        PostDTO pDTO = new PostDTO(p1);
        
        EntityManagerFactory _emf = null;
        PostFacade pFac = PostFacade.getPostFacade(_emf);
        
        PostDTO expResult = new PostDTO(p1);
    
        expResult.setTitle("This is a new title!");
    
        
        pDTO.setTitle("This is a new title!");
    
        
        PostDTO result = pFac.editPost(pDTO);
     
        assertEquals(expResult.getTitle(), result.getTitle());
    }
    
    @Test
    public void testGetPostsByUser() {
        System.out.println("---- Tester getPostsByUser ----");
        
        EntityManagerFactory _emf = null;
        PostFacade pFac = PostFacade.getPostFacade(_emf);
        
        String username = user.getUserName();
        
        List<Post> postList = user.getPosts();
        
        PostsDTO expList = new PostsDTO(postList);
        PostsDTO result = pFac.getPostsByUser(username);
        
        List<String> expTitleList = new ArrayList();
        for (PostDTO p : expList.getAll()){
            expTitleList.add(p.getTitle());
        }
        List<String> resTitleList = new ArrayList();
        for (PostDTO p : result.getAll()){
            resTitleList.add(p.getTitle());
        }
        
        assertEquals(expTitleList, resTitleList);
    }
}





























