package fr.eni.eniEncheres.dal;

import fr.eni.eniEncheres.bo.Utilisateur;

public interface UtilisateurDAO {

	void save(Utilisateur utilisateur);
	boolean existsByPseudo(String pseudo);
	boolean existsByEmail(String email);
	
	
	Utilisateur read(int noUtilisateur);
	Utilisateur getUtilisateur(String email);
	void supprimerByEmail(String email);
	
	
}
