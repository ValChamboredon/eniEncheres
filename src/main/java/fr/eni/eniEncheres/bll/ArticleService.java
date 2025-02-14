package fr.eni.eniEncheres.bll;

import java.util.List;

import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.exception.BusinessException;

public interface ArticleService {
	
	public void creerArticle (ArticleVendu article) throws BusinessException;
	
	public List<ArticleVendu> consulterTout();

	ArticleVendu getArticleById(int noArticle);

}
