//package fr.eni.eniEncheres.dal.mapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import org.springframework.jdbc.core.RowMapper;
//import fr.eni.eniEncheres.bo.ArticleVendu;
//import fr.eni.eniEncheres.bo.Categorie;
//import fr.eni.eniEncheres.bo.Enchere;
//import fr.eni.eniEncheres.bo.Utilisateur;
//
//public class EnchereRowMapper implements RowMapper<Enchere> {
//    @Override
//    public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
//        Enchere enchere = new Enchere();
//        
//        // Mapping des données de l'enchère
//        enchere.setDateEnchere(rs.getTimestamp("date_enchere").toLocalDateTime());
//        enchere.setMontantEnchere(rs.getInt("montant_enchere"));
//
//        // Mapping de l'utilisateur associé
//        Utilisateur utilisateur = new Utilisateur();
//        utilisateur.setNoUtilisateur(rs.getInt("no_utilisateur"));
//        utilisateur.setPseudo(rs.getString("pseudo"));
//        utilisateur.setNom(rs.getString("nom"));
//        utilisateur.setPrenom(rs.getString("prenom"));
//        enchere.setUtilisateur(utilisateur);
//
//        // Mapping de l'article associé
//        ArticleVendu article = new ArticleVendu();
//        article.setNoArticle(rs.getInt("no_article"));
//        article.setNomArticle(rs.getString("nom_article"));
//        enchere.setArticle(article);
//        
//        // Ajout de la catégorie à l'article
//        Categorie categorie = new Categorie();
//        categorie.setNoCategorie(rs.getInt("no_categorie"));
//        categorie.setLibelle(rs.getString("libelle"));
//        article.setCategorie(categorie);
//
//        return enchere;
//    }
//}