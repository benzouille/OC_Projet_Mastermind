package fr.lazarus.controller;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.Jeu;
import fr.lazarus.model.Partie;

public class Controller {

	private Partie partie;
	private Configuration config;
	private Jeu jeu;


	public Controller(Configuration config, Partie partie,Jeu jeu) {
		this.config =config;
		this.partie = partie;
		this.jeu = jeu;

	}

	public void sendProposition(Partie partie) {
		this.partie = partie;
		jeu.indiceOrdi(partie);
		
	}
	public void sendIndice(Partie partie) {
		this.partie = partie;
		jeu.propOrdi(partie);
	}
}

