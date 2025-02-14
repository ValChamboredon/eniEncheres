
package fr.eni.eniEncheres.bll.contexte;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import fr.eni.eniEncheres.bo.Utilisateur;

@Service
@SessionScope // L'utilisateur reste stocké pendant la session
public class ContexteServiceImpl implements ContexteService {

    private Utilisateur utilisateurConnecte;

    @Override
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    @Override
    public void setUtilisateurConnecte(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
    }

    @Override
    public void deconnexion() {
        this.utilisateurConnecte = null; // Déconnexion en supprimant l'utilisateur de la session.
    }
}
