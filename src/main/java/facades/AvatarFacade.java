package facades;

import dtos.AvatarDTO;
import entities.Avatar;
import entities.User;
import errorhandling.AvatarNotFound;
import errorhandling.MissingInput;
import errorhandling.UserNotFound;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.sql.rowset.serial.SerialBlob;
import security.errorhandling.AuthenticationException;
import security.errorhandling.UserNotFoundException;

public class AvatarFacade implements IAvatarFacade {

    private static AvatarFacade instance;
    private static EntityManagerFactory emf;
    
    
    private AvatarFacade() {}
    
    
    public static AvatarFacade getAvatarFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AvatarFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public User getUserFromDB(EntityManager em, String username) throws UserNotFound {
        User user = em.find(User.class, username);

        if (user == null) {
             throw new UserNotFound(String.format("No user with provided username found", username));
         } else {
             return user;
        }
    }
    
//    private static String encodeFileToBase64Binary(File file) throws Exception {
//        FileInputStream fileInputStreamReader = new FileInputStream(file);
//        byte[] bytes = new byte[(int)file.length()];
//        fileInputStreamReader.read(bytes);
//        return Base64.encode(bytes);
//    }

    @Override
    public AvatarDTO addAvatar(byte[] avatarImage, String username) throws MissingInput {
        EntityManager em = getEntityManager();
        
        User user = null;
        Avatar avatar = null;
        
//        byte[] avatarByte;
//        Blob avatarBlob;
        
        try {
//            avatarByte = Base64.decode(avatarImage);
//            avatarBlob = new SerialBlob(avatarByte);
            
            em.getTransaction().begin();
            user = getUserFromDB(em, username);
            avatar = new Avatar(avatarImage, user);
            
            em.persist(avatar);
            em.getTransaction().commit();
            
        } catch (UserNotFound ex) {
            Logger.getLogger(AvatarFacade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            em.clear();
        }
        return new AvatarDTO(avatar);
    }
    
    @Override
    public AvatarDTO deleteAvatar(int a_id) throws AvatarNotFound {
        EntityManager em = getEntityManager();
        
        Avatar avatar = em.find(Avatar.class, a_id);
        User user = avatar.getUser();
        
        if (avatar == null) {
            throw new AvatarNotFound(String.format("Avatar with id: (%d) not found", a_id));
        } else {
            try {
                em.getTransaction().begin();
                    em.remove(avatar);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
            return new AvatarDTO(avatar);
        }
    }
    
    public User temp(String username) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null) {
                throw new AuthenticationException("This didn't work...");
            }
        } finally {
            em.close();
        }
        return user;
    }
    
    @Override
    public AvatarDTO updateAvatar(int a_id, byte[] avatarImage, String username) throws AvatarNotFound, MissingInput {
        deleteAvatar(a_id);
        return addAvatar(avatarImage, username);
    }

    @Override
    public AvatarDTO getAvatarByUser(String username) throws UserNotFound {
        System.out.println("AAA");
        try {
            User testU = temp(username);
            System.out.println("testU: " + testU.getUserName());
        } catch (AuthenticationException ex) {
            Logger.getLogger(AvatarFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        // Find user på username
        // then find a by user
        EntityManager em = getEntityManager();
        User user = em.find(User.class, username);
        if (user == null) {
            System.out.println("WHAAAAAT?");
        }
                
                
                
        Query b = em.createQuery("SELECT a.avatarImage FROM Avatar a WHERE a.user.userName =:username");
        System.out.println("username: " + username);
        b.setParameter("username", username);
        System.out.println("b query: " + b.getResultList());
        Query q = em.createNamedQuery("Avatar.getAllRowsByUser");
        q.setParameter("username", username);
        System.out.println("BBB");
        System.out.println(q.getResultList());
        Avatar avatar = (Avatar) q.getSingleResult();
        
//        Blob blob = avatar.getAvatarImage();
//        
        try {
            if (avatar.getAvatarImage() == null) {
                throw new AvatarNotFound("No avatar/profile picture uploaded yet!");
            }
//            int blobLength = (int) blob.length();
//            byte[] blobAsBytes = blob.getBytes(1, blobLength);
//            String avatarBase64 = Base64.getEncoder().encodeToString(blobAsBytes);
//            
            System.out.println("CCC");
        } catch (AvatarNotFound ex) {
            Logger.getLogger(AvatarFacade.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(AvatarFacade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            em.close();
        }
        return new AvatarDTO(avatar);
    }

}
