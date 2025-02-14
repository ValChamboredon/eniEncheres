package fr.eni.eniEncheres.bll;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import fr.eni.eniEncheres.bo.ArticleVendu;

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
		if (article.getDateDebutEncheres().isBefore(LocalDate.now())) {
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
	public List<ArticleVendu> consulterTout() {

		return articleDAO.getAllArticles();
	}

	@Override
	public ArticleVendu getArticleById(int noArticle) {
	        return articleDAO.getArticleById(noArticle); 
	    
	}


}
