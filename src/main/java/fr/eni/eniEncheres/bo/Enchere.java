package fr.eni.eniEncheres.bo;

import java.time.LocalDateTime;

public class Enchere {
	private Integer noEnchere;
    private LocalDateTime dateEnchere;
    private Integer montantEnchere;
    
    private Utilisateur utilisateur;
    private ArticleVendu article;
    
    public boolean isValidEnchere(ArticleVendu article) {
        LocalDateTime now = LocalDateTime.now();
        return montantEnchere > 0 
               && now.isAfter(article.getDateDebutEncheres())
               && now.isBefore(article.getDateFinEncheres());
    }
    
    public Enchere(LocalDateTime dateEnchere, Integer montantEnchere, Utilisateur utilisateur, ArticleVendu article) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
        this.utilisateur = utilisateur;
        this.article = article;
    }

	public Enchere() 	{}

	public Integer getNoEnchere() {
		return noEnchere;
	}

	public void setNoEnchere(Integer idEnchere) {
		this.noEnchere = idEnchere;
	}

	public LocalDateTime getDateEnchere() {
		return dateEnchere;
	}

	public void setDateEnchere(LocalDateTime dateEnchere) {
		this.dateEnchere = dateEnchere;
	}

	public Integer getMontantEnchere() {
		return montantEnchere;
	}

	public void setMontantEnchere(Integer montantEnchere) {
		this.montantEnchere = montantEnchere;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public ArticleVendu getArticle() {
		return article;
	}

	public void setArticle(ArticleVendu article) {
		this.article = article;
	}

    
}
