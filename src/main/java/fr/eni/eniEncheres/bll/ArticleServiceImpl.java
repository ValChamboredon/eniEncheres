package fr.eni.eniEncheres.bll;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.EtatVente;
import fr.eni.eniEncheres.dal.ArticleDAO;
import fr.eni.eniEncheres.exception.BusinessException;

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
	
	@Override
	public List<ArticleVendu> getArticlesTermines() {
	    return articleDAO.getArticlesTermines();
	}

	@Override
	public void demarrerVente(int noArticle) throws BusinessException {
		ArticleVendu article = articleDAO.getArticleById(noArticle);
		
		BusinessException be = new BusinessException();
		
		if(article.getEtatVente() != EtatVente.CREEE) {
			be.addCleErreur("ERR_VENTE_DEJA_DEMARREE");
		}
	
		LocalDate now = LocalDate.now();
		if (now.isBefore(article.getDateDebutEncheres())) {
			be.addCleErreur("ERR_DATE_DEBUT_FUTURE");
		}
		if (!be.getClesErreurs().isEmpty()) {
	        throw be;
	    }
		article.setEtatVente(EtatVente.EN_COURS);
		articleDAO.updateArticle(article);
	}
	
	
	
}
