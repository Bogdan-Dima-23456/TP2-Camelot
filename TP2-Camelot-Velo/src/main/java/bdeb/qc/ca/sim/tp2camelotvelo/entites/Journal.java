package bdeb.qc.ca.sim.tp2camelotvelo.entites;

import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Camera;
import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Partie;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Journal extends ObjetDeJeu {
    // Constantes
    private static final double GRAVITE = 1500.0; // px/s²
    private static final double VITESSE_MAX = 1500.0; // px/s
    private static final double CHARGE_JOURNAL = 900.0; // Charge électrique en Coulombs

    // Attributs
    private Image imageJournal;
    private double masse; // Masse en kg (entre 1 et 2)
    private Partie partie;

    public Journal(Point2D position, double masse, Partie partie) {
        super(position, 52, 31); // Dimensions: 52x31 px
        this.masse = masse;
        this.partie = partie;

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
        Point2D accelerationGravite = new Point2D(0, GRAVITE);

        // Accélération totale commence avec la gravité
        Point2D accelerationTotale = accelerationGravite;

        // Si on est au niveau 2 ou plus, ajouter les forces électriques
        if (partie != null && partie.getNiveauActuel() >= 2) {
            // Position au centre du journal
            Point2D positionCentre = new Point2D(
                    position.getX() + taille.getX() / 2,
                    position.getY() + taille.getY() / 2
            );

            // Calculer le champ électrique à cette position
            Point2D champElectrique = partie.champElectrique(positionCentre);

            // Force électrique: F = E * q
            Point2D forceElectrique = champElectrique.multiply(CHARGE_JOURNAL);

            // Accélération électrique: a = F / m
            Point2D accelerationElectrique = forceElectrique.multiply(1.0 / masse);

            // Ajouter l'accélération électrique
            accelerationTotale = accelerationTotale.add(accelerationElectrique);
        }

        // Mise à jour de la vélocité
        velocite = velocite.add(accelerationTotale.multiply(deltaTemps));

        // Limiter la vitesse max à 1500 px/s
        limiterVitesse();

        // Mise à jour de la position
        position = position.add(velocite.multiply(deltaTemps));
    }

    @Override
    public void draw(GraphicsContext gc, Camera camera) {
        if (imageJournal == null) return;

        // Conversion position monde vers écran
        Point2D posEcran = camera.coordoEcran(position);
        gc.drawImage(imageJournal, posEcran.getX(), posEcran.getY(), taille.getX(), taille.getY());
    }
}
