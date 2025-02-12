package fr.eni.eniEncheres.bo;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ArticleVendu {

    //attributs
    private int noArticle;
    @NotBlank(message = "Le nom de l'article ne peut pas être vide")
    private String nomArticle;
    private String description;
    @NotNull(message = "La date de début des enchères est obligatoire")
    private LocalDate dateDebutEncheres;
    @NotNull(message = "La date de fin des enchères est obligatoire")
    private LocalDate dateFinEncheres;
    @AssertTrue(message = "La date de fin d'enchères doit être après la date de début")
    public boolean isValidDateRange() {
        return dateFinEncheres != null && dateDebutEncheres != null 
               && dateFinEncheres.isAfter(dateDebutEncheres);
    }
    @NotNull(message = "La mise à prix ne peut pas être nulle")
    @Positive(message = "La mise à prix doit être positive")
    private float miseAPrix;
    private float prixVente;

    private Utilisateur vendeur;             
    private Categorie categorie;             
    private Retrait lieuRetrait;            
    private List<Enchere> encheres;
    private EtatVente etatVente;

    //constructeur
    public ArticleVendu() {

    }

    public ArticleVendu(int noArticle, String nomArticle, String description, LocalDate dateDebutEncheres, LocalDate dateFinEncheres, float miseAPrix, float prixVente, EtatVente etatVente) {
        this.noArticle = noArticle;
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEncheres = dateFinEncheres;
        this.miseAPrix = miseAPrix;
        this.prixVente = prixVente;
        this.etatVente = etatVente;
    }

    //accesseurs et mutateurs
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

    public float getMiseAPrix() {
        return miseAPrix;
    }

    public void setMiseAPrix(float miseAPrix) {
        this.miseAPrix = miseAPrix;
    }

    public float getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(float prixVente) {
        this.prixVente = prixVente;
    }

    public Enum<EtatVente> getEtatVente() {
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

    public Retrait getLieuRetrait() {
        return lieuRetrait;
    }

    public void setLieuRetrait(Retrait lieuRetrait) {
        this.lieuRetrait = lieuRetrait;
    }

    public List<Enchere> getEncheres() {
        return encheres;
    }

    public void setEncheres(List<Enchere> encheres) {
        this.encheres = encheres;
    }
    

}
