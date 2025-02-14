package fr.eni.eniEncheres.bll;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	    List<String> erreurs = new ArrayList<>();

	    // Vérification email & pseudo
	    if (utilisateurDAO.existsByEmail(utilisateur.getEmail())) {
	        erreurs.add("Cet email est déjà utilisé.");
	    }
	    if (utilisateurDAO.existsByPseudo(utilisateur.getPseudo())) {
	        erreurs.add("Ce pseudo est déjà pris.");
	    }

	    // Vérification mot de passe
	    if (!utilisateur.getMotDePasse().equals(utilisateur.getConfirmationMotDePasse())) {
	        erreurs.add("Les mots de passe ne correspondent pas.");
	    }

	    if (!erreurs.isEmpty()) {
	        throw new BusinessException(erreurs);
	    }

	    // Hachage du mot de passe seulement si c'est un nouveau mot de passe
	    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	    utilisateur.setMotDePasse(encoder.encode(utilisateur.getMotDePasse()));

	    // Définir les valeurs par défaut
	    utilisateur.setCredit(0);
	    utilisateur.setAdministrateur(false);

	    // Sauvegarde en base
	    utilisateurDAO.save(utilisateur);
	}
	@Override
	public void modifier(@Valid Utilisateur utilisateur) {
		utilisateurDAO.update(utilisateur);
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