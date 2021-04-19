/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.User;

/**
 *
 * @author Selina A.S.
 */
public class UserDTO {
    
    /*mulige fields, men slet ikke endeligt.
    Det var blot for at komme igang med noget <3
    */
    private String username;
    private String password;
    //Kunne det ikke være ekstra sikkert med nedenstående field?
    private String prePassword; 
    private String role;
    private String email;
    
    public UserDTO(User user) {
        this.username = user.getUserName();
        this.email = user.getEmail();
    }
    
    public UserDTO(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrePassword() {
        return prePassword;
    }

    public void setPrePassword(String prePassword) {
        this.prePassword = prePassword;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
    
}
