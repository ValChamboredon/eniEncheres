/**
 * Interface pour gérer l'utilisateur actuellement connecté.
 * Permet de récupérer et modifier l'état de connexion.
 * 
 * @author Mariami
 * @version 1.0
 */
package fr.eni.eniEncheres.bll.contexte;

import fr.eni.eniEncheres.bo.Utilisateur;

public interface ContexteService {
    Utilisateur getUtilisateurConnecte();
    void setUtilisateurConnecte(Utilisateur utilisateur);
    void deconnexion();
}
