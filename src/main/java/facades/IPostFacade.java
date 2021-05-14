/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PostDTO;
import dtos.PostsDTO;
import errorhandling.MissingInput;
import errorhandling.PostNotFound;

/**
 *
 * @author miade
 */
public interface IPostFacade {
  public PostDTO addPost(String title, String text, String username) throws MissingInput;    
  public PostsDTO getAllPosts();  
  public PostDTO deletePost(int id) throws PostNotFound;
  public PostDTO editPost(PostDTO p) throws PostNotFound, MissingInput;  
  
  public PostsDTO getPostsByUser(String username);
  /*
  public PersonDTO getPerson(int id) throws PersonNotFound; 
  public PersonsDTO getAllPersons();  
  public PersonDTO editPerson(PersonDTO p) throws PersonNotFound, MissingInput;  
*/
}