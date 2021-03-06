package fr.lazarus.view.game.plusMoins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.Partie;
import fr.lazarus.observer.Observable;
import fr.lazarus.observer.Observateur;

/**
 * JDialog Permetant de choisir la solution dans le mode defenseur
 */
public class PopUpCombiPlus extends JDialog implements Observable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1975827366674767282L;
	//-- Les logs
	private static final Logger logger = LogManager.getLogger();

	protected ArrayList<Observateur> listObservateur = new ArrayList<>();
	private JPanel content;
	private JLabel jlCombi;
	private Configuration config;
	private Partie partie;
	private JTextField jfCombi;
	private boolean isOkData;
	private int nbreChiffre;
	private String solution;
	private boolean annuler = false;

	/**
	 * Constructeur utilisant les bean Partie et Configuration
	 * @param parent JFrame
	 * @param title String
	 * @param modal boolean
	 * @param config Configuration
	 * @param partie Partie
	 * @param obs Observateur
	 */
	public PopUpCombiPlus(JFrame parent, String title, boolean modal, Configuration config, Partie partie, Observateur obs) {
		super(parent, title, modal);
		this.config =config;
		this.partie = partie;
		nbreChiffre = config.getCombiPlusMoins();
		this.addObservateur(obs);
		this.setSize(600, 160);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.initComponent();
		this.setVisible(true);
	}

	/**
	 * Intitiation du contenu du Panel
	 */
    @SuppressWarnings("Duplicates")
	private void initComponent() {

		//Choix de la combinaison
		jlCombi = new JLabel("Choisissez votre code à " + nbreChiffre + " chiffres :");
		jfCombi = new JTextField();
		jfCombi.setPreferredSize(new Dimension(210, 40));
		jfCombi.setFont(new Font("Lucida Console", Font.BOLD, 30));

		JButton randomButton = new JButton("Aléatoire");
		randomButton.addActionListener(new RandomListener());

		JPanel panCombi = new JPanel();
		panCombi.setBackground(Color.white);
		panCombi.setPreferredSize(new Dimension(590, 80));
		panCombi.setBorder(BorderFactory.createTitledBorder("Choix de la combinaison"));
		panCombi.add(jlCombi);
		panCombi.add(jfCombi);
		panCombi.add(randomButton);

		//-- Ajout de tout les composants au content
		content = new JPanel();
		content.setBackground(Color.white);
		content.add(panCombi);

		JPanel control = new JPanel();
		JButton okBouton = new JButton("OK");

		okBouton.addActionListener(new RunListener());

		JButton cancelBouton = new JButton("Annuler");
		cancelBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				annuler = true;
			}      
		});

		control.setBackground(Color.white);
		control.add(okBouton);
		control.add(cancelBouton);

		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);
	}

    /**
     * Verifie l'integrité des données
     */
	private void acurateData() {

		isOkData = true;
		String comb = jfCombi.getText();
		
		if (comb.length() != nbreChiffre) {
			JOptionPane.showMessageDialog(null, "Erreur ! \n Veuillez entrer une combinaison à "+ nbreChiffre +" chiffres.", "ERREUR", JOptionPane.ERROR_MESSAGE);
			isOkData = false;
			jfCombi.setText("");
		}
		else if (!comb.matches("[0-9]*")) {
			JOptionPane.showMessageDialog(null, "Erreur ! \n Veuillez n'entrer que des chiffres.", "ERREUR", JOptionPane.ERROR_MESSAGE);
			isOkData = false;
			jfCombi.setText("");
		}
		else {
			solution = comb;
			isOkData = true;
		}
	}

	//GETTER SETTER

	public boolean isAnnuler() { return annuler; }

	//NESTED CLASS

	/**
     * NestedClass du bouton "ok"
     */
	class RunListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			acurateData();
			//- ferme la fenetre lorsque les données sont bonnes
			if (isOkData) {
				setVisible(false);
				partie.setSolution(solution);
				updateObservateur();
			}
			logger.info("Solution : " + solution);
		}
	}

    /**
     * NestedClass du bouton "random", permet de choisir une combinaison aléatoire
     */
	class RandomListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			logger.info("cliqué sur random ");
			jfCombi.setText(partie.random(nbreChiffre));
		}
	}


	public void addObservateur(Observateur obs) {
		listObservateur.add(obs);	
	}

	public void updateObservateur() {
			for (Observateur obs : listObservateur) {
				obs.update(partie);
			}
		}

	public void delObservateur() {}
}
