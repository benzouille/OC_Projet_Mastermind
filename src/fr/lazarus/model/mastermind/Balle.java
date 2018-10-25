package fr.lazarus.model.mastermind;

import javax.swing.ImageIcon;

public class Balle {
	
	private TypeCouleur typeCouleur;
	private ImageIcon imageIcon, imageIconSmall;
	
	public Balle(TypeCouleur typeCouleur) {
		this.typeCouleur = typeCouleur;
		imageIcon = new ImageIcon("resources/images/mastermind/" + typeCouleur.getCouleur() + ".png");
		imageIconSmall = new ImageIcon("resources/images/mastermind/" + typeCouleur.getCouleur() + "Min.png");	
	}
	
	public String toString() {
		String str = "couleur : " + getTypeCouleur().getCouleur() + " valeur :" + getTypeCouleur().getValeur();
		return str;
	}

	public TypeCouleur getTypeCouleur() {return typeCouleur;}
	public void setTypeCouleur(TypeCouleur typeCouleur) {this.typeCouleur = typeCouleur;}

	public ImageIcon getImageIcon() {return imageIcon;}
	public void setImageIcon(ImageIcon imageIcon) {this.imageIcon = imageIcon;}

	public ImageIcon getImageIconSmall() {return imageIconSmall;}
	public void setImageIconSmall(ImageIcon imageIconSmall) {this.imageIconSmall = imageIconSmall;}
}
