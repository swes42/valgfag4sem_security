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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

/**
 *
 * @author miade
 */
@Entity
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "title", length = 20)
    private String title;
    @NotNull
    @Column(name = "text", length = 100)
    private String text;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateCreated")
    private java.util.Date dateCreated;

    @ManyToOne(cascade = { CascadeType.PERSIST })
    private User user;

    public Post() {}

    public Post(String title, String text, Date dateCreated) {
        this.title = title;
        this.text = text;
        this.dateCreated = dateCreated;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
