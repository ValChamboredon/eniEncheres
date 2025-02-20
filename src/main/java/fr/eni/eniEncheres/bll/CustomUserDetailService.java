package fr.eni.eniEncheres.bll;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.dal.UtilisateurDAO;

public class CustomUserDetailService implements UserDetailsService{
	
	@Autowired
	private UtilisateurDAO utilisateurDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurDAO.getUtilisateurByPseudo(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(utilisateur.getPseudo())
                .password(utilisateur.getMotDePasse())
                .authorities(new ArrayList<>()) // Ajoutez les rôles ou autorités ici
                .build();
	}
}
