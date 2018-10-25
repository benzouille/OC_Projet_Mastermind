package avant_implementation;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import fr.lazarus.model.mastermind.Balle;



public class BalleBouton implements MouseListener {

	private Balle balle;
	private JLabel jLabel, jlAffichage;
	
	public BalleBouton(Balle balle, JLabel jLabel, JLabel jlAffichage) {
		this.balle = balle;
		this.jLabel = jLabel;
		this.jlAffichage = jlAffichage;
		
	}

	public void smallSize() {
		jLabel.setIcon(balle.getImageIconSmall());
		jLabel.revalidate();
	}

	public void bigSize() {
		jLabel.setIcon(balle.getImageIcon());
		jLabel.revalidate();
	}

	public void mouseClicked(MouseEvent arg0) {
		JLabel jLabel = new JLabel(balle.getImageIcon());
		jlAffichage.add(jLabel);
		jlAffichage.revalidate();
	}

	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		smallSize();
	}

	public void mouseReleased(MouseEvent e) {
		bigSize();
	}	
}
