package prawnsoup.com.taskmanaager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
//used to generate classes
@ConfigurationProperties(prefix="rsa")
public record RsaKeyProperties(RSAPublicKey publicKey , RSAPrivateKey privateKey) {

}
