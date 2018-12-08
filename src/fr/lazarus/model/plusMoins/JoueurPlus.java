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
 * Classe de résolution du plus moins en fonction des indices envoyé par le Bean Partie.
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

    /**
     * Constructeur, recupere les parametres et initialise la classe
     * @param config Configuration
     * @param partie Partie
     * @param obs Observateur
     */
    public JoueurPlus(Configuration config, Partie partie, Observateur obs) {
        this.obs = obs;
        this.config = config;
        this.partie = partie;
        this.addObservateur(obs);
        initOrdinateur(partie);
    }

    /**
     * Au début d'une partie lance la première proposition de l'ordinateur.
     * @param partie Partie
     */
    public void initOrdinateur(Partie partie) {

        if (partie.getIndice().equals("vide")) {
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
     * @param partie Partie
     * @return nouvelleProp String
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
                indiceTab[i] = partie.getIndice().charAt(i);
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
            this.partie.setProposition(nouvelleProp);
            if(partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
                this.partie.addTour();
            }
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
        long prop = Long.parseLong(proposition);
        long sol = Long.parseLong(solution);
        if (sol == prop) {
            partie.setEnCours(false);
            @SuppressWarnings("unused")
            PopUpFinPartie pufp = new PopUpFinPartie(null, "Perdu", true, partie, obs);
            logger.info("Perdu");
        }
        else {
            if(partie.getTour() == config.getTourPlusMoins()) {
                partie.setEnCours(false);
                @SuppressWarnings("unused")
                PopUpFinPartie pufp = new PopUpFinPartie(null, "Gagné", true, partie, obs);
                logger.info("Gagné");
            }
        }
        updateObservateur();
        return partie;
    }

    public void addObservateur(Observateur obs) {
        listObservateur.add(obs);
    }

    public void updateObservateur() {
        for(Observateur obs : listObservateur) {
            obs.update(partie);
        }
        logger.info(partie.toString());
    }

    public void delObservateur() {}
}

