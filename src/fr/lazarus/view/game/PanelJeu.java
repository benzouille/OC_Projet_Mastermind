package fr.lazarus.view.game;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fr.lazarus.controller.Controller;
import fr.lazarus.model.Configuration;
import fr.lazarus.model.Jeu;
import fr.lazarus.model.ModeDeJeu;
import fr.lazarus.model.ModeDePartie;
import fr.lazarus.observer.Observateur;
import fr.lazarus.view.PanelAccueil;
import fr.lazarus.view.game.mastermind.GamePanelMastermind;
import fr.lazarus.view.game.mastermind.PopUpCombiMastermind;
import fr.lazarus.view.game.plusMoins.GamePanelPlusMoins;
import fr.lazarus.view.game.plusMoins.PopUpCombiPlus;

/**
 * Classe qui initialise le panel du jeu en fonction du mode de jeu
 */
public class PanelJeu extends JPanel {

	private Jeu jeu;
	private	Controller controller, controller2;
	private Thread thread;
	private PanelAccueil accueil;
	private GamePanelPlusMoins jpChalPlus, jpDefPlus;
	private GamePanelMastermind jpChalMast, jpDefMast;
	private Configuration configuration;
	private Observateur obs;
	private Dimension bigSize = new Dimension (1710, 1050),
			smallSize = new Dimension (845, 1040),
			bourrageSize  = new Dimension(430, 1040);

	/**
	 * constructeur
	 * @param jeu Jeu
	 * @param configuration Configuration
	 * @param obs Observateur
	 */
	public PanelJeu(Jeu jeu, Configuration configuration, Observateur obs) {
		this.jeu = jeu;
		this.configuration = configuration;
		this.obs = obs;
		init();
	}

	/**
	 * initialisation du Panel
	 */
	public void init() {
		if(jeu.getModeDeJeu().equals(ModeDeJeu.PLUS_CHAL)){
			this.setPreferredSize(smallSize);
			controller = new Controller(configuration,jeu.getPartie1(), jeu);
			jpChalPlus = new GamePanelPlusMoins(configuration, jeu.getModeDeJeu(), jeu.getPartie1(), controller);
			ajouterPanneauxDeBourrageDeChaqueCote();
			this.add(jpChalPlus, BorderLayout.CENTER);
		}
		else if(jeu.getModeDeJeu().equals(ModeDeJeu.PLUS_DEF)) {
			this.setPreferredSize(smallSize);
			controller = new Controller(configuration,jeu.getPartie1(), jeu);
			PopUpCombiPlus popUpCombiPlus = new PopUpCombiPlus(null, "choix de la combinaison", true, configuration, jeu.getPartie1(), obs);
			if(!popUpCombiPlus.isAnnuler()) {
				jpDefPlus = new GamePanelPlusMoins(configuration, jeu.getModeDeJeu(), jeu.getPartie1(), controller);
				if (configuration.isDevModEnJeu() && jeu.getPartie1().getModeDePartie() == ModeDePartie.PLUS_DEF) {
					controller.sendProposition(jeu.getPartie1());
					jpDefPlus.devIndice();
				}
				ajouterPanneauxDeBourrageDeChaqueCote();
				this.add(jpDefPlus, BorderLayout.CENTER);
			}
			else {
				accueil = new PanelAccueil(new Dimension (1710, 1070));
				this.add(accueil);
			}
		}
		else if(jeu.getModeDeJeu().equals(ModeDeJeu.PLUS_DUEL)) {
			this.setPreferredSize(bigSize);
			controller = new Controller(configuration, jeu.getPartie1(), jeu);
			jpChalPlus = new GamePanelPlusMoins(configuration, jeu.getModeDeJeu(), jeu.getPartie1(), controller);
			jpChalPlus.setPreferredSize(smallSize);
			JPanel jpEspace = new JPanel();
			jpEspace.setPreferredSize(new Dimension(10, 1040));
			controller2 = new Controller(configuration, jeu.getPartie2(), jeu);
			PopUpCombiPlus popUpCombiPlus = new PopUpCombiPlus(null, "choix de la combinaison", true, configuration, jeu.getPartie2(), obs);
			if (!popUpCombiPlus.isAnnuler()) {
				jeu.getPartie2().setActif(false);
				jpDefPlus = new GamePanelPlusMoins(configuration, jeu.getModeDeJeu(), jeu.getPartie2(), controller);
				if (configuration.isDevModEnJeu() && jeu.getPartie2().getModeDePartie() == ModeDePartie.PLUS_DEF) {
					controller.sendProposition(jeu.getPartie2());
					jpDefPlus.devIndice();
				}
				jpDefPlus.setPreferredSize(smallSize);

				this.setLayout(new BorderLayout());
				this.add(jpChalPlus, BorderLayout.WEST);
				this.add(jpEspace, BorderLayout.CENTER);
				this.add(jpDefPlus, BorderLayout.EAST);
			}
			else {
				accueil = new PanelAccueil(new Dimension (1710, 1070));
				this.add(accueil);
			}
		}
		else if(jeu.getModeDeJeu().equals(ModeDeJeu.MAST_CHAL)){
			this.setPreferredSize(smallSize);
			controller = new Controller(configuration,jeu.getPartie1(), jeu);
			jpChalMast = new GamePanelMastermind(configuration, jeu.getModeDeJeu(), jeu.getPartie1(), controller);
			ajouterPanneauxDeBourrageDeChaqueCote();
			this.add(jpChalMast, BorderLayout.CENTER);
		}
		else if(jeu.getModeDeJeu().equals(ModeDeJeu.MAST_DEF)) {
			this.setPreferredSize(smallSize);
			controller = new Controller(configuration,jeu.getPartie1(), jeu);
			PopUpCombiMastermind popUpCombiMastermind = new PopUpCombiMastermind(null, "choix de la combinaison", true, configuration, jeu.getPartie1(), obs);
			if(!popUpCombiMastermind.isAnnuler()) {
				jpDefMast = new GamePanelMastermind(configuration, jeu.getModeDeJeu(), jeu.getPartie1(), controller);
				if (configuration.isDevModEnJeu() && jeu.getPartie1().getModeDePartie() == ModeDePartie.MAST_DEF) {
					controller.sendProposition(jeu.getPartie1());
					jpDefMast.devIndice();
				}
				ajouterPanneauxDeBourrageDeChaqueCote();
				this.add(jpDefMast);
			}
			else{
				accueil = new PanelAccueil(new Dimension (1710, 1070));
				this.add(accueil);
			}
		}
		else if(jeu.getModeDeJeu().equals(ModeDeJeu.MAST_DUEL)) {
			this.setPreferredSize(bigSize);
			controller = new Controller(configuration, jeu.getPartie1(), jeu);
			jpChalMast = new GamePanelMastermind(configuration, jeu.getModeDeJeu(), jeu.getPartie1(), controller);
			jpChalMast.setPreferredSize(smallSize);
			JPanel jpEspace = new JPanel();
			jpEspace.setPreferredSize(new Dimension(10, 1040));
			controller2 = new Controller(configuration, jeu.getPartie2(), jeu);
			PopUpCombiMastermind popUpCombiMastermind = new PopUpCombiMastermind(null, "choix de la combinaison", true, configuration, jeu.getPartie2(), obs);
			if (!popUpCombiMastermind.isAnnuler()) {
				jeu.getPartie2().setActif(false);
				jpDefMast = new GamePanelMastermind(configuration, jeu.getModeDeJeu(), jeu.getPartie2(), controller);
				jpDefMast.setPreferredSize(smallSize);
				if (configuration.isDevModEnJeu() && jeu.getPartie2().getModeDePartie() == ModeDePartie.MAST_DEF) {
					controller.sendProposition(jeu.getPartie2());
					jpDefMast.devIndice();
				}

				this.setLayout(new BorderLayout());
				this.add(jpChalMast, BorderLayout.WEST);
				this.add(jpEspace, BorderLayout.CENTER);
				this.add(jpDefMast, BorderLayout.EAST);
			}
		}
		else{
			accueil = new PanelAccueil(new Dimension (1710, 1070));
			this.add(accueil);
		}
	}

	/**
	 *  Les panneaux de bourrage
	 */
	private void ajouterPanneauxDeBourrageDeChaqueCote() {
		this.setLayout(new BorderLayout());
		JPanel jpLeft = new JPanel();
		JPanel jpRight = new JPanel();
		jpLeft.setPreferredSize(bourrageSize);
		jpRight.setPreferredSize(bourrageSize);
		this.add(jpLeft, BorderLayout.WEST);
		this.add(jpRight, BorderLayout.EAST);
	}

	/**
	 * Le focus du challenger passe en rouge (tout est en rouge) et une seconde plus tard, le focus de la partie Défenseur passe en vert.
	 */

	public void defTurn() {
		if(jeu.getModeDeJeu().equals(ModeDeJeu.PLUS_DUEL)) {
			jpChalPlus.stopTurn();
			updateFocus(jpDefPlus, 2);
		}
		else {
			jpChalMast.stopTurn();
			updateFocus(jpDefMast, 2);
		}
	}

	public void chalTurn() {
		if(jeu.getModeDeJeu().equals(ModeDeJeu.PLUS_DUEL)) {
			jpDefPlus.stopTurn();
			updateFocus(jpChalPlus, 2);
		}
		else{
			jpDefMast.stopTurn();
			updateFocus(jpChalMast, 2);
		}
	}

	/**
	 * Mettre à jour le focus du second panneau de jeu via un Thread Indépendant.
	 * @param jpTarget GamePanelPlusMoins
	 * @param secondes Integer
	 */
	public static void updateFocus(GamePanelPlusMoins jpTarget, Integer secondes) {
		new Thread(new Runnable() {
			public void run() {
				sleep(secondes);
				//-- Modification de notre composant dans l'EDT
				Thread t = new Thread(new Runnable() {
					public void run() {
						jpTarget.newTurn();
					}
				});
				//-- Si l'EDT est actif, le Thread est lancée sinon il le sera par l'EDT plus tard
				if (SwingUtilities.isEventDispatchThread())
					t.start();
				else {
					SwingUtilities.invokeLater(t);
				}
			}
		}).start();
	}

	/**
	 * Mettre à jour le focus du second panneau de jeu via un Thread Indépendant.
	 * @param jpTarget GamePanelMastermind
	 * @param secondes Integer
	 */
	public static void updateFocus(GamePanelMastermind jpTarget, Integer secondes) {
		new Thread(new Runnable() {
			public void run() {
				sleep(secondes);
				//-- Modification de notre composant dans l'EDT
				Thread t = new Thread(new Runnable() {
					public void run() {
						jpTarget.newTurn();
					}
				});
				//-- Si l'EDT est actif, le Thread est lancée sinon il le sera par l'EDT plus tard
				if (SwingUtilities.isEventDispatchThread())
					t.start();
				else {
					SwingUtilities.invokeLater(t);
				}
			}
		}).start();
	}

	/**
	 * Lance une temporisation de x secondes.
	 */
	private static void sleep(Integer secondes) {
		try {
			Thread.sleep(secondes * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
