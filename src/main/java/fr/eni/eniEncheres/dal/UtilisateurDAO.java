package fr.eni.eniEncheres.dal;


import org.springframework.stereotype.Repository;

import fr.eni.eniEncheres.bo.Utilisateur;

@Repository

public interface UtilisateurDAO {

	void save(Utilisateur utilisateur);
	boolean existsByPseudo(String pseudo);
	boolean existsByEmail(String email);
	



}
