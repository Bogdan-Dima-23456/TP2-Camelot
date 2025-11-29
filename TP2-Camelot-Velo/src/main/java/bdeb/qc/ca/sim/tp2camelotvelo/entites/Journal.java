package bdeb.qc.ca.sim.tp2camelotvelo.entites;

import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Journal extends ObjetDeJeu {
    // Constantes
    private static final double GRAVITE = 1500.0; // px/s²
    private static final double VITESSE_MAX = 1500.0; // px/s

    // Attributs
    private Image imageJournal;
    private double masse; // Masse en kg (entre 1 et 2)

    public Journal(Point2D position) {
        super(position, 52, 31); // Dimensions: 52x31 px
        this.masse = 1.0 + Math.random(); // Entre 1 et 2 kg

        // Chargement de l'image
        try {
            imageJournal = new Image("file:assets/journal.png");
        } catch (Exception e) {
            System.out.println("Image du journal introuvable: " + e.getMessage());
        }
    }


    public void lancerDepuisCamelot(Point2D posCamelot, Point2D velCamelot, boolean estZ, boolean estShift) {
        // Quantité de mouvement initiale selon la touche
        Point2D quantiteMouvement;
        if (estZ) {
            // Z = Lancer vers le haut
            quantiteMouvement = new Point2D(900, -900);
        } else {
            // X = Lancer vers l'avant
            quantiteMouvement = new Point2D(150, -1100);
        }

        // Si Shift enfoncé, multiplier par 1.5
        if (estShift) {
            quantiteMouvement = quantiteMouvement.multiply(1.5);
        }

        // Calcul de la vélocité initiale: v_initiale = v_camelot + p / masse
        Point2D velociteAjoutee = quantiteMouvement.multiply(1.0 / masse);
        this.velocite = velCamelot.add(velociteAjoutee);

        // Limiter la vitesse max à 1500 px/s
        limiterVitesse();
    }


    private void limiterVitesse() {
        double magnitude = velocite.magnitude();
        if (magnitude > VITESSE_MAX) {
            velocite = velocite.multiply(VITESSE_MAX / magnitude);
        }
    }

    @Override
    public void update(double deltaTemps) {
        // Accélération due à la gravité
        this.acceleration = new Point2D(0, GRAVITE);

        // Mise à jour de la physique
        super.updatePhysique(deltaTemps);
    }

    @Override
    public void draw(GraphicsContext gc, Camera camera) {
        if (imageJournal == null) return;

        // Conversion position monde vers écran
        Point2D posEcran = camera.coordoEcran(position);
        gc.drawImage(imageJournal, posEcran.getX(), posEcran.getY(), taille.getX(), taille.getY());
    }
}
