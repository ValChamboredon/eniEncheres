package fr.eni.eniEncheres.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import fr.eni.eniEncheres.bll.CategorieService;
import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.exception.BusinessException;

@Component
public class StringToCategorieConverter implements Converter<String, Categorie> {

    private final CategorieService categorieService;

    public StringToCategorieConverter(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @Override
    public Categorie convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null; // pour pas convertir chaine null
        }

        try {
            int id = Integer.parseInt(source);
            return categorieService.getCategorieById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID de catégorie invalide : " + source, e);
        } catch (BusinessException e) {
            throw new RuntimeException("Erreur lors de la récupération de la catégorie avec l'ID : " + source, e);
        }
    }
}