package fr.lazarus.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.lazarus.model.Configuration;
import fr.lazarus.model.Jeu;
import fr.lazarus.model.ModeDeJeu;
import fr.lazarus.model.Partie;
import fr.lazarus.observer.Observateur;
import fr.lazarus.view.game.PanelJeu;
import fr.lazarus.view.game.plusMoins.PopUpCombiPlus;


/**
 * Fenetre principale racine de la vue appelant le model, le controller et la vue.
 * @author Ben
 *
 */
public class MainFrame extends JFrame implements Observateur {

    private static final long serialVersionUID = -4971770858535210983L;

    //-- Les logs
    private static final Logger logger = LogManager.getLogger();
    //-- Les differents panels
    private PanelAccueil accueil;
    //-- La configuration
    private Configuration config;
    //-- L'observateur
    private Observateur obs = this;
    //-- Le Jeu
    private Jeu jeu;
    //-- La Vue
    private PanelJeu panelJeu;

    //-- Le Pop up pour choisir la combinaison
    @SuppressWarnings("unused")
    private PopUpCombiPlus popUpCombiPlus;
    //-- Les différents objets de notre IHM
    private JMenuBar bar = new JMenuBar();
    private JMenu fichier = new JMenu("Fichier"), nouveauJeu = new JMenu("Nouveau");
    private JMenuItem quitter = new JMenuItem("Quitter");

    private JMenu lePlusMoins = new JMenu("Le + -");
    private JMenuItem lePlusMoinsChal = new JMenuItem("Challenger"), lePlusMoinsDef = new JMenuItem("Défenseur"), lePlusMoinsDuel = new JMenuItem("Duel");

    private JMenu mastermind = new JMenu("Mastermind");
    private JMenuItem mastermindChal = new JMenuItem("Challenger"), mastermindDef = new JMenuItem("Défenseur"), mastermindDuel = new JMenuItem("Duel");

    private JMenu configuration = new JMenu("Configuration");
    private JMenuItem configMenuI = new JMenuItem("configuration");

    private JMenu aProp = new JMenu("A Propos");
    private JMenuItem aPropItem = new JMenuItem("A propos");

    private Dimension size = new Dimension (1710, 1070);
    private Container contentPane;

    /**
     * Constructeur par défaut qui permet d'initialiser la fenetre
     */
    public MainFrame() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Projet 3");
        this.setSize(size);
        config = new Configuration();

        initPanel();
        initMenu();
        logger.debug("Le contenu de la fenêtre a été initalisée");
        logger.debug("configuration : "+ config.toString());
    }

    /**
     * initialisation du panel avec le panel accueil
     */
    public void initPanel() {
        //-- Données
        accueil = new PanelAccueil(size);

        contentPane = this.getContentPane();
        contentPane.setBackground(Color.white);
        contentPane.add(accueil, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.size = new Dimension(this.getWidth(), this.getHeight());
    }


    /**
     * Méthode qui initialise la barre de menu et les differents boutons la composant ainsi que les actionlistener pour chaque JMenuItem
     */
    private void initMenu() {

        fichier.add(nouveauJeu);
        nouveauJeu.setMnemonic('n');

        //-- Le + -
        nouveauJeu.add(lePlusMoins);
        lePlusMoinsChal.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {

                jeu = new Jeu(ModeDeJeu.PLUS_CHAL, config, obs);
                logger.debug(jeu.getPartie1().getSolution());
                panelJeu = new PanelJeu(jeu, config, obs);
                contentPane.removeAll();
                contentPane.add(panelJeu, BorderLayout.CENTER);
                contentPane.revalidate();
            }});
        lePlusMoins.add(lePlusMoinsChal);

        lePlusMoinsDef.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                jeu = new Jeu(ModeDeJeu.PLUS_DEF, config, obs);
                panelJeu = new PanelJeu(jeu, config, obs);
                contentPane.removeAll();
                contentPane.add(panelJeu, BorderLayout.CENTER);
                contentPane.revalidate();
            }});
        lePlusMoins.add(lePlusMoinsDef);

        lePlusMoinsDuel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                jeu = new Jeu(ModeDeJeu.PLUS_DUEL, config, obs);
                panelJeu = new PanelJeu(jeu, config, obs);
                contentPane.removeAll();
                contentPane.add(panelJeu, BorderLayout.CENTER);
                contentPane.revalidate();
            }});
        lePlusMoins.add(lePlusMoinsDuel);

        lePlusMoins.setMnemonic('p');

        //-- Le mastermind
        nouveauJeu.add(mastermind);

        mastermindChal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                jeu = new Jeu(ModeDeJeu.MAST_CHAL, config, obs);
                panelJeu = new PanelJeu(jeu, config, obs);
                contentPane.removeAll();
                contentPane.add(panelJeu, BorderLayout.CENTER);
                contentPane.revalidate();
            }});
        mastermind.add(mastermindChal);

        mastermindDef.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jeu = new Jeu(ModeDeJeu.MAST_DEF, config, obs);
                panelJeu = new PanelJeu(jeu, config, obs);
                contentPane.removeAll();
                contentPane.add(panelJeu, BorderLayout.CENTER);
                contentPane.revalidate();
            }});
        mastermind.add(mastermindDef);

        mastermindDuel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jeu = new Jeu(ModeDeJeu.MAST_DUEL, config, obs);
                panelJeu = new PanelJeu(jeu, config, obs);
                contentPane.removeAll();
                contentPane.add(panelJeu, BorderLayout.CENTER);
                contentPane.revalidate();
            }});
        mastermind.add(mastermindDuel);

        mastermind.setMnemonic('m');

        fichier.addSeparator();

        //-- Quitter
        fichier.add(quitter);
        quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_MASK + KeyEvent.SHIFT_DOWN_MASK));
        //Pour quitter l'application
        quitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                System.exit(0);
            }
        });
        fichier.setMnemonic('f');

        configuration.add(configMenuI);
        configuration.setMnemonic('c');
        configMenuI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                @SuppressWarnings("unused")
                PopUpCfg ajout = new PopUpCfg(null, "Configuration", true, config , obs);
            }
        });

        aProp.add(aPropItem);
        aProp.setMnemonic('a');
        aPropItem.addActionListener(new ActionListener(){
            @SuppressWarnings("static-access")
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane jop = new JOptionPane();
                ImageIcon img = new ImageIcon("resources/images/banane.gif");
                String mess = "Crée par la Banane \n Amusez vous bien !\n";
                mess += "Pour toutes remarques ou suggestions concernant le jeu, contactez moi à :\n";
                mess += "\n benjamin@fa-tech.net";
                jop.showMessageDialog(null, mess, "À propos", JOptionPane.INFORMATION_MESSAGE, img);
            }
        });

        bar.add(fichier);
        bar.add(configuration);
        bar.add(aProp);

        this.setJMenuBar(bar);
    }

    /**
     * Methode de mise à jour de la configuration par popUpCfg
     */
    public void update(Configuration config) {
        this.config = config;
        logger.debug("configuration : "+ config.toString());
    }

    /**
     * Methode de mise à jour de la partie par le model
     */

    public void update(Partie partie) {
        if(partie.getNom() == jeu.getPartie1().getNom()) {
            System.out.println("methode update partie1 de TestSwitchPanel");
            jeu.setPartie1(partie);
            if(jeu.getModeDeJeu() == ModeDeJeu.PLUS_DUEL || jeu.getModeDeJeu() == ModeDeJeu.MAST_DUEL && panelJeu != null) {
                panelJeu.defTurn();
            }
        }
        else {
            System.out.println("methode update partie2 de TestSwitchPanel");
            jeu.setPartie2(partie);
            if(jeu.getModeDeJeu() == ModeDeJeu.PLUS_DUEL || jeu.getModeDeJeu() == ModeDeJeu.MAST_DUEL && panelJeu != null) {
                panelJeu.chalTurn();
            }
        }
    }

    /**
     * Vérification du choix de fin de partie si nouvelle partie ou menu principal
     */
    public void update(String choixFinJeu) {
        if 	(choixFinJeu == "nouvellePartie") {
            System.out.println("nouvelle partie");

            if (jeu.getModeDeJeu().equals(ModeDeJeu.PLUS_CHAL)) {
                lePlusMoinsChal.doClick();
            }
            else if (jeu.getModeDeJeu().equals(ModeDeJeu.PLUS_DEF)) {
                lePlusMoinsDef.doClick();
            }
            else if (jeu.getModeDeJeu().equals(ModeDeJeu.PLUS_DUEL)) {
                lePlusMoinsDuel.doClick();
            }
            else if (jeu.getModeDeJeu().equals(ModeDeJeu.MAST_CHAL)) {
                mastermindChal.doClick();
            }
            else if (jeu.getModeDeJeu().equals(ModeDeJeu.MAST_DEF)) {
                mastermindDef.doClick();
            }
            else {
                mastermindDuel.doClick();
            }
        }
        else if (choixFinJeu == "menuPrincipal"){
            contentPane.removeAll();
            contentPane.add(accueil, BorderLayout.CENTER);
            contentPane.repaint();
        }
    }
}
