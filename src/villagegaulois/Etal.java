package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Etal {
	private Gaulois vendeur;
	private String produit;
	private int quantiteDebutMarche;
	private int quantite;
	private boolean etalOccupe = false;

	public boolean isEtalOccupe() {
		return etalOccupe;
	}
	
	
	public Gaulois getVendeur() {
		return vendeur;
	}

	public int getQuantite() {
		return quantite;
	}

	public String getProduit() {
		return produit;
	}

	public void occuperEtal(Gaulois vendeur, String produit, int quantite) {
		this.vendeur = vendeur;
		this.produit = produit;
		this.quantite = quantite;
		quantiteDebutMarche = quantite;
		etalOccupe = true;
	}

	public boolean contientProduit(String produit) {
		return this.produit.equals(produit);
	}

	public int acheterProduit(int quantiteAcheter) {
	    // Original method logic
	    if (quantite == 0) {
	        quantiteAcheter = 0;
	    }
	    if (quantiteAcheter > quantite) {
	        quantiteAcheter = quantite;
	    }
	    if (etalOccupe) {
	        quantite -= quantiteAcheter;
	    }
	    return quantiteAcheter;
	}

	public int acheterProduit(int quantiteAcheter, Gaulois gaulois) {
	    // Specific logic for Gaulois type
	    return acheterProduit(quantiteAcheter); // Optional: reuse existing logic
	}


	public void libererEtal() {
		etalOccupe = false;
	}

	
	public String[] etatEtal() {
		String[] donneesVente = new String[5];
		donneesVente[0] = String.valueOf(etalOccupe);
		if (etalOccupe) {
			donneesVente[1] = vendeur.getNom();
			donneesVente[2] = produit;
			donneesVente[3] = String.valueOf(quantiteDebutMarche);
			donneesVente[4] = String.valueOf(quantiteDebutMarche - quantite);
		}
		return donneesVente;
	}

	public String afficherEtal() {
	    if (isEtalOccupe()) {
	        return getVendeur().getNom() + " vend " + getQuantite() + " " + getProduit();
	    } else {
	        return "Cet étal est vide.";
	    }
	}

	

}