package fr.eni.eniEncheres.exception;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception {

    private List<String> erreurs = new ArrayList<>();

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        erreurs.add(message);
    }

    public void ajouterErreur(String message) {
        erreurs.add(message);
    }

    public List<String> getErreurs() {
        return erreurs;
    }
}
