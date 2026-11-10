package ph.edu.lspu.museum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the LSPU Heritage Museum.
 * Run with: mvn spring-boot:run   then open http://localhost:8080
 */
@SpringBootApplication
public class MuseumApplication {
    public static void main(String[] args) {
        SpringApplication.run(MuseumApplication.class, args);
    }
}
