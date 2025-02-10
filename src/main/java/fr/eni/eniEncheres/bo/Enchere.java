package fr.eni.eniEncheres.bo;

import java.time.LocalDateTime;

public class Enchere {
	private Integer noEnchere;
    private LocalDateTime dateEnchere;
    private Integer montantEnchere;
    
    private Utilisateur utilisateur;
    private Article article;
    
    public Enchere(LocalDateTime dateEnchere, Integer montantEnchere, Utilisateur utilisateur, Article article) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
        this.utilisateur = utilisateur;
        this.article = article;
    }

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

    
}
