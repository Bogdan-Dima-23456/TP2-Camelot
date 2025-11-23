package bdeb.qc.ca.sim.tp2camelotvelo.entites;

import bdeb.qc.ca.sim.tp2camelotvelo.Main;
import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Camera;
import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Input;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class Camelot extends ObjetDeJeu {

    //Attributs
    private Image image1;
    private Image image2;
    private double tempsTotal = 0;
    private double tempEcouleLance = 0;
    private boolean estAuSol;
    private final ArrayList<Journal> journauxLances = new ArrayList<>();

    public Camelot(Point2D positionDepart) {
        super(positionDepart, 172, 144);

        try {
            image1 = new Image("camelot1.png");
            image2 = new Image("camelot2.png");
        } catch (Exception e) {
            System.out.println("Images du camelot introuvables");
        }


        this.acceleration = new Point2D(0, 15000);

        this.velocite = new Point2D(400, 0);


    }

    @Override
    public void update(double deltaTemps) {
        //Mise à jour des compteurs

        tempsTotal += deltaTemps;

        if (tempEcouleLance > 0) {
            tempEcouleLance -= deltaTemps;
        }

        //Gestion des entrees

        boolean gauche = Input.isKeyPressed(KeyCode.LEFT);
        boolean droite = Input.isKeyPressed(KeyCode.RIGHT);
        boolean saut = Input.isKeyPressed(KeyCode.SPACE) || Input.isKeyPressed(KeyCode.UP);

        //Logique du mouvement

        double accX = 0;
        double vx = velocite.getX();

        if (gauche) {
            accX = -300;
        } else if (droite) {
            accX = 300;
        } else {
            if (vx < 400) {
                accX = 300;
            } else if (vx > 400) {
                accX = 300;
            }

        }


        //logique de la physique

        this.acceleration = new Point2D(accX, 1500);
        super.updatePhysique(deltaTemps);

        double hauteurSol = Main.HAUTEUR; // Ou le nom de ta classe principale

        if (getBas() >= hauteurSol) {

            setPosition(new Point2D(position.getX(), hauteurSol - taille.getY()));


            velocite = new Point2D(velocite.getX(), 0);


            estAuSol = true;
        } else {

            estAuSol = false;
        }


        if (saut && estAuSol) {
            velocite = new Point2D(velocite.getX(), -500); // Impulsion vers le haut
            estAuSol = false; // On décolle
        }
        //limit de vitesse
        double nouveauVx = velocite.getX();

        if (nouveauVx < 200) {
            nouveauVx = 200;
        }
        if (nouveauVx > 600) {
            nouveauVx = 600;
        }

        this.velocite = new Point2D(nouveauVx, velocite.getY());
    }


    @Override
    public void draw(GraphicsContext gc, Camera camera) {
        if (image1 == null || image2 == null) return;

        int index = (int) Math.floor(tempsTotal * 4) % 2;
        Image imageCourante = (index == 0) ? image1 : image2;

        // Conversion position monde vers écran
        Point2D posEcran = camera.coordoEcran(position);
        gc.drawImage(imageCourante, posEcran.getX(), posEcran.getY(), taille.getX(), taille.getY());
    }

}


