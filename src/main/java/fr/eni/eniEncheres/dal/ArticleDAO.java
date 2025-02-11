package fr.eni.eniEncheres.dal;

import java.util.List;

import fr.eni.eniEncheres.bo.ArticleVendu;

public interface ArticleDAO {
	
	ArticleVendu getArticleById(int id);
    List<ArticleVendu> getAllArticles();
    void addArticle(ArticleVendu article);
    void updateArticle(ArticleVendu article);
    void deleteArticle(int id);
    List<ArticleVendu> getArticlesByCategory(int categoryId);
    List<ArticleVendu> getArticlesByUser(int userId);
    List<ArticleVendu> getArticlesEnVente();
    List<ArticleVendu> searchArticles(String keyword, int categoryId);
    
}
