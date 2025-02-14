package fr.eni.eniEncheres.controller;

import java.util.Collections;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.eniEncheres.bll.ArticleService;
import fr.eni.eniEncheres.bll.CategorieService;
//import fr.eni.eniEncheres.bll.EnchereService;
import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.bo.EtatVente;
import fr.eni.eniEncheres.bo.Retrait;
import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/encheres")
@SessionAttributes("CategoriesEnSession")
public class ArticleController {

	// Dépendances
	private ArticleService articleService;

	// private EnchereService enchereService;
	private CategorieService categorieService;
	private UtilisateurService utilisateurService;

	/**
	 * Constructeur
	 * 
	 * @param articleService
	 * @param categorieService
	 */
	// Injection des dépendances
	public ArticleController(ArticleService articleService, CategorieService categorieService,
			UtilisateurService utilisateurService) {
		this.articleService = articleService;
		this.categorieService = categorieService;
		this.utilisateurService = utilisateurService;
	}

	/**
	 * Route qui affiche la page prinicpale => liste des articles
	 * 
	 * @param model
	 * @return "index"
	 */

	@GetMapping
	public String afficherLesArticles(Model model) {

		List<ArticleVendu> articles = articleService.consulterTout();

		model.addAttribute("articles", articles);
		return "index";
	}

	/**
	 * Méthode permettant de mettre les Catégories en 'SessionAttribute'
	 * 
	 * @throws BusinessException
	 */
	@ModelAttribute("CategoriesEnSession")
	public List<Categorie> chargerCategories() throws BusinessException {
		System.out.println("Chargement en Session des CATEGORIES");
		return categorieService.getAllCategories();
	}

	/**
	 * Route qui affiche le formulaire de mise en vente d'un article
	 * 
	 * @param model
	 * @return "vendreArticle"
	 */
	@GetMapping("/vendre")
	public String afficherFormulaireVendreArticle(Model model) {

		// On injecte un objet avec le modèle pour que
		// le formulaire Thymeleaf n'explose pas sur le : "${#fields.hasGlobalErrors()}"
		model.addAttribute("article", new ArticleVendu());

		return "formulaireArticle";
	}

	/**
	 * Méthode qui crée l'article et l'envoi à la BLL
	 * 
	 * @param retrait
	 * @param article
	 * @param bindingResult
	 * @param model
	 * @return "redirect:/encheres" si la saisie est correcte
	 */
	@PostMapping("/vendre")
	public String mettreArticleEnVente(@Valid @ModelAttribute("retrait") Retrait retrait,
			@Valid @ModelAttribute("article") ArticleVendu article, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			System.out.println("Erreurs de BINDING_RESULT !");
			return "vendreArticle";
		}

		// TODO Duplication de code, faire une méthode !
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Si on a un·e utilisateur·ice connecté·e
		if (!(authentication instanceof AnonymousAuthenticationToken)) {

			Utilisateur utilisateurCourant = new Utilisateur();
			String currentEmail = authentication.getName();
			utilisateurCourant = utilisateurService.getUtilisateurByEmail(currentEmail);

			// On ajoute l'Utilisateur courant à l'article
			article.setVendeur(utilisateurCourant);

			try {
				// Passage de l'article à la BLL
				articleService.creerArticle(article);

				// Si tout marche on revient à "encheres"
				return "redirect:/encheres";

			} catch (BusinessException be) {

				for (String messageErreur : be.getMessagesErreur()) {

					// On ajoute les Exceptions levées au bindingResult dans le champ "globalError"
					bindingResult.addError(new ObjectError("globalError", messageErreur));

					model.addAttribute(bindingResult);
				}

				// S'il y a un problème on reste sur "vendreArticle"
				return "vendreArticle";
			}

		}

		// Si pas d'utilisateur·ice connecté·e on renvoit sur la page de connexion
		return "redirect:/encheres/connexion";
	}

	/**
	 * Route qui affiche le détail d'un article
	 * 
	 * @param id    ID de l'article
	 * @param model Modèle Thymeleaf
	 * @return "detailArticle" (nom du fichier HTML)
	 */
	@GetMapping("/article/detail/{noArticle}")
	public String afficherDetailArticle(@PathVariable("noArticle") int noArticle, Model model) {
		ArticleVendu article = articleService.getArticleById(noArticle);

		if (article == null) {
			return "redirect:/encheres"; // Redirige si l'article n'existe pas
		}

		// Vérifier si lieuDeRetrait est null, et le définir avec l'adresse du vendeur
		if (article.getLieuDeRetrait() == null) {
			Retrait lieuDeRetrait = new Retrait();
			lieuDeRetrait.setRue(article.getVendeur().getRue());
			lieuDeRetrait.setCodePostal(article.getVendeur().getCodePostal());
			lieuDeRetrait.setVille(article.getVendeur().getVille());
			article.setLieuDeRetrait(lieuDeRetrait);
		}

		model.addAttribute("article", article);
		return "detailArticle";
	}

	@PostMapping("/supprimer")
	public String supprimerArticle(@RequestParam("articleId") int articleId, Principal principal) {
	    // Récupérer l'article
	    ArticleVendu article = articleService.getArticleById(articleId);

	    // Vérifier si l'article existe et si l'utilisateur est bien le vendeur
	    if (article != null && article.getVendeur().getEmail().equals(principal.getName())) {

	        // Empêcher la suppression si l'enchère est terminée ou si l'article a été retiré
	        if (article.getEtatVente() == EtatVente.ENCHERES_TERMINEES || article.getEtatVente() == EtatVente.RETRAIT_EFFECTUE) {
	            System.out.println("Suppression non autorisée : L'enchère est terminée ou l'article a déjà été retiré.");
	            return "redirect:/encheres?error=suppressionNonAutorisee";
	        }

	        // Suppression autorisée uniquement si l'état est encore "CREEE" ou "EN_COURS"
	        articleService.supprimerArticle(articleId);
	        System.out.println("Article supprimé avec succès: " + articleId);
	    } else {
	        System.out.println("Tentative de suppression non autorisée !");
	    }

	    return "redirect:/encheres";
	}


	@GetMapping("/modifier")
	public String afficherFormulaireModification(@RequestParam("articleId") int articleId, Principal principal,
			Model model) {
		// Récupérer l'article
		ArticleVendu article = articleService.getArticleById(articleId);

		// Vérifier si l'utilisateur connecté est bien le vendeur et que l'état est
		// CREEE
		if (article != null && article.getEtatVente() == EtatVente.CREEE
				&& article.getVendeur().getEmail().equals(principal.getName())) {
			model.addAttribute("article", article);
			return "modifierArticle"; // Redirige vers la page de modification
		}

		// Si l'utilisateur n'a pas les droits ou que l'article a déjà commencé,
		// rediriger
		return "redirect:/encheres";
	}

	@PostMapping("/modifier")
	public String modifierArticle(@RequestParam("articleId") int articleId,
	                              @RequestParam("nomArticle") String nomArticle,
	                              @RequestParam("description") String description,
	                              @RequestParam("miseAPrix") int miseAPrix,
	                              @RequestParam("dateDebutEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebutEncheres,
	                              @RequestParam("dateFinEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFinEncheres,
	                              Principal principal) {

	    // Récupérer l'article
	    ArticleVendu article = articleService.getArticleById(articleId);

	    // Vérifier que seul le vendeur et un état "CREEE" peuvent modifier
	    if (article != null && article.getEtatVente() == EtatVente.CREEE && article.getVendeur().getEmail().equals(principal.getName())) {
	        
	        // Mettre à jour les valeurs de l'article
	        article.setNomArticle(nomArticle);
	        article.setDescription(description);
	        article.setMiseAPrix(miseAPrix);
	        article.setDateDebutEncheres(dateDebutEncheres);
	        article.setDateFinEncheres(dateFinEncheres);

	        // Enregistrer les modifications
	        articleService.modifierArticle(article);
	        System.out.println("Article modifié avec succès: " + articleId);
	    } else {
	        System.out.println("Tentative de modification non autorisée !");
	    }

	    return "redirect:/encheres/article/detail/" + articleId;
	}

}
