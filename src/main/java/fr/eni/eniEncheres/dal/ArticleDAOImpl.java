package fr.eni.eniEncheres.dal;
 
import java.util.ArrayList;
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
 
import fr.eni.eniEncheres.bo.ArticleVendu;

import fr.eni.eniEncheres.dal.mapper.ArticleRowMapper;
 
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
		return namedParameterJdbcTemplate.queryForObject(sql, params, new ArticleRowMapper());
	}
 
	@Override
	public List<ArticleVendu> getAllArticles() {
		String requeteSQL = "SELECT articles_vendus.*, utilisateurs.*,categories.* FROM articles_vendus INNER JOIN utilisateurs	ON utilisateurs.no_utilisateur = articles_vendus.no_utilisateur INNER JOIN categories ON categories.no_categorie = articles_vendus.no_categorie";

		return jdbcTemplate.query(requeteSQL, new ArticleRowMapper());

	}
 
	@Override
	public void addArticle(ArticleVendu article) {
		KeyHolder keyholder =new GeneratedKeyHolder();
		
		String requete = "INSERT INTO [ARTICLES_VENDUS] ([nom_article], [description], [date_debut_encheres], [date_fin_encheres], [prix_initial], [prix_vente], [no_utilisateur], [no_categorie]) VALUES (:nom_article, :description, :date_debut_encheres, :date_fin_encheres, :prix_initial, :prix_vente, :no_utilisateur, :no_categorie)";

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		namedParameters.addValue("nom_article", article.getNomArticle());
		namedParameters.addValue("description", article.getDescription());
		namedParameters.addValue("date_debut_encheres", article.getDateDebutEncheres());
		namedParameters.addValue("date_fin_encheres", article.getDateFinEncheres());
		namedParameters.addValue("prix_initial", article.getMiseAPrix());
		namedParameters.addValue("prix_vente", article.getPrixVente());

		namedParameters.addValue("no_utilisateur", article.getVendeur().getNoUtilisateur());
		namedParameters.addValue("no_categorie", article.getCategorie().getNoCategorie());

		namedParameterJdbcTemplate.update(requete, namedParameters, keyholder);
		
		if (keyholder != null && keyholder.getKey().intValue() > 0) {
			
			// Si le no_article a été crée, on l'ajoute à article
			article.setNoArticle(keyholder.getKey().intValue());
		}

	}
 
//	@Override
//	public void updateArticle(ArticleVendu article) {
//		String sql = "UPDATE ARTICLES_VENDUS SET nom_article = ?, description = ?, date_debut_encheres = ?, date_fin_encheres = ?, prix_initial = ?, prix_vente = ?, etat_vente = ?, no_utilisateur = ?, no_categorie = ? WHERE no_article = ?";
//        jdbcTemplate.update(sql,
//                article.getNomArticle(),
//                article.getDescription(),
//                article.getDateDebutEncheres(),
//                article.getDateFinEncheres(),
//                article.getMiseAPrix(),
//                article.getPrixVente(),
//                article.getEtatVente(),
//                article.getVendeur().getNoUtilisateur(),
//                article.getCategorie().getNoCategorie(),
//                article.getNoArticle()
//        );
//		
//	}
 
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
	    try {
	        String sql = "SELECT av.*, u.pseudo as vendeur_pseudo, c.libelle as categorie_libelle " +
	                     "FROM ARTICLES_VENDUS av " +
	                     "JOIN UTILISATEURS u ON av.no_utilisateur = u.no_utilisateur " +
	                     "JOIN CATEGORIES c ON av.no_categorie = c.no_categorie " +
	                     "WHERE av.etat_vente = 'EN_COURS'";
	        return jdbcTemplate.query(sql, new ArticleRowMapper());
	    } catch (Exception e) {
	    	System.err.println("Erreur SQL : " + e.getMessage());
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
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
		String sql = "SELECT * FROM ARTICLES_VENDUS WHERE date_fin_encheres < GETDATE()";
	    return jdbcTemplate.query(sql, new ArticleRowMapper());
	}
}
