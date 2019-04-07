package geacommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GeacommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeacommerceApplication.class, args);
    }

}
