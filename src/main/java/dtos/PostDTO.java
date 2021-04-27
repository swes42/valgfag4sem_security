
package dtos;

import entities.Post;
import java.util.Date;

/**
 *
 * @author miade
 */
public class PostDTO {
    
    private int id;
    private String title;
    private String text;
    private Date dateCreated;
    private String username;

    public PostDTO() {}

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.text = post.getText();
        this.dateCreated = post.getDateCreated();
        this.username = post.getUser().getUserName();
    }
    
    public long getId() {
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
    
}
