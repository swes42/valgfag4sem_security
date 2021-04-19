package facades;

import dto.UserDTO;
import entities.Role;
import entities.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import security.JWTAuthenticationFilter;
import security.errorhandling.AuthenticationException;

/*
Nedenstående linjer som har // er linjer jeg har tilføjet
*/
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;
    //tilføjet
    private static JWTAuthenticationFilter JWT;

    private UserFacade() {
    }

    
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }
    
    /*
    Metoder som jeg tænker vi skal have med..
    
    //public void validationPass(String userPass) throws ? {} 
    
    //public void addRoles(EntityManager em) {}

    //private void checkUserExists(User user, EntityManager em) throw ? {}
    
    //private void checkUserNameExists(String username, EntityManager em) throws? {}
    
    //public UserDTO editUser(UserDTO userdto) throws? {}
    
    //public UserDTO deleteUser(String email?){}
    
    //public UserDTO getUser(String email?) {}
    
    
    
    //
    
    */
    
    public UserDTO addUser(UserDTO userDTO) throws AuthenticationException {
        
        //validationPass(userDTO.getPassword());
        
        EntityManager em = emf.createEntityManager();
        User user = new User (userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword());
        
        //addRoles(em);
        //user.setRole(getUserRole(em));
        //checkExisting(user, em);
        
        try{
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            //tilføjet return statement, men kunne ikke udkommentere for så ville den ikke compile.
            return new UserDTO(user);
            
        } finally {
            em.close();
        }
    }
    
    public UserDTO deleteUser(String email) {
        EntityManager em = emf.createEntityManager();
        
            try {
                em.getTransaction().begin();
                User user = em.find(User.class, email);
                em.remove(user);
                em.getTransaction().commit();
                
                return new UserDTO(user);
                
            } finally {
                em.close();
            }
    }
    
    //Skal ændres tror jeg. Til noget med at lave en liste userDTOlist??
    //Den skal bruges i UserResource
    public List<User> getAllUsers(){
        EntityManager em = emf.createEntityManager();
        try {
            List<User> userList = em.createQuery("SELECT u from User u").getResultList();
            return userList;
        } finally {
            em.close();
        }
    }

}
