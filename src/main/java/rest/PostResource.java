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
import dtos.PostsDTO;
import entities.User;
import errorhandling.MissingInput;
import errorhandling.PostNotFound;
import facades.PostFacade;
import java.text.ParseException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    public String addPost(String post, @HeaderParam("x-access-token") String token) 
            throws MissingInput, ParseException, JOSEException, AuthenticationException {
        
        UserPrincipal user = JWT.getUserPrincipalFromTokenIfValid(token);
        PostDTO pDTO = GSON.fromJson(post, PostDTO.class);
        PostDTO addPost = facade.addPost(pDTO.getTitle(), pDTO.getText(), user.getName());
        return GSON.toJson(addPost);
    }
    
    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPosts() {
        PostsDTO posts = facade.getAllPosts();
        return GSON.toJson(posts);
    }
    
    @DELETE
    @RolesAllowed({"user", "admin"})
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String deletePerson(@PathParam("id") int id, @HeaderParam("x-access-token") String token) 
            throws PostNotFound, ParseException, JOSEException, AuthenticationException {
        
        UserPrincipal userP = JWT.getUserPrincipalFromTokenIfValid(token);
        
        PostDTO pDeleted = facade.deletePost(id);
        pDeleted.setUsername(userP.getName());
        return GSON.toJson(pDeleted);
    }
    
    @PUT
    @RolesAllowed({"user", "admin"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String editPost(@PathParam("id") int id, String post, @HeaderParam("x-access-token") String token) 
            throws PostNotFound, MissingInput, ParseException, JOSEException, AuthenticationException {
        
        UserPrincipal userP = JWT.getUserPrincipalFromTokenIfValid(token);
        
        PostDTO postDTO = GSON.fromJson(post, PostDTO.class);
        postDTO.setId(id);
        PostDTO pEdit = facade.editPost(postDTO);
        
        pEdit.setUsername(userP.getName());
        
        return GSON.toJson(pEdit);
    }
    
    @Path("{username}/all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPosts(@PathParam("username") String username) {
        PostsDTO posts = facade.getPostsByUser(username);
        return GSON.toJson(posts);
    }
}