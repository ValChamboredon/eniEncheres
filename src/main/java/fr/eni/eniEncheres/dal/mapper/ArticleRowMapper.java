package fr.eni.eniEncheres.dal.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.bo.Utilisateur;

/**
 * ✅ Mapper pour transformer une ligne SQL en objet ArticleVendu.
 */
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

        // Associer le vendeur
        Utilisateur vendeur = new Utilisateur();
        vendeur.setNoUtilisateur(rs.getInt("no_utilisateur"));
        article.setVendeur(vendeur);

        // Associer la catégorie
        Categorie categorie = new Categorie();
        categorie.setNoCategorie(rs.getInt("no_categorie"));
        article.setCategorie(categorie);

        return article;
    }
}
