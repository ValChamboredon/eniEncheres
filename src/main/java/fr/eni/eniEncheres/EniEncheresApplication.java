package fr.eni.eniEncheres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"fr.eni.eniEncheres"})
public class EniEncheresApplication {

	public static void main(String[] args) {
		SpringApplication.run(EniEncheresApplication.class, args);
	}

}
