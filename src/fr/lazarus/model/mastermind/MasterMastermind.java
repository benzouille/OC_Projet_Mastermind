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

public class MasterMastermind implements ModelMaster, Observable {

    //-- Les logs
    private static final Logger logger = LogManager.getLogger();

    private ArrayList<Observateur> listObservateur = new ArrayList<Observateur>();
    private Observateur obs;

    private Configuration config;
	private Partie partie;
	private String solution;
	private String proposition;

	public MasterMastermind(Configuration config, Partie partie, Observateur obs){
	    this.config = config;
	    this.partie = partie;
	    this.obs = obs;
        this.addObservateur(obs);

        if (partie.getModeDePartie().equals(ModeDePartie.MAST_CHAL)) {
            this.partie.ordiPartie(config.getCombiPlusMoins(), config.getCouleurMast());
        }
    }

    /**
     * Compare les differences entre la proposition et la solution puis modifie dans l'objet partie l'indices avec des 1 pour les noirs et des 2 pour les blancs
     * @param partie Objet Partie
     * @return partie Objet Partie
     */
	public void resolve(Partie partie) {
		this.partie = partie;
		solution = partie.getSolution();
		proposition = partie.getProposition();

		ArrayList<Integer> prop = new ArrayList<Integer>();
		ArrayList<Integer> sol = new ArrayList<Integer>();
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i<proposition.length(); i++) {
			prop.add(Integer.valueOf(proposition.substring(i, i+1)));
			sol.add(Integer.valueOf(solution.substring(i, i+1)));
		}
		//-- Boucle pour trouver les noirs
		for (int i = 0; i<prop.size(); i++) {
			if(prop.get(i).equals(sol.get(i))) {
				prop.remove(i);
				sol.remove(i);
				result.add("1");
			}
		}
		//-- Boucle pour trouver les blancs
		for (int i = 0; i<prop.size(); i++) {
			for(int y = 0; y<sol.size(); y++) {
				if(prop.get(i).equals(sol.get(y))) {
					prop.remove(i);
					sol.remove(y);
					result.add("2");
				}
			}
		}
		String str = result.toString();

		partie.setIndice(str);
	}

    /**
     * Verifie si l'objet partie correspond aux conditions de victoire ou défaite
     * @return partie
     */
    public Partie endGame() {
        System.out.println("endGame() de MasterMastermind");
        int prop = Integer.parseInt(proposition);
        int sol = Integer.parseInt(solution);
        if (sol == prop) {
            partie.setEnCours(false);
            PopUpFinPartie pufp = new PopUpFinPartie(null, "Gagné", true, partie, obs);
        }
        else {
            if(partie.getTour() < config.getTourMast()) {
            }
            else {
                partie.setEnCours(false);
                PopUpFinPartie pufp = new PopUpFinPartie(null, "Perdu", true, partie, obs);
            }
        }
        return partie;
    }

    public void addObservateur(Observateur obs) {
        listObservateur.add(obs);
    }

    public void updateObservateur() {
        for(Observateur obs : listObservateur) {
            System.out.println("updateObservateur() de MasterMastermind");
            System.out.println(partie.toString());
            obs.update(partie);
        }
    }

    public void delObservateur() {}
}
