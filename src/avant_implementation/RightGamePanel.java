package avant_implementation;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.lazarus.model.mastermind.Balle;
import fr.lazarus.model.mastermind.TypeCouleur;


public class RightGamePanel extends JPanel {

	private Container contentPane = this;
	private Dimension size = new Dimension(125, 700);
	
	private JLabel jLabel;
	
	private JLabel jlBleu, jlBrun, jlCyan, jlJaune, jlOrange, jlRose, jlRouge, jlVert, jlVertClair, jlViolet;
	private JLabel label[] = {jlBleu, jlBrun, jlCyan, jlJaune, jlOrange, jlRose, jlRouge, jlVert, jlVertClair, jlViolet};

	private Balle bleu = new Balle(TypeCouleur.BLEU),
			brun = new Balle(TypeCouleur.BRUN),
			cyan = new Balle(TypeCouleur.CYAN),
			jaune = new Balle(TypeCouleur.JAUNE),
			orange = new Balle(TypeCouleur.ORANGE),
			rose = new Balle(TypeCouleur.ROSE),
			rouge = new Balle(TypeCouleur.ROUGE),
			vert = new Balle(TypeCouleur.VERT),
			vertClair = new Balle(TypeCouleur.VERT_CLAIR),
			violet = new Balle(TypeCouleur.VIOLET);

	private Balle balles[] = {bleu, brun, cyan, jaune, orange, rose, rouge, vert, vertClair, violet};
	
	public RightGamePanel(JLabel jLabel) {
		
		this.jLabel = jLabel;
		initBouton();
	}
	
	public void initBouton() {

		for (int i = 0; i < label.length; i++) {
			label[i] = new JLabel(balles[i].getImageIcon());
			label[i].setBackground(Color.WHITE);
			label[i].setPreferredSize(new Dimension(60, 60));
			label[i].addMouseListener(new BalleBouton(balles[i], label[i], jLabel));
			this.add(label[i]);
		}
	}
}
