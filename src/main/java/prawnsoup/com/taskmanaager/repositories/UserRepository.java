package prawnsoup.com.taskmanaager.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import prawnsoup.com.taskmanaager.models.User;
// Interface for performing database operations with the User entity.
// E.g saving entity , 
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     *
     * @param username
     * @return optional list containing list of user objects
     */
    //Optional<User> findById(Long id);
    Optional<User> findByUsername (String username);


    // can check Role if i feel like implementing that in the future
   // List<User> findAllByRole (Role role);


}
