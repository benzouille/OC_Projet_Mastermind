package fr.lazarus.model;

import fr.lazarus.model.mastermind.JoueurMastermind;
import fr.lazarus.model.mastermind.MasterMastermind;
import fr.lazarus.model.plusMoins.JoueurPlus;
import fr.lazarus.model.plusMoins.MasterPlus;
import fr.lazarus.observer.Observateur;

/**
 * Classe instanciant les parties en fonction des modes de jeu
 */
public class Jeu {

	private Partie partie1, partie2;
	private ModeDeJeu modeDeJeu;
	private Configuration config;
	private Observateur obs;
	private MasterPlus masterPlus;
	private MasterMastermind masterMastermind;
	private JoueurPlus joueurPlus;
	private JoueurMastermind joueurMastermind;

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
		if(modeDeJeu.equals(ModeDeJeu.PLUS_CHAL)) {
			partie1 = new Partie("partie1", ModeDePartie.PLUS_CHAL);
            masterPlus = new MasterPlus(config, partie1, obs);
		}
		else if(modeDeJeu.equals(ModeDeJeu.MAST_CHAL)) {
			partie1 = new Partie("partie1", ModeDePartie.MAST_CHAL);
            masterMastermind = new MasterMastermind(config, partie1, obs);
		}
		else if(modeDeJeu.equals(ModeDeJeu.PLUS_DEF)) {
			partie1 = new Partie("partie1", ModeDePartie.PLUS_DEF);
            joueurPlus = new JoueurPlus(config, partie1, obs);
			if(config.isDevModEnJeu()) {
                masterPlus = new MasterPlus(config, partie1, obs);
			}
            joueurPlus.initOrdinateur(partie1);
		}
		else if(modeDeJeu.equals(ModeDeJeu.MAST_DEF)) {
			partie1 = new Partie("partie1", ModeDePartie.MAST_DEF);
			joueurMastermind = new JoueurMastermind(config, partie1, obs);
			if(config.isDevModEnJeu()) {
				masterMastermind = new MasterMastermind(config, partie1, obs);
			}
			joueurMastermind.initOrdinateur(partie1);
		}
		else if(modeDeJeu.equals(ModeDeJeu.PLUS_DUEL)) {
			partie1 = new Partie("partie1", ModeDePartie.PLUS_CHAL);
            masterPlus = new MasterPlus(config, partie1, obs);
			partie2 = new Partie("partie2", ModeDePartie.PLUS_DEF);
            joueurPlus = new JoueurPlus(config, partie2, obs);
            joueurPlus.initOrdinateur(partie2);
		}
		else {
			partie1 = new Partie("partie1", ModeDePartie.MAST_CHAL);
            masterMastermind = new MasterMastermind(config, partie1, obs);
			partie2 = new Partie("partie2", ModeDePartie.MAST_DEF);
            joueurMastermind = new JoueurMastermind(config, partie2, obs);
            joueurMastermind.initOrdinateur(partie2);
		}
	}

	/**
	 * L'ordinateur renvoie un indice en fonction de la variable proposition de l'objet partie passé en parametre
	 * @param partie Partie
	 */
	public void indiceOrdi (Partie partie) {
        if(partie.getModeDePartie().equals(ModeDePartie.PLUS_CHAL)){
            masterPlus.resolve(partie);
        }
        else if(partie.getModeDePartie().equals(ModeDePartie.PLUS_DEF)) {
            masterPlus.resolve(partie);
        }
        else if (partie.getModeDePartie().equals(ModeDePartie.MAST_CHAL)){
            masterMastermind.resolve(partie);
        }
        else{
            masterMastermind.resolve(partie);
        }
	}

	/**
	 * L'ordinateur renvoie une proposition en fonction de la variable indice et de la proposition précdente de l'objet partie passé en parametre,
	 * puis il fait un switchMode.
	 * @param partie Partie
	 */
	public void propOrdi(Partie partie) {
		if(partie.getModeDePartie().equals(ModeDePartie.PLUS_CHAL)){
            joueurPlus.propositionOrdinateur(partie);
        }
		else if(partie.getModeDePartie().equals(ModeDePartie.PLUS_DEF)) {
		    joueurPlus.propositionOrdinateur(partie);
        }
        else if(partie.getModeDePartie().equals(ModeDePartie.MAST_DEF)) {
            joueurMastermind.propositionOrdinateur(partie);
        }
        else{
            joueurMastermind.propositionOrdinateur(partie);
        }
	}

	// -- GUETTER SETTER

	public Partie getPartie1() {return partie1;}
	public void setPartie1(Partie partie1) {this.partie1 = partie1;}

	public Partie getPartie2() {return partie2;}
	public void setPartie2(Partie partie2) {this.partie2 = partie2;}

	public ModeDeJeu getModeDeJeu() {return modeDeJeu;}
	public void setModeDeJeu(ModeDeJeu modeDeJeu) {this.modeDeJeu = modeDeJeu;}
}
