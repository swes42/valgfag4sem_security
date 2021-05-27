
package facades;

import dtos.AvatarDTO;
import errorhandling.AvatarNotFound;
import errorhandling.MissingInput;
import errorhandling.UserNotFound;
import java.sql.Blob;

/**
 *
 * @author malthew
 */
public interface IAvatarFacade {
  public AvatarDTO addAvatar(Blob avatarImage, String username) throws MissingInput;
  public AvatarDTO updateAvatar(int id, Blob avatarImage, String username) throws AvatarNotFound, MissingInput;
  public AvatarDTO deleteAvatar(int id) throws AvatarNotFound;
  public AvatarDTO getAvatarByUser(String username) throws UserNotFound; 

}