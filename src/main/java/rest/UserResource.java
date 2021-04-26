package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.UserDTO;
import entities.User;
import security.errorhandling.MissingInputException;
import security.errorhandling.UserAlreadyExistsException;
import security.errorhandling.UserNotFoundException;
import facades.UserFacade;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

@Path("info")
public class UserResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;
    private static final UserFacade facade = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public String addUser(String user) throws MissingInputException, UserAlreadyExistsException {
        UserDTO u = GSON.fromJson(user, UserDTO.class);
        UserDTO uAdded = facade.addUser(u.getUsername(),u.getPassword());
        return GSON.toJson(uAdded);
    }
    
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("{username}")
    @RolesAllowed({"user", "admin"})

    public String editUser(@PathParam("username") String username, String user) throws MissingInputException, UserNotFoundException {
        UserDTO u = GSON.fromJson(user, UserDTO.class);
        UserDTO uToEdit = new UserDTO(u.getUsername(),u.getPassword());
        UserDTO editedUser = facade.editUser(uToEdit);
        return GSON.toJson(editedUser);
    }
    
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{username}")
    @RolesAllowed("user")

    public String deleteUser(@PathParam("username") String username, String password) throws UserNotFoundException, AuthenticationException {
        facade.deleteUser(username,password);
        return "{\"status\":\"deleted\"}";
    }
    
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("admin/{username}")
    @RolesAllowed("admin")
    public String deleteUserAdmin(@PathParam("username") String username) throws UserNotFoundException {
        facade.deleteUserAdmin(username);
        return "{\"status\":\"deleted\"}";
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("allusers")
    @RolesAllowed("admin")
    public String getAllUsers() {
        return GSON.toJson(facade.getAllUsers());
    }
}