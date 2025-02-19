package fr.eni.eniEncheres.dal;

import java.util.List;

import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.EtatVente;

public interface ArticleDAO {

	ArticleVendu getArticleById(int noArticle);

    List<ArticleVendu> getAllArticles(); // itération 1 
    void addArticle(ArticleVendu article); // itération 1
    List<ArticleVendu> getArticlesByCategory(int categoryId);
    List<ArticleVendu> getArticlesByUser(int userId);
    List<ArticleVendu> getArticlesEnVente();
    List<ArticleVendu> searchArticles(String keyword, int categoryId);
    List<ArticleVendu> getArticlesTermines();
    void updateEtatVente(int noArticle, EtatVente nouvelEtat);
    void updatePrixVente(int noArticle, int nouveauPrix);
    void deleteArticle(int articleId);
    public void modifierArticle(ArticleVendu article);
 


	List<ArticleVendu> filtrerVentes(int userId, Boolean ventesEnCours, Boolean ventesNonDebutees,
			Boolean ventesTerminees);
	List<ArticleVendu> filtrerAchats(int userId, Boolean encheresOuvertes, Boolean mesEncheresEnCours,
			Boolean mesEncheresRemportees);

}
