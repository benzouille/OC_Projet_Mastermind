package fr.lazarus.model.mastermind;

import javax.swing.ImageIcon;

public class Balle {
	
	private TypeCouleur typeCouleur;
	private ImageIcon imageIcon, imageIconSmall, imageIconMoy;
	
	public Balle(TypeCouleur typeCouleur) {
		this.typeCouleur = typeCouleur;
		imageIcon = new ImageIcon("resources/images/mastermind/" + typeCouleur.getCouleur() + ".png");
		imageIconSmall = new ImageIcon("resources/images/mastermind/" + typeCouleur.getCouleur() + "Min.png");
		imageIconMoy = new ImageIcon("resources/images/mastermind/" + typeCouleur.getCouleur() + "Moy.png");
	}
	
	public String toString() {
		String str = "couleur : " + getTypeCouleur().getCouleur() + " valeur :" + getTypeCouleur().getValeur();
		return str;
	}

	public TypeCouleur getTypeCouleur() {return typeCouleur;}

	public ImageIcon getImageIcon() {return imageIcon;}

	public ImageIcon getImageIconSmall() {return imageIconSmall;}

	public ImageIcon getImageIconMoy() { return imageIconMoy; }
}
