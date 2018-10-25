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


public class PopUpCombi extends JDialog implements Observable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1975827366674767282L;
	//-- Les logs
	private static final Logger logger = LogManager.getLogger();

	protected ArrayList<Observateur> listObservateur = new ArrayList<Observateur>();
	private JPanel content;
	private JLabel jlCombi;
	private Configuration config;
	private Partie partie;
	private JTextField jfCombi;
	private boolean isOkData;
	private int nbreChiffre, combi;
	private String solution;


	public PopUpCombi(JFrame parent, String title, boolean modal, Configuration config, Partie partie, Observateur obs) {
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

	public void initComponent() {

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
			}      
		});

		control.setBackground(Color.white);
		control.add(okBouton);
		control.add(cancelBouton);

		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);
	}

	private void acurateData() {

		isOkData = true;
		String comb = jfCombi.getText();
		
		if (comb.length() != nbreChiffre) {
			JOptionPane.showMessageDialog(null, "Erreur ! \n Veuillez entrer une combinaison à "+ nbreChiffre +".", "ERREUR", JOptionPane.ERROR_MESSAGE);
			isOkData = false;
			jfCombi.setText("");
		}
		else if (comb.matches("[0-9]*") == false) {
			JOptionPane.showMessageDialog(null, "Erreur ! \n Veuillez n'entrer que des chiffres.", "ERREUR", JOptionPane.ERROR_MESSAGE);
			isOkData = false;
			jfCombi.setText("");
		}
		else {
			solution = comb;
			logger.debug("la combinaison : "+combi);
			isOkData = true;
		}
	}


	class RunListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			acurateData();
			//- ferme la fenetre lorsque les données sont bonnes
			if (isOkData) {
				setVisible(false);
				partie.setSolution(solution);
				updateObservateur();
			}
			logger.debug("cliqué sur ok ");
		}

	}

	class RandomListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			logger.info("cliqué sur random ");
			jfCombi.setText(partie.random(nbreChiffre));
		}
	}
	
	public String convertTabIntToString(int [] tab) {
		String str= "";
		for(int i = 0; i<tab.length; i++) {
			str += Integer.toString(tab[i]);
		}
		return str;
		
	}

	public void addObservateur(Observateur obs) {
		listObservateur.add(obs);	
	}

	public void updateObservateur() {
		for(Observateur obs : listObservateur) {
			obs.update(partie);
		}
	}

	public void delObservateur() {}
}
