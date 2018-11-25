package fr.lazarus.view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.lazarus.model.ModeDePartie;
import fr.lazarus.model.mastermind.Balle;
import fr.lazarus.model.mastermind.TypeCouleur;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lazarus.model.Partie;
import fr.lazarus.observer.Observable;
import fr.lazarus.observer.Observateur;

/**
 * Popup s'ouvrant à la fin de la partie et propose de rejouer, quitter, retourner au menu principal.
 */
public class PopUpFinPartie extends JDialog implements Observable{

	private static final long serialVersionUID = 1L;
	//-- Les logs
	private static final Logger logger = LogManager.getLogger();
	//-- L'observateur
	private ArrayList<Observateur> listObservateur = new ArrayList<>();
	//-- La partie
	private Partie partie;

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
	
	private Font font = new Font("Sego UI",Font.PLAIN,24);
	private Container contentPane;
	private JPanel jpSolution;
	private JLabel jlText, jlSolution;
	private String gagne = "Bravo vous avez gagné !", 
	perdu = "Dommage vous avez perdu. La solution était : ",
	title, choixFinJeu;
	private JButton nouvJeu, menuPrinc, quitter;

	public PopUpFinPartie(JFrame parent, String title, boolean modal,Partie partie, Observateur obs) {
		this.addObservateur(obs);
		this.setSize(870, 140);
		this.setModal(modal);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.title = title;
		this.partie = partie;
		this.initComponent();
		this.setVisible(true);
	}

	public void initComponent() {
		contentPane = this.getContentPane();

		nouvJeu = new JButton("Recommencer");
		nouvJeu.addActionListener(new RejouerListener());
		menuPrinc = new JButton("Menu principal");
		menuPrinc.addActionListener(new MenuPrincipalListener());
		quitter = new JButton("Quitter");
		quitter.addActionListener(new QuitterListener());

		JPanel jpText = new JPanel();
		jpText.setBackground(Color.WHITE);
		jlText = new JLabel();
		jlText.setFont(font);
		if (title == "Perdu" ) {
			if(partie.getModeDePartie().equals(ModeDePartie.PLUS_CHAL) || partie.getModeDePartie().equals(ModeDePartie.PLUS_DEF)) {
                jlSolution = new JLabel(partie.getSolution());
                jlSolution.setFont(font);
                jlText.setText(perdu);
                jpText.add(jlText);
                jpText.add(jlSolution);
            }
            else{
                jpSolution = new JPanel();
                jpSolution.setBackground(Color.white);
                stringToImage(partie.getSolution(), jpSolution, balles);
                jlText.setText(perdu);
                jpText.add(jlText);
                jpText.add(jpSolution);
            }


		}
		else {
			jlText.setText(gagne);
			jpText.add(jlText);
		}
		//-- Ajout les boutons
		JPanel jpButton = new JPanel();

		jpButton.setBackground(Color.white);
		jpButton.add(nouvJeu);
		jpButton.add(menuPrinc);
		jpButton.add(quitter);

		//-- Ajout de tout les composants au content
		contentPane.add(jpText, BorderLayout.CENTER);
		contentPane.add(jpButton, BorderLayout.SOUTH);
	}

	/**
	 * Action listener, envoie au MainFrame choixFinJeu : "nouvellePartie" par le biais de l'observateur.
	 * @author Ben
	 *
	 */
	class RejouerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			choixFinJeu = "nouvellePartie";
			logger.trace("choix : rejouer");
			updateObservateur();
		}
	}

	/**
	 * Action listener, envoie au MainFrame choixFinJeu : "menuPrincipal" par le biais de l'observateur.
	 * @author Ben
	 *
	 */
	class MenuPrincipalListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			choixFinJeu = "menuPrincipal";
			logger.trace("choix : menu principal");
			updateObservateur();
		}
	}

	/**
	 * Action listener, quitte l'application.
	 * @author Ben
	 *
	 */
	class QuitterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			logger.trace("choix : quitter");
			System.exit(0);
		}
	}

    /**
     * transforme les charactères d'un string en suite d'images inserés dans un JPanel.
     * @param str String à convertir
     * @param jPanel panel dans lequel y ajouter les images
     * @param balles le tableau d'objet balle pour aller y chercher les images
     */
    public void stringToImage(String str, JPanel jPanel, Balle[] balles){
        for(int i = 0; i<str.length(); i++){
            JLabel jLabel = new JLabel(balles[Character.getNumericValue(str.charAt(i))].getImageIconMoy());
            jPanel.add(jLabel);
        }
        jPanel.revalidate();
    }

	/**
	 * Ajout des observateurs
	 */
	public void addObservateur(Observateur obs) {
		listObservateur.add(obs);
		this.updateObservateur();
	}

	/**
	 * Renvoie aux observateurs le choix du pop up
	 */
	public void updateObservateur() {
		for(Observateur obs : listObservateur)
			obs.update(choixFinJeu);
	}

	public void delObservateur() {}
}
