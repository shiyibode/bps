package org.nmgns.bps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BpsApplication.class, args);
    }

}
