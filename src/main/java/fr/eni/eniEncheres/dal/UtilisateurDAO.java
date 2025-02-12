package fr.eni.eniEncheres.dal;

import java.util.List;

import fr.eni.eniEncheres.bo.Utilisateur;

public interface UtilisateurDAO {
    void ajouterUtilisateur(Utilisateur utilisateur);
    Utilisateur getUtilisateurById(int noUtilisateur);
    Utilisateur getUtilisateurByEmail(String email);
    Utilisateur getUtilisateurByPseudo(String pseudo); // ✅ Nouvelle méthode
    List<Utilisateur> getAllUtilisateurs();
    void supprimerUtilisateur(int noUtilisateur);
    void supprimerByEmail(String email); // ✅ Nouvelle méthode
    void mettreAJourUtilisateur(Utilisateur utilisateur);
}
