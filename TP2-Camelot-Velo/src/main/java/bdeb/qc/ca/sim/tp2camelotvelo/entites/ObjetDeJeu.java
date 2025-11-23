package bdeb.qc.ca.sim.tp2camelotvelo.entites;

import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class ObjetDeJeu {
    //Attributs
    protected Point2D position;
    protected Point2D velocite;
    protected Point2D acceleration;
   protected Point2D taille;

    //Constructeurs
    public ObjetDeJeu(Point2D position, double largeur, double hauteur) {
        this.position = position;
        this.velocite = new Point2D(0, 0);
        this.acceleration = new Point2D(0, 0);
        this.taille =new Point2D(largeur,hauteur);
    }


    //Methodes
    public void updatePhysique(double deltaTemps) {
        velocite = velocite.add(acceleration.multiply(deltaTemps));
        position = position.add(velocite.multiply(deltaTemps));
    }


    public abstract void update(double deltaTemps);

    public abstract void draw(GraphicsContext context, Camera camera);

    //Getters et Setters
    public double getHaut(){
        return position.getY();
    }

    public double getBas(){
        return position.getY() + taille.getY();
    }

    public Point2D getCentre(){
        return position.add(taille.multiply(1/2.0));
    }

    public double getGauche(){
        return position.getX();
    }

    public double getDroite(){
        return position.getX() + taille.getX();
    }

    public double getHauteur() {
        return taille.getY();
    }

    public double getLargeur() {
        return taille.getX();
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


}