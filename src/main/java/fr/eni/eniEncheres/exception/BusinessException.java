package fr.eni.eniEncheres.exception;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception {
    private static final long serialVersionUID = 1L;
    private List<String> clesErreurs;

    public BusinessException() {
        this.clesErreurs = new ArrayList<>();
    }


    public BusinessException(List<String> erreurs) {
        super("Erreur de validation: " + erreurs);  // Ajout d'un message dans l'exception
        this.clesErreurs = erreurs;
    }

    public List<String> getClesErreurs() {
        return clesErreurs;
    }

    public void addCleErreur(String cleErreur) {
        clesErreurs.add(cleErreur);
    }

}




