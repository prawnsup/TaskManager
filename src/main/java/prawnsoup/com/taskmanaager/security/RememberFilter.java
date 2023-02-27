package prawnsoup.com.taskmanaager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.filter.OncePerRequestFilter;
import prawnsoup.com.taskmanaager.models.User;
import prawnsoup.com.taskmanaager.repositories.UserRepository;
import prawnsoup.com.taskmanaager.services.TokenService;
import org.springframework.mock.web.MockHttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

//@Component
//@Order(1)
public class RememberFilter extends OncePerRequestFilter {
    private Map<String,String>  CurrentUser;
    private UserRepository userRepository;
    private TokenService tokenService;
  //  @Autowired
    public RememberFilter(Map<String,String> CurrentUser , UserRepository userRepository , TokenService tokenService){
        this.CurrentUser=CurrentUser;
        this.userRepository=userRepository;
        this.tokenService=tokenService;
    }
    //@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("Starting reminderfilter");
        if(!CurrentUser.isEmpty() && tokenService.getBearerToken(request)==null ){
            String username = CurrentUser.keySet().iterator().next(); // gets the first element from dictionary the only element
            String token =CurrentUser.get(username);
            MockHttpServletRequest mockRequest = new MockHttpServletRequest();
            mockRequest.addHeader("Authorization", "Bearer " + token);

            HttpServletRequest Originalrequest = request;
            HttpServletRequest request1 = (HttpServletRequest) mockRequest;
            logger.info("header added: "+ token);
        }
        else{
            logger.debug("header is specified already or current user is empty");
        }

        filterChain.doFilter(request,response);
    }
}
