package bdeb.qc.ca.sim.tp2camelotvelo.entites;

import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Classe pour les particules chargées qui génèrent un champ électrique
// Les journaux sont affectés par ce champ
public class ParticuleChargee extends ObjetDeJeu {
    // Charge électrique en Coulombs
    private static final double CHARGE = 900.0;
    
    // Rayon de la particule en pixels
    private static final double RAYON = 10.0;

    // Couleur de la particule (pour le rendu)
    private Color couleur;

    // Constructeur - crée une particule à une position fixe
    public ParticuleChargee(Point2D position) {
        super(position, RAYON * 2, RAYON * 2);
        
        // couleur random vive (HSB, teinte 0-360)
    }

    @Override
    public void update(double deltaTemps) {
        // statique, bouge pas
    }

    @Override
    public void draw(GraphicsContext gc, Camera camera) {
        // convertir pos monde -> écran
        // dessiner cercle rempli couleur, taille = rayon*2
    }

    // Getter pour récupérer la charge
    public double getCharge() {
        return CHARGE;
    }
}
