/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.JSONValue;
import dtos.AvatarDTO;
import dtos.PostDTO;
import dtos.PostsDTO;
import entities.User;
import errorhandling.AvatarNotFound;
import errorhandling.MissingInput;
import errorhandling.PostNotFound;
import errorhandling.UserNotFound;
import facades.AvatarFacade;
import facades.PostFacade;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Base64;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.rowset.serial.SerialBlob;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.w3c.dom.Element;
import security.JWTAuthenticationFilter;
import security.UserPrincipal;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

/**
 *
 * @author miade
 */

@Path("avatar")
public class AvatarResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;
    private static final AvatarFacade facade = AvatarFacade.getAvatarFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static JWTAuthenticationFilter JWT = new JWTAuthenticationFilter();
    @Context
    SecurityContext securityContext;
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    @POST	
    @RolesAllowed({"user", "admin"})
    @Path("/upload")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addAvatar(String avatar, @HeaderParam("x-access-token") String token) 
            throws MissingInput, ParseException, JOSEException, AuthenticationException, SQLException {
        
        Object obj = JSONValue.parse(avatar);
        JSONObject jsonObject = (JSONObject) obj;
        
        UserPrincipal user = JWT.getUserPrincipalFromTokenIfValid(token);
        AvatarDTO aDTO = GSON.fromJson(avatar, AvatarDTO.class);
        
        String avatarString = (String) jsonObject.get("image");
//        byte[] avatarByte = Base64.getMimeDecoder().decode(avatarString);
//        Blob avatarBlob = new SerialBlob(avatarByte);
//        
        AvatarDTO addAvatar = facade.addAvatar(avatarString, user.getName());
        return GSON.toJson(addAvatar);
    }
    
    @DELETE
    @RolesAllowed({"user", "admin"})
    @Path("/delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String deleteAvatar(@PathParam("id") int id, @HeaderParam("x-access-token") String token) 
            throws ParseException, JOSEException, AuthenticationException, AvatarNotFound {
        
        UserPrincipal userP = JWT.getUserPrincipalFromTokenIfValid(token);
        
        AvatarDTO aDeleted = facade.deleteAvatar(id);
        aDeleted.setUsername(userP.getName());
        return GSON.toJson(aDeleted);
    }
    
    @Path("/{username}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAvatarByUser(@PathParam("username") String username) throws UserNotFound {
        AvatarDTO avatar = facade.getAvatarByUser(username);
//        
//        Blob blob = avatar.getAvatarImage();
//        
//        String s = GSON.toJson(avatar);
//        Object obj = JSONValue.parse(s);
//        JSONObject jsonObject = (JSONObject) obj;
//        
        return GSON.toJson(avatar);
    }
    
}