package prawnsoup.com.taskmanaager.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import prawnsoup.com.taskmanaager.models.User;
import prawnsoup.com.taskmanaager.repositories.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    private TokenService tokenService;


    public UserDetailsServiceImpl(UserRepository userRepository , TokenService tokenService){
        this.userRepository = userRepository;
        this.tokenService=tokenService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(userRepository.findByUsername(username).isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        User user= userRepository.findByUsername(username).get();
        List<GrantedAuthority> authorities = Collections.EMPTY_LIST;

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword() , authorities);
    }

    public User loadUserFromPricipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }
        Jwt principal = (Jwt) authentication.getPrincipal(); //oath2 principal is saved as token
        String username= tokenService.getUsername(principal.getTokenValue());
        User user= userRepository.findByUsername(username).get();
        return user;
    }
}
