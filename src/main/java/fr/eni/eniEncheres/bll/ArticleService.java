package fr.eni.eniEncheres.bll;

import java.util.List;

import fr.eni.eniEncheres.bo.ArticleVendu;

public interface ArticleService {
	
	ArticleVendu getArticleById (int id);
	List<ArticleVendu> getAllArticles();
	void addArticle(ArticleVendu article);
	void updateArticle(ArticleVendu article);
	void deleteArticle(int id);
	List<ArticleVendu> getArticlesByCategory(int categoryId);
	List<ArticleVendu> getArticlesByUser(int userId);
	List<ArticleVendu> searchArticles(String keyword, int categoryId);

}
