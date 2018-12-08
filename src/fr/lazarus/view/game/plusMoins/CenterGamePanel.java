package fr.lazarus.view.game.plusMoins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.ModeDePartie;
import fr.lazarus.model.Partie;

/**
 * Le panel central du jeu plus moins avec un tableau affichant les données de la partie 
 * @author Ben
 *
 */
public class CenterGamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7562434621456598977L;
	private Configuration config;
	private Partie partie;

	private JPanel jpLeft = new JPanel(), 
			jpCenter = new JPanel(),
            jpRight = new JPanel(),
			jpScrollable = new JPanel();

    private JScrollPane jScrollPane = new JScrollPane();

	private Font font = new Font("Sego UI",Font.PLAIN,24);

	private String[] tour = { "#", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19","20"};

	private LigneTableau ligneTableau = null;
	private List<LigneTableau> listLigneTableau = new ArrayList<LigneTableau>();

	private Container contentPane = this;

	/**
	 * constructeur
	 * @param config Configuration
	 * @param partie Partie
	 */
	public CenterGamePanel(Configuration config, Partie partie) {

		this.config = config;
		this.partie = partie;

		initPanel();
	}

    /**
     * intialise le Panel
     */
	public void initPanel(){

		jpLeft.setLayout(new GridLayout(config.getTourPlusMoins()+1, 1, 10, 5));

		jpCenter.setLayout(new GridLayout(config.getTourPlusMoins()+1, 1, 5, 5));

        jpRight.setLayout(new GridLayout(config.getTourPlusMoins()+1, 1, 10, 5));

		//-- Je rempli le GridLayout
		for (int i=0; i<config.getTourPlusMoins()+1; i++) {
			//- La numérotation du nombre de tour
			JLabel jlTour = new JLabel(tour[i]);
			jlTour.setVisible(false);
			jlTour.setFont(font);
            jlTour.setPreferredSize(new Dimension(75, 60));
			jlTour.setHorizontalAlignment(SwingConstants.CENTER);
			jlTour.setVerticalAlignment(SwingConstants.CENTER);
			jpLeft.add(jlTour);

			//-- Les champs de saisie
			JTextField jtfProps = new JTextField();
			if (i > 0) {
				jtfProps.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			jtfProps.setVisible(false);
			jtfProps.setEditable(false);
			jtfProps.setBackground(Color.WHITE);
			jtfProps.setPreferredSize(new Dimension(250, 60));
			jtfProps.setFont(font);
			jtfProps.setHorizontalAlignment(JLabel.CENTER);
			jpCenter.add(jtfProps);

			//- Les indications du Maitre du jeux
			JLabel jlIndic= new JLabel();
			jlIndic.setVisible(false);
			jlIndic.setFont(font);
            jlIndic.setPreferredSize(new Dimension(250, 60));
			jlIndic.setHorizontalAlignment(JLabel.CENTER);
			jlIndic.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			jpRight.add(jlIndic);

			ligneTableau = new LigneTableau(jlTour, jtfProps, jlIndic);
			listLigneTableau.add(ligneTableau);
		}

		listLigneTableau.get(0).getJtfProps().setText("Proposition");
		listLigneTableau.get(0).getJlIndic().setText("Indice");
		if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
			listLigneTableau.get(1).getJtfProps().setText(partie.getProposition());;
		}

		setVisibleLine(0);

        jpScrollable.setLayout(new BorderLayout());
        jpScrollable.add(jpLeft, BorderLayout.WEST);
        jpScrollable.add(jpCenter, BorderLayout.CENTER);
        jpScrollable.add(jpRight, BorderLayout.EAST);
        jScrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
        jScrollPane.setViewportView(jpScrollable);
        jScrollPane.setPreferredSize(new Dimension(650, 700));
        this.add(jScrollPane, BorderLayout.CENTER);
	}

	/**
	 * rend les lignes visibles à chaque tour.
	 * @param index int
	 */
	public void setVisibleLine(int index) {
		listLigneTableau.get(index).getJlTour().setVisible(true);
		listLigneTableau.get(index).getJlIndic().setVisible(true);
		listLigneTableau.get(index).getJtfProps().setVisible(true);
		if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
			listLigneTableau.get(index+1).getJlTour().setVisible(true);
			listLigneTableau.get(index+1).getJtfProps().setVisible(true);	
		}
	}

	/**
	 * Ajoute les données de la partie dans le tableau puis rend les lignes visibles. 
	 * @param partie Partie
	 */
	public void addDataLine(Partie partie) {
		if (partie.getModeDePartie() == ModeDePartie.PLUS_CHAL) {
			listLigneTableau.get(partie.getTour()).getJtfProps().setText(partie.getProposition());
		}
		else if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
			listLigneTableau.get(partie.getTour()+1).getJtfProps().setText(partie.getProposition());
		}
		listLigneTableau.get(partie.getTour()).getJlIndic().setText(partie.getIndice());
		setVisibleLine(partie.getTour());
		this.repaint();
	}
}
