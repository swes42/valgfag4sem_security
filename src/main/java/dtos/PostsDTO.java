
package dtos;

import entities.Post;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author miade
 */
public class PostsDTO {
    
    List<PostDTO> all = new ArrayList();

    public PostsDTO(List<Post> postEntities) {
        postEntities.forEach((p) -> {
            all.add(new PostDTO(p));
        });
    }

    public List<PostDTO> getAll() {
        return all;
    }
    
    
}
