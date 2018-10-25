package fr.lazarus.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lazarus.model.Configuration;
import fr.lazarus.observer.Observable;
import fr.lazarus.observer.Observateur;

/**
 * Pop up des configurations des jeux, le nombre de tours, taille de la combinaison, nombre de couleur.
 * permet d'activer le devMode si activé préalablement dans le fichier config.property 
 * @author Ben
 *
 */
public class PopUpCfg extends JDialog implements Observable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3752761523806608959L;
	//-- Les logs
	private static final Logger logger = LogManager.getLogger();
	//-- L'observateur
	protected ArrayList<Observateur> listObservateur = new ArrayList<Observateur>();
	private JCheckBox devMode;
	private Configuration config;
	private int tourPlusMoins, tourMast, combiPlusMoins, combiMast, couleurMast;
	private boolean devMod, devModEnJeu;

	/**
	 * constructeur avec parametres
	 * @param parent
	 * @param title
	 * @param modal
	 * @param config
	 * @param obs
	 */
	public PopUpCfg(JFrame parent, String title, boolean modal, Configuration config, Observateur obs) {
		super(parent, title, modal);
		
		this.devMod = config.isDevMod();
        this.devModEnJeu = config.isDevModEnJeu();
		this.tourPlusMoins = config.getTourPlusMoins();
		this.tourMast = config.getTourMast();
		this.combiPlusMoins = config.getCombiPlusMoins();
		this.combiMast = config.getCombiMast();
		this.couleurMast = config.getCouleurMast();
		this.config = config;
		
		this.addObservateur(obs);
		this.setSize(870, 340);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.initComponent();
		this.setVisible(true);
	}

	/**
	 * Initialisation des sliders et du JCheckBox
	 */
	public void initComponent() {

		//Le nombre de tour du plus moins
		JPanel panTourPlusMoins = new JPanel();
		panTourPlusMoins.setBackground(Color.white);
		panTourPlusMoins.setPreferredSize(new Dimension(270, 80));

		JSlider slideTourPlusMoins = new JSlider();
		slideTourPlusMoins.setBackground(Color.white);
		slideTourPlusMoins.setMaximum(20);
		slideTourPlusMoins.setMinimum(5);
		slideTourPlusMoins.setValue(tourPlusMoins);
		slideTourPlusMoins.setPaintTicks(true);
		slideTourPlusMoins.setPaintLabels(true);
		slideTourPlusMoins.setMinorTickSpacing(1);
		slideTourPlusMoins.setMajorTickSpacing(5);
		slideTourPlusMoins.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent event){ 
				tourPlusMoins = ((JSlider)event.getSource()).getValue();
				logger.trace("Nombre de tour du plus moins : " + ((JSlider)event.getSource()).getValue());
			}
		});

		panTourPlusMoins.setBorder(BorderFactory.createTitledBorder("Nombre de tour"));
		panTourPlusMoins.add(slideTourPlusMoins);

		//nombre de cases de la combinaison du plus moins
		JPanel panChiffrePlusMoins = new JPanel();
		panChiffrePlusMoins.setBackground(Color.white);
		panChiffrePlusMoins.setPreferredSize(new Dimension(270, 80));

		JSlider slideChiffrePlusMoins = new JSlider();
		slideChiffrePlusMoins.setBackground(Color.white);
		slideChiffrePlusMoins.setMaximum(10);
		slideChiffrePlusMoins.setMinimum(4);
		slideChiffrePlusMoins.setValue(combiPlusMoins);
		slideChiffrePlusMoins.setPaintTicks(true);
		slideChiffrePlusMoins.setPaintLabels(true);
		slideChiffrePlusMoins.setMinorTickSpacing(1);
		slideChiffrePlusMoins.setMajorTickSpacing(2);
		slideChiffrePlusMoins.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent event){
				combiPlusMoins = ((JSlider)event.getSource()).getValue();
				logger.trace("Nombre de chiffre du plus moins : " + ((JSlider)event.getSource()).getValue());
			}
		});

		panChiffrePlusMoins.setBorder(BorderFactory.createTitledBorder("nombre de cases de la combinaison"));
		panChiffrePlusMoins.add(slideChiffrePlusMoins);

		//Option plus moins
		JPanel panPlusMoins = new JPanel();
		panPlusMoins.setBackground(Color.white);
		panPlusMoins.setPreferredSize(new Dimension(850, 120));
		panPlusMoins.setBorder(BorderFactory.createTitledBorder("Option du plus moins"));
		panPlusMoins.add(panTourPlusMoins);
		panPlusMoins.add(panChiffrePlusMoins);

		//Le nombre de tour du mastermind
		JPanel panTourMast = new JPanel();
		panTourMast.setBackground(Color.white);
		panTourMast.setPreferredSize(new Dimension(270, 80));

		JSlider slideTourMast = new JSlider();
		slideTourMast.setBackground(Color.white);
		slideTourMast.setMaximum(20);
		slideTourMast.setMinimum(5);
		slideTourMast.setValue(tourMast);
		slideTourMast.setPaintTicks(true);
		slideTourMast.setPaintLabels(true);
		slideTourMast.setMinorTickSpacing(1);
		slideTourMast.setMajorTickSpacing(5);
		slideTourMast.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent event){
				tourMast = ((JSlider)event.getSource()).getValue();
				logger.trace("Nombre de tour du mastermind : " + ((JSlider)event.getSource()).getValue());
			}
		});

		panTourMast.setBorder(BorderFactory.createTitledBorder("Nombre de tour"));
		panTourMast.add(slideTourMast);

		//nombre de cases de la combinaison du mastermind
		JPanel panCasesMast = new JPanel();
		panCasesMast.setBackground(Color.white);
		panCasesMast.setPreferredSize(new Dimension(270, 80));

		JSlider slideCasesMast = new JSlider();
		slideCasesMast.setBackground(Color.white);
		slideCasesMast.setMaximum(10);
		slideCasesMast.setMinimum(4);
		slideCasesMast.setValue(combiMast);
		slideCasesMast.setPaintTicks(true);
		slideCasesMast.setPaintLabels(true);
		slideCasesMast.setMinorTickSpacing(1);
		slideCasesMast.setMajorTickSpacing(2);
		slideCasesMast.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent event){
				combiMast  = ((JSlider)event.getSource()).getValue();
				logger.trace("Nombre de chiffre du plus moins : " + ((JSlider)event.getSource()).getValue());
			}
		});

		panCasesMast.setBorder(BorderFactory.createTitledBorder("nombre de cases de la combinaison"));
		panCasesMast.add(slideCasesMast);

		//nombre de couleur disponible du mastermind
		JPanel panCouleurMast = new JPanel();
		panCouleurMast.setBackground(Color.white);
		panCouleurMast.setPreferredSize(new Dimension(270, 80));

		JSlider slideCouleurMast = new JSlider();
		slideCouleurMast.setBackground(Color.white);
		slideCouleurMast.setMaximum(10);
		slideCouleurMast.setMinimum(4);
		slideCouleurMast.setValue(couleurMast);
		slideCouleurMast.setPaintTicks(true);
		slideCouleurMast.setPaintLabels(true);
		slideCouleurMast.setMinorTickSpacing(1);
		slideCouleurMast.setMajorTickSpacing(2);
		slideCouleurMast.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent event){
				couleurMast = ((JSlider)event.getSource()).getValue();
				logger.trace("Nombre de couleurs du mastermind : " + ((JSlider)event.getSource()).getValue());
			}
		});

		panCouleurMast.setBorder(BorderFactory.createTitledBorder("nombre de couleurs"));
		panCouleurMast.add(slideCouleurMast);

		//Option mastermind
		JPanel panMast = new JPanel();
		panMast.setBackground(Color.white);
		panMast.setPreferredSize(new Dimension(850, 120));
		panMast.setBorder(BorderFactory.createTitledBorder("Option du mastermind"));
		panMast.add(panTourMast);
		panMast.add(panCasesMast);
		panMast.add(panCouleurMast);

		//Le Dev Mode
		JPanel panDev = new JPanel();
		panDev.setBackground(Color.white);
		panDev.setBorder(BorderFactory.createTitledBorder("Mode developpeur"));
		panDev.setPreferredSize(new Dimension(850, 60));
		devMode = new JCheckBox("Mode Developpeur");
		devMode.setSelected(devModEnJeu);
		panDev.add(devMode);

		//-- Ajout de tout les composants au content
		JPanel content = new JPanel();
		content.setBackground(Color.white);
		content.add(panPlusMoins);
		content.add(panMast);
		if(devMod) {
			content.add(panDev);
			this.setSize(870, 400);
		}

		JPanel control = new JPanel();
		JButton okBouton = new JButton("OK");

		okBouton.addActionListener(new RunListener());

		JButton cancelBouton = new JButton("Annuler");
		cancelBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}      
		});

		control.add(okBouton);
		control.add(cancelBouton);

		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);
	}

	/**
	 * Listener du bouton ok 
	 * @author Ben
	 *
	 */
	class RunListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			if(devMode.isSelected())
				devModEnJeu = true;
			else
				devModEnJeu = false;
			
			config.setDevModEnJeu(devModEnJeu);
			config.setTourPlusMoins(tourPlusMoins);
			config.setTourMast(tourMast);
			config.setCombiPlusMoins(combiPlusMoins);
			config.setCombiMast(combiMast);
			config.setCouleurMast(couleurMast);
			config.setCfg();
			logger.debug("configuration : "+ config.toString());
			updateObservateur();
		}
	}

	public int getTourPlusMoins() {return tourPlusMoins;}
	public void setTourPlusMoins(int tourPlusMoins) {this.tourPlusMoins = tourPlusMoins;}

	public int getTourMast() {return tourMast;}
	public void setTourMast(int tourMast) {this.tourMast = tourMast;}

	public int getCombiPlusMoins() {return combiPlusMoins;}
	public void setCombiPlusMoins(int combiPlusMoins) {this.combiPlusMoins = combiPlusMoins;}

	public int getCombiMast() {return combiMast;}
	public void setCombiMast(int combiMast) {this.combiMast = combiMast;}

	public int getCouleurMast() {return couleurMast;}
	public void setCouleurMast(int couleurMast) {this.couleurMast = couleurMast;}

	public boolean isDevMod() {return devMod;}
	public void setDevMod(boolean devMod) {this.devMod = devMod;}

	/**
	 * Ajout des observateurs
	 */
	public void addObservateur(Observateur obs) {
		listObservateur.add(obs);
		this.updateObservateur();
	}

	/**
	 * Renvoie aux observateurs l'objet configuration
	 */
	public void updateObservateur() {
		for(Observateur obs : listObservateur)
			obs.update(config);
	}

	public void delObservateur() {
		// TODO Auto-generated method stub
	}
}
