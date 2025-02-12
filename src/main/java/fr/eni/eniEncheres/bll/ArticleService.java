package fr.eni.eniEncheres.bll;

import java.util.List;

import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.exception.BusinessException;

public interface ArticleService {
	
	ArticleVendu getArticleById (int id);
	List<ArticleVendu> getAllArticles();
	void addArticle(ArticleVendu article);
	void updateArticle(ArticleVendu article);
	void deleteArticle(int id);
	List<ArticleVendu> getArticlesByCategory(int categoryId);
	List<ArticleVendu> getArticlesByUser(int userId);
	List<ArticleVendu> searchArticles(String keyword, int categoryId);
	List<ArticleVendu> getArticlesTermines();
	void demarrerVente(int noArticle) throws BusinessException;
	List<ArticleVendu> getArticlesEnVente();
}
