package fr.eni.eniEncheres.bll;

import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.dal.UtilisateurDAO;

public class UtilisateurServiceImpl implements UtilisateurService {
	
	private UtilisateurDAO utlisateurDAO;
	private static UtilisateurServiceImpl utilisateur;
	
	public UtilisateurServiceImpl(UtilisateurDAO utlisateurDAO) {
		this.utlisateurDAO = utlisateurDAO;
	}
	
	private void inscrire(Utilisateur utlisateur) {
		

	}

}
