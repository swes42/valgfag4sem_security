package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.UserDTO;
import entities.User;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import security.JWTAuthenticationFilter;
import security.errorhandling.AuthenticationException;

/**
 * Det som står med // er mine tilføjelser
 */


@Path("user")
public class UserResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    //tilføjet 
    private static JWTAuthenticationFilter JWT = new JWTAuthenticationFilter();
    //tilføjet 
    public static final UserFacade U_FACADE = UserFacade.getUserFacade(EMF);

    
    //scroll nederst under udkommenteringen for at se mine forslag
    
/* 
    
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }
    
    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u", User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String addUser(String user) {
        User newUser = GSON.fromJson(user, User.class);
        UserFacade.getUserFacade(EMF).addUser(newUser.getUserName(), newUser.getUserPass());
        return "New user is added";
    }
    
    */
    
    /*Nedenstående metode er helt rød fordi 
    userDTOlist ikke findes, se UserFacade linje 111 ! :) 

    @GET
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    public String getUsers(){
        List<UserDTO> userDTOlist = U_FACADE.getAllUsers();
        return GSON.toJson(userDTOlist);
    }*/
    
    @GET
    @RolesAllowed("user")
    @Path("count")
    @Produces({MediaType.APPLICATION_JSON})
    public String countUsers() {
        int usersCount = U_FACADE.getAllUsers().size();
        return "{\"count\":" + usersCount + "}";
    }
    
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String addUser(String user) throws AuthenticationException {
        UserDTO userDTO = GSON.fromJson(user, UserDTO.class);
        UserDTO newUserDTO = U_FACADE.addUser(userDTO);
        return GSON.toJson(newUserDTO);
    }
    
    @DELETE
    @RolesAllowed("admin")
    @Path("{email}")
    @Produces({MediaType.APPLICATION_JSON})
    public String deleteUserByEmail(@PathParam("email") String email) {
        UserDTO userDTO = U_FACADE.deleteUser(email);
        return GSON.toJson(userDTO);
    }
    
}