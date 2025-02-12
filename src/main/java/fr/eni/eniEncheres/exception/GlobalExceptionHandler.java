package fr.eni.eniEncheres.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex, Model model) {
        model.addAttribute("erreurs", ex.getClesErreurs()); // Changé de getErreurs() à getClesErreurs()
        return "erreur";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        List<String> erreurs = new ArrayList<>();
        erreurs.add("ERR_SYSTEME");
        BusinessException be = new BusinessException(erreurs); // Utilise le constructeur avec List<String>
        model.addAttribute("erreurs", be.getClesErreurs());
        return "erreur";
    }
}