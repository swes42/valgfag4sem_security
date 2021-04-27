
package dtos;

import entities.Post;
import java.util.Date;

/**
 *
 * @author miade
 */
public class PostDTO {
    
    private long id;
    private String title;
    private String text;
    private java.util.Date dateCreated;
    private String username;

    public PostDTO() {}

    public PostDTO(Post post) {
        this.title = post.getTitle();
        this.text = post.getText();
        this.dateCreated = post.getDateCreated();
        this.username = post.getUser().getUserName();
    }
    
    // Til test:
    public PostDTO(String title, String text, Date dateT, String username) {
        this.title = title;
        this.text = text;
        this.dateCreated = dateT;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PostDTO other = (PostDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    
    
    
    
}
