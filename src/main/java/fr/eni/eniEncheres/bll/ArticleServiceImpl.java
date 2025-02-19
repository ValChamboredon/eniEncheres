package fr.eni.eniEncheres.bll;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import fr.eni.eniEncheres.bll.ArticleService;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.EtatVente;
import fr.eni.eniEncheres.dal.ArticleDAO;
import fr.eni.eniEncheres.exception.BusinessException;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleDAO articleDAO;

    /**
     * Constructeur avec injection de dépendance.
     */
    public ArticleServiceImpl(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    /**
     * Méthode qui fait des vérifications sur l'Article avant de l'envoyer vers la DAL.
     * @throws BusinessException
     */
    @Override
    public void creerArticle(ArticleVendu article) throws BusinessException {
        BusinessException be = new BusinessException();

        // Vérification des dates d'enchère
        if (article.getDateDebutEncheres().isBefore(LocalDate.now())) {
            be.getMessagesErreur().add("La date de début d'enchère est inférieure à la date du jour.");
        }
        if (!(article.getDateFinEncheres().isAfter(article.getDateDebutEncheres()))) {
            be.getMessagesErreur().add("La date de fin d'enchère doit être après la date de début.");
        }    

        // Enregistrement si aucune erreur
        if (be.getMessagesErreur().isEmpty()) {
            article.setPrixVente(article.getMiseAPrix());  // Initialiser le prix de vente

            try {
                articleDAO.addArticle(article);
                article.getVendeur().getArticlesEnVente().add(article);
            } catch (DataAccessException dae) {
                throw new RuntimeException("Erreur lors de l'ajout de l'article", dae);
            }
        } else {
            throw be;
        }
    }

    /**
     * Récupération de tous les articles en vente.
     */
    @Override
    public List<ArticleVendu> consulterTout() {
        List<ArticleVendu> articles = articleDAO.getAllArticles();
        articles.forEach(this::mettreAJourEtatVente);
        return articles;
    }

    /**
     * Récupération d'un article par son ID et mise à jour de son état.
     */
    @Override
    public ArticleVendu getArticleById(int noArticle) {
        ArticleVendu article = articleDAO.getArticleById(noArticle);

        if (article != null) {
            mettreAJourEtatVente(article);
        }
        return article;
    }

    /**
     * Suppression d'un article.
     */
    @Override
    public void supprimerArticle(int articleId) {
        try {
            articleDAO.deleteArticle(articleId);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'article", e);
        }
    }

    /**
     * Modification d'un article.
     */
    @Override
    public void modifierArticle(ArticleVendu article) {
        articleDAO.modifierArticle(article);
    }

    /**
     * Mise à jour automatique de l'état des ventes.
     */
    public void mettreAJourEtatVente(ArticleVendu article) {
        LocalDate today = LocalDate.now();

        if (article.getEtatVente() == EtatVente.CREEE && 
                (today.isEqual(article.getDateDebutEncheres()) || today.isAfter(article.getDateDebutEncheres()))) {
            article.setEtatVente(EtatVente.EN_COURS);
            articleDAO.updateEtatVente(article.getNoArticle(), EtatVente.EN_COURS);
        }

        if (article.getEtatVente() == EtatVente.EN_COURS && today.isAfter(article.getDateFinEncheres())) {
            article.setEtatVente(EtatVente.ENCHERES_TERMINEES);
            articleDAO.updateEtatVente(article.getNoArticle(), EtatVente.ENCHERES_TERMINEES);
        }
    }

    /**
     * Recherche d'articles par mot-clé et catégorie (pas connecté et connecté).
     */
    @Override
    public List<ArticleVendu> rechercherArticles(String recherche, Integer noCategorie) {
        return articleDAO.searchArticles(recherche, noCategorie);
    }

    /**
     * Filtrage des ventes par utilisateur connecté.
     * Filtrage des ventes selon les critères (ventes ouvertes, mes ventes en cours, terminées).
     */
    @Override
    public List<ArticleVendu> filtrerVentes(int userId, Boolean ventesEnCours, Boolean ventesNonDebutees, Boolean ventesTerminees) {
        return articleDAO.filtrerVentes(userId, ventesEnCours, ventesNonDebutees, ventesTerminees);
    }

    /**
     * Filtrage des ventes par utilisateur connecté.
     * Filtrage des achats selon les critères (enchères ouvertes, mes enchères en cours, remportées).
     */
    @Override
    public List<ArticleVendu> filtrerAchats(int userId, Boolean encheresOuvertes, Boolean mesEncheresEnCours, Boolean mesEncheresRemportees) {
        return articleDAO.filtrerAchats(userId, encheresOuvertes, mesEncheresEnCours, mesEncheresRemportees);
    }
}

