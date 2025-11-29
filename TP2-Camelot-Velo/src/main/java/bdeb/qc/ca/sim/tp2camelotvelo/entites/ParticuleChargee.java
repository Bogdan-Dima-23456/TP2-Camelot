package bdeb.qc.ca.sim.tp2camelotvelo.entites;

import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Camera;
import bdeb.qc.ca.sim.tp2camelotvelo.jeu.UtilitairesDessins;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

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
    public ParticuleChargee(Point2D position) { // Source, AI
        super(position, RAYON * 2, RAYON * 2);
        
        double teinte = Math.random() * 360; // Angle entre 0 et 360 degrés
        this.couleur = Color.hsb(teinte, 1.0, 1.0);
    }

    @Override
    public void update(double deltaTemps) {
        // statique, bouge pas
    }

    @Override
    public void draw(GraphicsContext gc, Camera camera) {
        // convertir pos monde -> écran
        Point2D posEcran = camera.coordoEcran(position);
        
        // dessiner cercle rempli couleur, taille = rayon*2
        gc.setFill(couleur);
        gc.fillOval(posEcran.getX(), posEcran.getY(), RAYON * 2, RAYON * 2);
    }

    // Getter pour récupérer la charge
    public double getCharge() {
        return CHARGE;
    }

    // Calcule le champ électrique total à la position donnée
    public static Point2D champElectrique(List<ParticuleChargee> particules, Point2D positionMonde) {
        Point2D champTotal = Point2D.ZERO;
        double K = 8.99e9; // Constante de Coulomb (N⋅m²/C²)
        
        for (ParticuleChargee particule : particules) {
            Point2D direction = positionMonde.subtract(particule.getPosition());
            double distance = direction.magnitude();
            
            if (distance > 0.1) { // Éviter division par zéro
                double E = K * particule.getCharge() / (distance * distance);
                Point2D vecteurChamp = direction.normalize().multiply(E);
                champTotal = champTotal.add(vecteurChamp);
            }
        }
        
        return champTotal;
    }

    // Dessine la visualisation du champ électrique sur une grille
    public static void dessinerVisualisationChamp(
            List<ParticuleChargee> particules,
            double LARGEUR_NIVEAU,
            double HAUTEUR_ECRAN,
            double largeurEcran,
            Camera camera,
            GraphicsContext contexteGraphique) {
        
        for (double x = 0; x < LARGEUR_NIVEAU; x += 50) {
            for (double y = 0; y < HAUTEUR_ECRAN; y += 50) {
                var positionMonde = new Point2D(x, y);
                var positionEcran = camera.coordoEcran(positionMonde);
                
                // Seulement faire ça si la position (x, y) est visible dans l'écran
                if (positionEcran.getX() >= 0 && positionEcran.getX() < largeurEcran &&
                    positionEcran.getY() >= 0 && positionEcran.getY() < HAUTEUR_ECRAN) {
                    
                    Point2D force = champElectrique(particules, positionMonde);
                    
                    UtilitairesDessins.dessinerVecteurForce(
                            positionEcran,
                            force,
                            contexteGraphique
                    );
                }
            }
        }
    }
}
