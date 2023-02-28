package prawnsoup.com.taskmanaager.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import prawnsoup.com.taskmanaager.config.RsaKeyProperties;


import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenService  { //my own implementation of jwttokenprovider basically


    private JwtEncoder encoder;


    private JwtDecoder decoder;


    private  RsaKeyProperties rsaKeyProperties;
    public TokenService(JwtEncoder encoder, JwtDecoder decoder , RsaKeyProperties rsaKeyProperties){
        this.encoder=encoder;
        this.decoder=decoder;
        this.rsaKeyProperties=rsaKeyProperties;
    }




    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining()); // creates a string of users authorities to join together into a singngle string seperated by commas
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())

                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(); // returns a jwt encoded string

    }
    public String getUsername(String token){
        Jwt jwttoken = decoder.decode(token);
        String username= jwttoken.getSubject();
        return username;
    }
    public String getBearerToken(HttpServletRequest request){
        String auth_header= request.getHeader("Authorization");
        if(auth_header != null && auth_header.startsWith("Bearer ")){
            return auth_header.substring(7);
        }
        return null;
    }

    public boolean isValid(String token) {
        String tokenValue= token;
        // Parse the JWS and verify its RSA signature
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(tokenValue);
            JWSVerifier verifier = new RSASSAVerifier(rsaKeyProperties.publicKey());
            return signedJWT.verify(verifier);
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }


    }



