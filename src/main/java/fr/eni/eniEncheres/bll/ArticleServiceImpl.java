package fr.eni.eniEncheres.bll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.dal.ArticleDAO;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDAO articleDAO;

	@Override
	public ArticleVendu getArticleById(int id) {
		return articleDAO.getArticleById(id);
	}

	@Override
	public List<ArticleVendu> getAllArticles() {
		return articleDAO.getAllArticles();
	}

	@Override
	public void addArticle(ArticleVendu article) {
		articleDAO.addArticle(article);
		
	}

	@Override
	public void updateArticle(ArticleVendu article) {
		articleDAO.updateArticle(article);
		
	}

	@Override
	public void deleteArticle(int id) {
		articleDAO.deleteArticle(id);
		
	}

	@Override
	public List<ArticleVendu> getArticlesByCategory(int categoryId) {
		return articleDAO.getArticlesByCategory(categoryId);
	}

	@Override
	public List<ArticleVendu> getArticlesByUser(int userId) {
		return articleDAO.getArticlesByUser(userId);
	}

	@Override
	public List<ArticleVendu> searchArticles(String keyword, int categoryId) {
		return articleDAO.searchArticles(keyword, categoryId);
	}
	
	
	
}
