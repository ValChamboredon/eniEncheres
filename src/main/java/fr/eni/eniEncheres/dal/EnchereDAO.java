// Interface DAO pour la gestion des enchères
package fr.eni.eniEncheres.dal;

import java.util.List;

import fr.eni.eniEncheres.bo.Enchere;

public interface EnchereDAO {
    void ajouterEnchere(Enchere enchere);
    List<Enchere> getEncheresParArticle(int noArticle);
    Enchere getEnchereMaxParArticle(int noArticle);
    void supprimerEncheresParArticle(int noArticle);
    void mettreAJourEnchere(Enchere enchere);
}
