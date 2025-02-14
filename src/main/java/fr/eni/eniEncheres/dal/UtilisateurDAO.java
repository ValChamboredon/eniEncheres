package fr.eni.eniEncheres.dal;



import org.springframework.stereotype.Repository;

import fr.eni.eniEncheres.bo.Utilisateur;



import fr.eni.eniEncheres.bo.Utilisateur;

@Repository
public interface UtilisateurDAO {
	
	void update(Utilisateur utilisateur);
	
	void save(Utilisateur utilisateur);
	
	boolean existsByPseudo(String pseudo);
	
	boolean existsByEmail(String email);
	
	Utilisateur read(int noUtilisateur);
	
	Utilisateur getUtilisateur(String email);
	
	void supprimerByEmail(String email);
	
	void supprimerById(int id);
	
}
