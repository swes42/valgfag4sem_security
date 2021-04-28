/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.JOSEException;
import dtos.PostDTO;
import entities.Post;
import errorhandling.MissingInput;
import facades.PostFacade;
import java.text.ParseException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import security.JWTAuthenticationFilter;
import security.UserPrincipal;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

/**
 *
 * @author miade
 */

@Path("post")
public class PostResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;
    private static final PostFacade facade = PostFacade.getPostFacade(EMF);
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addPost(String post, @HeaderParam("x-access-token") String token) throws MissingInput, ParseException, JOSEException, AuthenticationException {
        UserPrincipal user = JWT.getUserPrincipalFromTokenIfValid(token);
        PostDTO pDTO = GSON.fromJson(post, PostDTO.class);
        PostDTO addPost = facade.addPost(pDTO.getTitle(), pDTO.getText(), user.getName());
        return GSON.toJson(addPost);
    }
    
    
}
