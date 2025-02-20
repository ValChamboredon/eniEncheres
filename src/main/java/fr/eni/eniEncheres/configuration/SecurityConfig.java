package fr.eni.eniEncheres.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;


import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import fr.eni.eniEncheres.bll.UtilisateurService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
  	@Autowired
	private RememberMe rememberMe;

	@Bean
	SecurityFilterChain getFilterChain(HttpSecurity security) throws Exception {
		
	    security.authorizeHttpRequests(auth -> {
	    	
	        // Ressources statiques accessibles à tous
	        auth.requestMatchers("/css/*", "/images/*", "/img/*").permitAll();
	        
	        // Pages accessibles sans authentification
	        auth.requestMatchers("/", "/encheres", "/articles", "/inscription", "/connexion", "/motdepasseoublie", "/motdepasseoublie/reset").permitAll();
	        auth.requestMatchers(HttpMethod.POST, "/motdepasseoublie").permitAll();
	        
	        // Utilisation de anyRequest() en dernier
	        auth.anyRequest().authenticated();
	    	});
	    
	    security.formLogin(formLogin -> {
	        formLogin
	        	.loginPage("/connexion")
	        	.loginProcessingUrl("/connexion")
	        	.successHandler(rememberMe)
	        	.permitAll();
	    });

	    security.logout(logout -> logout.invalidateHttpSession(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/deconnexion", "GET"))
            .logoutSuccessUrl("/encheres")
            .permitAll());


        // Désactivation du CSRF
        security.csrf(csrf -> csrf.disable());

        return security.build();
    }

    @Bean
    UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager userManager = new JdbcUserDetailsManager(dataSource);
        userManager.setUsersByUsernameQuery(
                "SELECT email AS username, mot_de_passe AS password, 1 AS enabled FROM UTILISATEURS WHERE email = ?");
        userManager.setAuthoritiesByUsernameQuery(
                "SELECT email AS username, 'ROLE_USER' AS authority FROM UTILISATEURS WHERE email = ?");
        return userManager;
    }


}



