package fr.lazarus.model;

public interface ModelJoueur {

	public void initOrdinateur(Partie partie);
	public String propositionOrdinateur(Partie partie);
	public Partie endGame();
}
