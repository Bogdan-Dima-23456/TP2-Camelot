package bdeb.qc.ca.sim.tp2camelotvelo.entites;

import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Camera;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class Maison {

    //Attributs
    private int adresse;
    private double positionX;
    private ArrayList<Fenetre> fenetres;
    private BoiteAuxLettres boiteAuxLettres;
    private boolean estAbonnee;
    private Image imagePote;


    // Constructeur

    public Maison(int adresse, double positionX, boolean estAbonnee, double hauteurEcran){
        this.adresse = adresse;
        this.positionX = positionX;
        this.estAbonnee = estAbonnee;
        this.fenetres = new ArrayList<>();

        //Chargement de l'image de la porte
        try{
            imagePote = new Image("file:assets/porte.png");

        } catch (Exception e){
            System.out.println("Erreur lors du chargement de l'image de la porte");
        }

        genererElementsMaisons(hauteurEcran);

    }

    public void genererElementsMaisons(double hauteurEcran){


        //Création de la boite aux lettres (position aléatoire en y entre 20% et 70% de l'hauteur de l'écran)
        //position en x +200px a droite de la coordonée de la maison

        double yMin = hauteurEcran * 0.2;
        double yMax = hauteurEcran *0.7;
        double yBoite = yMin + Math.random() * (yMax - yMin);

        //position en x
        double xBoite = positionX + 200;

        this.boiteAuxLettres = new BoiteAuxLettres(new Point2D(xBoite, yBoite), estAbonnee);

        //Génération des fenetres

        double yFenetre = 50; //50px en haut de l'ecran

        int nombreFenetre = (int) (Math.random() * 3); //nombre entre 1 et 3

        if(nombreFenetre >= 1){
            fenetres.add(new Fenetre(new Point2D(positionX + 300 ,yFenetre)));

        }

        if (nombreFenetre==2) {
            fenetres.add(new Fenetre(new Point2D(positionX + 600, yFenetre)));

        }


    }

    public void draw(GraphicsContext context, Camera camera) {
        //Position de la porte
            double hauteurPorte = 200;
            Point2D posPorteMonde = new Point2D(positionX, 580 - hauteurPorte);
            Point2D posPorteEcran = camera.coordoEcran(posPorteMonde);


        //Dessin porte
        context.setFill(Color.SADDLEBROWN);
        context.fillRect(posPorteEcran.getX(), posPorteEcran.getY(), 100, hauteurPorte);

        //Dessin adresses
        context.setFill(Color.GOLD);
        context.setFont(new Font("Arial", 30));
        context.fillText(String.valueOf(adresse), posPorteEcran.getX() + 20, posPorteEcran.getY() + 50);

        //Dessin boite aux lettres
        boiteAuxLettres.draw(context, camera);

        //dessin fenetre
        for (Fenetre f : fenetres) {
            f.draw(context, camera);
        }
    }

    //Getters et setters

    public double getPositionX() {
        return positionX;
    }

    public ArrayList<Fenetre> getFenetres() {
        return fenetres;
    }

    public BoiteAuxLettres getBoiteAuxLettres() {
        return boiteAuxLettres;
    }

    public boolean isEstAbonnee() {
        return estAbonnee;
    }
}
