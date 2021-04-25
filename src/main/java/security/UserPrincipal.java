package security;

import java.security.Principal;

public class UserPrincipal implements Principal {

  private String username;
  private String role;
  private String email;
  
  public UserPrincipal(String username, String role, String email) {
    super();
    this.username = username;
    this.role = role;
    this.email = email;
  }

  
  @Override
  public String getName() {
    return username;
  }

  public boolean isUserInRole(String role) {
    return this.role.equals(role);
  }
  
  public String getEmail(){
      return email;
  }
}