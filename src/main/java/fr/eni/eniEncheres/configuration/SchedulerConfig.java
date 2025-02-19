package fr.eni.eniEncheres.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import fr.eni.eniEncheres.bll.ArticleService;

@Configuration
@EnableScheduling
public class SchedulerConfig {

	@Autowired
    private ArticleService articleService;

    @Scheduled(fixedRate = 60000) // Toutes les minutes
    public void mettreAJourEtatsVente() {
        try {
            System.out.println("Début de la mise à jour automatique des états de vente");
            articleService.mettreAJourEtatDesArticles();
            System.out.println("Fin de la mise à jour automatique des états de vente");
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des états de vente : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
