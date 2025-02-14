package fr.eni.eniEncheres.bll;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.dal.ArticleDAO;
import fr.eni.eniEncheres.exception.BusinessException;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleDAO articleDAO;

    public ArticleServiceImpl(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    @Override
    public void creerArticle(ArticleVendu article) throws BusinessException {
        BusinessException be = new BusinessException();

        if (article.getDateDebutEncheres().isBefore(LocalDate.now())) {
            be.getMessagesErreur().add("La date de début d'enchère est inférieure à la date du jour");
        }

        if (!(article.getDateFinEncheres().isAfter(article.getDateDebutEncheres()))) {
            be.getMessagesErreur().add("La date de fin d'enchère n'est pas après la date de début.");
        }

        if (be.getMessagesErreur().size() == 0) {
            article.setPrixVente(article.getMiseAPrix());
            try {
                articleDAO.addArticle(article);
                article.getVendeur().getArticlesEnVente().add(article);
            } catch (DataAccessException dae) {
                System.err.println(dae.getStackTrace());
            }
        } else {
            throw be;
        }
    }

    @Override
    public List<ArticleVendu> consulterTout() {
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
    public List<ArticleVendu> getArticlesEnVente() {
        return articleDAO.getArticlesEnVente();
    }

    @Override
    public ArticleVendu getArticleById(int id) {
        return articleDAO.getArticleById(id);
    }

    @Override
    public List<ArticleVendu> getAllArticles() {
        return articleDAO.getAllArticles();
    }

    @Override
    public void demarrerVente(int noArticle) throws BusinessException {
        ArticleVendu article = articleDAO.getArticleById(noArticle);

        BusinessException be = new BusinessException();
        if (article.getEtatVente() == null || !article.getEtatVente().equals("CREEE")) {
            be.getMessagesErreur().add("ERR_VENTE_DEJA_DEMARREE");
        }

        LocalDate now = LocalDate.now();
        if (now.isBefore(article.getDateDebutEncheres())) {
            be.getMessagesErreur().add("ERR_DATE_DEBUT_FUTURE");
        }

        if (!be.getMessagesErreur().isEmpty()) {
            throw be;
        }

        article.setEtatVente("EN_COURS");
        articleDAO.updateArticle(article);
    }
}
