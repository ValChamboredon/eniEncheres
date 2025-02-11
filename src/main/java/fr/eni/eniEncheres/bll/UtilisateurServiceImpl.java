package fr.eni.eniEncheres.bll;



import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.dal.UtilisateurDAO;


import jakarta.validation.Valid;


import jakarta.validation.Valid;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {
	
	private  UtilisateurDAO utilisateurDAO;

	
	@Autowired
	public UtilisateurServiceImpl(UtilisateurDAO utlisateurDAO) {
		this.utilisateurDAO = utlisateurDAO;
	}
	


	@Override
	public void enregistrer(@Valid Utilisateur utilisateur) {
		utilisateur.setCredit(0);
		utilisateur.setAdministrateur(false);
		
		utilisateurDAO.save(utilisateur);
		
	}


	@Override
	public boolean pseudoExistant(String pseudo) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean emailExistant(String email) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public Utilisateur getUtilisateurById(int noUtilisateur) {
		return utilisateurDAO.read(noUtilisateur);
	}


	@Override
	public Utilisateur getUtilisateurByEmail(String email) {
		return utilisateurDAO.getUtilisateur(email);
	}



	@Override
	public void supprimerByEmail(String email) {
		// TODO Auto-generated method stub
		
	}


}