package villagegaulois;

import personnages.Chef;
import personnages.Druide;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtal) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtal);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		Gaulois gaulois = null;
		if (nomGaulois.equals(chef.getNom())) {
			gaulois = chef;
		} else {
			for (int i = 0; i < nbVillageois; i++) {
				Gaulois habitant = villageois[i];
				if (habitant.getNom().equals(nomGaulois)) {
					gaulois = habitant;
				}
			}
		}
		return gaulois;
	}

	public String[] donnerVillageois() {
		String[] donnees = new String[nbVillageois + 1];
		donnees[0] = chef.getNom();
		for (int i = 0; i < nbVillageois; i++) {
			if (villageois[i] instanceof Druide) {
				donnees[i + 1] = "le druide " + villageois[i].getNom();
			} else {
				donnees[i + 1] = villageois[i].getNom();
			}
		}
		return donnees;
	}

	public int donnerNbEtal() {
		return marche.getNbEtal();
	}

	
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
	    int indiceEtal = marche.trouverEtalLibre();
	    if (indiceEtal >= 0) {
	        marche.utiliserEtal(indiceEtal, vendeur, produit, nbProduit);
	        return "Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n° " + indiceEtal + ".";
	    } else {
	        return vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + ".";
	    }
	}


	public void partirVendeur(Gaulois vendeur) {
	    Etal etal = marche.trouverVendeur(vendeur);
	    if (etal != null && etal.isEtalOccupe()) {
	        int quantiteVendue = etal.getQuantite();
	    
	        etal.libererEtal();
	        System.out.println("Le vendeur " + vendeur.getNom() 
	            + " quitte son étal, il a vendu " + quantiteVendue 
	            + " " + etal.getProduit() + " parmi les "  + " qu'il voulait vendre.");
	    }
	}


	public boolean rechercherEtalVide() {
		return marche.trouverEtalLibre() != -1;
	}

	public String rechercherVendeursProduit(String produit) {
	    Etal[] etalsProduit = marche.trouverEtals(produit);
	    StringBuilder vendeurs = new StringBuilder();
	    
	    if (etalsProduit == null || etalsProduit.length == 0) {
	        return "Il n'y a pas de vendeur qui propose " + produit + " au marché.";
	    } else {
	        vendeurs.append("Les vendeurs qui proposent ").append(produit).append(" sont :\n");
	        for (Etal etal : etalsProduit) {
	            Gaulois vendeur = etal.getVendeur();
	            if (vendeur != null) {
	                vendeurs.append("- ").append(vendeur.getNom()).append("\n");
	            }
	        }
	    }
	    return vendeurs.toString();
	}


	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}

	public String[] donnerEtatMarche() {
		return marche.donnerEtat();
	}

	
	private static class Marche {
		private Etal[] etals;

		private Marche(int nbEtals) {
			etals = new Etal[nbEtals];
			for (int i = 0; i < nbEtals; i++) {
				etals[i] = new Etal();
			}
		}

		private void utiliserEtal(int indiceEtal, Gaulois vendeur,
				String produit, int nbProduit) {
			if (indiceEtal >= 0 && indiceEtal < etals.length) {
				etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
			}
		}

		
		private int trouverEtalLibre() {
			int indiceEtalLibre = -1;
			for (int i = 0; i < etals.length && indiceEtalLibre < 0; i++) {
				if (!etals[i].isEtalOccupe()) {
					indiceEtalLibre = i;
				}
			}
			return indiceEtalLibre;
		}

		private Etal[] trouverEtals(String produit) {
			int nbEtal = 0;
			for (Etal etal : etals) {
				if (etal.isEtalOccupe() && etal.contientProduit(produit)) {
					nbEtal++;
				}
			}
			Etal[] etalsProduitsRecherche = null;
			if (nbEtal > 0) {
				etalsProduitsRecherche = new Etal[nbEtal];
				int nbEtalTrouve = 0;
				for (int i = 0; i < etals.length && nbEtalTrouve < nbEtal; i++) {
					if (etals[i].isEtalOccupe()&& etals[i].contientProduit(produit)) {
						etalsProduitsRecherche[nbEtalTrouve] = etals[i];
						nbEtalTrouve++;
					}
				}
			}
			return etalsProduitsRecherche;
		}

		private Etal trouverVendeur(Gaulois gaulois) {
			boolean vendeurTrouve = false;
			Etal etalVendeur = null;
			for (int i = 0; i < etals.length && !vendeurTrouve; i++) {
				Gaulois vendeur = etals[i].getVendeur();
				if (vendeur != null) {
					vendeurTrouve = vendeur.getNom().equals(gaulois.getNom());
					if (vendeurTrouve) {
						etalVendeur = etals[i];
					}
				}
			}
			return etalVendeur;
		}

		private int getNbEtal() {
			return etals.length;
		}

		private int getNbEtalsOccupe() {
			int nbEtal = 0;
			for (Etal etal : etals) {
				if (etal.isEtalOccupe()) {
					nbEtal++;
				}
			}
			return nbEtal;
		}
		
		public String afficherMarche() {
		    StringBuilder affichage = new StringBuilder();
		    int nbEtalsVides = 0;

		    for (Etal etal : etals) {
		        if (etal.isEtalOccupe()) {
		            affichage.append(etal.afficherEtal()).append("\n");
		        } else {
		            nbEtalsVides++;
		        }
		    }

		    affichage.append("Il reste ").append(nbEtalsVides)
		             .append(" étals non utilisés dans le marché.\n");

		    return affichage.toString();
		}

		
		private String[] donnerEtat() {
			int tailleTableau = getNbEtalsOccupe() * 3;
			String[] donnees = new String[tailleTableau];
			for (int i = 0, j = 0; i < etals.length; i++) {
				Etal etal = etals[i];
				if (etal.isEtalOccupe()) {
					Gaulois vendeur = etal.getVendeur();
					int nbProduit = etal.getQuantite();
					donnees[j] = vendeur.getNom();
					j++;
					donnees[j] = String.valueOf(nbProduit);
					j++;
					donnees[j] = etal.getProduit();
					j++;
				}
			}
			return donnees;
		}
	}


	public void afficherVillageois() {
	    System.out.println("Dans le village " + nom + " dirigé par le chef " + chef.getNom() + " :");
	    if (nbVillageois == 0) {
	        System.out.println("Il n'y a actuellement aucun villageois dans le village.");
	    } else {
	        for (int i = 0; i < nbVillageois; i++) {
	            Gaulois habitant = villageois[i];
	            if (habitant instanceof Druide) {
	                System.out.println("- Le druide " + habitant.getNom());
	            } else {
	                System.out.println("- " + habitant.getNom());
	            }
	        }
	    }
	}


}