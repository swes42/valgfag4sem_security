
package dto;

import entities.User;

/**
 *
 * @author Selina A.S.
 */
public class UserDTO {
    
    private String username;
    private String password;
    //Denne field er jeg usikker på, men tanken var der.
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
