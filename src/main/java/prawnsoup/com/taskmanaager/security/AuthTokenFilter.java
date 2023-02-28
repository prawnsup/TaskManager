package prawnsoup.com.taskmanaager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import prawnsoup.com.taskmanaager.models.User;
import prawnsoup.com.taskmanaager.repositories.UserRepository;
import prawnsoup.com.taskmanaager.services.TokenService;
import prawnsoup.com.taskmanaager.services.UserDetailsServiceImpl;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

//@Component
//@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final String URI_TO_ALLOW_POST = "/api/auth/login";
  //  @Autowired
    private UserDetailsServiceImpl userDetailsService;
    //@Autowired
    private TokenService tokenService;
   // @Autowired
    private UserRepository userRepository;
    //@Override
   // protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
     //   return request.getMethod().equals(HttpMethod.POST.name()) || !request.getRequestURI().equals(URI_TO_ALLOW_POST);
   // }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearer=getBearerToken(request);
      // if(isAuthenticated()) {
        //   UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          // String user_name = userDetails.getUsername();
          // if (bearer == null) {
            //   bearer = "Bearer " + tokenCache.get(user_name);
             //  response.setHeader("Authorization", bearer);
          // }
        //}
        try {
            logger.debug("Authentication starting");
            if(bearer!=null && tokenService.isValid(bearer) && userRepository.findByUsername(tokenService.getUsername(bearer)).isPresent()) {
                String username = tokenService.getUsername(bearer);
                User user = userRepository.findByUsername(username).get();
                UsernamePasswordAuthenticationToken up = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), Collections.EMPTY_LIST);
                up.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(up);
                logger.debug("Authentication is set");
            }
            else{
                logger.error("Empty token");
            }
        }catch (Exception e){
            logger.error("Authentication could not be set");
            }
        filterChain.doFilter(request,response);

        }

    private String getBearerToken(HttpServletRequest request){
        String auth_header= request.getHeader("Authorization");
        if(auth_header != null && auth_header.startsWith("Bearer ")){
            return auth_header.substring(7);
        }
        return null;
    }




}
