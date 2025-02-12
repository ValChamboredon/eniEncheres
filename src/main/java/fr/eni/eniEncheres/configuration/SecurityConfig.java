package fr.eni.eniEncheres.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
	SecurityFilterChain getFilterChain(HttpSecurity security) throws Exception {

		security.authorizeHttpRequests(auth -> {
			auth.requestMatchers(HttpMethod.GET, "/css/*").permitAll();
			auth.requestMatchers(HttpMethod.GET, "/images/*").permitAll();

			auth.anyRequest().permitAll();
		});

		security
			.formLogin(formLogin -> {
				formLogin
						.loginPage("/connexion")
						.defaultSuccessUrl("/",true);
		})
			.logout((logout) ->
				logout
					.invalidateHttpSession(true)
					.logoutRequestMatcher(new AntPathRequestMatcher("/deconnexion", "GET"))
					.logoutSuccessUrl("/")
        );
		
		return security.build();
	}


    
    @Bean
    UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager userManager = new JdbcUserDetailsManager(dataSource);

        // Requête pour charger un utilisateur
        userManager.setUsersByUsernameQuery(
            "SELECT email AS username, mot_de_passe AS password, 1 AS enabled FROM UTILISATEURS WHERE email = ?"
        );

        // Requête pour charger les rôles
        userManager.setAuthoritiesByUsernameQuery(
            "SELECT email AS username, 'ROLE_USER' AS authority FROM UTILISATEURS WHERE email = ?"
        );

        return userManager;
    }
 
   
}

