package fr.eni.eniEncheres.dal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import fr.eni.eniEncheres.bo.ArticleVendu;

public class ArticleDAOImpl implements ArticleDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;


	@SuppressWarnings("deprecation")
	@Override
	public ArticleVendu getArticleById(int id) {
		String sql = "SELECT * FROM ARTICLES_VENDUS WHERE no_article = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
			new ArticleVendu(
					rs.getInt("no_article"),
					rs.getString("nom_article"),
					rs.getString("description"),
					rs.getDate("date_debut_encheres").toLocalDate(),
					rs.getDate("date_fin_encheres").toLocalDate(),
					rs.getFloat("prix_initial"),
					rs.getFloat("prix_vente"),
					rs.getString("etat_vente")
					)
		);
	}

	@Override
	public List<ArticleVendu> getAllArticles() {
		String sql = "SELECT * FROM ARTICLES_VENDUS";
		return jdbcTemplate.query(sql, (rs, rowNum) ->
        new ArticleVendu(
                rs.getInt("no_article"),
                rs.getString("nom_article"),
                rs.getString("description"),
                rs.getDate("date_debut_encheres").toLocalDate(),
                rs.getDate("date_fin_encheres").toLocalDate(),
                rs.getFloat("prix_initial"),
                rs.getFloat("prix_vente"),
                rs.getString("etat_vente")
        	)
		);
	}

	@Override
	public void addArticle(ArticleVendu article) {
		String sql = "INSERT INTO ARTICLES_VENDUS (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, etat_vente, no_utilisateur, no_categorie) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql,
                article.getNomArticle(),
                article.getDescription(),
                article.getDateDebutEncheres(),
                article.getDateFinEncheres(),
                article.getMiseAPrix(),
                article.getPrixVente(),
                article.getEtatVente(),
                article.getVendeur().getNoUtilisateur(),
                article.getCategorie().getNoCategorie()
        );
	}

	@Override
	public void updateArticle(ArticleVendu article) {
		String sql = "UPDATE ARTICLES_VENDUS SET nom_article = ?, description = ?, date_debut_encheres = ?, date_fin_encheres = ?, prix_initial = ?, prix_vente = ?, etat_vente = ?, no_utilisateur = ?, no_categorie = ? WHERE no_article = ?";
        jdbcTemplate.update(sql,
                article.getNomArticle(),
                article.getDescription(),
                article.getDateDebutEncheres(),
                article.getDateFinEncheres(),
                article.getMiseAPrix(),
                article.getPrixVente(),
                article.getEtatVente(),
                article.getVendeur().getNoUtilisateur(),
                article.getCategorie().getNoCategorie(),
                article.getNoArticle()
        );
		
	}

	@Override
	public void deleteArticle(int id) {
		String sql = "DELETE FROM ARTICLES_VENDUS WHERE no_article = ?";
        jdbcTemplate.update(sql, id);
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ArticleVendu> getArticlesByCategory(int categoryId) {
		String sql = "SELECT * FROM ARTICLES_VENDUS WHERE no_categorie = ?";
        return jdbcTemplate.query(sql, new Object[]{categoryId}, (rs, rowNum) ->
                new ArticleVendu(
                        rs.getInt("no_article"),
                        rs.getString("nom_article"),
                        rs.getString("description"),
                        rs.getDate("date_debut_encheres").toLocalDate(),
                        rs.getDate("date_fin_encheres").toLocalDate(),
                        rs.getFloat("prix_initial"),
                        rs.getFloat("prix_vente"),
                        rs.getString("etat_vente")
                )
        );
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ArticleVendu> getArticlesByUser(int userId) {
		 String sql = "SELECT * FROM ARTICLES_VENDUS WHERE no_utilisateur = ?";
	        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
	                new ArticleVendu(
	                        rs.getInt("no_article"),
	                        rs.getString("nom_article"),
	                        rs.getString("description"),
	                        rs.getDate("date_debut_encheres").toLocalDate(),
	                        rs.getDate("date_fin_encheres").toLocalDate(),
	                        rs.getFloat("prix_initial"),
	                        rs.getFloat("prix_vente"),
	                        rs.getString("etat_vente")
	                )
	        );
	}

	@Override
	public List<ArticleVendu> getArticlesEnVente() {
		String sql = "SELECT * FROM ARTICLES_VENDUS WHERE etat_vente = 'EN_COURS'";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ArticleVendu(
                        rs.getInt("no_article"),
                        rs.getString("nom_article"),
                        rs.getString("description"),
                        rs.getDate("date_debut_encheres").toLocalDate(),
                        rs.getDate("date_fin_encheres").toLocalDate(),
                        rs.getFloat("prix_initial"),
                        rs.getFloat("prix_vente"),
                        rs.getString("etat_vente")
                )
        );
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ArticleVendu> searchArticles(String keyword, int categoryId) {
		String sql = "SELECT * FROM ARTICLES_VENDUS WHERE nom_article LIKE ? AND (? = 0 OR no_categorie = ?)";
        return jdbcTemplate.query(sql, new Object[]{"%" + keyword + "%", categoryId, categoryId}, (rs, rowNum) ->
                new ArticleVendu(
                        rs.getInt("no_article"),
                        rs.getString("nom_article"),
                        rs.getString("description"),
                        rs.getDate("date_debut_encheres").toLocalDate(),
                        rs.getDate("date_fin_encheres").toLocalDate(),
                        rs.getFloat("prix_initial"),
                        rs.getFloat("prix_vente"),
                        rs.getString("etat_vente")
                )
        );
	}
}
