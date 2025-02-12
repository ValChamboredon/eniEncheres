/**
 * Représente le lieu de retrait d'un article après une vente.
 * Chaque article vendu possède une adresse de retrait.
 * 
 * @author Mariami
 * @version 1.0
 */
package fr.eni.eniEncheres.bo;

public class Retrait {
    private String rue;
    private String codePostal;
    private String ville;

    public Retrait() {}

    public Retrait(int noArticle, String rue, String codePostal, String ville) {
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    // Getters et Setters

    public String getRue() { return rue; }
    public void setRue(String rue) { this.rue = rue; }
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
}
