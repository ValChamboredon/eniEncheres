package fr.eni.eniEncheres.dal;

import java.util.List;
import fr.eni.eniEncheres.bo.ArticleVendu;

/**
 * ✅ Interface DAO pour la gestion des articles.
 * 
 * Cette interface définit les opérations CRUD pour la gestion des articles vendus.
 */
public interface ArticleDAO {
    
    /**
     * ✅ Récupère un article par son ID.
     * 
     * @param id L'identifiant de l'article.
     * @return L'article correspondant.
     */
    ArticleVendu getArticleById(int id);

    /**
     * ✅ Récupère tous les articles disponibles.
     * 
     * @return Liste de tous les articles.
     */
    List<ArticleVendu> getAllArticles();

    /**
     * ✅ Ajoute un nouvel article en base de données.
     * 
     * @param article L'article à ajouter.
     */
    void addArticle(ArticleVendu article);

    /**
     * ✅ Met à jour un article existant.
     * 
     * @param article L'article mis à jour.
     */
    void updateArticle(ArticleVendu article);

    /**
     * ✅ Supprime un article en base de données par son ID.
     * 
     * @param id L'identifiant de l'article.
     */
    void deleteArticle(int id);

    /**
     * ✅ Récupère une liste d'articles en fonction d'une catégorie.
     * 
     * @param categoryId L'ID de la catégorie.
     * @return Liste des articles correspondants.
     */
    List<ArticleVendu> getArticlesByCategory(int categoryId);

    /**
     * ✅ Récupère une liste d'articles vendus par un utilisateur spécifique.
     * 
     * @param userId L'ID de l'utilisateur.
     * @return Liste des articles mis en vente par cet utilisateur.
     */
    List<ArticleVendu> getArticlesByUser(int userId);

    /**
     * ✅ Récupère les articles actuellement en vente.
     * 
     * @return Liste des articles en vente.
     */
    List<ArticleVendu> getArticlesEnVente(); 

    /**
     * ✅ Recherche des articles en fonction d'un mot-clé et d'une catégorie.
     * 
     * @param keyword Le mot-clé pour la recherche.
     * @param categoryId L'ID de la catégorie (peut être 0 pour toutes).
     * @return Liste des articles correspondants.
     */
    List<ArticleVendu> searchArticles(String keyword, int categoryId);

    /**
     * ✅ Récupère les articles dont la vente est terminée.
     * 
     * @return Liste des articles vendus ou dont la vente est terminée.
     */
    List<ArticleVendu> getArticlesTermines();
}
