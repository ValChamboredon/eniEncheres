package fr.eni.eniEncheres.bll;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.bo.EtatVente;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.dal.EnchereDAO;
import fr.eni.eniEncheres.dal.ArticleDAO;
import fr.eni.eniEncheres.exception.BusinessException;

@Service
public class EnchereServiceImpl implements EnchereService {

    private final EnchereDAO enchereDAO;
    private final ArticleDAO articleDAO;
    private final CreditService creditService;

    @Autowired
    public EnchereServiceImpl(EnchereDAO enchereDAO, ArticleDAO articleDAO, CreditService creditService) {
        this.enchereDAO = enchereDAO;
        this.articleDAO = articleDAO;
        this.creditService = creditService;
    }

    @Override
    @Transactional
    public void ajouterEnchere(Enchere enchere) throws BusinessException {
        BusinessException businessException = new BusinessException();
        
        // Récupération de l'article et vérification de son état
        ArticleVendu article = articleDAO.getArticleById(enchere.getArticle().getNoArticle());
     // Mise à jour forcée de l'état si nécessaire
        LocalDateTime now = LocalDateTime.now();
        if (article.getEtatVente() == EtatVente.CREEE && 
            !now.isBefore(article.getDateDebutEncheres())) {
            article.setEtatVente(EtatVente.EN_COURS);
            articleDAO.updateEtatVente(article.getNoArticle(), EtatVente.EN_COURS);
            // Recharger l'article après la mise à jour
            article = articleDAO.getArticleById(enchere.getArticle().getNoArticle());
        }
        if (article.getEtatVente() != EtatVente.EN_COURS) {
            businessException.addCleErreur("ERR_ARTICLE_NON_EN_VENTE");
        }

        // Vérification que l'enchérisseur n'est pas le vendeur
        if (article.getVendeur().getNoUtilisateur() == enchere.getUtilisateur().getNoUtilisateur()) {
            businessException.addCleErreur("ERR_ENCHERE_SUR_PROPRE_ARTICLE");
        }

        // Validation basique de l'enchère
        if (enchere.getMontantEnchere() <= 0) {
            businessException.addCleErreur("ERR_MONTANT_ENCHERE_INVALIDE");
        }

        if (enchere.getUtilisateur() == null) {
            businessException.addCleErreur("ERR_UTILISATEUR_OBLIGATOIRE");
        }

        if (enchere.getArticle() == null) {
            businessException.addCleErreur("ERR_ARTICLE_OBLIGATOIRE");
        }

       
            // 1. Obtenir la meilleure enchère actuelle
            List<Enchere> encheres = enchereDAO.getEnchereParArticle(article);
            Enchere meilleureEnchere = encheres.isEmpty() ? null : encheres.get(0);
            try {
            // 2. Vérifier que l'utilisateur a assez de crédit
            creditService.verifierCredit(enchere.getUtilisateur().getNoUtilisateur(), enchere.getMontantEnchere());

            // 3. Si une enchère précédente existe, rembourser l'ancien enchérisseur
            if (meilleureEnchere != null) {
            	System.out.println("Remboursement ancienne enchère : " + meilleureEnchere.getMontantEnchere() + " points");
                creditService.transfererPoints(
                    article.getVendeur().getNoUtilisateur(),  // source (vendeur rembourse)
                    meilleureEnchere.getUtilisateur().getNoUtilisateur(),  // destination (ancien enchérisseur)
                    meilleureEnchere.getMontantEnchere()
                );
                
              enchereDAO.supprimerEnchere(meilleureEnchere);
            }

            // 4. Débiter le nouvel enchérisseur
            System.out.println("Nouvelle enchère : " + enchere.getMontantEnchere() + " points");
            creditService.transfererPoints(
                enchere.getUtilisateur().getNoUtilisateur(),
                article.getVendeur().getNoUtilisateur(),
                enchere.getMontantEnchere()
            );

            // 5. Sauvegarder l'enchère
            enchere.setDateEnchere(LocalDateTime.now());
            enchereDAO.ajouterEnchere(enchere);

            // 6. Mettre à jour le prix de l'article
            article.setPrixVente(enchere.getMontantEnchere());
            articleDAO.updatePrixVente(article.getNoArticle(), enchere.getMontantEnchere());

        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            BusinessException be = new BusinessException();
            be.addCleErreur("ERR_TECHNIQUE");
            throw be;
        }
    }

    @Override
    public List<Enchere> getEncheresParArticle(ArticleVendu article) throws BusinessException {
        // Validation de l'article
        BusinessException be = new BusinessException();
        
        if (article == null) {
            be.addCleErreur("ERR_ARTICLE_NULL");
            throw be;
        }

        if (article.getNoArticle() <= 0) {
            be.addCleErreur("ERR_ID_ARTICLE_INVALIDE");
            throw be;
        }

        try {
            // Vérification que l'article existe en base
            ArticleVendu articleBD = articleDAO.getArticleById(article.getNoArticle());
            if (articleBD == null) {
                be.addCleErreur("ERR_ARTICLE_INEXISTANT");
                throw be;
            }

            // Récupération des enchères
            return enchereDAO.getEnchereParArticle(article);
            
        } catch (BusinessException businessException) {
            // On propage l'erreur business si c'en est une
            throw businessException;
        } catch (Exception e) {
            // Pour toute autre erreur, on crée une nouvelle BusinessException
            be = new BusinessException();
            be.addCleErreur("ERR_RECUPERATION_ENCHERES");
            throw be;
        }
    }

//    @Override
//    public Enchere getEnchereMaxParArticle(int noArticle) throws BusinessException {
//        if (noArticle <= 0) {
//            BusinessException be = new BusinessException();
//            be.addCleErreur("ERR_ID_ARTICLE_INVALIDE");
//            throw be;
//        }
//        return enchereDAO.getEnchereMaxParArticle(noArticle);
//    }

//    @Override
//    @Transactional
//    public void supprimerEncheresParArticle(int noArticle) throws BusinessException {
//        if (noArticle <= 0) {
//            BusinessException be = new BusinessException();
//            be.addCleErreur("ERR_ID_ARTICLE_INVALIDE");
//            throw be;
//        }
//        enchereDAO.supprimerEncheresParArticle(noArticle);
//    }

    @Override
    @Transactional
    public void mettreAJourEnchere(Enchere enchere) throws BusinessException {
        BusinessException be = new BusinessException();
        
        if (enchere.getMontantEnchere() <= 0) {
            be.addCleErreur("ERR_MONTANT_ENCHERE_INVALIDE");
        }

        if (!be.getClesErreurs().isEmpty()) {
            throw be;
        }

        try {
            creditService.verifierCredit(enchere.getUtilisateur().getNoUtilisateur(), enchere.getMontantEnchere());
            enchereDAO.mettreAJourEnchere(enchere);
        } catch (BusinessException creditException) {
            throw creditException;
        }
    }

	@Override
	public Enchere getMeilleureEnchere(int articleId) throws BusinessException {
		ArticleVendu article = articleDAO.getArticleById(articleId);
		List<Enchere> encheres = enchereDAO.getEnchereParArticle(article);
		return encheres.isEmpty() ? null : encheres.get(0);
	}

//    @Override
//    public List<Enchere> getEncheresByUtilisateur(int noUtilisateur) throws BusinessException {
//        if (noUtilisateur <= 0) {
//            BusinessException be = new BusinessException();
//            be.addCleErreur("ERR_ID_UTILISATEUR_INVALIDE");
//            throw be;
//        }
//        return enchereDAO.getEncheresByUtilisateur(noUtilisateur);
//    }
}