
package fr.eni.eniEncheres.bll.contexte;

import fr.eni.eniEncheres.bo.Utilisateur;

public interface ContexteService {
    Utilisateur getUtilisateurConnecte();
    void setUtilisateurConnecte(Utilisateur utilisateur);
    void deconnexion();
}
