package fr.eni.eniEncheres.bll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.bo.EtatVente;
import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.dal.EnchereDAO;
import fr.eni.eniEncheres.dal.ArticleDAO;
import fr.eni.eniEncheres.exception.BusinessException;

@Service
public class EnchereServiceImpl implements EnchereService {

    private final EnchereDAO enchereDAO;
    private final ArticleDAO articleDAO;
    private final CreditService creditService;
    private final ArticleService articleService;
    private final UtilisateurService utilisateurService;

    public EnchereServiceImpl(EnchereDAO enchereDAO, ArticleDAO articleDAO, CreditService creditService, ArticleService articleService, UtilisateurService utilisateurService) {
        this.enchereDAO = enchereDAO;
        this.articleDAO = articleDAO;
        this.creditService = creditService;
		this.articleService = articleService;
		this.utilisateurService = utilisateurService;
    }

    @Override
    public void ajouterEnchere(Enchere enchere) throws BusinessException {
        BusinessException be = new BusinessException();
        
        ArticleVendu article = articleDAO.getArticleById(enchere.getArticle().getNoArticle());
        if (article.getEtatVente() != EtatVente.EN_COURS) {
            be.addCleErreur("ERR_ARTICLE_NON_EN_VENTE");
        }
        // Validation de l'enchère
        if (enchere.getMontantEnchere() <= 0) {
            be.addCleErreur("ERR_MONTANT_ENCHERE_INVALIDE");
        }

        if (enchere.getUtilisateur() == null) {
            be.addCleErreur("ERR_UTILISATEUR_OBLIGATOIRE");
        }

        if (enchere.getArticle() == null) {
            be.addCleErreur("ERR_ARTICLE_OBLIGATOIRE");
        }

        // Vérification du crédit de l'utilisateur
        creditService.verifierCredit(enchere.getUtilisateur().getNoUtilisateur(), enchere.getMontantEnchere());

        // Vérification que le montant est supérieur à la meilleure enchère
        Enchere meilleureEnchere = enchereDAO.getEnchereMaxParArticle(enchere.getArticle().getNoArticle());
        if (meilleureEnchere != null && enchere.getMontantEnchere() <= meilleureEnchere.getMontantEnchere()) {
            be.addCleErreur("ERR_MONTANT_INFERIEUR_MEILLEURE_ENCHERE");
        }

        // Si des erreurs ont été détectées, on les lance
        if (be.getClesErreurs() != null && !be.getClesErreurs().isEmpty()) {
            throw be;
        }

        // Mise à jour de la date de l'enchère
        enchere.setDateEnchere(LocalDateTime.now());
        
        // Ajout de l'enchère
        enchereDAO.ajouterEnchere(enchere);
    }

    @Override
    public List<Enchere> getEncheresParArticle(int noArticle) throws BusinessException {
        if (noArticle <= 0) {
            BusinessException be = new BusinessException();
            be.addCleErreur("ERR_ID_ARTICLE_INVALIDE");
            throw be;
        }
        return enchereDAO.getEncheresParArticle(noArticle);
    }

    @Override
    public Enchere getEnchereMaxParArticle(int noArticle) throws BusinessException {
        if (noArticle <= 0) {
            BusinessException be = new BusinessException();
            be.addCleErreur("ERR_ID_ARTICLE_INVALIDE");
            throw be;
        }
        return enchereDAO.getEnchereMaxParArticle(noArticle);
    }

    @Override
    public void supprimerEncheresParArticle(int noArticle) throws BusinessException {
        if (noArticle <= 0) {
            BusinessException be = new BusinessException();
            be.addCleErreur("ERR_ID_ARTICLE_INVALIDE");
            throw be;
        }
        enchereDAO.supprimerEncheresParArticle(noArticle);
    }

    @Override
    public void mettreAJourEnchere(Enchere enchere) throws BusinessException {
        BusinessException be = new BusinessException();

        if (enchere.getMontantEnchere() <= 0) {
            be.addCleErreur("ERR_MONTANT_ENCHERE_INVALIDE");
        }

        // Vérification du crédit
        creditService.verifierCredit(enchere.getUtilisateur().getNoUtilisateur(), enchere.getMontantEnchere());

        if (be.getClesErreurs() != null && !be.getClesErreurs().isEmpty()) {
            throw be;
        }

        enchereDAO.mettreAJourEnchere(enchere);
    }

	@Override
	public void placerEnchere(int articleId, String email, int montantEnchere) throws BusinessException {
		 BusinessException be = new BusinessException();

		    ArticleVendu article = articleService.getArticleById(articleId);
		    Utilisateur encherisseur = utilisateurService.getUtilisateurByEmail(email);

		    if (article == null || encherisseur == null) {
		        be.addCleErreur("ERR_ARTICLE_OU_UTILISATEUR_NON_TROUVE");
		    }

		    if (article != null && article.getDateFinEncheres().isBefore(LocalDate.now())) {
		        be.addCleErreur("ERR_ENCHERE_TERMINEE");
		    }

		    Enchere meilleureEnchere = enchereDAO.getEnchereMaxParArticle(articleId);
		    if (meilleureEnchere != null && montantEnchere <= meilleureEnchere.getMontantEnchere()) {
		        be.addCleErreur("ERR_MONTANT_INFERIEUR");
		    }

		    try {
		        creditService.verifierCredit(encherisseur.getNoUtilisateur(), montantEnchere);
		    } catch (BusinessException creditException) {
		        be.getClesErreurs().addAll(creditException.getClesErreurs());
		    }

		    if (!be.getClesErreurs().isEmpty()) {
		        throw be;
		    }

		    Enchere nouvelleEnchere = new Enchere();
		    nouvelleEnchere.setArticle(article);
		    nouvelleEnchere.setUtilisateur(encherisseur);
		    nouvelleEnchere.setDateEnchere(LocalDateTime.now());
		    nouvelleEnchere.setMontantEnchere(montantEnchere);

		    enchereDAO.ajouterEnchere(nouvelleEnchere);

		    // Mettre à jour le prix de vente de l'article
		    article.setPrixVente(montantEnchere);
		    articleService.updateArticle(article);
	}
	
	@Override
	public void finaliserEncheresTerminees() {
	    List<ArticleVendu> articlesTermines = articleService.getArticlesTermines();
	    for (ArticleVendu article : articlesTermines) {
	        try {
	            Enchere meilleureEnchere = enchereDAO.getEnchereMaxParArticle(article.getNoArticle());
	            if (meilleureEnchere != null) {
	                article.setEtatVente(EtatVente.VENDU);
	                Utilisateur gagnant = meilleureEnchere.getUtilisateur();
	                Utilisateur vendeur = article.getVendeur();
	                
	                creditService.transfererPoints(gagnant.getNoUtilisateur(), vendeur.getNoUtilisateur(), meilleureEnchere.getMontantEnchere());
	                
	                
	            } else {
	                article.setEtatVente(EtatVente.NON_VENDU);
	            }
	            articleService.updateArticle(article);
	        } catch (BusinessException be) {
	            // Log l'erreur ou gérez-la d'une manière appropriée
	            System.err.println("Erreur lors de la finalisation de l'enchère pour l'article " + article.getNoArticle() + ": " + be.getClesErreurs());
	        }
	    }
	}
	
	@Override
	public List<Enchere> getEncheresByUtilisateur(int noUtilisateur) throws BusinessException {
	    BusinessException be = new BusinessException();

	    if (noUtilisateur <= 0) {
	        be.addCleErreur("ERR_ID_UTILISATEUR_INVALIDE");
	        throw be;
	    }

	    Utilisateur utilisateur = utilisateurService.getUtilisateurById(noUtilisateur);
	    if (utilisateur == null) {
	        be.addCleErreur("ERR_UTILISATEUR_NON_TROUVE");
	        throw be;
	    }

	    return enchereDAO.getEncheresByUtilisateur(noUtilisateur);
	}
}