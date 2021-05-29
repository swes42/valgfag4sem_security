package entities;

import java.io.Serializable;
import java.sql.Blob;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author malthew
 */
@Entity
@Table(name = "avatars")
@NamedQueries({
    @NamedQuery(name = "Avatar.deleteAllRows", query = "DELETE FROM Avatar"),
    @NamedQuery(name = "Avatar.getAllRows", query = "SELECT a from Avatar a"),
    @NamedQuery(name = "Avatar.getAllRowsByUser", 
            query = "SELECT a from Avatar a WHERE a.user.userName LIKE :username")
})
        
public class Avatar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
//    @NotNull
//    @Column(name = "avatarName", length = 20)
//    private String avatarName;
    @NotNull
    @Column(name = "avatarImage", columnDefinition = "MEDIUMBLOB")
     private String avatarImage;
//    private Blob avatarImage;

    @JoinColumn(name = "username")
    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;

    public Avatar() {
    }

    public Avatar(String avatarImage, User user) {
        // this.avatarName = avatarName;
        this.avatarImage = avatarImage;
        this.user = user;
    }
    
    // Skal bruges i AvatarFacadeTest
    public Avatar(String avatarImage){
        // this.avatarName = avatarName;
        this.avatarImage = avatarImage;
    }
    
//    public Avatar(String avatarImage, User user) {
////        this.avatarName = avatarName;
//        this.avatarImage = avatarImage;
//        this.user = user;
//    }
//    
//    // Skal bruges i AvatarFacadeTest
//    public Avatar(String avatarImage){
////        this.avatarName = avatarName;
//        this.avatarImage = avatarImage;
//    }
    
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

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

//    public String getAvatarImage() {
//        return avatarImage;
//    }
//
//    public void setAvatarImage(String avatarImage) {
//        this.avatarImage = avatarImage;
//    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user != null){
            this.user = user;
            user.setAvatar(this);
        } else {
            this.user = null;
        }
    }
    
    
    
}
