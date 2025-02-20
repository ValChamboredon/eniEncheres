package fr.eni.eniEncheres.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fr.eni.eniEncheres.bll.ArticleService;
import fr.eni.eniEncheres.bo.ArticleVendu;

@Component
public class EtatVenteScheduler {
	
	@Autowired
	private ArticleService articleService;
	
	@Scheduled(fixedRate = 60000) //Toutes les minutes
	public void mettreAJourEtatsVente() {
		try {
			List<ArticleVendu> articles = articleService.consulterTout();
			System.out.println("Mise à jour automatique des états de vente : " + articles.size() + " articles traités");
		} catch (Exception e) {
			System.err.println("Erreur lors de la mise à jour des états de vente : " + e.getMessage());
		}
	}

}