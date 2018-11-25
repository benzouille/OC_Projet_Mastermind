package fr.lazarus.model.plusMoins;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.ModeDePartie;
import fr.lazarus.model.ModelJoueur;
import fr.lazarus.model.Partie;
import fr.lazarus.observer.Observable;
import fr.lazarus.observer.Observateur;
import fr.lazarus.view.game.PopUpFinPartie;

/**
 * Classe de résolution du plus moins en fonction des indices envoyé.
 * @author Ben
 *
 */
public class JoueurPlus implements ModelJoueur, Observable {

	//-- Les logs
	private static final Logger logger = LogManager.getLogger();

	private ArrayList<Observateur> listObservateur = new ArrayList<Observateur>();
	private Observateur obs;

	private Partie partie;
	private Configuration config;
	private float intervaleMax[], intervaleMin[];
	private String solution, proposition;

	public JoueurPlus(Configuration config, Partie partie, Observateur obs) {
		this.obs = obs;
		this.config = config;
		this.partie = partie;
		this.addObservateur(obs);
			initOrdinateur(partie);
	}
	
	/**
	 * Au début d'une partie lance la première proposition de l'ordinateur. 
	 */
	public void initOrdinateur(Partie partie) {

		if (partie.getIndice() == "vide") {
			String proposition = "";
			for (int i = 0; i<config.getCombiPlusMoins(); i++) {
				proposition += '5';
			}
			this.partie.setProposition(proposition);
		}

		intervaleMax = new float[config.getCombiPlusMoins()];
		intervaleMin = new float[config.getCombiPlusMoins()];
		for (int i = 0; i<config.getCombiPlusMoins(); i++) {
			intervaleMax[i] =9.0f;
			intervaleMin[i] =0.0f;
		}
	}

	/**
	 * Methode permettant que l'ordinateur donne des proposition en fonction des indices et de la proposition précédament données
	 * @param partie
	 * @return 
	 */
	public String propositionOrdinateur(Partie partie) {
		Integer [] propositionTab = new Integer[config.getCombiPlusMoins()];
		char [] indiceTab = new char[config.getCombiPlusMoins()];
		String nouvelleProp = "";
		if(partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
		endGame();
		}
		if(partie.isEnCours()) {
		for (int i = 0; i<config.getCombiPlusMoins(); i++) {
			propositionTab[i] = Integer.valueOf(partie.getProposition().substring(i, i+1));
			//System.out.print("proposition à l'enplacement "+i+" : "+propositionTab[i]+". ");
			indiceTab[i] = partie.getIndice().charAt(i);
			//System.out.println(" indice à l'enplacement "+i+" : "+indiceTab[i]);
		}
		for(int i =0; i<partie.getSolution().length(); i++) {
			if(indiceTab[i] == '+') {
				this.intervaleMin[i] = propositionTab[i];
				nouvelleProp += propositionTab[i]+Math.round((intervaleMax[i]-intervaleMin[i])/2);
			}
			else if(indiceTab[i] == '-') {
				this.intervaleMax[i] = propositionTab[i];
				nouvelleProp += propositionTab[i]-Math.round((intervaleMax[i]-intervaleMin[i])/2);
			}
			else {
				nouvelleProp += propositionTab[i];
			}
		}
		logger.debug(" nouvelle proposition et l'intervale Max et min", nouvelleProp, intervaleMax, intervaleMin);
		this.partie.setProposition(nouvelleProp);
		if(partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
		this.partie.addTour();
		}
		System.out.println("propositionOrdinateur() de Joueur");
		updateObservateur();
		}
		return nouvelleProp;
		
	}

	/**
	 * Verifie si l'objet partie correspond aux conditions de victoire ou défaite
	 * @return partie
	 */
	public Partie endGame() {
		proposition = partie.getProposition();
		solution = partie.getSolution();
		int prop = Integer.parseInt(proposition);
		int sol = Integer.parseInt(solution);
		if (sol == prop) {
			partie.setEnCours(false);
			@SuppressWarnings("unused")
			PopUpFinPartie pufp = new PopUpFinPartie(null, "Gagné", true, partie, obs);
		}
		else {
			if(partie.getTour() == config.getTourPlusMoins()) {
				partie.setEnCours(false);
				@SuppressWarnings("unused")
				PopUpFinPartie pufp = new PopUpFinPartie(null, "Perdu", true, partie, obs);
			}
		}
		System.out.println("endGame() de JoueurPlus");
		updateObservateur();
		return partie;
	}
	
	public void addObservateur(Observateur obs) {
		listObservateur.add(obs);
	}

	public void updateObservateur() {
		for(Observateur obs : listObservateur)
			obs.update(partie);
		System.out.println("updateObservateur() de Joueur");
	}

	public void delObservateur() {}
}

