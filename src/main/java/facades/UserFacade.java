package facades;

import dto.UserDTO;
import entities.Role;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import security.JWTAuthenticationFilter;
import security.errorhandling.AuthenticationException;

public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

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
    
    public UserDTO getUser(String email) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, email);
        
        if(user == null) {
                throw new AuthenticationException("User could not be found");
        }
        return new UserDTO(user);
    }
    
    public List<UserDTO> getAll(){
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery query = em.createQuery("SELECT u from User u", User.class);
            List<User> userList = query.getResultList();
            List<UserDTO> userDTOlist = new ArrayList<>();
            
            for (User user : userList) {
                userDTOlist.add(new UserDTO(user));
            }
            return userDTOlist;
        } finally {
            em.close();
        }
    }
    
    public UserDTO addUser(UserDTO userDTO) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user = new User (userDTO.getEmail(), userDTO.getUsername(), userDTO.getPassword());
        
        //kalder metode som er lavet i klassen.
        addStarterRole(em);
        //kalder metode der er lavet i klassen.
        user.setRole(getUserRole(em));
        
        
        try{
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return new UserDTO(user);
            
        } finally {
            em.close();
        }
    }
    
    //Beklager manglede kreativitet mht. metode-navn haha
    //Må jeg ikke gerne have entity med som parameteren?
    public void addStarterRole(EntityManager em){
        Query q = em.createQuery("SELECT r FROM Role r");
        
        if (q.getResultList().isEmpty()) {
            em.getTransaction().begin();
            em.persist(new Role("user"));
            em.persist(new Role("admin"));
            em.getTransaction().commit();
        }
    }
    
    public Role getUserRole(EntityManager em) {
        return em.find(Role.class, "user");
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
}
