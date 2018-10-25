package fr.lazarus.view.game.plusMoins;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class LigneTableau {

    private JLabel jlTour;
    private JTextField jtfProps;
    private JLabel jlIndic;

    /**
     *
     */
    public LigneTableau(JLabel jlTour, JTextField jtfProps, JLabel jlIndic) {
        this.jlTour = jlTour;
        this.jtfProps = jtfProps;
        this.jlIndic = jlIndic;
    }

    //-- GETTER ET SETTER
    public JLabel getJlTour() {
        return jlTour;
    }

    public void setJlTour(JLabel jlTour) {
        this.jlTour = jlTour;
    }

    public JTextField getJtfProps() {
        return jtfProps;
    }

    public void setJtfProps(JTextField jtfProps) {
        this.jtfProps = jtfProps;
    }

    public JLabel getJlIndic() {
        return jlIndic;
    }

    public void setJlIndic(JLabel jlIndic) {
        this.jlIndic = jlIndic;
    }
}