/**
 * Interface définissant les méthodes de service pour la gestion des utilisateurs.
 * Elle sert d'intermédiaire entre la couche métier (BLL) et la couche d'accès aux données (DAL).
 * 
 * - Enregistrement d'un utilisateur
 * - Vérification de l'existence d'un pseudo ou email
 * - Récupération des informations utilisateur
 * - Mise à jour et suppression d'un utilisateur
 * 
 * @author Mariami
 * @version 1.0
 */
package fr.eni.eniEncheres.bll;

import java.util.List;

import fr.eni.eniEncheres.bo.Utilisateur;
import jakarta.validation.Valid;

public interface UtilisateurService {

    /**
     * Enregistre un nouvel utilisateur après validation.
     * @param utilisateur L'utilisateur à enregistrer.
     */
    void enregistrer(@Valid Utilisateur utilisateur);

    /**
     * Vérifie si un pseudo est déjà utilisé.
     * @param pseudo Le pseudo à vérifier.
     * @return true si le pseudo existe, false sinon.
     */
    boolean pseudoExistant(String pseudo);

    /**
     * Vérifie si un email est déjà utilisé.
     * @param email L'email à vérifier.
     * @return true si l'email existe, false sinon.
     */
    boolean emailExistant(String email);

    /**
     * Récupère un utilisateur par son identifiant unique.
     * @param noUtilisateur L'identifiant de l'utilisateur.
     * @return L'utilisateur correspondant.
     */
    Utilisateur getUtilisateurById(int noUtilisateur);

    /**
     * Récupère un utilisateur par son email.
     * @param email L'email de l'utilisateur.
     * @return L'utilisateur correspondant.
     */
    Utilisateur getUtilisateurByEmail(String email);

    /**
     * Récupère un utilisateur par son pseudo.
     * @param pseudo Le pseudo de l'utilisateur.
     * @return L'utilisateur correspondant.
     */
    Utilisateur getUtilisateurByPseudo(String pseudo);

    /**
     * Récupère la liste de tous les utilisateurs enregistrés.
     * @return Liste des utilisateurs.
     */
    List<Utilisateur> getAllUtilisateurs();

    /**
     * Supprime un utilisateur par son identifiant.
     * @param noUtilisateur L'identifiant de l'utilisateur à supprimer.
     */
    void supprimerUtilisateur(int noUtilisateur);

    /**
     * Supprime un utilisateur par son email.
     * @param email L'email de l'utilisateur à supprimer.
     */
    void supprimerByEmail(String email);

    /**
     * Met à jour les informations d'un utilisateur existant.
     * @param utilisateur L'utilisateur à mettre à jour.
     */
    void mettreAJourUtilisateur(Utilisateur utilisateur);
}
