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
    public AvatarDTO addAvatar(String avatarImage, String username) throws MissingInput {
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
    
        @Override
    public AvatarDTO updateAvatar(int a_id, String avatarImage, String username) throws AvatarNotFound, MissingInput {
        deleteAvatar(a_id);
        return addAvatar(avatarImage, username);
    }

    @Override
    public AvatarDTO getAvatarByUser(String username) throws UserNotFound {
        EntityManager em = getEntityManager();
        
        Query q = em.createNamedQuery("Avatar.getAllRowsByUser");
        q.setParameter("username", username);
        
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