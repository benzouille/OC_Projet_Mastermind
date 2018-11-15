package fr.lazarus.model.mastermind;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.ModelJoueur;
import fr.lazarus.model.Partie;
import fr.lazarus.observer.Observable;
import fr.lazarus.observer.Observateur;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class JoueurMastermind implements ModelJoueur, Observable {

    //-- Les logs
    private static final Logger logger = LogManager.getLogger();

    private ArrayList<Observateur> listObservateur = new ArrayList<Observateur>();
    private Observateur obs;

    private Partie partie;
    private Configuration config;

	public JoueurMastermind(Configuration config, Partie partie, Observateur obs){
	    this.config = config;
	    this.partie = partie;
	    this.obs = obs;
	    this.addObservateur(obs);
        initOrdinateur(partie);
    }

	@Override
	public void initOrdinateur(Partie partie) {
		// TODO Auto-generated method stub

	}

	@Override
	public String propositionOrdinateur(Partie partie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Partie endGame() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public void addObservateur(Observateur obs) {

    }

    @Override
    public void updateObservateur() {

    }

    @Override
    public void delObservateur() {

    }
}
