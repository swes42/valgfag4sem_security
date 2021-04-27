package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@NamedQuery(name = "Post.deleteAllRows", query = "DELETE FROM Post")
        
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

    public Post() {}

    public Post(User user, String text, String title){
        this.id = id;
        this.title = title;
        this.text = text; 
        this.dateCreated = new Date();
        this.user = user;
    }
    
    // Bruges i PostFacadeTest
    public Post(String text, String title, Date date){
        this.title = title;
        this.text = text;
        this.dateCreated = new Date();
        
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
   

    

    
    
}
