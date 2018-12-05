package fr.lazarus.observer;

/**
 * Interface permetant à l'objet d'être observé
 */
public interface Observable {
	public void addObservateur(Observateur obs);
	public void updateObservateur();
	public void delObservateur();
}