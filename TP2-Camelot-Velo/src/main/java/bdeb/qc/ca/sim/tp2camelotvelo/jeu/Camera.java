package bdeb.qc.ca.sim.tp2camelotvelo.jeu;

import javafx.geometry.Point2D;

public class Camera {
    //Attributs
    private Point2D positionCamera;

    //Constructeur
    public Camera(){
        this.positionCamera = new Point2D(0,0);
    }

    //Methodes

    public Point2D coordoEcran(Point2D positionMonde){
        return positionMonde.subtract(positionCamera);
    }

    public void update(double xCamelot, double largeurEcran){
        double x = xCamelot - (largeurEcran * 0.20);

        double y = 0;

        this.positionCamera = new Point2D(x,y);
    }

    public double getX(){
        return positionCamera.getX();
    }


    public double getY(){
        return positionCamera.getY();
    }
}
