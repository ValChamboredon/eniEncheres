package fr.eni.eniEncheres.bll;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.dal.UtilisateurDAO;
import fr.eni.eniEncheres.exception.BusinessException;
import jakarta.validation.Valid;


@Service
public class UtilisateurServiceImpl implements UtilisateurService {
	
	private  UtilisateurDAO utilisateurDAO;
	
	public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO) {
	    this.utilisateurDAO = utilisateurDAO;
	}

/**
 * Méthode permettant de d'enregistrer un utilisateur
 *
 */
	@Override
	public void enregistrer(@Valid Utilisateur utilisateur) throws BusinessException {
		System.out.println("début enregistrer");
	    List<String> erreurs = new ArrayList<>();

	    // Vérifier si l'email ou le pseudo existent déjà
	    if (utilisateurDAO.existsByEmail(utilisateur.getEmail())) {
	        erreurs.add("Cet email est déjà utilisé.");
	        System.out.println("L'email existe déjà en base !");
	    }
	    if (utilisateurDAO.existsByPseudo(utilisateur.getPseudo())) {
	        erreurs.add("Ce pseudo est déjà pris.");
	        System.out.println("Le pseudo existe déjà en base !");
	    }

	    // Vérifier si les mots de passe correspondent
	    System.out.println("mot de passe : " + utilisateur.getMotDePasse());
	    System.out.println("confirmation : " + utilisateur.getConfirmationMotDePasse());
	    

	    if (!utilisateur.getMotDePasse().equals(utilisateur.getConfirmationMotDePasse())) {
	        erreurs.add("Les mots de passe ne correspondent pas.");
	        System.out.println("Les mots de passe ne correspondent pas !");
	    }

	    // Si des erreurs existent, on lève une exception
	    if (!erreurs.isEmpty()) {
	        System.out.println("Erreurs trouvées : " + erreurs);
	        throw new BusinessException(erreurs);
	    }

	    // Hachage du mot de passe
	    utilisateur.setMotDePasse(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(utilisateur.getMotDePasse()));

	    // Définir les valeurs par défaut
	    utilisateur.setCredit(0);
	    utilisateur.setAdministrateur(false);

	    // Sauvegarde en base
	    utilisateurDAO.save(utilisateur);
	}
	
	@Override
	@Transactional(rollbackFor = BusinessException.class)
	public void modifier(@Valid Utilisateur utilisateur)throws BusinessException {
//		BusinessException be = new BusinessException();
//		
//		boolean valider = true;
//				
//		if(valider) {
//			utilisateurDAO.update(utilisateur);
//		}else {
//			throw be;
//		}
	}
	
	@Override
    public boolean pseudoExistant(String pseudo) {
        return utilisateurDAO.existsByPseudo(pseudo);
    }

    @Override
    public boolean emailExistant(String email) {
        return utilisateurDAO.existsByEmail(email);
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
		utilisateurDAO.supprimerByEmail(email);
	}


}