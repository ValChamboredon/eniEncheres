package fr.eni.eniEncheres.dal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.eniEncheres.bo.ArticleVendu;

@Repository
public class ArticleDAOImpl implements ArticleDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


	@Override
	public ArticleVendu getArticleById(int id) {
		String sql = "SELECT * FROM ARTICLES_VENDUS WHERE no_article = :id";
		MapSqlParameterSource params = new MapSqlParameterSource("id", id);
		return namedParameterJdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(ArticleVendu.class));
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


	@Override
	public List<ArticleVendu> getArticlesByCategory(int categoryId) {
		String sql = "SELECT * FROM ARTICLES_VENDUS WHERE no_categorie = :categoryId";
		MapSqlParameterSource params = new MapSqlParameterSource("categoryId", categoryId);
	    return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(ArticleVendu.class));
	}

	
	@Override
	public List<ArticleVendu> getArticlesByUser(int userId) {
		 String sql = "SELECT * FROM ARTICLES_VENDUS WHERE no_utilisateur = :userId";
		 MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);
		 return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(ArticleVendu.class));  
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


	@Override
	public List<ArticleVendu> searchArticles(String keyword, int categoryId) {
		String sql = "SELECT * FROM ARTICLES_VENDUS WHERE nom_article LIKE :keyword AND (:categoryId = 0 OR no_categorie = :categoryId)";
		MapSqlParameterSource params = new MapSqlParameterSource();
	    params.addValue("keyword", "%" + keyword + "%");
	    params.addValue("categoryId", categoryId);
	    return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(ArticleVendu.class));
	}
	
	@Override
	public List<ArticleVendu> getArticlesTermines() {
	    String sql = "SELECT * FROM ARTICLES_VENDUS WHERE date_fin_encheres < CURRENT_DATE()";
	    return jdbcTemplate.query(sql, new ArticleRowMapper());
	}
}
