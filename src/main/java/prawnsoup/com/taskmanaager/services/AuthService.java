package prawnsoup.com.taskmanaager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import prawnsoup.com.taskmanaager.models.User;
import prawnsoup.com.taskmanaager.repositories.UserRepository;

import java.util.Map;

@Service
public class AuthService {

    private Map<String, String > currentUser;
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder , Map<String , String > currentUser ){

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUser= currentUser;
    }

    public User register(User user) {
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        return userRepository.save(user);
    }

    public void remember(User user , String token){
        currentUser.put(user.getUsername(), token);
    }
    public void notremember(){
        currentUser.clear();
    }





}
