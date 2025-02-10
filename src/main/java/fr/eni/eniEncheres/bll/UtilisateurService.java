package fr.eni.eniEncheres.bll;

import fr.eni.eniEncheres.bo.Utilisateur;
import jakarta.validation.Valid;

public interface UtilisateurService {

	void enregistrer(@Valid Utilisateur utilisateur);

	boolean pseudoExistant(String pseudo);

	boolean emailExistant(String email);

}
