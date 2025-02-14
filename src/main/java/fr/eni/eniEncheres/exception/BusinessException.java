package fr.eni.eniEncheres.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * ✅ Exception personnalisée pour gérer les erreurs métier dans l'application.
 */
public class BusinessException extends Exception {
    
    private static final long serialVersionUID = 1L;
    private List<String> clesErreurs;

    public BusinessException() {
        this.clesErreurs = new ArrayList<>();
    }

    /**
     * ✅ Constructeur avec liste d'erreurs.
     * @param erreurs Liste des erreurs.
     */
    public BusinessException(List<String> erreurs) {
        super("Erreur métier : " + erreurs);
        this.clesErreurs = erreurs;
    }

    /**
     * ✅ Retourne la liste des erreurs.
     * @return Liste des erreurs.
     */
    public List<String> getClesErreurs() {
        return clesErreurs;
    }

    /**
     * ✅ Ajoute une erreur à la liste.
     * @param cleErreur Message d'erreur.
     */
    public void addCleErreur(String cleErreur) {
        clesErreurs.add(cleErreur);
    }
}
