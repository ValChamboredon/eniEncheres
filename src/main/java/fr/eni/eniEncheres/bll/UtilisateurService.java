package fr.eni.eniEncheres.bll;

import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.exception.BusinessException;
import jakarta.validation.Valid;

public interface UtilisateurService {

	void enregistrer(@Valid Utilisateur utilisateur)throws BusinessException;
	
	void modifier(@Valid Utilisateur utilisateur)throws BusinessException;

	boolean pseudoExistant(String pseudo);

	boolean emailExistant(String email);
	
	Utilisateur getUtilisateurById(int noUtilisateur);
	
	Utilisateur getUtilisateurByEmail(String email);
	
	Utilisateur getUtilisateurByPseudo(String pseudo);
	
	int getIdByEmail(String email);
	
	void supprimerByEmail(String email);
	
	void supprimerById(int id);

}