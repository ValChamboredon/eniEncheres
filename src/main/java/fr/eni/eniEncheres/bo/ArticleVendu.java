package fr.eni.eniEncheres.bo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public class ArticleVendu {

   
    private int noArticle;

    @NotBlank
    private String nomArticle;

    @NotBlank
    private String description;

    private LocalDate dateDebutEncheres;
    private LocalDate dateFinEncheres;

    @Min(value = 1)
    private int miseAPrix;

    private int prixVente;

    
    private EtatVente etatVente = EtatVente.CREEE; // Valeur par défaut lors de la création

    
    private Utilisateur vendeur;

    
    private Categorie categorie;

    
    private List<Enchere> encheres = new ArrayList<>();

    
    private Retrait lieuDeRetrait;
    
    private String imageUrl;
    
    /**
     * Constructeur par défaut
     */
    public ArticleVendu() {
        this.etatVente = EtatVente.CREEE; // Assure que l'article est créé avec l'état "CREEE"
    }

    /**
     * Constructeur avec tous les attributs
     */
    public ArticleVendu(String nomArticle, String description, LocalDate dateDebutEncheres, LocalDate dateFinEncheres,
                        int miseAPrix, int prixVente, Utilisateur vendeur, Categorie categorie, Retrait lieuDeRetrait, String imageUrl) {
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEncheres = dateFinEncheres;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.vendeur = vendeur;
        this.categorie = categorie;
        this.lieuDeRetrait = lieuDeRetrait != null ? lieuDeRetrait : new Retrait(vendeur.getRue(), vendeur.getCodePostal(), vendeur.getVille());
        this.etatVente = EtatVente.CREEE; // Valeur par défaut
        this.imageUrl = imageUrl;
    }

    // Getters & Setters
    public int getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(int noArticle) {
        this.noArticle = noArticle;
    }

    public String getNomArticle() {
        return nomArticle;
    }

    public void setNomArticle(String nomArticle) {
        this.nomArticle = nomArticle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDebutEncheres() {
        return dateDebutEncheres;
    }

    public void setDateDebutEncheres(LocalDate dateDebutEncheres) {
        this.dateDebutEncheres = dateDebutEncheres;
    }

    public LocalDate getDateFinEncheres() {
        return dateFinEncheres;
    }

    public void setDateFinEncheres(LocalDate dateFinEncheres) {
        this.dateFinEncheres = dateFinEncheres;
    }

    public int getMiseAPrix() {
        return miseAPrix;
    }

    public void setMiseAPrix(int miseAPrix) {
        this.miseAPrix = miseAPrix;
    }

    public int getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(int prixVente) {
        this.prixVente = prixVente;
    }

    public EtatVente getEtatVente() {
        return etatVente;
    }

    public void setEtatVente(EtatVente etatVente) {
        this.etatVente = etatVente;
    }

    public Utilisateur getVendeur() {
        return vendeur;
    }

    public void setVendeur(Utilisateur vendeur) {
        this.vendeur = vendeur;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public List<Enchere> getEncheres() {
        return encheres;
    }

    public void setEncheres(List<Enchere> encheres) {
        this.encheres = encheres;
    }

    public Retrait getLieuDeRetrait() {
        return lieuDeRetrait;
    }
    
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

    public void setLieuDeRetrait(Retrait lieuDeRetrait) {
        // Si le retrait est null, on met l'adresse du vendeur par défaut
        this.lieuDeRetrait = (lieuDeRetrait != null) ? lieuDeRetrait : 
                             new Retrait(vendeur.getRue(), vendeur.getCodePostal(), vendeur.getVille());
    }

    @Override
    public String toString() {
        return "ArticleVendu{" +
                "noArticle=" + noArticle +
                ", nomArticle='" + nomArticle + '\'' +
                ", description='" + description + '\'' +
                ", dateDebutEncheres=" + dateDebutEncheres +
                ", dateFinEncheres=" + dateFinEncheres +
                ", miseAPrix=" + miseAPrix +
                ", prixVente=" + prixVente +
                ", etatVente=" + etatVente +
                ", vendeur=" + (vendeur != null ? vendeur.getPseudo() : "N/A") +
                ", categorie=" + (categorie != null ? categorie.getLibelle() : "N/A") +
                ", lieuDeRetrait=" + (lieuDeRetrait != null ? lieuDeRetrait.toString() : "N/A") +
                '}';
    }
}

