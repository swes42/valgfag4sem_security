package security.errorhandling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Noell Zane
 */
@Provider
public class UserAlreadyExistsMapper implements ExceptionMapper<UserAlreadyExistsException> 
{
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();   
    @Override
    public Response toResponse(UserAlreadyExistsException ex) {
       Logger.getLogger(UserAlreadyExistsMapper.class.getName()).log(Level.SEVERE, null, ex);
       ExceptionDTO err = new ExceptionDTO(404,ex.getMessage());
//       ExceptionDTO err = new ExceptionDTO(Response.Status.NOT_FOUND.getStatusCode(),ex.getMessage());
       return Response
               .status(404)
               .entity(gson.toJson(err))
               .type(MediaType.APPLICATION_JSON)
               .build();
	}
}