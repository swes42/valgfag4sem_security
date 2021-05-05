package entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author miade
 */
@Entity
@Table(name = "posts")
@NamedQueries({
    @NamedQuery(name = "Post.deleteAllRows", query = "DELETE FROM Post"),
    @NamedQuery(name = "Post.deleteAllRowsByUser", query = "DELETE FROM Post p WHERE p.user.userName LIKE :username"),
    @NamedQuery(name = "Post.getAllRows", query = "SELECT p from Post p ORDER BY p.lastEdited DESC"),
    @NamedQuery(name = "Post.getAllRowsByUser", 
            query = "SELECT p from Post p WHERE p.user.userName LIKE :username")
})
        
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotNull
    @Column(name = "title", length = 20)
    private String title;
    @NotNull
    @Column(name = "text", length = 100)
    private String text;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateCreated")
    private java.util.Date dateCreated;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastEdited")
    private Date lastEdited;

    @JoinColumn(name = "username")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

    public Post() {
    }

    public Post(String title, String text, User user){
        this.title = title;
        this.text = text; 
        this.user = user;
        this.dateCreated = new Date();
        this.lastEdited = new Date();
    }
    
    // Bruges i PostFacadeTest
    public Post(String title, String text, Date date){
        this.title = title;
        this.text = text;
        this.dateCreated = new Date();
        this.lastEdited = new Date();
        
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user != null){
            this.user = user;
            user.setPosts(this);
        } else {
            this.user = null;
        }
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited() {
        this.lastEdited = new Date();
    }
   

    

    
    
}
