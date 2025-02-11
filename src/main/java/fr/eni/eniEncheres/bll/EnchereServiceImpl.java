package fr.eni.eniEncheres.bll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.dal.ArticleDAO;
import fr.eni.eniEncheres.dal.EnchereDAO;
import fr.eni.eniEncheres.dal.UtilisateurDAO;
import fr.eni.eniEncheres.exception.BusinessException;

@Service
public class EnchereServiceImpl implements EnchereService {

	 @Autowired
	 private EnchereDAO enchereDAO;

	 @Autowired
	 private ArticleDAO articleDAO;

	 @Autowired
	    private UtilisateurDAO utilisateurDAO;

    
    @Override
    @Transactional
    public void creerEnchere(Enchere enchere) throws BusinessException {
        ArticleVendu articleVendu = articleDAO.getArticleById(enchere.getArticle().getNoArticle());
        Utilisateur utilisateur = utilisateurDAO.read(enchere.getUtilisateur().getNoUtilisateur());

        if (articleVendu == null || utilisateur == null) {
            throw new BusinessException("Article ou utilisateur non trouvé.");
        }

        if (enchere.getMontantEnchere() <= articleVendu.getPrixVente()) {
            throw new BusinessException("Le montant de l'enchère doit être supérieur au prix actuel.");
        }

        if (utilisateur.getCredit() < enchere.getMontantEnchere()) {
            throw new BusinessException("Crédits insuffisants pour placer l'enchère.");
        }

        // Mise à jour du prix de vente de l'article
        articleVendu.setPrixVente(enchere.getMontantEnchere());
        articleDAO.update(articleVendu);

        // Déduction des crédits de l'utilisateur
        utilisateur.setCredit(utilisateur.getCredit() - enchere.getMontantEnchere());
        utilisateurDAO.update(utilisateur);

        // Enregistrement de l'enchère
        enchereDAO.save(enchere);
    }



    @Override
    public Enchere obtenirEnchereLaPlusHaute(int noArticle) {
        List<Enchere> encheres = enchereDAO.findByArticleId(noArticle);
        return encheres.stream()
                       .max((e1, e2) -> e1.getMontantEnchere().compareTo(e2.getMontantEnchere()))
                       .orElse(null);
    }

}
