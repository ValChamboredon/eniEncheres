package fr.eni.eniEncheres.bo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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
	
	private String etatVente;

	// Association
	private Utilisateur vendeur;
	private Categorie categorie;
	private List<Enchere> encheres = new ArrayList<Enchere>();
	private Retrait lieuDeRetrait;

	
	/**
	 * Constructeur
	 */
	public ArticleVendu() {
	}

	/**
	 * Constructeur avec tout les attributs
	 * 
	 * @param noArticle
	 * @param nomArticle
	 * @param description
	 * @param dateDebutEncheres
	 * @param dateFinEncheres
	 * @param miseAPrix
	 * @param prixVente
	 * @param etatVente
	 * @param vendeur
	 * @param categorie
	 * @param encheres
	 * @param lieuDeRetrait
	 */
	public ArticleVendu(int noArticle, String nomArticle, String description, LocalDate dateDebutEncheres,
			LocalDate dateFinEncheres, int miseAPrix, int prixVente, String etatVente, Utilisateur vendeur,
			Categorie categorie, List<Enchere> encheres, Retrait lieuDeRetrait) {
		super();
		this.noArticle = noArticle;
		this.nomArticle = nomArticle;
		this.description = description;
		this.dateDebutEncheres = dateDebutEncheres;
		this.dateFinEncheres = dateFinEncheres;
		this.miseAPrix = miseAPrix;
		this.prixVente = prixVente;
		this.etatVente = etatVente;
		this.vendeur = vendeur;
		this.setCategorie(categorie);
		this.setEncheres(encheres);
		this.setLieuDeRetrait(lieuDeRetrait);
	}

	/**
	 * Constructeur sans le noArticle
	 * 
	 * @param nomArticle
	 * @param description
	 * @param dateDebutEncheres
	 * @param dateFinEncheres
	 * @param miseAPrix
	 * @param prixVente
	 * @param etatVente
	 * @param vendeur
	 * @param categorie
	 * @param encheres
	 * @param lieuDeRetrait
	 */
	public ArticleVendu(String nomArticle, String description, LocalDate dateDebutEncheres, LocalDate dateFinEncheres,
			int miseAPrix, int prixVente, String etatVente, Utilisateur vendeur, Categorie categorie,
			List<Enchere> encheres, Retrait lieuDeRetrait) {
		super();
		this.nomArticle = nomArticle;
		this.description = description;
		this.dateDebutEncheres = dateDebutEncheres;
		this.dateFinEncheres = dateFinEncheres;
		this.miseAPrix = miseAPrix;
		this.prixVente = prixVente;
		this.etatVente = etatVente;
		this.vendeur = vendeur;
		this.setCategorie(categorie);
		this.setEncheres(encheres);
		this.setLieuDeRetrait(lieuDeRetrait);
	}

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

	public String getEtatVente() {
		return etatVente;
	}

	public void setEtatVente(String etatVente) {
		this.etatVente = etatVente;
	}

	public Utilisateur getVendeur() {
		return vendeur;
	}

	public void setVendeur(Utilisateur vendeur) {
		this.vendeur = vendeur;
	}

	public Retrait getLieuDeRetrait() {
		return lieuDeRetrait;
	}

	public void setLieuDeRetrait(Retrait lieuDeRetrait) {
		this.lieuDeRetrait = lieuDeRetrait;
	}

	public List<Enchere> getEncheres() {
		return encheres;
	}

	public void setEncheres(List<Enchere> encheres) {
		this.encheres = encheres;
	}

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	@Override
	public String toString() {
		return "Article [noArticle=" + noArticle + ", nomArticle=" + nomArticle + ", description=" + description
				+ ", dateDebutEncheres=" + dateDebutEncheres + ", dateFinEncheres=" + dateFinEncheres + ", miseAPrix="
				+ miseAPrix + ", prixVente=" + prixVente + ", etatVente=" + etatVente
//				+ ", vendeur="
//				+ vendeur.getPseudo() 
				+ ", categorie=" + categorie.getLibelle() 
//				+ ", lieuDeRetrait="
//				+ lieuDeRetrait 
				+ "]";
	}


    

}
