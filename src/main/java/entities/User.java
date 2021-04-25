package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;
import security.errorhandling.AuthenticationException;

@Entity
//førhen users, nu user
@Table(name = "user")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  
  @Basic(optional = false)
  @NotNull
  @Column(name = "user_name", length = 25)
  private String userName;
  
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "email", length = 25)
  private String email;
  
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "user_pass")
  private String userPass;
  
  @JoinTable(name = "user_role", joinColumns = {
    @JoinColumn(name = "email", referencedColumnName = "email")}, inverseJoinColumns = {
    @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
  
  @ManyToOne (cascade = CascadeType.PERSIST)
  private Role role;
  
  
  public User() {}

   public boolean verifyPassword(String pw){
        return (BCrypt.checkpw(pw, this.userPass));
    }

  public User(String email, String userName, String userPass) {
    
    this.email = email;
    this.userName = userName;
    this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt(12));

  }
  
 //hensigten med denne er at ændre kode. Men jeg ved ikke om det virker.
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
  
  public Role getRole() {
      return role;
  }
  
  public void setRole(Role role) {
      this.role = role;
  }
  
  

}
