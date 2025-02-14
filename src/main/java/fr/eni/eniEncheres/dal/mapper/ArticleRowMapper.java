package fr.eni.eniEncheres.dal.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.bo.Enchere;

import fr.eni.eniEncheres.bo.Retrait;
import fr.eni.eniEncheres.bo.Utilisateur;

public class ArticleRowMapper implements RowMapper<ArticleVendu> {
	
	@Override
	public ArticleVendu mapRow(ResultSet rs, int rowNum) throws SQLException {

	    ArticleVendu article = new ArticleVendu();


	    article.setNoArticle(rs.getInt("no_article"));
	    article.setNomArticle(rs.getString("nom_article"));
	    article.setDescription(rs.getString("description"));
	    article.setDateDebutEncheres(rs.getDate("date_debut_encheres").toLocalDate());
	    article.setDateFinEncheres(rs.getDate("date_fin_encheres").toLocalDate());
	    article.setMiseAPrix(rs.getInt("prix_initial"));
	    article.setPrixVente(rs.getInt("prix_vente"));

	    // Vendeur
	    Utilisateur vendeur = new Utilisateur();
	    vendeur.setNoUtilisateur(rs.getInt("no_utilisateur"));
	    vendeur.setPseudo(rs.getString("pseudo"));
	    vendeur.setRue(rs.getString("user_rue"));
	    vendeur.setCodePostal(rs.getString("user_code_postal"));
	    vendeur.setVille(rs.getString("user_ville"));
	    article.setVendeur(vendeur);

	    // Catégorie
	    Categorie categorie = new Categorie();
	    categorie.setNoCategorie(rs.getInt("no_categorie"));
	    categorie.setLibelle(rs.getString("libelle"));
	    article.setCategorie(categorie);

	    // Lieu de retrait (déjà géré par la requête SQL avec `COALESCE`)
	    Retrait lieuDeRetrait = new Retrait();
	    lieuDeRetrait.setRue(rs.getString("retrait_rue"));
	    lieuDeRetrait.setCodePostal(rs.getString("retrait_code_postal"));
	    lieuDeRetrait.setVille(rs.getString("retrait_ville"));
	    article.setLieuDeRetrait(lieuDeRetrait);

	    return article;
	}

}

