/**
 * Implémentation de l'interface UtilisateurService.
 * Cette classe effectue les opérations métier et délègue l'accès aux données au DAO.
 * 
 * Elle est marquée comme un Service Spring (@Service) et gérée par le conteneur IoC.
 * 
 * @author Mariami
 * @version 1.0
 */
package fr.eni.eniEncheres.bll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.dal.UtilisateurDAO;
import jakarta.validation.Valid;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurDAO utilisateurDAO;

    /**
     * Injection de dépendance du DAO pour l'accès aux données utilisateurs.
     * @param utilisateurDAO L'instance du DAO.
     */
    @Autowired
    public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }

    @Override
    public void enregistrer(@Valid Utilisateur utilisateur) {
        utilisateur.setCredit(0); // Un nouvel utilisateur commence avec 0 crédits.
        utilisateur.setAdministrateur(false); // Par défaut, il n'est pas administrateur.
        utilisateurDAO.ajouterUtilisateur(utilisateur);
    }

    @Override
    public boolean pseudoExistant(String pseudo) {
        return utilisateurDAO.getUtilisateurByPseudo(pseudo) != null;
    }

    @Override
    public boolean emailExistant(String email) {
        return utilisateurDAO.getUtilisateurByEmail(email) != null;
    }

    @Override
    public Utilisateur getUtilisateurById(int noUtilisateur) {
        return utilisateurDAO.getUtilisateurById(noUtilisateur);
    }

    @Override
    public Utilisateur getUtilisateurByEmail(String email) {
        return utilisateurDAO.getUtilisateurByEmail(email);
    }

    @Override
    public Utilisateur getUtilisateurByPseudo(String pseudo) {
        return utilisateurDAO.getUtilisateurByPseudo(pseudo);
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurDAO.getAllUtilisateurs();
    }

    @Override
    public void supprimerUtilisateur(int noUtilisateur) {
        utilisateurDAO.supprimerUtilisateur(noUtilisateur);
    }

    @Override
    public void supprimerByEmail(String email) {
        utilisateurDAO.supprimerByEmail(email);
    }

    @Override
    public void mettreAJourUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.mettreAJourUtilisateur(utilisateur);
    }
}
