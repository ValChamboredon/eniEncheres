/**
 * Implémentation du service métier pour la gestion des enchères.
 * Gère la logique métier et interagit avec la couche DAO pour la persistance des enchères.
 */
package fr.eni.eniEncheres.bll;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.dal.EnchereDAO;

/**
 * Annotation @Service permet à Spring de gérer cette classe comme un service métier.
 */
@Service
public class EnchereServiceImpl implements EnchereService {

    // Dépendance vers le DAO pour interagir avec la base de données.
    private final EnchereDAO enchereDAO;

    /**
     * Injection de dépendance via le constructeur.
     * @param enchereDAO DAO des enchères.
     */
    @Autowired
    public EnchereServiceImpl(EnchereDAO enchereDAO) {
        this.enchereDAO = enchereDAO;
    }

    /**
     * Ajoute une enchère.
     */
    @Override
    public void ajouterEnchere(Enchere enchere) {
        enchereDAO.ajouterEnchere(enchere);
    }

    /**
     * Récupère toutes les enchères pour un article donné.
     */
    @Override
    public List<Enchere> getEncheresParArticle(int noArticle) {
        return enchereDAO.getEncheresParArticle(noArticle);
    }

    /**
     * Récupère l'enchère la plus élevée pour un article donné.
     */
    @Override
    public Enchere getEnchereMaxParArticle(int noArticle) {
        return enchereDAO.getEnchereMaxParArticle(noArticle);
    }

    /**
     * Supprime toutes les enchères d'un article donné.
     */
    @Override
    public void supprimerEncheresParArticle(int noArticle) {
        enchereDAO.supprimerEncheresParArticle(noArticle);
    }

    /**
     * Met à jour une enchère existante.
     */
    @Override
    public void mettreAJourEnchere(Enchere enchere) {
        enchereDAO.mettreAJourEnchere(enchere);
    }
}
