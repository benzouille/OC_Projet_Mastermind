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
	private ArrayList<Observateur> listObservateur = new ArrayList<Observateur>();
	//-- La partie
	private Partie partie;
	
	private Font font = new Font("Sego UI",Font.PLAIN,24);
	private Container contentPane;
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
		//TODO ajouter si c'est gagné ou perdu si perdu donner la solution donc ajouter au constructeur la partie pour le set solution
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
			jlSolution = new JLabel(partie.getSolution());
			jlSolution.setFont(font);
			jlText.setText(perdu);
			jpText.add(jlText);
			jpText.add(jlSolution);
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
