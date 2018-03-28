package clud.zen.scholar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ScholarAssembleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScholarAssembleApplication.class, args);
    }
}
