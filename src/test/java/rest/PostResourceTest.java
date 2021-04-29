package rest;

import dtos.PostDTO;
import entities.Post;
import entities.User;
import errorhandling.API_Exception;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import security.LoginEndpoint;
import security.errorhandling.AuthenticationException;
//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PostResourceTest {
    

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Post p1,p2,p3;
    private static User user, admin;
    private static LoginEndpoint loginEndpoint;
    
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }
    
    @AfterAll
    public static void closeTestServer(){
        //System.in.read();
         //Don't forget this, if you called its counterpart in @BeforeAll
         EMF_Creator.endREST_TestWithDB();
         httpServer.shutdownNow();
    }
    
    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
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
        
//        Role r1 = new Role("user");
//        Role r2 = new Role("admin");
        p1 = new Post("First Post", "Hello World!", new Date(System.currentTimeMillis()));
        p2 = new Post("Got a cat!", "My cat is so sweet.", new Date(System.currentTimeMillis()));
        p3 = new Post("Driving away", "Im driving to a resort!", new Date(System.currentTimeMillis()));
        
//        user.addRole(r1);
//        admin.addRole(r2);
        
        p1.setUser(user);
        p2.setUser(admin);
        p3.setUser(user);
        
            em.persist(user);
            em.persist(admin);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    @Test
    public void testServerIsUp() {
        System.out.println("---- Testing is server UP ----");
        given().when().get("/post").then().statusCode(200);
    }
   
    
    @Test
    public void getAllPosts(){
        System.out.println("---- Testing getting all ----");
        
            List<PostDTO> postsDTOs;
        
            postsDTOs = given()
                .contentType("application/json")
                .when()
                .get("/post/all")
                .then()
                .extract().body().jsonPath().getList("allPosts", PostDTO.class);
            
            List<String> resultTitles = new ArrayList();
            
            for (PostDTO p : postsDTOs){
                resultTitles.add(p.getTitle());
            }
            
            PostDTO p1DTO = new PostDTO(p1);
            PostDTO p2DTO = new PostDTO(p2);
            PostDTO p3DTO = new PostDTO(p3);
            
            assertThat(resultTitles, containsInAnyOrder(p1DTO.getTitle(), p2DTO.getTitle(), p3DTO.getTitle()));
    }
    
    // Fordi man skal logge ind for at adde en post og slette en post er det 
    // svært bare at sætte en header på i test, så vi har testet det i Postman    
    /*
    @Test
    public void testAddPost() throws AuthenticationException, API_Exception{
        System.out.println("---- Testing add post ----");
        System.out.println(p1.getTitle());
        System.out.println("");
        
        given()
            .contentType(ContentType.JSON)
            .body(new PostDTO(p1))
            .when()
            .post("post")
            .then()
            .header("x-access-token", NEED-TOKEN-FROM-LOGGEDIN )
            .body("title", equalTo("First Post"))
            .body("text", equalTo("Hello World!"))
            .body("username", equalTo("user1"));
    }

    @Test
      public void testDelete() throws Exception {
        
        PostDTO post = new PostDTO(p1);
        
        given()
            .contentType("application/json")
            .delete("/post/" + post.getId())
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200.getStatusCode());
        
        List<PostDTO> postDTOs;
        
        postDTOs = given()
                .contentType("application/json")
                .when()
                .get("/post/all")
                .then()
                .extract().body().jsonPath().getList("allPosts", PostDTO.class);

     

        PostDTO p2DTO = new PostDTO(p2);
        PostDTO p3DTO = new PostDTO(p3);

        assertThat(postDTOs, containsInAnyOrder(p2DTO, p3DTO));
            
    }
    // Det samme gælder edit
*/

}


















