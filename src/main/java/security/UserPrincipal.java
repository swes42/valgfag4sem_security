package security;

import entities.Role;
import entities.User;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserPrincipal implements Principal {

  private String username;
//  private List<String> roles = new ArrayList<>();
  private String role;
  private String email;

  /* Create a UserPrincipal, given the Entity class User*/
//  public UserPrincipal(User user) {
//    this.username = user.getUserName();
//    this.role = user.getRole();
//  }

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
}