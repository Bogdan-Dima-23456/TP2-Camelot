package bdeb.qc.ca.sim.tp2camelotvelo.entites;

import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BoiteAuxLettres extends ObjetDeJeu{

    //Attributs
    private boolean estAbonnee;
    private Image imageDeBase;
    private Image imageVerte;
    private Image imageRouge;
    private Image imageActuelle;
    private boolean estTouchee;

    public BoiteAuxLettres(Point2D position, boolean estAbonnee) {
        super(position, 81, 76 );

        this.estAbonnee = estAbonnee;
        this.estTouchee = false;

        // Chargement des images
        try {
            imageDeBase = new Image("file:assets/boite-aux-lettres.png");
            imageVerte = new Image("file:assets/boite-aux-lettres-vert.png");
            imageRouge = new Image("file:assets/boite-aux-lettres-rouge.png");
            imageActuelle = imageDeBase;
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'image de la boîte aux lettres: " + e.getMessage());
        }
    }

    @Override
    public void update(double deltaTemps) {

    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        if (imageActuelle == null) return;

        // Conversion position monde vers écran
        Point2D posEcran = camera.coordoEcran(position);
        context.drawImage(imageActuelle, posEcran.getX(), posEcran.getY(), taille.getX(), taille.getY());
    }

    public void marquerTouchee() {
        estTouchee = true;
        if (estAbonnee) {
            imageActuelle = imageVerte;
        } else {
            imageActuelle = imageRouge;
        }
    }

    public boolean isEstAbonnee() {
        return estAbonnee;
    }
}
