package facades;

import dtos.UserDTO;
import entities.Post;
import entities.Role;
import entities.User;
import errorhandling.PostNotFound;
import security.errorhandling.MissingInputException;
import security.errorhandling.UserAlreadyExistsException;
import security.errorhandling.UserNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import security.errorhandling.AuthenticationException;

public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
        private EntityManager getEntityManager() {
        return emf.createEntityManager();
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

    public UserDTO addUser(String username, String password) throws MissingInputException, UserAlreadyExistsException{ //Add person
        EntityManager em = getEntityManager();
        User addedUser = new User(username, password);
        if (username.length() == 0 || password.length() == 0) { //Checks to see if our inputs are empty
            throw new MissingInputException("Username and/or password is missing");
        }
        try {
            em.getTransaction().begin();
            TypedQuery query = em.createQuery("SELECT u FROM User u WHERE u.userName = :username", User.class).setParameter("username", username);
           
            List<User> users = query.getResultList(); //Get list of Users that matches query
            if (users.size() > 0) { //If User size is bigger than zero, that means it exists.
                throw new UserAlreadyExistsException("User already exists, please try another username");
            } else {
            addedUser.addRole(em.createQuery("SELECT r FROM Role r WHERE r.roleName = :role_name",Role.class).setParameter("role_name", "user").getSingleResult());
            em.persist(addedUser);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(addedUser);
    }
     public UserDTO editUser(UserDTO u) throws MissingInputException, UserNotFoundException {
        EntityManager em = getEntityManager();
        if (u.getUsername().length() == 0 || u.getPassword().length() == 0) { //Checks to see if our inputs are empty
            throw new MissingInputException("Username and/or password is missing");
        }
        User user = em.find(User.class, u.getUsername());
        if (user == null) {
            throw new UserNotFoundException(String.format("User with username: (%s) not found", u.getUsername()));
        }
            user.setUserPass(u.getPassword());
        try {
            em.getTransaction().begin();
            user.setLastEdited(new Date()); //We changed the date of last edited
            em.merge(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(user);
    }
        public void deleteUser(String username, String password) throws UserNotFoundException, AuthenticationException, PostNotFound {
        EntityManager em = getEntityManager();
        User user = instance.getVeryfiedUser(username, password);
        
        if (user == null || !user.verifyPassword(password)) {
            throw new UserNotFoundException(String.format("User with username: (%s) not found", user.getUserName()));
        }
        
        User u = em.find(User.class, user.getUserName());
        
        List<Post> posts = u.getPosts();
        if (posts.isEmpty()) {
            throw new PostNotFound(String.format("User (%s) has no posts", username));
        } else {
            try {
                em.getTransaction().begin();
                Query q = em.createNamedQuery("Post.deleteAllRowsByUser");
                q.setParameter("username", username);
                for (Post p : posts){
                    em.remove(p);
                }
                em.remove(u);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
//        return new UserDTO(user);
        }
    }
        public void deleteUserAdmin(String username) throws UserNotFoundException, PostNotFound {
        EntityManager em = getEntityManager();
        User user = em.find(User.class, username);
        if (user == null) {
            throw new UserNotFoundException(String.format("User with username: (%s) not found", user.getUserName()));
        }
            
        List<Post> posts = user.getPosts();
        try {
            em.getTransaction().begin();

            Query q = em.createNamedQuery("Post.deleteAllRowsByUser");
            q.setParameter("username", username);
            for (Post p : posts){
                em.remove(p);
            }

            em.remove(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
//        return new UserDTO(user);
        

    }
         public List<UserDTO> getAllUsers() {
        {
            EntityManager em = emf.createEntityManager();
            List<UserDTO> userDTOs = new ArrayList<>();
            try {
                em.getTransaction().begin();
                TypedQuery query = em.createQuery("SELECT u FROM User u", User.class);
//                TypedQuery query = em.createQuery("SELECT u.userName FROM User u", User.class);
                List<User> users = query.getResultList();
                for (User user : users) {
                    userDTOs.add(new UserDTO(user));
                }
            } finally {
                em.close();
            }
            return userDTOs;
        }
    }
}
