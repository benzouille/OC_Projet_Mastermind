package fr.lazarus.model.mastermind;

/**
 * Les differentes couleurs possible du mastermind ainsi que la valeur associé à chaque couleur pour l' utilisation dans le model.
 * @author Ben
 *
 */
public enum TypeCouleur {

	BLEU(0, "bleu"), BRUN(1, "brun"), CYAN(2, "cyan"), JAUNE(3, "jaune"), ORANGE(4, "orange"), ROSE(5, "rose"), ROUGE(6, "rouge"), VERT(7, "vert"), 
	VERT_CLAIR(8, "vertClair"), VIOLET(9, "violet"), BLANC(10, "blanc"), NOIR(11, "noir");

	private final int valeur;
	private final String nom;

	private TypeCouleur(int valeur, String nom) {
		this.valeur = valeur;
		this.nom = nom;
	}

	public int getValeur() {return this.valeur;}
	
	public String getCouleur() {return this.nom;}
	
	public TypeCouleur getCouleur(int valeur) {
		TypeCouleur typeCouleur = null;
		switch(valeur) {
		case 0 :
			typeCouleur = TypeCouleur.BLEU;
		case 1 :
			typeCouleur = TypeCouleur.BRUN;
		case 2 :
			typeCouleur = TypeCouleur.CYAN;
		case 3 :
			typeCouleur = TypeCouleur.JAUNE;
		case 4 :
			typeCouleur = TypeCouleur.ORANGE;
		case 5 :
			typeCouleur = TypeCouleur.ROSE;
		case 6 :
			typeCouleur = TypeCouleur.ROUGE;
		case 7 :
			typeCouleur = TypeCouleur.VERT;
		case 8 :
			typeCouleur = TypeCouleur.VERT_CLAIR;
		case 9 :
			typeCouleur = TypeCouleur.VIOLET;
		case 10 :
			typeCouleur = TypeCouleur.BLANC;
		case 11 :
			typeCouleur = TypeCouleur.NOIR;
		}
		return typeCouleur;
	}
}