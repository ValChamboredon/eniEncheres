package fr.eni.eniEncheres.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Utilisateur {

	
	private int noUtilisateur;
	
	@NotBlank(message = "{FormulaireProfil.pseudo.NotBlank}")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Le pseudonyme doit contenir uniquement des caractères alphanumériques.")
	private String pseudo;
	
	@NotBlank(message = "{FormulaireProfil.nom.NotBlank}")
	@Pattern(regexp = "^[a-zA-ZÀ-ÿ]+([ '-][a-zA-ZÀ-ÿ]+)*$", message = "Le nom ne peut pas contenir de chiffre ou de caractères spéciaux.")
	private String nom;

	@NotBlank(message = "{FormulaireProfil.prenom.NotBlank}")
	@Pattern(regexp = "^[a-zA-ZÀ-ÿ]+([ '-][a-zA-ZÀ-ÿ]+)*$", message = "Le prénom ne peut pas contenir de chiffre ou de caractères spéciaux.")
	private String prenom;

	@NotBlank(message = "{FormulaireProfil.email.NotBlank}")
	 @Pattern(regexp = "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$", message = "L'adresse mail n'est pas valide.")
	private String email;

	@Pattern(regexp = "^(\\+33|0)[1-9](\\d{2}){4}$",message = "{FormulaireProfil.telephone.Format}")
	private String telephone;

	@NotBlank(message = "{FormulaireProfil.rue.NotBlank}")
	private String rue;

	@NotBlank(message = "{FormulaireProfil.codePostal.NotBlank}")
	@Pattern(regexp = "^\\d{5}$", message = "Le code postal doit comprendre 5 chiffres.")
	private String codePostal;

	@NotBlank(message = "{FormulaireProfil.ville.NotBlank}")
	private String ville;

	@NotBlank(message = "{FormulaireProfil.motDePasse.NotBlank}")
	@Size(min = 8, message = "{FormulaireProfil.motDePasse.Size}")
	private String motDePasse;
	
	@Transient
	private String confirmationMotDePasse;

	@AssertTrue(message= "Les mots de passe ne correspondent pas.")
	public boolean isMotDePasseValide() {
		return motDePasse!=null && motDePasse.equals(confirmationMotDePasse);
	}
	
	private int credit;
	private boolean administrateur;
	
	//Relations
	private List<ArticleVendu> article = new ArrayList<ArticleVendu>();
	private List<Enchere> encheres = new ArrayList<Enchere>();
	
	//Constructeurs
	public Utilisateur() {
	}
	
	public Utilisateur(int noUtilisateur, String pseudo, String nom, String prenom, String email, String telephone,
			String rue, String codePostal, String ville, String motDePasse, int credit, boolean administrateur) {
		this.noUtilisateur = noUtilisateur;
		this.pseudo = pseudo;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
		this.motDePasse = motDePasse;
		this.credit = credit;
		this.administrateur = administrateur;
	}

	public int getNoUtilisateur() {
		return noUtilisateur;
	}

	public void setNoUtilisateur(int noUtilisateur) {
		this.noUtilisateur = noUtilisateur;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public boolean isAdministrateur() {
		return administrateur;
	}

	public void setAdministrateur(boolean administrateur) {
		this.administrateur = administrateur;
	}

	public List<ArticleVendu> getArticle() {
		return article;
	}

	public void setArticle(List<ArticleVendu> article) {
		this.article = article;
	}

	public List<Enchere> getEncheres() {
		return encheres;
	}

	public void setEncheres(List<Enchere> encheres) {
		this.encheres = encheres;
	}

	public String getConfirmationMotDePasse() {
		return confirmationMotDePasse;
	}

	public void setConfirmationMotDePasse(String confirmationMotDePasse) {
		this.confirmationMotDePasse = confirmationMotDePasse;
	}
	
	
	
	
}
