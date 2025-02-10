package fr.eni.eniEncheres.bo;

import java.time.LocalDate;
import java.util.List;

public class Article {

    //attributs
    private int noArticle;
    private String nomArticle;
    private String description;
    private LocalDate dateDebutEncheres;
    private LocalDate dateFinEncheres;
    private float miseAPrix;
    private float prixVente;
    private String etatVente;

    private Utilisateur vendeur;             
    private Categorie categorie;             
    private Retrait lieuRetrait;            
    private List<Enchere> encheres; 
    
    //constructeur
    public Article() {

    }

    public Article(int noArticle, String nomArticle, String description, LocalDate dateDebutEncheres, LocalDate dateFinEncheres, float miseAPrix, float prixVente, String etatVente) {
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

    public String getEtatVente() {
        return etatVente;
    }

    public void setEtatVente(String etatVente) {
        this.etatVente = etatVente;
    }

    

}
