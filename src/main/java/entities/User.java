package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "User.deleteAllRows", query = "DELETE FROM User"),
    @NamedQuery(name = "User.getByUsername", query = "SELECT u FROM User u WHERE u.userName LIKE :userName"),
    @NamedQuery(name = "User.getAllRows", query = "SELECT u from User u")
})

public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "user_name", length = 25)
  private String userName;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "user_pass")
  private String userPass;
  @JoinTable(name = "user_roles", joinColumns = {
  @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
  @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
  @ManyToMany
  private List<Role> roleList = new ArrayList<>();
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "dateCreated")
  private java.util.Date dateCreated;
    
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "lastEdited")
  private java.util.Date lastEdited;
  public List<String> getRolesAsStrings() {
    if (roleList.isEmpty()) {
      return null;
    }
    List<String> rolesAsStrings = new ArrayList<>();
    roleList.forEach((role) -> {
        rolesAsStrings.add(role.getRoleName());
      });
    return rolesAsStrings;
  }

  @OneToMany(mappedBy = "user")
  private List<Post> posts = new ArrayList();
  
  public User() {}

   public boolean verifyPassword(String pw){
//        return(pw.equals(userPass));
//Check password using Bcrypt.
        return (BCrypt.checkpw(pw, this.userPass));    
    }

  public User(String userName, String userPass) {
        this.userName = userName;
//      this.userPass = userPass;
//Hash password using BCrypt and gensalt. to generate salt.
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt(12));
        this.dateCreated = new Date();
        this.lastEdited = new Date();
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserPass() {
    return this.userPass;
  }

  public void setUserPass(String userPass) {
    this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt(12));
  }

  public List<Role> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<Role> roleList) {
    this.roleList = roleList;
  }

  public void addRole(Role userRole) {
    roleList.add(userRole);
  }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    
    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(Post post) {
        if (post != null){
            posts.add(post);
        }
    }
}
