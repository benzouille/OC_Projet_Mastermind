package fr.lazarus.observer;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.Jeu;
import fr.lazarus.model.Partie;

/**
 * Interface permetant à l'objet d'observer
 */
public interface Observateur {
	public void update(Configuration config);
	public void update(Partie partie);
	public void update(String choixFinJeu);
}
