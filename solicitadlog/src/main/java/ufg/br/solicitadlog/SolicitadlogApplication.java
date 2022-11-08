package ufg.br.solicitadlog;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan(basePackages = {"ufg.br.*"})
@EntityScan(basePackages = "ufg.br.*")
@EnableTransactionManagement
@EnableWebMvc
@SpringBootApplication
public class SolicitadlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolicitadlogApplication.class, args);
	}

}
