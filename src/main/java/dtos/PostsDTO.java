
package dtos;

import entities.Post;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author miade
 */
public class PostsDTO {
    
    List<PostDTO> allPosts = new ArrayList();

    public PostsDTO(List<Post> postEntities) {
        postEntities.forEach((p) -> {
            allPosts.add(new PostDTO(p));
        });
    }

    public List<PostDTO> getAll() {
        return allPosts ;
    }
    
    
}
