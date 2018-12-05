package fr.lazarus.controller;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.Jeu;
import fr.lazarus.model.Partie;

/**
 * Controller, envoie les donndées au model.
 */
public class Controller {

    private Partie partie;
    private Configuration config;
    private Jeu jeu;

    /**
     * Constructeur
     * @param config
     * @param partie
     * @param jeu
     */
    public Controller(Configuration config, Partie partie,Jeu jeu) {
        this.config =config;
        this.partie = partie;
        this.jeu = jeu;
    }

    /**
     * Envoie la proposition au model
     * @param partie
     */
    public void sendProposition(Partie partie) {
        this.partie = partie;
        jeu.indiceOrdi(partie);
    }

    /**
     * Envoie l'indice au model
     * @param partie
     */
    public void sendIndice(Partie partie) {
        this.partie = partie;
        jeu.propOrdi(partie);
    }
}

