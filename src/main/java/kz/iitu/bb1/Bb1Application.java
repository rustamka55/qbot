package kz.iitu.bb1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Bb1Application {

    public static void main(String[] args) {
        SpringApplication.run(Bb1Application.class, args);
    }

}
