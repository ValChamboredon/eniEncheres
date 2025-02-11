package fr.eni.eniEncheres.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests((authorize) -> authorize
					.anyRequest().permitAll()
					// ajouter permit config plus tard

		)
		.httpBasic(Customizer.withDefaults())
			.formLogin((formLogin) -> 
				formLogin
					.loginPage("/connexion")
					.defaultSuccessUrl("/"))
				
				.logout((logout) -> 
				logout
					.invalidateHttpSession(true)
					.logoutRequestMatcher(new AntPathRequestMatcher("/deconnexion", "GET"))
					.logoutSuccessUrl("/"));
		return http.build();
	}

	@Bean
	UserDetailsManager users(DataSource dataSource) {
	    JdbcUserDetailsManager userManager = new JdbcUserDetailsManager(dataSource);

	    // Requête pour charger un utilisateur
	    userManager.setUsersByUsernameQuery(
	        "SELECT pseudo AS username, mot_de_passe AS password, administrateur AS enabled FROM UTILISATEURS WHERE pseudo = ?"
	    );

	    // Requête pour charger les rôles
	    userManager.setAuthoritiesByUsernameQuery(
	        "SELECT pseudo AS username, 'ROLE_USER' AS authority FROM UTILISATEURS WHERE pseudo = ?"
	    );

	    return userManager;
	}

}