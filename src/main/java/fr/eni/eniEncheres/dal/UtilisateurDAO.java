package fr.eni.eniEncheres.dal;

import fr.eni.eniEncheres.bo.Utilisateur;

/**
 * ✅ Interface DAO pour la gestion des utilisateurs.
 * 
 * Cette interface définit les opérations CRUD pour les utilisateurs.
 */
public interface UtilisateurDAO {
	
    /**
     * ✅ Met à jour un utilisateur existant.
     * 
     * @param utilisateur L'utilisateur à mettre à jour.
     */
    void update(Utilisateur utilisateur);

    /**
     * ✅ Sauvegarde un nouvel utilisateur en base de données.
     * 
     * @param utilisateur L'utilisateur à enregistrer.
     */
    void save(Utilisateur utilisateur);

    /**
     * ✅ Vérifie si un pseudo existe déjà dans la base de données.
     * 
     * @param pseudo Le pseudo à vérifier.
     * @return `true` si le pseudo existe, sinon `false`.
     */
    boolean existsByPseudo(String pseudo);

    /**
     * ✅ Vérifie si un email existe déjà dans la base de données.
     * 
     * @param email L'email à vérifier.
     * @return `true` si l'email existe, sinon `false`.
     */
    boolean existsByEmail(String email);

    /**
     * ✅ Récupère un utilisateur par son identifiant unique.
     * 
     * @param noUtilisateur L'ID de l'utilisateur.
     * @return L'utilisateur correspondant.
     */
    Utilisateur read(int noUtilisateur);

    /**
     * ✅ Récupère un utilisateur par son email.
     * 
     * @param email L'email de l'utilisateur.
     * @return L'utilisateur correspondant.
     */
    Utilisateur getUtilisateur(String email);

    /**
     * ✅ Supprime un utilisateur en utilisant son email.
     * 
     * @param email L'email de l'utilisateur à supprimer.
     */
    void supprimerByEmail(String email);
}
