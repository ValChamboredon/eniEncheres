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
				
				//Penssez a faire un parse pour les deux localDate
				
				LocalDate dateDebutEncheres = rs.getDate("date_debut_encheres").toLocalDate();
		        LocalDate dateFinEncheres = rs.getDate("date_fin_encheres").toLocalDate();
		        article.setDateDebutEncheres(dateDebutEncheres);
		        article.setDateFinEncheres(dateFinEncheres);
		        
		        article.setMiseAPrix(rs.getInt("prix_initial"));
		        article.setPrixVente(rs.getInt("prix_vente"));
		        
		        Utilisateur vendeur = new Utilisateur();
		        vendeur.setNoUtilisateur(rs.getInt("no_utilisateur"));
		        vendeur.setPseudo(rs.getString("pseudo"));
		        vendeur.setNom(rs.getString("nom"));
		        vendeur.setPrenom(rs.getString("prenom"));
		        vendeur.setEmail(rs.getString("email"));
		        vendeur.setTelephone(rs.getString("telephone"));
		        vendeur.setRue(rs.getString("rue"));
		        vendeur.setCodePostal(rs.getString("code_postal"));
		        vendeur.setVille(rs.getString("ville"));
		        vendeur.setMotDePasse(rs.getString("mot_de_passe"));
		        vendeur.setCredit(rs.getInt("credit"));
		        vendeur.setAdministrateur(rs.getBoolean("administrateur"));
		        
		        article.setVendeur(vendeur);
		        
		        Categorie categorie = new Categorie();
		        categorie.setNoCategorie(rs.getInt("no_categorie"));
		        categorie.setLibelle(rs.getString("libelle"));
		        article.setCategorie(categorie);
		        
		        Retrait lieuDeRetrait = new Retrait();
		        lieuDeRetrait.setRue(rs.getString("rue"));
		        lieuDeRetrait.setCodePostal(rs.getString("code_postal"));
		        lieuDeRetrait.setVille(rs.getString("ville"));
		        article.setLieuDeRetrait(lieuDeRetrait);
		        
		        List<Enchere> encheres = new ArrayList<>();
		        article.setEncheres(encheres);

		        
		        

				return article;
			}


}
