package fr.lazarus.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Java Bean contenant toute les informations nécessaires pour jouer aux jeux.
 * @author Ben
 *
 */
public class Partie {

	//-- Les logs
	private static final Logger logger = LogManager.getLogger();

	private ModeDePartie modeDePartie;
	private String nom, solution, proposition, indice;
	private int tour;
	private boolean enCours = true, actif = true;

	/**
	 * Constructeur
	 * @param nom String
	 */
	public Partie(String nom) {
		this.nom = nom;
		modeDePartie = null;
		solution = "vide";
		proposition = "vide";
		indice = "vide";
		tour = 0;
	}

	/**
	 * Constructeur
	 * @param nom String
	 * @param modeDePartie ModeDePartie
	 */
	public Partie(String nom, ModeDePartie modeDePartie) {
		this.nom = nom;
		this.modeDePartie = modeDePartie;
		solution = "vide";
		proposition = "vide";
		indice = "vide";
		tour = 0;
	}

	/**
	 * Constructeur
	 * @param nom String
	 * @param modeDePartie ModeDePartie
	 * @param solution String
	 */
	public Partie(String nom, ModeDePartie modeDePartie, String solution) {
		this.nom = nom;
		this.solution = solution;
		this.modeDePartie = modeDePartie;
		proposition = "vide";
		indice = "vide";
		tour = 0;
	}

    /**
     * Génère la solution en fonction du nombre de chiffres présent dans nbreChiffre
     * @param nbreChiffre int
     */
	public void ordiPartie(int nbreChiffre) { setSolution(random(nbreChiffre)); }

    /**
     * Génère la solution en fonction du nombre de chiffres présent dans nbreChiffre et les chiffres à utiliser avec nbreCouleur
     * @param nbreChiffre int
     * @param nbreCouleur int
     */
    public void ordiPartie(int nbreChiffre, int nbreCouleur) { setSolution(random(nbreChiffre, nbreCouleur)); }

	/**
	 * Génère une combinaison aléatoire d'une longueur égale à combiPlusMoins de l'objet configuration
	 * @param nbreChiffre int
	 * @return random String
	 */
	public String random(int nbreChiffre) {
		int [] combi = new int[nbreChiffre];
		for (int i = 0; i<nbreChiffre; i++) {
			combi[i] = (int)(Math.random()*10);
		}
		String random = convertTabIntToString(combi);
		return random;
	}

	/**
	 * Génère une combinaison aléatoire d'une longueur égale à combiMast avec des chiffres compris entre 0 et couleurMast de l'objet configuration
	 * @param nbreChiffre int
	 * @param nbreCouleur int
	 * @return random String
	 */
	public String random(int nbreChiffre, int nbreCouleur) {
		int [] combi = new int[nbreChiffre];
		for (int i = 0; i<nbreChiffre; i++) {
			combi[i] = (int)(Math.random()*nbreCouleur);
		}
		String random = convertTabIntToString(combi);
		return random;
	}

	/**
	 * Convertit un tableau d'int en string
	 * @param tab tableau de int
	 * @return str String
	 */
	private String convertTabIntToString(int [] tab) {
		String str= "";
		for(int i = 0; i<tab.length; i++) {
			str += Integer.toString(tab[i]);
		}
		return str;
	}

	/**
	 * Ajoute un tour au compteur de tour
	 */
	public void addTour() {
		tour += 1;
	}

	/**
	 * Decrit ce que contient le Bean.
	 */
	public String toString() {
		String str = "Partie : [ Nom : "+ nom + ", mode de Partie : " + modeDePartie + ", solution : " + solution + ", proposition : " + proposition + ", indice : " + indice
				+ ", tour : " + tour + ", enCours : " + enCours + "]";
		return str;
	}

	//GETTER SETTER
	
	public String getNom() {return nom;}
	public void setNom(String nom) {this.nom = nom;}

	public ModeDePartie getModeDePartie() {return modeDePartie;}
	public void setModeDePartie(ModeDePartie modeDePartie) {this.modeDePartie = modeDePartie;}	

	public String getSolution() {return solution;}
	public void setSolution(String solution) {this.solution = solution;}

	public String getProposition() {return proposition;}
	public void setProposition(String proposition) {this.proposition = proposition;}

	public String getIndice() {return indice;}
	public void setIndice(String indice) {this.indice = indice;}

	public int getTour() {return tour;}
	public void setTour(int tour) {this.tour = tour;}

	public boolean isEnCours() {return enCours;}
	public void setEnCours(boolean enCours) {this.enCours = enCours;}

	public boolean isActif() {return actif;}
	public void setActif(boolean actif) {this.actif = actif;}

}