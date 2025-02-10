package fr.eni.eniEncheres.bll;

import org.springframework.stereotype.Service;

import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.dal.UtilisateurDAO;
@Service
public class UtilisateurServiceImpl implements UtilisateurService {
	
	private UtilisateurDAO utlisateurDAO;
	private static UtilisateurServiceImpl utilisateur;
	
	public UtilisateurServiceImpl(UtilisateurDAO utlisateurDAO) {
		this.utlisateurDAO = utlisateurDAO;
	}
	
	

}
