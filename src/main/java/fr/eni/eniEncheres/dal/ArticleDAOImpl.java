package fr.eni.eniEncheres.dal;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.dal.mapper.ArticleRowMapper;

/**
 * ✅ Implémentation de l'interface ArticleDAO pour gérer les articles vendus.
 */
@Repository
public class ArticleDAOImpl implements ArticleDAO {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ArticleDAOImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * ✅ Récupère un article par son ID.
     * 
     * @param id L'identifiant de l'article.
     * @return L'article correspondant.
     */
    @Override
    public ArticleVendu getArticleById(int id) {
        String sql = "SELECT * FROM ARTICLES_VENDUS WHERE no_article = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, params, new ArticleRowMapper());
    }

    /**
     * ✅ Récupère tous les articles en base de données.
     * 
     * @return Liste de tous les articles.
     */
    @Override
    public List<ArticleVendu> getAllArticles() {
        String sql = "SELECT * FROM ARTICLES_VENDUS";
        return jdbcTemplate.query(sql, new ArticleRowMapper());
    }

    /**
     * ✅ Ajoute un nouvel article en base de données.
     * 
     * @param article L'article à ajouter.
     */
    @Override
    public void addArticle(ArticleVendu article) {
        KeyHolder keyholder = new GeneratedKeyHolder();
        String sql = "INSERT INTO ARTICLES_VENDUS (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie) "
                + "VALUES (:nom_article, :description, :date_debut_encheres, :date_fin_encheres, :prix_initial, :prix_vente, :no_utilisateur, :no_categorie)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("nom_article", article.getNomArticle());
        params.addValue("description", article.getDescription());
        params.addValue("date_debut_encheres", article.getDateDebutEncheres());
        params.addValue("date_fin_encheres", article.getDateFinEncheres());
        params.addValue("prix_initial", article.getMiseAPrix());
        params.addValue("prix_vente", article.getPrixVente());
        params.addValue("no_utilisateur", article.getVendeur().getNoUtilisateur());
        params.addValue("no_categorie", article.getCategorie().getNoCategorie());

        namedParameterJdbcTemplate.update(sql, params, keyholder);
        if (keyholder.getKey() != null) {
            article.setNoArticle(keyholder.getKey().intValue());
        }
    }

    /**
     * ✅ Supprime un article par son ID.
     * 
     * @param id L'identifiant de l'article.
     */
    @Override
    public void deleteArticle(int id) {
        String sql = "DELETE FROM ARTICLES_VENDUS WHERE no_article = ?";
        jdbcTemplate.update(sql, id);
    }
}
