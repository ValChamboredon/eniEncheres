package fr.eni.eniEncheres.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MotDePasseDTO {
    
    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit avoir au moins 6 caractères")
    private String motDePasseNouveau;

    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    private String motDePasseConfirme;

    // Getters et Setters
    public String getMotDePasseNouveau() {
        return motDePasseNouveau;
    }

    public void setMotDePasseNouveau(String motDePasseNouveau) {
        this.motDePasseNouveau = motDePasseNouveau;
    }

    public String getMotDePasseConfirme() {
        return motDePasseConfirme;
    }

    public void setMotDePasseConfirme(String motDePasseConfirme) {
        this.motDePasseConfirme = motDePasseConfirme;
    }
}

