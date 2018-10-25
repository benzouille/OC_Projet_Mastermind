package avant_implementation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.ModeDePartie;
import fr.lazarus.model.Partie;
import fr.lazarus.view.game.plusMoins.LigneTableau;

public class CenterGamePanelMastermind extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7562434621456598977L;
	private Configuration config;
	private Partie partie;

	private JPanel jpLeft = new JPanel(), 
			jpCenter = new JPanel();

	private Font font = new Font("Sego UI",Font.PLAIN,24);

	private String[] tour = { "#", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19","20"};

	private LigneTableau ligneTableau = null;
	private List<LigneTableau> listLigneTableau = new ArrayList<LigneTableau>();

	private Container contentPane = this;

	public CenterGamePanelMastermind(Configuration config, Partie partie) {
		
		this.config = config;
		this.partie = partie;

		initPanel();
	}

	public void initPanel(){

		jpLeft.setLayout(new GridLayout(config.getTourPlusMoins()+1, 1, 10, 5));
		jpLeft.setPreferredSize(new Dimension(125, 700));
		//jpLeft.setBorder(BorderFactory.createLineBorder(Color.GREEN));

		jpCenter.setLayout(new GridLayout(config.getTourPlusMoins()+1, 2, 5, 5));
		jpCenter.setPreferredSize(new Dimension(500, 700));
		//jpCenter.setBorder(BorderFactory.createLineBorder(Color.PINK));

		//-- Je rempli le GridLayout
		for (int i=0; i<config.getTourPlusMoins()+1; i++) {
			//- La numérotation du nombre de tour
			JLabel jlTour = new JLabel(tour[i]);
			jlTour.setVisible(false);
			jlTour.setFont(font);
			jlTour.setHorizontalAlignment(JLabel.RIGHT);
			jpLeft.add(jlTour);

			//-- Les champs de saisie
			JTextField jtfProps = new JTextField();
			if (i > 0) {
				jtfProps.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			jtfProps.setVisible(false);
			jtfProps.setEditable(false);
			jtfProps.setBackground(Color.WHITE);
			jtfProps.setFont(font);
			jtfProps.setHorizontalAlignment(JLabel.CENTER);
			jpCenter.add(jtfProps);

			//- Les indications du Maitre du jeux
			JLabel jlIndic= new JLabel();
			jlIndic.setVisible(false);
			jlIndic.setFont(font);
			jlIndic.setHorizontalAlignment(JLabel.CENTER);
			jlIndic.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			jpCenter.add(jlIndic);

			ligneTableau = new LigneTableau(jlTour, jtfProps, jlIndic);
			listLigneTableau.add(ligneTableau);
		}

		listLigneTableau.get(0).getJtfProps().setText("Proposition");
		listLigneTableau.get(0).getJlIndic().setText("Indice");
		if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
			listLigneTableau.get(1).getJtfProps().setText(partie.getProposition());;
		}
		
		setVisibleLine(0);

		contentPane.add(jpLeft, BorderLayout.WEST);
		contentPane.add(jpCenter, BorderLayout.CENTER);
	}

	public void setVisibleLine(int index) {
		if (partie.getModeDePartie() == ModeDePartie.PLUS_CHAL) {
			listLigneTableau.get(index).getJlTour().setVisible(true);
			listLigneTableau.get(index).getJlIndic().setVisible(true);
			listLigneTableau.get(index).getJtfProps().setVisible(true);
		}
		else if (partie.getModeDePartie() == ModeDePartie.PLUS_DEF) {
			listLigneTableau.get(index).getJlTour().setVisible(true);
			listLigneTableau.get(index).getJlIndic().setVisible(true);
			listLigneTableau.get(index).getJtfProps().setVisible(true);
			listLigneTableau.get(index+1).getJlTour().setVisible(true);
			listLigneTableau.get(index+1).getJtfProps().setVisible(true);
		}
	}

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
