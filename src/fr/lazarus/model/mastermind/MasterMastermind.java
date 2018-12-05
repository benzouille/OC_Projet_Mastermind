package fr.lazarus.model.mastermind;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.ModeDePartie;
import fr.lazarus.model.ModelMaster;
import fr.lazarus.model.Partie;
import fr.lazarus.observer.Observable;
import fr.lazarus.observer.Observateur;
import fr.lazarus.view.game.PopUpFinPartie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe du model mastermind retournant un indice en fonction de la proposition donnée par le Bean partie
 * @author Ben
 */
public class MasterMastermind implements ModelMaster, Observable {

    //-- Les logs
    private static final Logger logger = LogManager.getLogger();

    private ArrayList<Observateur> listObservateur = new ArrayList<Observateur>();
    private Observateur obs;

    private Configuration config;
    private Partie partie;
    private String solution;
    private String proposition;

    private int noir = 0, blanc =1;

    private ArrayList<Integer> prop;
    private ArrayList<Integer> sol;
    private ArrayList<Integer> result;

    /**
     * Constructeur, recupere les parametres et initialise la classe
     * @param config
     * @param partie
     * @param obs
     */
    public MasterMastermind(Configuration config, Partie partie, Observateur obs){
        this.config = config;
        this.partie = partie;
        this.obs = obs;
        this.addObservateur(obs);

        if (partie.getModeDePartie().equals(ModeDePartie.MAST_CHAL)) {
            this.partie.ordiPartie(config.getCombiMast(), config.getCouleurMast());
        }
    }

    /**
     * Compare les differences entre la proposition et la solution puis modifie dans l'objet partie l'indices avec des 0 pour les noirs et des 1 pour les blancs
     * @param partie Objet Partie
     * @return partie Objet Partie
     */
    public void resolve(Partie partie) {
        this.partie = partie;
        solution = partie.getSolution();
        proposition = partie.getProposition();

        prop = new ArrayList<>();
        sol = new ArrayList<>();
        result = new ArrayList<>();

        for (int i = 0; i<proposition.length(); i++) {
            prop.add(Integer.valueOf(proposition.substring(i, i+1)));
            sol.add(Integer.valueOf(solution.substring(i, i+1)));
        }

        if(partie.getModeDePartie() == ModeDePartie.MAST_CHAL) {
            endGame();
        }

        //les noirs
        boolean match = false;
        for (int i = 0; i < prop.size(); i++) {
            if (match) {
                i = 0;
                match = false;
            }
            if (prop.get(i).equals(sol.get(i))) {
                addIndice(noir,i,i);
                match = true;
            }
        }

        //les blancs
        for (int i = 0; i < prop.size();) {
            for (int y = 0; y < sol.size(); y++) {
                if (prop.get(i).equals(sol.get(y))) {
                    addIndice(blanc,i,y);
                    match = true;
                    break;
                }
            }
            if (match) {
                i = 0;
                match = false;
            }
            else {
                i++;
            }
        }

        String str = "";
        for (int i = 0; i<result.size(); i++){
            str += result.get(i);
        }
        partie.setIndice(str);
        if(partie.getModeDePartie() == ModeDePartie.MAST_CHAL) {
            this.partie.addTour();
        }
        updateObservateur();
    }

    /**
     * Ajout des indices dans l'arrayList result et supprime l'index donné dans les arrayList prop et sol
     * @param couleur int
     * @param indexProp int
     * @param indexSol int
     */
    private void addIndice(int couleur, int indexProp, int indexSol){
        result.add(couleur);
        prop.remove(indexProp);
        sol.remove(indexSol);
    }

    /**
     * Verifie si l'objet partie correspond aux conditions de victoire ou défaite
     * @return partie
     */
    public Partie endGame() {
        Long propInt = Long.parseLong(proposition);
        Long solInt = Long.parseLong(solution);
        if (solInt.equals(propInt)) {
            partie.setEnCours(false);
            PopUpFinPartie pufp = new PopUpFinPartie(null, "Gagné", true, partie, obs);
            logger.info("Gagné");
        }
        else {
            if(partie.getTour() == config.getTourMast()) {
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
