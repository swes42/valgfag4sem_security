package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.JOSEException;
import dto.UserDTO;
import facades.UserFacade;
import java.text.ParseException;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import security.JWTAuthenticationFilter;
import security.UserPrincipal;
import security.errorhandling.AuthenticationException;

/**
 * Det som står med // er mine tilføjelser
 */


@Path("user")
public class UserResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
 
    private static JWTAuthenticationFilter JWT = new JWTAuthenticationFilter();
    public static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);

    
    @GET
    @RolesAllowed("user")
    @Path("count")
    @Produces({MediaType.APPLICATION_JSON})
    public String countUsers() {
        int usersCount = USER_FACADE.getAll().size();
        return "{\"count\":" + usersCount + "}";
    }
    
    @GET
    @Path("profile")
    @RolesAllowed({"user", "admin"})
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String getUser(@HeaderParam("x-access-token") String token) throws ParseException, JOSEException, AuthenticationException {
        UserPrincipal user = JWT.getUserPrincipalFromTokenIfValid(token);
        UserDTO userDTO = USER_FACADE.getUser(user.getEmail());
        return GSON.toJson(userDTO);
    }
            
            
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String addUser(String user) throws AuthenticationException {
        UserDTO userDTO = GSON.fromJson(user, UserDTO.class);
        UserDTO newUserDTO = USER_FACADE.addUser(userDTO);
        return GSON.toJson(newUserDTO);
    }
    
    @DELETE
    @RolesAllowed("admin")
    @Path("{email}") //bruger email til at finde den bruger der skal slettes
    @Produces({MediaType.APPLICATION_JSON})
    public String deleteUserByEmail(@PathParam("email") String email) {
        UserDTO userDTO = USER_FACADE.deleteUser(email);
        return GSON.toJson(userDTO);
    }
    
}