package fr.lazarus.model.plusMoins;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.ModeDePartie;
import fr.lazarus.model.ModelMaster;
import fr.lazarus.model.Partie;
import fr.lazarus.observer.Observable;
import fr.lazarus.observer.Observateur;
import fr.lazarus.view.game.PopUpFinPartie;

/**
 * Classe envoyant des indices en fonction de la proposition donnée par le Bean Partie
 * @author Ben
 *
 */
public class MasterPlus implements ModelMaster, Observable {

    //-- Les logs
    private static final Logger logger = LogManager.getLogger();

    private ArrayList<Observateur> listObservateur = new ArrayList<>();
    private Observateur obs;

    private Partie partie;
    private Configuration config;
    private String solution, proposition, indices;

    /**
     * Constructeur, recupere les parametres et initialise la classe
     * @param config Configuration
     * @param partie Partie
     * @param obs Observateur
     */
    public MasterPlus(Configuration config, Partie partie, Observateur obs) {
        this.obs = obs;
        this.config = config;
        this.partie = partie;
        this.addObservateur(obs);

        if (partie.getModeDePartie().equals(ModeDePartie.PLUS_CHAL)) {
            this.partie.ordiPartie(config.getCombiPlusMoins());
        }
    }

    /**
     * Compare les differences entre la proposition et la solution puis modifie dans l'objet partie l'indices avec les signes + - =
     * @param partie Partie
     */
    public void resolve(Partie partie) {

        this.partie = partie;
        solution = partie.getSolution();
        proposition = partie.getProposition();
        Integer [] propositionTab = new Integer[config.getCombiPlusMoins()];
        Integer [] solutionTab = new Integer[config.getCombiPlusMoins()];
        for (int i = 0; i<config.getCombiPlusMoins(); i++) {
            propositionTab[i] = Integer.valueOf(proposition.substring(i, i+1));
            solutionTab[i] = Integer.valueOf(solution.substring(i, i+1));
        }
        indices = "";
        for (int i = 0; i<config.getCombiPlusMoins(); i++) {
            if(propositionTab[i] < solutionTab[i]) {
                indices += "+";
            }
            else if(propositionTab[i] > solutionTab[i]) {
                indices += "-";
            }
            else {
                indices += "=";
            }
        }
        this.partie.setIndice(indices);
        if(partie.getModeDePartie() == ModeDePartie.PLUS_CHAL) {
            this.partie.addTour();
            endGame();
        }
        updateObservateur();
    }

    /**
     * Verifie si l'objet partie correspond aux conditions de victoire ou défaite
     * @return partie
     */
    public Partie endGame() {
        long prop = Long.parseLong(proposition);
        long sol = Long.parseLong(solution);
        if (sol == prop) {
            partie.setEnCours(false);
            PopUpFinPartie pufp = new PopUpFinPartie(null, "Gagné", true, partie, obs);
            logger.info("Gagné");
        }
        else {
            if(partie.getTour() < config.getTourPlusMoins()) {
            }
            else {
                partie.setEnCours(false);
                PopUpFinPartie pufp = new PopUpFinPartie(null, "Perdu", true, partie, obs);
                logger.info("Perdu");
            }
        }
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
