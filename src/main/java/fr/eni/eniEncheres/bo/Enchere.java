/**
 * Représente une enchère placée par un utilisateur sur un article.
 * Une enchère est définie par son montant et la date à laquelle elle a été placée.
 * 
 * @author Mariami
 * @version 1.0
 */
package fr.eni.eniEncheres.bo;

import java.time.LocalDateTime;

public class Enchere {
    private Utilisateur utilisateur; // L'utilisateur qui a placé l'enchère
    private ArticleVendu article; // L'article concerné par l'enchère
    private LocalDateTime dateEnchere; // Date de l'enchère
    private int montantEnchere; // Montant proposé

    public Enchere() {}

    public Enchere(Utilisateur utilisateur, ArticleVendu article, LocalDateTime dateEnchere, int montantEnchere) {
        this.utilisateur = utilisateur;
        this.article = article;
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
    }

    // Getters et Setters
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public ArticleVendu getArticle() { return article; }
    public void setArticle(ArticleVendu article) { this.article = article; }
    public LocalDateTime getDateEnchere() { return dateEnchere; }
    public void setDateEnchere(LocalDateTime dateEnchere) { this.dateEnchere = dateEnchere; }
    public int getMontantEnchere() { return montantEnchere; }
    public void setMontantEnchere(int montantEnchere) { this.montantEnchere = montantEnchere; }
}
