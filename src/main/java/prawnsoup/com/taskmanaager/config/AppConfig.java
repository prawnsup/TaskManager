package prawnsoup.com.taskmanaager.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
//trying to fix a circular dependency error by splitting up my security config. contains beans used for rest of API
public class AppConfig {
    private final RsaKeyProperties rsaKeys;
    public AppConfig(RsaKeyProperties rsaKeys){
        this.rsaKeys=rsaKeys;
    }



    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build(); // nimbus jwt is an implementation of jwtdecoder interface we configure this instance to use a public key
    }

    @Bean
    public PasswordEncoder delegatingPasswordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        // Use this encoder for bcrypt
        encoders.put("bcrypt", new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y, 12));
        DelegatingPasswordEncoder delegatingPasswordEncoder =
                new DelegatingPasswordEncoder("bcrypt", encoders);

        PasswordEncoder defaultDelegatingPasswordEncoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // If a password ID does not match "bcrypt", use defaultDelegatingPasswordEncoder
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(defaultDelegatingPasswordEncoder);
        return delegatingPasswordEncoder;
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        //creates a json web key object which is built using the public/private key from the rsakey record object
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        //this creates a JWKSource object which is an interface of type security context that defines source of json web key
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks); //returns a jwtencoder using jwks
    }


}
