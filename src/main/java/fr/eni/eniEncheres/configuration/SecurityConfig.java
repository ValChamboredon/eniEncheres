package fr.eni.eniEncheres.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
 
@Configuration
@EnableWebSecurity
public class SecurityConfig {

 
    private final String SELECT_USER = "SELECT email, password, enabled FROM users WHERE email=?";
    private final String SELECT_ROLES = "SELECT u.email, r.role FROM users u INNER JOIN user_roles ur ON u.id = ur.user_id INNER JOIN roles r ON ur.role_id = r.id WHERE u.email=?";
 
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(SELECT_USER);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(SELECT_ROLES);
        return jdbcUserDetailsManager;
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
    @Bean
	SecurityFilterChain getFilterChain(HttpSecurity security) throws Exception {

		security.authorizeHttpRequests(auth -> {
			auth.requestMatchers(HttpMethod.GET, "/css/*").permitAll();
			auth.requestMatchers(HttpMethod.GET, "/images/*").permitAll();

			auth.anyRequest().permitAll();
		});

		security.formLogin(formLogin -> {
				formLogin
						.loginPage("/connexion")
						.defaultSuccessUrl("/");
		});
		
		security.logout(logout -> {
				logout.logoutUrl("deconnexion")
					.logoutSuccessUrl("/")
					.deleteCookies("JSESSIONID");
		});
		
		return security.build();
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

