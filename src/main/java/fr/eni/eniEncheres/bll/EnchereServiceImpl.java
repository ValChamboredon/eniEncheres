
//package fr.eni.eniEncheres.bll;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import fr.eni.eniEncheres.bo.Enchere;
//import fr.eni.eniEncheres.bo.EtatVente;
//import fr.eni.eniEncheres.bo.ArticleVendu;
//import fr.eni.eniEncheres.dal.EnchereDAO;
//import fr.eni.eniEncheres.dal.ArticleDAO;
//import fr.eni.eniEncheres.exception.BusinessException;
//
//@Service
//public class EnchereServiceImpl implements EnchereService {
//
//    private final EnchereDAO enchereDAO;
//    private final ArticleDAO articleDAO;
//    private final CreditService creditService;
//
//    @Autowired
//    public EnchereServiceImpl(EnchereDAO enchereDAO, ArticleDAO articleDAO, CreditService creditService) {
//        this.enchereDAO = enchereDAO;
//        this.articleDAO = articleDAO;
//        this.creditService = creditService;
//    }
//
//    @Override
//    public void ajouterEnchere(Enchere enchere) throws BusinessException {
//        BusinessException be = new BusinessException();
//        
//        ArticleVendu article = articleDAO.getArticleById(enchere.getArticle().getNoArticle());
//        if (article.getEtatVente() != EtatVente.EN_COURS) {
//            be.addCleErreur("ERR_ARTICLE_NON_EN_VENTE");
//        }
//        // Validation de l'enchère
//        if (enchere.getMontantEnchere() <= 0) {
//            be.addCleErreur("ERR_MONTANT_ENCHERE_INVALIDE");
//        }
//
//        if (enchere.getUtilisateur() == null) {
//            be.addCleErreur("ERR_UTILISATEUR_OBLIGATOIRE");
//        }
//
//        if (enchere.getArticle() == null) {
//            be.addCleErreur("ERR_ARTICLE_OBLIGATOIRE");
//        }
//
//        // Vérification du crédit de l'utilisateur
//        creditService.verifierCredit(enchere.getUtilisateur().getNoUtilisateur(), enchere.getMontantEnchere());
//
//        // Vérification que le montant est supérieur à la meilleure enchère
//        Enchere meilleureEnchere = enchereDAO.getEnchereMaxParArticle(enchere.getArticle().getNoArticle());
//        if (meilleureEnchere != null && enchere.getMontantEnchere() <= meilleureEnchere.getMontantEnchere()) {
//            be.addCleErreur("ERR_MONTANT_INFERIEUR_MEILLEURE_ENCHERE");
//        }
//
//        // Si des erreurs ont été détectées, on les lance
//        if (be.getClesErreurs() != null && !be.getClesErreurs().isEmpty()) {
//            throw be;
//        }
//
//        // Mise à jour de la date de l'enchère
//        enchere.setDateEnchere(LocalDateTime.now());
//        
//        // Ajout de l'enchère
//        enchereDAO.ajouterEnchere(enchere);
//    }
//
//    @Override
//    public List<Enchere> getEncheresParArticle(int noArticle) throws BusinessException {
//        if (noArticle <= 0) {
//            BusinessException be = new BusinessException();
//            be.addCleErreur("ERR_ID_ARTICLE_INVALIDE");
//            throw be;
//        }
//        return enchereDAO.getEncheresParArticle(noArticle);
//    }
//
//    @Override
//    public Enchere getEnchereMaxParArticle(int noArticle) throws BusinessException {
//        if (noArticle <= 0) {
//            BusinessException be = new BusinessException();
//            be.addCleErreur("ERR_ID_ARTICLE_INVALIDE");
//            throw be;
//        }
//        return enchereDAO.getEnchereMaxParArticle(noArticle);
//    }
//
//    @Override
//    public void supprimerEncheresParArticle(int noArticle) throws BusinessException {
//        if (noArticle <= 0) {
//            BusinessException be = new BusinessException();
//            be.addCleErreur("ERR_ID_ARTICLE_INVALIDE");
//            throw be;
//        }
//        enchereDAO.supprimerEncheresParArticle(noArticle);
//    }
//
//    @Override
//    public void mettreAJourEnchere(Enchere enchere) throws BusinessException {
//        BusinessException be = new BusinessException();
//
//        if (enchere.getMontantEnchere() <= 0) {
//            be.addCleErreur("ERR_MONTANT_ENCHERE_INVALIDE");
//        }
//
//        // Vérification du crédit
//        creditService.verifierCredit(enchere.getUtilisateur().getNoUtilisateur(), enchere.getMontantEnchere());
//
//        if (be.getClesErreurs() != null && !be.getClesErreurs().isEmpty()) {
//            throw be;
//        }
//
//        enchereDAO.mettreAJourEnchere(enchere);
//    }
//}

