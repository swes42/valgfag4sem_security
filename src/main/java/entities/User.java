package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;
import security.errorhandling.AuthenticationException;

/*
Hi guys. 
Jeg har kun udkommenteret det jeg har rettet, 
istedet for at slette, i tilfælde af det skulle være
helt forkert.*/


@Entity
@Table(name = "users")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "user_name", length = 25)
  private String userName;
  
  //Jeg er i tvivl om vi skal gøre email til id.
  //Indtil videre har jeg bare sat username som id.
  //Email kolonne tilføjet.
  @Column(name = "email", length = 25)
  private String email;
  
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "user_pass")
  private String userPass;
  
  @JoinTable(name = "user_roles", joinColumns = {
    @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
    @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
  
  //Ny relation - kommentar længere nede.
  @ManyToOne (cascade = CascadeType.PERSIST)
  private Role role;
  
  
  /* 
  
  Jeg tænker ikke at en bruger skal kunne have mange 
  roller. For at "styrer" sikkerheden, skal udefra
  kunder kun kunne tildeles "user" rollen.
  
  Jeg ville umiddelbart mene at vi skal lave det om,
  til ManyToOne - så hver bruger kun får én rolle. 
  
  således:
  
  @ManyToOne (cascade = CascadeType.PERSIST)
  private Role role;
  
  Men dette er blot mine tanker <3 
  Lad mig høre hvad I tænker.
  
  
  @ManyToMany
  private List<Role> roleList = new ArrayList<>(); 
  

  public List<String> getRolesAsStrings() {
    if (roleList.isEmpty()) {
      return null;
    }
    List<String> rolesAsStrings = new ArrayList<>();
    roleList.forEach((role) -> {
        rolesAsStrings.add(role.getRoleName());
      });
    return rolesAsStrings;
  }*/

  public User() {}

   public boolean verifyPassword(String pw){
        //return(pw.equals(userPass));
        return (BCrypt.checkpw(pw, this.userPass));
    }

   //String email tilføjet som parameter
  public User(String userName, String email, String userPass) {
    this.userName = userName;
    this.email = email;
    //this.userPass = userPass;
    this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt(12));

  }
  
  //tilføj metode som kan ændre password
  public void modifyPass (String prePass, String postPass) throws AuthenticationException {
      if(BCrypt.checkpw(prePass, this.userPass)) {
          this.userPass = BCrypt.hashpw(postPass, BCrypt.gensalt(12));
      } else {
          throw new AuthenticationException("Previous password did not match");
      }
  }

  public String getUserName() {
    return userName;
  }
  
  public String getEmail(){
      return email;
  }
  
  public void setEmail(String email) {
      this.email = email;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPass() {
    return this.userPass;
  }

  public void setUserPass(String userPass) {
    //this.userPass = userPass;
    this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt(12));

  }

  /* Slettes, hvis vi vælger at der ikke skal være
  valget for at vælge rolle.
  
  public List<Role> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<Role> roleList) {
    this.roleList = roleList;
  }

  public void addRole(Role userRole) {
    roleList.add(userRole);
  }
  */
  
  //tilføjet
  public Role getRole() {
      return role;
  }
  
  //tilføjet
  public void setRole(Role role) {
      this.role = role;
  }
  
  

}
