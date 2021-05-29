/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.TempFacade;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import security.errorhandling.AuthenticationException;
import security.errorhandling.UserNotFoundException;
import utils.EMF_Creator;

/**
 *
 * @author miade
 */

@Path("temp")
public class TempR {
    
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;
    private static final TempFacade facade = TempFacade.getTempFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    SecurityContext securityContext;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{username}")
    @RolesAllowed("user")
    public String test(@PathParam("username") String username) throws UserNotFoundException, AuthenticationException {
        return GSON.toJson(facade.temp(username));
    }
    
}
