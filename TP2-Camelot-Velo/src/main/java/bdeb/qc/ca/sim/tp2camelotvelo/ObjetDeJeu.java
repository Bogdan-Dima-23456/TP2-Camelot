package bdeb.qc.ca.sim.tp2camelotvelo;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class ObjetDeJeu {
    //Attributs
    protected Point2D position;
    protected Point2D velocite;
    protected Point2D acceleration;
    protected double largeur;
    protected double hauteur;

    //Constructeurs
    public ObjetDeJeu(Point2D position, double largeur, double hauteur) {
        this.position = position;
        this.velocite = new Point2D(0, 0);
        this.acceleration = new Point2D(0, 0);
        this.largeur = largeur;
        this.hauteur = hauteur;
    }


    //Methodes
    public void updatePhysique(double deltaTemps) {
        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));
    }


    public abstract void update(double deltaTemps);

    public abstract void dessiner(GraphicsContext context, Camera camera);

    //Getters et Setters
    public double getHaut(){
        return position.getY();
    }

    public double getBas(){
        return position.getY() + hauteur;
    }

    public double getGauche(){
        return position.getX();
    }

    public double getDroite(){
        return position.getX() + largeur;
    }

    public double getHauteur() {
        return hauteur;
    }

    public double getLargeur() {
        return largeur;
    }

    public void setVelocite(Point2D velocite) {
        this.velocite = velocite;
    }

    public Point2D getVelocite() {
        return velocite;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Point2D getCentre(){
        return position.add(largeur/2, hauteur/2);
    }
}