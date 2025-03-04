package fr.eni.eniEncheres.bll;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.eniEncheres.bll.ArticleService;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.EtatVente;
import fr.eni.eniEncheres.dal.ArticleDAO;
import fr.eni.eniEncheres.exception.BusinessException;

@Service
public class ArticleServiceImpl implements ArticleService {

	// Dépendance
	private ArticleDAO articleDAO;

	/**
	 * Constructeur
	 * 
	 * @param articleDAO
	 */
	// Injection de dépendance
	public ArticleServiceImpl(ArticleDAO articleDAO) {
		this.articleDAO = articleDAO;
	}

	/**
	 * Méthode qui fait des vérification sur l'Article, si tout est OK elle envoie
	 * l'Article vers la DAL.<br>
	 * Sinon elle lève de·s BusinessException·s et les propage vers le Controller
	 * 
	 * @throws BusinessException
	 */
	@Override
	public void creerArticle(ArticleVendu article) throws BusinessException {

		BusinessException be = new BusinessException();

		// Test de la date de début d'enchère (erreur si elle est déjà passée)
		if (article.getDateDebutEncheres().isBefore(LocalDateTime.now())) {
			be.getMessagesErreur().add("La date de début d'enchère est inférieure à la date du jour");
		}

		// Test de la date de fin d'enchère (erreur si avant ou égale à la date de
		// début)
		if (!(article.getDateFinEncheres().isAfter(article.getDateDebutEncheres()))) {
			be.getMessagesErreur().add("La date de fin d'enchère n'est pas après la date de fin de début d'enchère");
		}

		// S'il n'y a pas eu d'erreur on envoi l'Article vers la DAL
		if (be.getMessagesErreur().size() == 0) {

			// On initialise le prix de vente avec la valeur de la mise à prix
			article.setPrixVente(article.getMiseAPrix());

			try {
				articleDAO.addArticle(article);

				// On ajoute article à la liste des articles en vente du vendeur
				article.getVendeur().getArticlesEnVente().add(article);

			} catch (DataAccessException dae) {
				System.err.println(dae.getStackTrace());
			}

		} else {
			throw be;
		}

	}

	/**
	 * Méthode qui appelle la DAL pour récupérer tout les Articles en vente
	 */
	@Override
	public List<ArticleVendu> consulterTout() {
		List<ArticleVendu> articles = articleDAO.getAllArticles();

		for (ArticleVendu article : articles) {
			mettreAJourEtatVente(article);
		}

		return articles;
	}
	
	private void verifierEtMettreAJourEtatVente(ArticleVendu article) {
        LocalDateTime now = LocalDateTime.now();
        
        System.out.println("Vérification article #" + article.getNoArticle());
        System.out.println("État actuel : " + article.getEtatVente());
        System.out.println("Date début : " + article.getDateDebutEncheres());
        System.out.println("Date fin : " + article.getDateFinEncheres());
        System.out.println("Date actuelle : " + now);
        
        // Vérification pour passage à EN_COURS
        if (article.getEtatVente() == EtatVente.CREEE && 
            (now.isEqual(article.getDateDebutEncheres()) || 
             now.isAfter(article.getDateDebutEncheres()))) {
            
            article.setEtatVente(EtatVente.EN_COURS);
            articleDAO.updateEtatVente(article.getNoArticle(), EtatVente.EN_COURS);
            System.out.println("Mise à jour en EN_COURS pour l'article: " + article.getNoArticle());
        }

        // Vérification pour passage à ENCHERES_TERMINEES
        if (article.getEtatVente() == EtatVente.EN_COURS && 
            now.isAfter(article.getDateFinEncheres())) {
            
            article.setEtatVente(EtatVente.ENCHERES_TERMINEES);
            articleDAO.updateEtatVente(article.getNoArticle(), EtatVente.ENCHERES_TERMINEES);
            System.out.println("Mise à jour en ENCHERES_TERMINEES pour l'article: " + article.getNoArticle());
        }
    }

	@Override
	public ArticleVendu getArticleById(int noArticle) {
		ArticleVendu article = articleDAO.getArticleById(noArticle);

		System.out.println("Vérification état vente pour l'article: " + article.getNoArticle());
		System.out.println("Date de début: " + article.getDateDebutEncheres());
		System.out.println("Date de fin: " + article.getDateFinEncheres());
		System.out.println("Date actuelle: " + LocalDate.now());
		System.out.println("État actuel: " + article.getEtatVente());
		
		verifierEtMettreAJourEtatVente(article);

		return article;
	}

	@Override
	public void supprimerArticle(int articleId) {
		try {
			articleDAO.deleteArticle(articleId);
			System.out.println("Article supprimé avec succès : " + articleId);
		} catch (Exception e) {
			System.err.println("Erreur lors de la suppression de l'article : " + e.getMessage());
		}
	}

	public void modifierArticle(ArticleVendu article) {
		articleDAO.modifierArticle(article);
	}

	/**
	 * Met à jour l'état de vente d'un article en fonction de la date actuelle. Pour
	 * l'index
	 */
	public void mettreAJourEtatVente(ArticleVendu article) {
		verifierEtMettreAJourEtatVente(article);
	}
	
	@Transactional
	public void mettreAJourEtatDesArticles() {
	    List<ArticleVendu> articles = articleDAO.getAllArticles();
	    for (ArticleVendu article : articles) {
	        verifierEtMettreAJourEtatVente(article);
	    }
	}

	// Filtre non connecté
	@Override
	public List<ArticleVendu> rechercherArticles(String recherche, Integer noCategorie) {
		return articleDAO.searchArticles(recherche, noCategorie);
	}

	@Override
	public List<ArticleVendu> filtrerVentes(int userId, Boolean ventesEnCours, Boolean ventesNonDebutees, Boolean ventesTerminees) {
	    // Vérification des valeurs null
	    if (ventesEnCours == null) ventesEnCours = false;
	    if (ventesNonDebutees == null) ventesNonDebutees = false;
	    if (ventesTerminees == null) ventesTerminees = false;

	    // Appel au DAO pour récupérer les articles filtrés
	    return articleDAO.filtrerVentes(userId, ventesEnCours, ventesNonDebutees, ventesTerminees);
	}


	@Override
	public List<ArticleVendu> filtrerAchats(int userId, Boolean encheresOuvertes, Boolean mesEncheresEnCours,
			Boolean mesEncheresRemportees) {
		// Vérification des valeurs null
	    if (encheresOuvertes == null) encheresOuvertes = false;
	    if (mesEncheresEnCours == null) mesEncheresEnCours = false;
	    if (mesEncheresRemportees == null) mesEncheresRemportees = false;

	    // Appel au DAO pour récupérer les articles filtrés
	    return articleDAO.filtrerAchats(userId, encheresOuvertes, mesEncheresEnCours, mesEncheresRemportees);
	}

	



}


