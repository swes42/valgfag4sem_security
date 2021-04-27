/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PostDTO;
import entities.Post;
import errorhandling.MissingInput;
import facades.PostFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
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
    private static final PostFacade facade = PostFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    SecurityContext securityContext;
    
    
    @POST	
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addPost(String post) throws MissingInput {
        PostDTO pDTO = GSON.fromJson(post, PostDTO.class);
        Post p = new Post(pDTO.getTitle(), pDTO.getText(), pDTO.getDateCreated());
        
        PostDTO pAdded = facade.addPost(p);
        return GSON.toJson(pAdded);
    }
    
    
}
