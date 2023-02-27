package prawnsoup.com.taskmanaager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import prawnsoup.com.taskmanaager.config.RsaKeyProperties;


@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
@EnableWebSecurity
public class TaskManaagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManaagerApplication.class, args);
    }

}

