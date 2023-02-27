package prawnsoup.com.taskmanaager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//
@Configuration
public class CacheConfig {
    @Bean
    public Map<String, String> CurrentUser(){ return new ConcurrentHashMap<>();}
}
