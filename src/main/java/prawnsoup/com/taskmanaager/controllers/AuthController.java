package prawnsoup.com.taskmanaager.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import prawnsoup.com.taskmanaager.models.User;
import prawnsoup.com.taskmanaager.repositories.UserRepository;
import prawnsoup.com.taskmanaager.services.TokenService;
import prawnsoup.com.taskmanaager.services.AuthService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService userService;

    private TokenService tokenService;

    private UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager , UserRepository userRepository , TokenService tokenService , AuthService userService ) {
        this.authenticationManager = authenticationManager;
        this.userRepository=userRepository;
        this.tokenService=tokenService;
        this.userService=userService;
    }
    /**

     Registers a new user with the specified details

     @param user A JSON object containing the user details (username, password, and email)

     @return ResponseEntity with HTTP status code and message
            - If user is already registered, returns a 404 NOT FOUND status code
            - If registration is successful, returns a 200 OK status code
     @throws Exception if registration fails due to an unexpected error
     */

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){

            //return ResponseEntity.badRequest().body("Username already exists");

        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            return ResponseEntity.notFound().build();

        }

        userService.register(user);
        return ResponseEntity.ok().build();
    }
    /**

     This endpoint handles user authentication and generates a JWT token.
     @param request the HttpServletRequest object
     @param user the user object containing the username and password for authentication
     @param rememberMe a boolean flag indicating if the user has requested to be remembered
     @return a ResponseEntity containing the JWT token if authentication is successful, else a 404 error
     */

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody User user , @RequestHeader(value = "remember", defaultValue = "false") boolean rememberMe){
        //check if user is in database

        if(userRepository.findByUsername(user.getUsername()).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        //user= userRepository.findByUsername(user.getUsername()).get();
        UsernamePasswordAuthenticationToken ut= new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(), Collections.EMPTY_LIST);
        //ut.setDetails(new WebAuthenticationDetails);
        Authentication authenticate= authenticationManager.authenticate(ut);
        String token= tokenService.generateToken(authenticate); // token is generated on login and not registration
        //updates security context
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        if(rememberMe){
            //functionality yet to be implemented
            userService.remember(user , token );
        }
        else{
            userService.notremember();
        }
        //SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()+": "
        //returns token object
        return ResponseEntity.ok(token);

    }



  //  @PostMapping("/test")
    //public ResponseEntity<?> test(){
        //return ResponseEntity.ok().build();
  //  }






}
