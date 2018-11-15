package fr.lazarus.view.game.mastermind;

import javax.swing.*;

public class LigneTableauMast {

    private JLabel jlTour;
    private JPanel jpProps;
    private JPanel jpIndic;

    /**
     *
     */
    public LigneTableauMast(JLabel jlTour, JPanel jpProps, JPanel jpIndic) {
        this.jlTour = jlTour;
        this.jpProps = jpProps;
        this.jpIndic = jpIndic;
    }

    //-- GETTER ET SETTER
    public JLabel getJlTour() {
        return jlTour;
    }

    public void setJlTour(JLabel jlTour) {
        this.jlTour = jlTour;
    }

    public JPanel getJpProps() {
        return jpProps;
    }

    public void setJpProps(JPanel jlProps) {
        this.jpProps = jlProps;
    }

    public JPanel getJpIndic() {
        return jpIndic;
    }

    public void setJpIndic(JPanel jlIndic) {
        this.jpIndic = jlIndic;
    }
}