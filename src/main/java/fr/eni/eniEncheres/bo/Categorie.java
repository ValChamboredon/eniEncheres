package fr.eni.eniEncheres.bo;

import java.util.List;

public class Categorie {
    private int noCategorie; // ID de la catégorie
    private String libelle; // Nom de la catégorie
    private List<ArticleVendu> articles; // Liste des articles appartenant à cette catégorie

    public Categorie() {}

    public Categorie(int noCategorie, String libelle) {
        this.noCategorie = noCategorie;
        this.libelle = libelle;
    }

    // Getters et Setters
    public int getNoCategorie() { return noCategorie; }
    public void setNoCategorie(int noCategorie) { this.noCategorie = noCategorie; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public List<ArticleVendu> getArticles() { return articles; }
    public void setArticles(List<ArticleVendu> articles) { this.articles = articles; }

    @Override
    public String toString() {
        return "Categorie{noCategorie=" + noCategorie + ", libelle='" + libelle + "'}";
    }
}
