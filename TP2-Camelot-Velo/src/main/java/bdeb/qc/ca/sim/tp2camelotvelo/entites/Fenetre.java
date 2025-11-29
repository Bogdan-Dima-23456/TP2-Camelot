package bdeb.qc.ca.sim.tp2camelotvelo.entites;

import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Fenetre extends ObjetDeJeu{

    //Attributs
    private Image imageDeBase;
    private Image imageBriseeVerte;
    private Image imageBriseeRouge;
    private Image imageActuelle;

    //Etat de la fenêtre
    private boolean estCassee;

    //Constructeur
    public Fenetre(Point2D position){
        super(position, 159, 130);
        estCassee= false;

        //chargement des images
        try {
            imageDeBase = new Image("file:assets/fenetre.png");
            imageBriseeRouge = new Image("file:assets/fenetre-brisee-rouge.png");
            imageBriseeVerte = new Image("file:assets/fenetre-brisee-vert.png");
            imageActuelle = imageDeBase;

        }catch (Exception e){
            System.out.println();

        }


        //Methodes


    }
    public boolean verifierCollisions(Journal journal){
        return super.verifierCollision(journal);
    }

    public void changerCouleur(String couleur){
        if(couleur.equals("vert")){
            imageActuelle = imageBriseeVerte;
        } else if (couleur.equals("rouge")) {
            imageActuelle = imageBriseeRouge;

        }
    }

    @Override
    public void update(double deltaTemps){
        //Vide car la fenetre ne bouge pas
    }


    @Override
    public void draw(GraphicsContext context, Camera camera){
        if(imageActuelle==null) return;

        Point2D positionEcran = camera.coordoEcran(position);
        context.drawImage(imageActuelle, positionEcran.getX(), positionEcran.getY(), taille.getX(), taille.getY());
    }


    public boolean isEstCassee(){
        return estCassee;
    }

    public void setEstCassee(boolean estCassee) {
        this.estCassee = estCassee;
    }
}
