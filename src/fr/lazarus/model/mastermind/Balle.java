package fr.lazarus.model.mastermind;

import javax.swing.ImageIcon;

/**
 * Classe liant le typeCouleur et les images de balles pour le mastermind
 */
public class Balle {
	
	private TypeCouleur typeCouleur;
	private ImageIcon imageIcon, imageIconSmall, imageIconMoy, imageIconNano;

    /**
     * Constructeur, génère les ImageIcon du TypeCouleur donné.
     * @param typeCouleur TypeCouleur
     */
	public Balle(TypeCouleur typeCouleur) {
		this.typeCouleur = typeCouleur;
		imageIcon = new ImageIcon("resources/images/mastermind/" + typeCouleur.getCouleur() + ".png");
		imageIconSmall = new ImageIcon("resources/images/mastermind/" + typeCouleur.getCouleur() + "Min.png");
		imageIconMoy = new ImageIcon("resources/images/mastermind/" + typeCouleur.getCouleur() + "Moy.png");
        imageIconNano = new ImageIcon("resources/images/mastermind/" + typeCouleur.getCouleur() + "Nano.png");
	}

    /**
     * Descriptif de l'objet
     * @return str String
     */
	public String toString() {
		String str = "couleur : " + getTypeCouleur().getCouleur() + " valeur :" + getTypeCouleur().getValeur();
		return str;
	}

	//GETTER

	public TypeCouleur getTypeCouleur() {return typeCouleur;}

	public ImageIcon getImageIcon() {return imageIcon;}

	public ImageIcon getImageIconSmall() {return imageIconSmall;}

	public ImageIcon getImageIconMoy() { return imageIconMoy; }

    public ImageIcon getImageIconNano() { return imageIconNano; }
}
