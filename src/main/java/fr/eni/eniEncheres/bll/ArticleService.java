package fr.eni.eniEncheres.bll;

import java.util.List;

import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.exception.BusinessException;

public interface ArticleService {
	
	public void creerArticle (ArticleVendu article) throws BusinessException;
	
	public List<ArticleVendu> consulterTout();

	ArticleVendu getArticleById(int noArticle);
	
	void supprimerArticle(int articleId);
	
	public void modifierArticle(ArticleVendu article);
	    
	void mettreAJourEtatVente(ArticleVendu article);
	
	void mettreAJourEtatDesArticles();

	//Filtre
	public List<ArticleVendu> rechercherArticles(String recherche, Integer noCategorie);
	
	List<ArticleVendu> filtrerVentes(int userId, Boolean ventesEnCours, Boolean ventesNonDebutees, Boolean ventesTerminees);

    List<ArticleVendu> filtrerAchats(int userId, Boolean encheresOuvertes, Boolean mesEncheresEnCours, Boolean mesEncheresRemportees);




}