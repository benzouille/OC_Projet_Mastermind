package fr.lazarus.view.game.plusMoins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import fr.lazarus.controller.Controller;
import fr.lazarus.model.Configuration;
import fr.lazarus.model.ModeDeJeu;
import fr.lazarus.model.ModeDePartie;
import fr.lazarus.model.Partie;

/**
 * Panel de jeu du plus moins avec le centerGamePanel au centre et les differents boutons et labels au sud neccessaire au le jeu.  
 * @author Ben
 *
 */
public class GamePanelPlusMoins extends JPanel {

	private Configuration config;
	private ModeDeJeu modeDeJeu;
	private Partie partie;
	private Controller controller;

	private Container contentPane = this;

	//-- Elements du panel SOUTH
	private JPanel jpSouth, jpGauche, jpGaucheHaut, jpGaucheBas, jpCentre, jpCentreHaut, jpCentreBas, jpDroit, jpDroitHaut, jpDroitBas;
	private JLabel jlTitreSolution, jlSolution, jlTitreProposition, jlTitreIndice;
	private JTextField jtfProposition;
	private JButton okButton;

	//-- Elements du panel NORTH
	private JPanel jpTop = new JPanel(), jpRight = new JPanel();
	private JLabel jlModeDeJeu = new JLabel();

	//-- panel Center
	private CenterGamePanel centerGamePanel;

	private Font font = new Font("Sego UI",Font.PLAIN,24), fontTitre = new Font("Sego UI",Font.PLAIN,40);

	private boolean devMode;
	private boolean dataIsOk;

	/**
	 * Constructeur
	 */
	public GamePanelPlusMoins(Configuration config, ModeDeJeu modeDeJeu, Partie partie, Controller controller) {
		this.partie = partie;
		this.modeDeJeu = modeDeJeu;
		this.config = config;
		this.controller = controller;
		devMode = config.isDevModEnJeu();
		this.setSize(800, 1000);
		this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 15, true));

		jpTop.setPreferredSize(new Dimension(750, 90));
		jpRight.setPreferredSize(new Dimension(125, 700));
		
		initPanelSouth();

		if (partie.getModeDePartie() == ModeDePartie.PLUS_CHAL) {
			jlModeDeJeu.setText("Mode challenger");
			jlModeDeJeu.setForeground(Color.RED);
		}
		else if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
			jlModeDeJeu.setText("Mode défenseur");
			jlModeDeJeu.setForeground(Color.BLUE);
		}
		jlModeDeJeu.setFont(fontTitre);
		jpTop.add(jlModeDeJeu);

		contentPane.add(jpTop, BorderLayout.NORTH);
		
		centerGamePanel = new CenterGamePanel(config, partie);
		centerGamePanel.add(jpRight, BorderLayout.EAST);
		contentPane.add(centerGamePanel, BorderLayout.CENTER);
		
		contentPane.add(jpSouth, BorderLayout.SOUTH);
		
		if(partie.isActif() == false) {
			stopTurn();
		}
	}

	/**
	 * Panel sud avec les entrées pour les propositions en mode challanger et les indices en mode defenseur
	 */
	public void initPanelSouth() {

		jpSouth = new JPanel();
		jpSouth.setPreferredSize(new Dimension(750,150));
		jpSouth.setLayout(new BorderLayout());

		initPanelGauche();
		initPanelCentre();
		initPanelDroit();
	}

	/**
	 * Panel sud gauche avec les labels proposition et solution
	 */
	private void initPanelGauche() {
		jpGauche = new JPanel();
		jpGauche.setPreferredSize(new Dimension(125, 150));

		jpGaucheHaut = new JPanel();
		jpGaucheHaut.setPreferredSize(new Dimension(125, 75));

		jlTitreProposition = new JLabel("Proposition");
		jlTitreProposition.setFont(font);
		jlTitreProposition.setVerticalTextPosition(JLabel.CENTER);

		jlTitreIndice = new JLabel("Indice");
		jlTitreIndice.setFont(font);
		jlTitreIndice.setVerticalTextPosition(JLabel.CENTER);

		if(partie.getModeDePartie() == ModeDePartie.PLUS_CHAL) {
			jpGaucheHaut.add(jlTitreProposition);
		}
		else if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
			jpGaucheHaut.add(jlTitreIndice);
		}

		jpGaucheBas = new JPanel();
		jpGaucheBas.setPreferredSize(new Dimension(125, 75));

		jlTitreSolution = new JLabel("Solution");
		jlTitreSolution.setFont(font);

		if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF || devMode == true) {
			jpGaucheBas.add(jlTitreSolution);
			jpGaucheBas.setVisible(true);
		}

		jpGauche.setLayout(new BorderLayout());
		jpGauche.add(jpGaucheHaut, BorderLayout.NORTH);
		jpGauche.add(jpGaucheBas, BorderLayout.CENTER);

		jpSouth.add(jpGauche, BorderLayout.WEST);
	}

	/**
	 * Panel sud centre avec le jTextField proposition pour les entrées de l'indice et de la proposition, le label de la solution apparaissant sur le mode defenseur
	 * et quand le devMode est activé.
	 */
	private void initPanelCentre() {

		jpCentre = new JPanel();
		jpCentre.setLayout(new BorderLayout());
		jpCentre.setPreferredSize(new Dimension(300, 150));

		jpCentreHaut = new JPanel();
		jpCentreHaut.setPreferredSize(new Dimension(350, 75));

		jtfProposition = new JTextField();
		jtfProposition.setPreferredSize(new Dimension(350, 70));
		jtfProposition.setFont(font);
		jtfProposition.addKeyListener(new EnterListener());
		
		jpCentreHaut.add(jtfProposition);

		jpCentreBas = new JPanel();
		jpCentreBas.setPreferredSize(new Dimension(350, 75));
		jlSolution = new JLabel(partie.getSolution());
		jlSolution.setFont(font);
		jlSolution.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		jlSolution.setBackground(Color.WHITE);
		jlSolution.setPreferredSize(new Dimension(350, 70));
		jpCentreBas.add(jlSolution);
		jpCentreBas.setVisible(devMode);
		if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
			jpCentreBas.setVisible(true);
		}

		jpCentre.add(jpCentreHaut, BorderLayout.NORTH);
		jpCentre.add(jpCentreBas, BorderLayout.CENTER);

		jpSouth.add(jpCentre, BorderLayout.CENTER);
	}

	/**
	 * Panel avec le bouton de validation.
	 */
	private void initPanelDroit() {

		jpDroit = new JPanel();
		jpDroit.setPreferredSize(new Dimension(125, 150));

		jpDroitHaut = new JPanel();
		jpDroitHaut.setPreferredSize(new Dimension(125, 75));
		okButton = new JButton("Ok");
		okButton.setPreferredSize(new Dimension(125, 70));
		okButton.setHorizontalAlignment(JButton.CENTER);
		okButton.setVerticalAlignment(JButton.CENTER);
		okButton.addActionListener(new OkButtonListener());
		jpDroitHaut.add(okButton);

		jpDroitBas = new JPanel();
		jpDroitBas.setPreferredSize(new Dimension(125, 75));
		jpDroitBas.setVisible(false);

		jpDroit.setLayout(new BorderLayout());
		jpDroit.add(jpDroitHaut, BorderLayout.NORTH);
		jpDroit.add(jpDroitBas, BorderLayout.CENTER);

		jpSouth.add(jpDroit, BorderLayout.EAST);

	}
	
	/**
	 * Methode permettant avec le mode dev activé de mettre directement 
	 */
	public void devIndice() {
		jtfProposition.setText(partie.getIndice());
	}

	public void newTurn() {
		this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 15, true));
		jtfProposition.setEditable(true);
		okButton.setEnabled(true);
		this.revalidate();
	}

	public void stopTurn() {
		this.setBorder(BorderFactory.createLineBorder(Color.RED, 15, true));
		jtfProposition.setEditable(false);
		okButton.setEnabled(false);
		this.revalidate();
	}

	public void okPlusChal() {
		System.out.println("okPlusChal() de GamePanelPlusMoins");
		String proposition = jtfProposition.getText();

		if (isOkProposition(proposition)) {
		partie.setProposition(proposition);
		partie.setActif(false);
		controller.sendProposition(partie);
		centerGamePanel.addDataLine(partie);
		jtfProposition.setText("");
		if(modeDeJeu == ModeDeJeu.PLUS_DUEL) {
			stopTurn();
			}
		this.revalidate();
		}
	}

	public void okPlusDef() {
		System.out.println("okPlusDef() de GamePanelPlusMoins");
		String indice = jtfProposition.getText();
		if(isOkIndice(indice)){
		partie.setIndice(indice);
		partie.setActif(false);
		controller.sendIndice(partie);
		centerGamePanel.addDataLine(partie);
		if(config.isDevModEnJeu() == true && partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
			controller.sendProposition(partie);
			jtfProposition.setText(partie.getIndice());
		}
		else {
			jtfProposition.setText("");
		}
		if(modeDeJeu == ModeDeJeu.PLUS_DUEL) {
		stopTurn();
		}
		this.revalidate();
		}
	}
	
	public boolean isOkIndice(String indice) {
		dataIsOk = true;
		if (indice.length() != config.getCombiPlusMoins()) {
			JOptionPane.showMessageDialog(null, "Erreur ! \n Veuillez entrer une solution à "+ config.getCombiPlusMoins() +".", "ERREUR", JOptionPane.ERROR_MESSAGE);
			dataIsOk = false;
			jtfProposition.setText("");
		}
		else if (!Pattern.matches("^[+-=]{" + config.getCombiPlusMoins() + "}$", indice)){
			JOptionPane.showMessageDialog(null, "Erreur ! \n Veuillez n'entrer que les signes \"+\", \"-\" ou \"=\".", "ERREUR", JOptionPane.ERROR_MESSAGE);
			dataIsOk = false;
			jtfProposition.setText("");
		}
		else {
			dataIsOk = true;
		}
		return dataIsOk;
	}
	
	public Boolean isOkProposition(String proposition) {
		dataIsOk = true;

		if (proposition.length() != config.getCombiPlusMoins()) {
			JOptionPane.showMessageDialog(null, "Erreur ! \n Veuillez entrer une solution à "+ config.getCombiPlusMoins() +".", "ERREUR", JOptionPane.ERROR_MESSAGE);
			dataIsOk = false;
			jtfProposition.setText("");
		}
		else if (!Pattern.matches("^[0-9]{" + config.getCombiPlusMoins() + "}$", proposition)){
			JOptionPane.showMessageDialog(null, "Erreur ! \n Veuillez n'entrer que des chiffres.", "ERREUR", JOptionPane.ERROR_MESSAGE);
			dataIsOk = false;
			jtfProposition.setText("");	
		}
		else {
			dataIsOk = true;
		}
		return dataIsOk;
	}

	class OkButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (partie.getModeDePartie() == ModeDePartie.PLUS_CHAL) {
				okPlusChal();
			}
			else if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
				okPlusDef();	
			}
		}
	}
	
	class EnterListener implements KeyListener{

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode()==KeyEvent.VK_ENTER){
				okButton.doClick();
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
}