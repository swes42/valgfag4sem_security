
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
    private String username;
    private Date dateCreated;
    private Date lastEdited;

    // Bruges i facade test:
    public PostDTO(int postID, String title, String text, String username, 
            Date dateC, Date dateL) {
        this.id = postID;
        this.title = title;
        this.text = text;
        this.username = username;
        this.dateCreated = dateC;
        this.lastEdited = dateL;
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.text = post.getText();
        this.username = post.getUser().getUserName();
        this.dateCreated = post.getDateCreated();
        this.lastEdited = post.getLastEdited();
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

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }
    
    
    
}
