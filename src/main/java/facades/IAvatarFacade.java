
package facades;

import dtos.AvatarDTO;
import errorhandling.AvatarNotFound;
import errorhandling.MissingInput;
import errorhandling.UserNotFound;
import java.sql.Blob;
import java.util.Base64;

/**
 *
 * @author malthew
 */
public interface IAvatarFacade {

  public AvatarDTO addAvatar(String avatarImage, String username) throws MissingInput;
  public AvatarDTO updateAvatar(int id, String avatarImage, String username) throws AvatarNotFound, MissingInput;
  public AvatarDTO deleteAvatar(int id) throws AvatarNotFound;
  public AvatarDTO getAvatarByUser(String username) throws UserNotFound; 

}