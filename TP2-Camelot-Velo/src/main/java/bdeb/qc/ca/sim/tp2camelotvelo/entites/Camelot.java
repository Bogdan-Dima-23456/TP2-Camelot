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
    private int journauxRestants = 0; // Nombre de journaux restants dans l'inventaire

    public Camelot(Point2D positionDepart) {
        super(positionDepart, 172, 144);

        try {
            image1 = new Image("camelot1.png");
            image2 = new Image("camelot2.png");
        } catch (Exception e) {
            System.out.println("Images du camelot introuvables");
        }


        this.acceleration = new Point2D(0, 1500);

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
                accX = -300;
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

        // Gestion du lancer de journal
        boolean estShift = Input.isKeyPressed(KeyCode.SHIFT);
        Journal journalLance = null;

        if (Input.isKeyPressed(KeyCode.Z)) {
            // Lancer vers le haut
            journalLance = lancerJournal(true, estShift);
        } else if (Input.isKeyPressed(KeyCode.X)) {
            // Lancer vers l'avant
            journalLance = lancerJournal(false, estShift);
        }

        // Si un journal a été lancé, l'ajouter à la liste
        if (journalLance != null) {
            journauxLances.add(journalLance);
        }
    }


    /**
     * Définit le nombre de journaux restants dans l'inventaire
     * @param journauxRestants nombre de journaux restants
     */
    public void setJournauxRestants(int journauxRestants) {
        this.journauxRestants = journauxRestants;
    }

    /**
     * Retourne le nombre de journaux restants dans l'inventaire
     * @return nombre de journaux restants
     */
    public int getJournauxRestants() {
        return journauxRestants;
    }

    /**
     * Retourne la liste des journaux lancés
     * @return ArrayList des journaux lancés
     */
    public ArrayList<Journal> getJournauxLances() {
        return journauxLances;
    }

    /**
     * Lance un journal si possible
     * @param estZ true si c'est un lancer vers le haut (Z), false si vers l'avant (X)
     * @param estShift true si Shift est enfoncé (lancer plus fort)
     * @return le Journal lancé, ou null si le lancement n'est pas possible
     */
    public Journal lancerJournal(boolean estZ, boolean estShift) {
        // Vérifier le cooldown
        if (tempEcouleLance > 0) {
            return null;
        }

        // Vérifier s'il reste des journaux
        if (journauxRestants <= 0) {
            return null;
        }

        // Position au centre du camelot
        Point2D positionCentre = new Point2D(
                position.getX() + taille.getX() / 2 - 26, // 26 = moitié de la largeur du journal
                position.getY() + taille.getY() / 2 - 15.5 // 15.5 = moitié de la hauteur du journal
        );

        // Créer et lancer le journal
        Journal journal = new Journal(positionCentre);
        journal.lancerDepuisCamelot(position, velocite, estZ, estShift);

        // Définir le cooldown
        tempEcouleLance = 0.5; // 0.5 seconde de cooldown

        // Décrémenter le nombre de journaux restants
        journauxRestants--;

        return journal;
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


