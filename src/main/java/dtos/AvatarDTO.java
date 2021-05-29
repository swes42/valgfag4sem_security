
package dtos;

import entities.Avatar;
import java.sql.Blob;
import java.util.Base64;

/**
 *
 * @author malthew
 */
public class AvatarDTO {
    
    private int id;
//    private String avatarName;
    private byte[] avatarImage;
//    private String avatarImage;
    private String username; // Er ikke sikker om der skal bruges et username til en Avatar eller ej.

//    public AvatarDTO(int id, String avatarName, byte[] avatarImage, String username) {
//        this.id = id;
//        this.avatarName = avatarName;
//        this.avatarImage = avatarImage;
//        this.username = username;
//    }
    
    public AvatarDTO(int id, byte[] avatarImage, String username) {
        this.id = id;
//        this.avatarName = avatarName;
        this.avatarImage = avatarImage;
        this.username = username;
    }

    public AvatarDTO(Avatar avatar) {
        this.id = avatar.getId();
//        this.avatarName = avatar.getAvatarName();
        this.avatarImage = avatar.getAvatarImage();
        this.username = avatar.getUser().getUserName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getAvatarName() {
//        return avatarName;
//    }
//
//    public void setAvatarName(String avatarName) {
//        this.avatarName = avatarName;
//    }

    public byte[] getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(byte[] avatarImage) {
        this.avatarImage = avatarImage;
    }
    
//    public String getAvatarImage() {
//        return avatarImage;
//    }
//
//    public void setAvatarImage(String avatarImage) {
//        this.avatarImage = avatarImage;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
