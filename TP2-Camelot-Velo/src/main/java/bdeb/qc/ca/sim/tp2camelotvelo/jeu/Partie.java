package bdeb.qc.ca.sim.tp2camelotvelo.jeu;

import bdeb.qc.ca.sim.tp2camelotvelo.entites.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Classe principale contenant toute la logique du jeu
public class Partie {
    // États du jeu
    public enum EtatJeu {
        CHARGEMENT,
        EN_COURS,
        FIN
    }

    // Constantes du jeu
    private static final int HAUTEUR_SOL = 580; // Position Y du sol
    public static final double GRAVITE = 1500.0; // Accélération de la gravité en px/s²

    // Dimensions de l'écran
    private final double largeurEcran;
    private final double hauteurEcran;

    // Variables d'état du jeu
    private int niveauActuel;
    private int argentTotal;
    private int journauxRestants;
    private EtatJeu etatJeu;
    private double tempsChargement; // Timer pour l'écran de chargement (3 secondes)
    private double tempsTotal; // Temps total écoulé depuis le début du jeu

    // Caméra
    private Camera camera;

    // Entités du jeu
    private Camelot camelot;
    private List<Journal> journaux;
    private List<Maison> maisons;
    private List<ParticuleChargee> particules;
    private List<Integer> adressesAbonnees;

    // Variables de débogage
    private boolean modeDebogageAffichage;
    private boolean modeVisualisationChamp;

    // Images pour le décor
    private Image imageBrique;
    private Image iconeJournalHUD;
    private Image iconeArgent;
    private Image iconeMaison;

    // Constructeur de la partie
    public Partie(double largeurEcran, double hauteurEcran) {
        this.largeurEcran = largeurEcran;
        this.hauteurEcran = hauteurEcran;
        this.niveauActuel = 1;
        this.argentTotal = 0;
        this.journauxRestants = 12;
        this.etatJeu = EtatJeu.CHARGEMENT;
        this.tempsChargement = 3.0;
        this.tempsTotal = 0.0;
        this.camera = new Camera();
        this.camelot = new Camelot(new Point2D(largeurEcran / 2, hauteurEcran - 144));
        this.journaux = new ArrayList<>();
        this.maisons = new ArrayList<>();
        this.particules = new ArrayList<>();
        this.adressesAbonnees = new ArrayList<>();
        this.modeDebogageAffichage = false;
        this.modeVisualisationChamp = false;
        this.imageBrique = new Image("file:assets/brique.png");
        this.iconeJournalHUD = new Image("file:assets/icone-journal.png");
        this.iconeArgent = new Image("file:assets/icone-dollar.png");
        this.iconeMaison = new Image("file:assets/icone-maison.png");
        chargerImages();
        demarrerNiveau(1);
    }

    // Charge les images nécessaires au jeu
    private void chargerImages() {
        // charger brique, icones journal/argent/maison
    }

    // Démarre un nouveau niveau
    private void demarrerNiveau(int numeroNiveau) {
        // update niveau, état CHARGEMENT, timer 3s
        // +12 journaux, générer niveau, reset camelot
    }

    // Génère un nouveau niveau avec 12 maisons
    private void genererNiveau(int numeroNiveau) {
        // clear listes
        // première adresse random 100-950
        // 12 maisons adresses consécutives, espacées régulièrement
        // random 50% abonnées, ajouter adresses à liste
        // particules niveau 2+ (30/niveau, max 400)
    }

    // Met à jour la logique du jeu
    public void update(double dt, Set<KeyCode> touchesEnfoncees) {
        // update temps total
        // gérer CHARGEMENT (timer -> EN_COURS)
        // gérer FIN (timer -> restart niveau 1)
        // update camelot, journaux
        // supprimer journaux hors écran
        // collisions, caméra suit camelot
        // check fin niveau/partie
    }

    // Dessine tout le jeu à l'écran
    public void dessiner(GraphicsContext gc) {
        // fond noir
        // si CHARGEMENT: écran chargement avec numéro
        // si FIN: écran fin avec argent total
        // briques, maisons, particules, camelot, journaux, HUD
        // debug si activé, visualisation champ si niveau 2+
    }

    // Dessine l'arrière-plan avec des briques
    private void dessinerArrierePlan(GraphicsContext gc) {
        // check si image chargée
        // calculer pos départ selon caméra
        // dessiner briques visibles
    }

    // Dessine le HUD (interface utilisateur)
    private void dessinerHUD(GraphicsContext gc) {
        // fond semi-transparent
        // gauche: icône + journaux restants
        // milieu: icône + argent total
        // droite: icône + adresses abonnées
    }

    // Vérifie les collisions entre journaux et éléments du niveau
    private void verifierCollisions() {
        // pour chaque journal:
        // collision boîte aux lettres -> marquer touchée
        //   si abonnée: +1$, vert; sinon rouge
        // collision fenêtre -> marquer cassée
        //   si abonnée: -2$, rouge; sinon +2$, vert
        // supprimer journal après collision
    }

    // Vérifie si le niveau est terminé
    private void verifierFinNiveau() {
        // camelot dépasse fin niveau -> niveau suivant
    }

    // Vérifie si la partie est terminée
    private void verifierFinPartie() {
        // journaux restants == 0 et liste vide -> FIN, timer 3s
    }

    // Dessine les éléments de débogage (rectangles de collision, ligne 20%)
    private void dessinerDebogageAffichage(GraphicsContext gc) {
        // ligne 20% écran
        // rectangles collision journaux, boîtes, fenêtres
    }

    // Dessine la visualisation du champ électrique
    private void dessinerVisualisationChamp(GraphicsContext gc) {
        // grille 50px
        // pour chaque point visible: calculer champ, dessiner vecteur
    }

    // Calcule le champ électrique total à la position donnée
    public Point2D champElectrique(Point2D position) {
        // champ total = 0
        // pour chaque particule: direction, distance, E = K*q/r²
        // ajouter vecteur normalisé * E au total
        return Point2D.ZERO;
    }

    // Gère les touches de débogage appuyées
    public void gererTouchesDebogage(KeyCode code) {
        // D: toggle debug, Q: +10 journaux, K: journaux=0
        // L: niveau suivant, F: toggle champ, I: particules test
    }

    // Crée des particules de test pour le débogage
    private void creerParticulesTest() {
        // clear particules
        // ligne haut (y=10), ligne bas (y=hauteur-10)
    }

    // Getters pour accès depuis les autres classes
    public int getNiveauActuel() {
        return niveauActuel;
    }

    public List<ParticuleChargee> getParticules() {
        return particules;
    }

    public void ajouterJournal(Journal journal) {
        // ajouter à liste, décrémenter compteur
    }

    public int getJournauxRestants() {
        return journauxRestants;
    }

    public static int getHauteurSol() {
        return HAUTEUR_SOL;
    }

    public double getTempsTotal() {
        return tempsTotal;
    }
}
