package bdeb.qc.ca.sim.tp2camelotvelo.entites;

import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class BoiteAuxLettres extends ObjetDeJeu{

    //Attributs
    private boolean estAbonnee;

    public BoiteAuxLettres(Point2D position, boolean estAbonnee) {
        super(position, 81, 76 );

        this.estAbonnee = estAbonnee;
    }

    @Override
    public void update(double deltaTemps) {

    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {

    }
}
