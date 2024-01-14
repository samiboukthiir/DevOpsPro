package spring.jpa.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import spring.jpa.model.Produit;
import spring.jpa.repository.ProduitRepository;
@Controller // pour déclarer un contrôleur
@RequestMapping (value = "/produit") // nom logique dans l'URL pour accéder au contrôleur
public class ProduitController
{
@Autowired // pour l'injection de dépendances
private ProduitRepository produitRepos;
//nom logique pour accéder à l'action "index" ou méthode "index
@RequestMapping (value = "/index") 
public String index (Model model,
//paramètre pour le numéro de la page (0 par défaut)
@RequestParam (name="page" , defaultValue ="0") int p,
//paramètre "motCle"
@RequestParam (name="motCle", defaultValue="") String mc) 
{
//récupérer la page numero "p" (de taille 6)
Page <Produit> pg = 
produitRepos.findByDesignationLike("%"+mc+"%", PageRequest.of(p, 6));
//nombre total des pages
int nbrePages =pg.getTotalPages();
//créer un tableau d'entier qui contient les numéros des pages
int [] pages = new int[nbrePages];
for(int i= 0 ; i< nbrePages; i++)
{
pages[i]=i;
}
//placer le tableau dans le "Model"
model.addAttribute("pages", pages);
//placer la page des produits dans le "Model" comme un attribut"
model.addAttribute("pageProduits", pg); 
//retourner le numéro de la page courante
model.addAttribute("pageCourante",p);
//retourner la valeur du mot clé
model.addAttribute("motCle", mc);
//retourner le nom de la vue WEB à afficher
return "produits"; 
}

@RequestMapping(value="/form",method=RequestMethod.GET)
public String formProduit (Model model)
{
//placer un objet de type "Produit" dans le modèle
model.addAttribute("produit", new Produit());
//retourner le nom de la vue WEB à afficher (le formulaire)
return "formProduit";
}

@RequestMapping(value="/save",method=RequestMethod.POST)
public String save (Model model, @Valid Produit produit , BindingResult bindingResult)
{
if (bindingResult.hasErrors())
// en cas d'erreurs de validation
return "formProduit";
//sinon
//enregistrer le produit dans la BD 
produitRepos.save(produit);
//Afficher une page pour confirmer l'enregistrement
return "confirmation";
}

@RequestMapping(value="/delete",method=RequestMethod.GET)
public String delete (Long id, int page, String motCle)
 {
produitRepos.deleteById(id);
 return "redirect:index?page="+page+"&motCle="+motCle;
 }

@RequestMapping(value="/edit",method=RequestMethod.GET)
public String edit (Model model, 
@RequestParam (name="id")Long id)

{
//récupérer l'objet ayant l'id spécifié
Produit p =(Produit) 
produitRepos.findById(id).orElse(null);
// placer le produit trouvé dans le modèle
model.addAttribute("produit", p);
// rediriger l'affichage vers la vue "editProduit"
return "editProduit";
}

@RequestMapping(value="/update",method=RequestMethod.POST)
public String update (Model model, @Valid Produit produit , 
BindingResult bindingResult) {
if (bindingResult.hasErrors())
//en cas d'erreurs de validation
return "editProduit";
//enregistrer le produit dans la BD 
produitRepos.save(produit);
//Afficher une page pour confirmer l'enregistrement
return "confirmation"; 
}





}