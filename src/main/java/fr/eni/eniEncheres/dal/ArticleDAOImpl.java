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
import fr.eni.eniEncheres.bo.EtatVente;
import fr.eni.eniEncheres.dal.mapper.ArticleRowMapper;

@Repository
public class ArticleDAOImpl implements ArticleDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public ArticleVendu getArticleById(int noArticle) {
		String sql = "SELECT av.*, "
				+ "u.pseudo, u.nom, u.prenom, u.email, u.telephone, u.rue AS user_rue, u.code_postal AS user_code_postal, u.ville AS user_ville, "
				+ "c.libelle, " + "r.rue AS retrait_rue, " + "r.code_postal AS retrait_code_postal, "
				+ "r.ville AS retrait_ville, "
				+ "CASE WHEN r.no_article IS NULL THEN u.rue ELSE r.rue END AS display_rue, "
				+ "CASE WHEN r.no_article IS NULL THEN u.code_postal ELSE r.code_postal END AS display_code_postal, "
				+ "CASE WHEN r.no_article IS NULL THEN u.ville ELSE r.ville END AS display_ville "
				+ "FROM ARTICLES_VENDUS av " + "JOIN UTILISATEURS u ON av.no_utilisateur = u.no_utilisateur "
				+ "JOIN CATEGORIES c ON av.no_categorie = c.no_categorie "
				+ "LEFT JOIN RETRAITS r ON av.no_article = r.no_article " + "WHERE av.no_article = :noArticle";

		MapSqlParameterSource params = new MapSqlParameterSource("noArticle", noArticle);
		ArticleVendu article = namedParameterJdbcTemplate.queryForObject(sql, params, new ArticleRowMapper());

		// Pour debug
		if (article != null && article.getLieuDeRetrait() != null) {
			System.out.println("Lieu de retrait pour article " + noArticle + ":");
			System.out.println("Rue: " + article.getLieuDeRetrait().getRue());
			System.out.println("Code postal: " + article.getLieuDeRetrait().getCodePostal());
			System.out.println("Ville: " + article.getLieuDeRetrait().getVille());
		}

		return article;
	}

	@Override
	public List<ArticleVendu> getAllArticles() {
		String sql = "SELECT av.*, " + "u.pseudo, u.nom, u.prenom, u.email, u.telephone, "
				+ "u.rue AS user_rue, u.code_postal AS user_code_postal, u.ville AS user_ville, " + "c.libelle, "
				+ "COALESCE(r.rue, u.rue) AS retrait_rue, "
				+ "COALESCE(r.code_postal, u.code_postal) AS retrait_code_postal, "
				+ "COALESCE(r.ville, u.ville) AS retrait_ville " + "FROM ARTICLES_VENDUS av "
				+ "JOIN UTILISATEURS u ON av.no_utilisateur = u.no_utilisateur "
				+ "JOIN CATEGORIES c ON av.no_categorie = c.no_categorie "
				+ "LEFT JOIN RETRAITS r ON av.no_article = r.no_article";

		return jdbcTemplate.query(sql, new ArticleRowMapper());

	}

	@Override
	public void addArticle(ArticleVendu article) {
	    KeyHolder keyholder = new GeneratedKeyHolder();

	    // 1. Insertion de l'article
	    String requete = "INSERT INTO [ARTICLES_VENDUS] ([nom_article], [description], [date_debut_encheres], [date_fin_encheres], [prix_initial], [prix_vente], [etat_vente], [no_utilisateur], [no_categorie]) VALUES (:nom_article, :description, :date_debut_encheres, :date_fin_encheres, :prix_initial, :prix_vente, :etat_vente, :no_utilisateur, :no_categorie)";

	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("nom_article", article.getNomArticle());
	    namedParameters.addValue("description", article.getDescription());
	    namedParameters.addValue("date_debut_encheres", article.getDateDebutEncheres());
	    namedParameters.addValue("date_fin_encheres", article.getDateFinEncheres());
	    namedParameters.addValue("prix_initial", article.getMiseAPrix());
	    namedParameters.addValue("prix_vente", article.getPrixVente());
	    namedParameters.addValue("no_utilisateur", article.getVendeur().getNoUtilisateur());
	    namedParameters.addValue("no_categorie", article.getCategorie().getNoCategorie());
	    namedParameters.addValue("etat_vente", article.getEtatVente().name());

	    namedParameterJdbcTemplate.update(requete, namedParameters, keyholder, new String[]{"no_article"});

	    // Récupère l'ID de l'article créé
	    if (keyholder.getKey() != null) {
	        int articleId = keyholder.getKey().intValue();
	        article.setNoArticle(articleId);

	        // 2. Insertion du lieu de retrait
	        if (article.getLieuDeRetrait() != null) {
	            String requeteRetrait = "INSERT INTO RETRAITS (no_article, rue, code_postal, ville) VALUES (:no_article, :rue, :code_postal, :ville)";
	            
	            MapSqlParameterSource retraitParams = new MapSqlParameterSource();
	            retraitParams.addValue("no_article", articleId);
	            retraitParams.addValue("rue", article.getLieuDeRetrait().getRue());
	            retraitParams.addValue("code_postal", article.getLieuDeRetrait().getCodePostal());
	            retraitParams.addValue("ville", article.getLieuDeRetrait().getVille());

	            try {
	                namedParameterJdbcTemplate.update(requeteRetrait, retraitParams);
	                System.out.println("Retrait enregistré pour l'article " + articleId + 
	                    " à l'adresse: " + article.getLieuDeRetrait().getRue() + 
	                    ", " + article.getLieuDeRetrait().getCodePostal() + 
	                    " " + article.getLieuDeRetrait().getVille());
	            } catch (Exception e) {
	                System.err.println("Erreur lors de l'insertion du retrait pour l'article " + articleId + ": " + e.getMessage());
	                throw e;
	            }
	           
	        }
	    }
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
			String sql = "SELECT av.*, u.pseudo as vendeur_pseudo, c.libelle as categorie_libelle "
					+ "FROM ARTICLES_VENDUS av " + "JOIN UTILISATEURS u ON av.no_utilisateur = u.no_utilisateur "
					+ "JOIN CATEGORIES c ON av.no_categorie = c.no_categorie " + "WHERE av.etat_vente = 'EN_COURS'";
			return jdbcTemplate.query(sql, new ArticleRowMapper());
		} catch (Exception e) {
			System.err.println("Erreur SQL : " + e.getMessage());

			return new ArrayList<>();
		}
	}

	// filtre pour les enchères
	// affiche les articles en COURS uniquement sur l'index avec cette requête SQL
	// utilisateur pas co
	@Override
	public List<ArticleVendu> searchArticles(String keyword, int categoryId) {

		String sql = "SELECT av.*, " + "u.no_utilisateur, u.pseudo, u.email, "
				+ "u.rue AS user_rue, u.code_postal AS user_code_postal, u.ville AS user_ville, "
				+ "c.no_categorie, c.libelle, " + "COALESCE(r.rue, u.rue) AS retrait_rue, "
				+ "COALESCE(r.code_postal, u.code_postal) AS retrait_code_postal, "
				+ "COALESCE(r.ville, u.ville) AS retrait_ville " + "FROM ARTICLES_VENDUS av "
				+ "JOIN UTILISATEURS u ON av.no_utilisateur = u.no_utilisateur "
				+ "JOIN CATEGORIES c ON av.no_categorie = c.no_categorie "
				+ "LEFT JOIN RETRAITS r ON av.no_article = r.no_article "
				+ "WHERE (av.etat_vente = 'CREEE' AND av.date_debut_encheres <= CURRENT_TIMESTAMP) "
				+ "   OR av.etat_vente = 'EN_COURS' " + "AND (:keyword IS NULL OR av.nom_article LIKE :keyword) "
				+ "AND (:categoryId = 0 OR av.no_categorie = :categoryId)";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("keyword", keyword != null && !keyword.isEmpty() ? "%" + keyword + "%" : null);
		params.addValue("categoryId", categoryId);

		return namedParameterJdbcTemplate.query(sql, params, new ArticleRowMapper());

	}

	@Override
	public List<ArticleVendu> getArticlesTermines() {
		String sql = "SELECT * FROM ARTICLES_VENDUS WHERE date_fin_encheres <= CURRENT_DATE AND etat_vente = 'EN_COURS'";
		return jdbcTemplate.query(sql, new ArticleRowMapper());
	}

	@Override
	public void updateEtatVente(int noArticle, EtatVente nouvelEtat) {
		String sql = "UPDATE ARTICLES_VENDUS SET etat_vente = :etat_vente WHERE no_article = :no_article";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("etat_vente", nouvelEtat.name()); // Convertit l'Enum en String
		params.addValue("no_article", noArticle);

		int rowsUpdated = namedParameterJdbcTemplate.update(sql, params);
		System.out.println("Mise à jour de l'état de vente - Article #" + noArticle + ", Nouvel état : "
				+ nouvelEtat.name() + ", Lignes mises à jour : " + rowsUpdated);

	}

	@Override
	public void deleteArticle(int articleId) {
		// Supprimer d'abord l'entrée de RETRAITS associée à l'article
	    String sqlRetrait = "DELETE FROM RETRAITS WHERE no_article = ?";
	    jdbcTemplate.update(sqlRetrait, articleId);

	    // Ensuite, supprimer l'article
	    String sqlArticle = "DELETE FROM ARTICLES_VENDUS WHERE no_article = ?";
	    jdbcTemplate.update(sqlArticle, articleId);
	}

	@Override

	public void modifierArticle(ArticleVendu article) {
		String requete = "UPDATE ARTICLES_VENDUS SET nom_article = ?, description = ?, "
				+ "date_debut_encheres = ?, date_fin_encheres = ?, prix_initial = ? "
				+ "WHERE no_article = ? AND etat_vente = 'CREEE'";

		jdbcTemplate.update(requete, article.getNomArticle(), article.getDescription(), article.getDateDebutEncheres(),
				article.getDateFinEncheres(), article.getMiseAPrix(), article.getNoArticle());

	}

	@Override
	public void updatePrixVente(int noArticle, int nouveauPrix) {
		String sql = "UPDATE ARTICLES_VENDUS SET prix_vente = :prix_vente WHERE no_article = :no_article";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("prix_vente", nouveauPrix);
		params.addValue("no_article", noArticle);

		namedParameterJdbcTemplate.update(sql, params);
	}

	// Methode pour filtrer les ventes en fonction de l'utilisateur co. Vente en
	// cours,terminées et pas commencées.
	@Override
	public List<ArticleVendu> filtrerVentes(int userId, Boolean ventesEnCours, Boolean ventesNonDebutees,
			Boolean ventesTerminees) {
		StringBuilder sql = new StringBuilder("SELECT av.*, " + "u.no_utilisateur, u.pseudo, u.email, "
				+ "u.rue AS user_rue, u.code_postal AS user_code_postal, u.ville AS user_ville, "
				+ "c.no_categorie, c.libelle, " + "COALESCE(r.rue, u.rue) AS retrait_rue, "
				+ "COALESCE(r.code_postal, u.code_postal) AS retrait_code_postal, "
				+ "COALESCE(r.ville, u.ville) AS retrait_ville " + "FROM ARTICLES_VENDUS av "
				+ "JOIN UTILISATEURS u ON av.no_utilisateur = u.no_utilisateur "
				+ "JOIN CATEGORIES c ON av.no_categorie = c.no_categorie "
				+ "LEFT JOIN RETRAITS r ON av.no_article = r.no_article " + "WHERE av.no_utilisateur = :userId ");

		MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);

		List<String> conditions = new ArrayList<>();

		if (ventesEnCours != null && ventesEnCours) {
			conditions.add("av.etat_vente = 'EN_COURS'");
		}
		if (ventesNonDebutees != null && ventesNonDebutees) {
			conditions.add("av.etat_vente = 'CREEE'");
		}
		if (ventesTerminees != null && ventesTerminees) {
			conditions.add("av.etat_vente = 'ENCHERES_TERMINEES'");
		}

		// Vérifier s'il y a des conditions et les ajouter proprement
		if (!conditions.isEmpty()) {
			sql.append(" AND (").append(String.join(" OR ", conditions)).append(") ");
		}

		return namedParameterJdbcTemplate.query(sql.toString(), params, new ArticleRowMapper());
	}

	// filtrer les achats en fonction de l'utilisateur co. Toutes les enchères, les
	// enchères auxquelles il participe et les enchères win

	@Override
	public List<ArticleVendu> filtrerAchats(int userId, Boolean encheresOuvertes, Boolean mesEncheresEnCours, Boolean mesEncheresRemportees) {
	    StringBuilder sql = new StringBuilder(
	        "SELECT av.*, " +
	        "u.no_utilisateur, u.pseudo, u.email, " +
	        "u.rue AS user_rue, u.code_postal AS user_code_postal, u.ville AS user_ville, " +
	        "c.no_categorie, c.libelle, " +
	        "COALESCE(r.rue, u.rue) AS retrait_rue, " +
	        "COALESCE(r.code_postal, u.code_postal) AS retrait_code_postal, " +
	        "COALESCE(r.ville, u.ville) AS retrait_ville " +
	        "FROM ARTICLES_VENDUS av " +
	        "JOIN UTILISATEURS u ON av.no_utilisateur = u.no_utilisateur " +
	        "JOIN CATEGORIES c ON av.no_categorie = c.no_categorie " +
	        "LEFT JOIN RETRAITS r ON av.no_article = r.no_article "
	    );

	    MapSqlParameterSource params = new MapSqlParameterSource();
	    List<String> conditions = new ArrayList<>();

	    if (encheresOuvertes != null && encheresOuvertes) {
	        conditions.add("av.etat_vente = 'EN_COURS'");
	    }
	    if (mesEncheresEnCours != null && mesEncheresEnCours) {
	        conditions.add("av.no_article IN (SELECT e.no_article FROM ENCHERES e WHERE e.no_utilisateur = :userId)");
	        params.addValue("userId", userId);
	    }
	    if (mesEncheresRemportees != null && mesEncheresRemportees) {
	        conditions.add(
	            "av.no_article IN ( " +
	            "    SELECT e.no_article FROM ENCHERES e " +
	            "    WHERE e.no_utilisateur = :userId " +
	            "    AND e.montant_enchere = ( " +
	            "        SELECT MAX(e2.montant_enchere) FROM ENCHERES e2 " +
	            "        WHERE e2.no_article = e.no_article " +
	            "    ) " +
	            ") " +
	            "AND av.etat_vente = 'ENCHERES_TERMINEES'"
	        );
	        params.addValue("userId", userId);
	    }


	    // Ajout des conditions seulement si elles existent
	    if (!conditions.isEmpty()) {
	        sql.append(" WHERE ").append(String.join(" OR ", conditions));
	    }

	    return namedParameterJdbcTemplate.query(sql.toString(), params, new ArticleRowMapper());
	}


}
