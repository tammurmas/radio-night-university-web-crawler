package org.tamm.nightuni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"org.tamm.nightuni"})
public class NightuniApplication {

	public static void main(String[] args) {
		SpringApplication.run(NightuniApplication.class, args);
	}

}
