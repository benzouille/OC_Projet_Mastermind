package fr.lazarus.model;

import fr.lazarus.model.plusMoins.JoueurPlus;
import fr.lazarus.model.plusMoins.MasterPlus;
import fr.lazarus.observer.Observateur;

public class Jeu {

	private Partie partie1, partie2;
	private ModeDeJeu modeDeJeu;
	private Configuration config;
	private Observateur obs;
	private MasterPlus master;
	private JoueurPlus joueur;
	private boolean actifChal = true, actifDef = false;

	public Jeu(ModeDeJeu modeDeJeu, Configuration config, Observateur obs) {
		this.modeDeJeu = modeDeJeu;
		this.config = config;
		this.obs = obs;
		initJeu();
	}

	/**
	 * Initialise les Objets Joueur et Master en fonction du mode de jeu et crée un objet partie supplémentaire pour le mode duel
	 */
	public void initJeu() {
		//TODO en fonction du ModeDeJeu lancer le master le joueur avec ordinateur/humain
		if(modeDeJeu.equals(ModeDeJeu.PLUS_CHAL)) {
			partie1 = new Partie("partie1", ModeDePartie.PLUS_CHAL);
			master = new MasterPlus(config, partie1, obs);
		}
		else if(modeDeJeu.equals(ModeDeJeu.MAST_CHAL)) {
			partie1 = new Partie("partie1", ModeDePartie.MAST_CHAL);
			//master = new MasterPlus(config, partie1, obs);
		}
		else if(modeDeJeu.equals(ModeDeJeu.PLUS_DEF)) {
			partie1 = new Partie("partie1", ModeDePartie.PLUS_DEF);
			joueur = new JoueurPlus(config, partie1, obs);
			if(config.isDevModEnJeu()) {
				master = new MasterPlus(config, partie1, obs);	
			}
			joueur.initOrdinateur(partie1);
		}
		else if(modeDeJeu.equals(ModeDeJeu.MAST_DEF)) {
			partie1 = new Partie("partie1", ModeDePartie.MAST_DEF);
			joueur = new JoueurPlus(config, partie1, obs);
			//joueur.initOrdinateur(partie1);
		}
		else if(modeDeJeu.equals(ModeDeJeu.PLUS_DUEL)) {
			partie1 = new Partie("partie1", ModeDePartie.PLUS_CHAL);
			master = new MasterPlus(config, partie1, obs);
			partie2 = new Partie("partie2", ModeDePartie.PLUS_DEF);
			joueur = new JoueurPlus(config, partie2, obs);
			joueur.initOrdinateur(partie2);
		}
		else {
			partie1 = new Partie("partie1", ModeDePartie.MAST_CHAL);
			master = new MasterPlus(config, partie1, obs);
			partie2 = new Partie("partie2", ModeDePartie.MAST_DEF);
			joueur = new JoueurPlus(config, partie2, obs);
			joueur.initOrdinateur(partie2);
		}
	}

	public void initOrdi(Partie partie) {
		joueur.initOrdinateur(partie);
	}

	/**
	 * L'ordinateur renvoie un indice en fonction de la variable proposition de l'objet partie passé en parametre
	 * @param partie
	 */
	public void indiceOrdi (Partie partie) {
		System.out.println("indiceOrdi() de Jeu");
		master.resolve(partie);
	}

	/**
	 * L'ordinateur renvoie une proposition en fonction de la variable indice et de la proposition précdente de l'objet partie passé en parametre,
	 * puis il fait un switchMode.
	 * @param partie
	 */
	public void propOrdi(Partie partie) {
		System.out.println("propOrdi() de Jeu");
		joueur.propositionOrdinateur(partie);
	}

	/**
	 * Modifie les boolean actifChal en false et actifDef en true a chaque appel.
	 */
	public void switchModeDef() {
		System.out.println("switchModeDef() de Jeu");
		actifChal = false;
		actifDef = true;
	}

	/**
	 * Modifie les boolean actifChal en true et actifDef en false a chaque appel.
	 */
	public void switchModeChal() {
		System.out.println("switchModeChal() de Jeu");
		actifChal = true;
		actifDef = false;
	}

	// -- GUETTER SETTER
	
	public boolean isActifChal() {return actifChal;}
	public void setActifChal(boolean actifChal) {this.actifChal = actifChal;}

	public boolean isActifDef() {return actifDef;}
	public void setActifDef(boolean actifDef) {this.actifDef = actifDef;}

	public Partie getPartie1() {return partie1;}
	public void setPartie1(Partie partie1) {this.partie1 = partie1;}

	public Partie getPartie2() {return partie2;}
	public void setPartie2(Partie partie2) {this.partie2 = partie2;}

	public ModeDeJeu getModeDeJeu() {return modeDeJeu;}
	public void setModeDeJeu(ModeDeJeu modeDeJeu) {this.modeDeJeu = modeDeJeu;}
}
