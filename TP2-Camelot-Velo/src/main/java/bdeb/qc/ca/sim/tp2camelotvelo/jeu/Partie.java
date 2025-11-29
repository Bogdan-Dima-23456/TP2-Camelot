package bdeb.qc.ca.sim.tp2camelotvelo.jeu;

import bdeb.qc.ca.sim.tp2camelotvelo.entites.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Classe principale contenant toute la logique du jeu
public class Partie {
    // Constantes du jeu
    private static final int HAUTEUR_SOL = 580; // Position Y du sol
    public static final double GRAVITE = 1500.0; // Accélération de la gravité en px/s²
    private static final double TAILLE_BRIQUE = 64.0; // Taille d'une brique en pixels

    // Dimensions de l'écran
    private final double largeurEcran;
    private final double hauteurEcran;

    // Variables d'état du jeu
    private int niveauActuel;
    private int argentTotal;
    private int journauxRestants;
    private boolean enChargement; // Remplace EtatJeu.CHARGEMENT
    private boolean partieTerminee; // Remplace EtatJeu.FIN
    private double tempsChargement; // Timer pour l'écran de chargement (3 secondes)
    private double tempsTotal; // Temps total écoulé depuis le début du jeu
    private double finNiveauX; // Position X de fin du niveau

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
        this.enChargement = true;
        this.partieTerminee = false;
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
        chargerImages();
        demarrerNiveau(1);
    }

    // Charge les images nécessaires au jeu
    private void chargerImages() {
        try {
            this.imageBrique = new Image("file:resources/brique.png");
            this.iconeJournalHUD = new Image("file:resources/icone-journal.png");
            this.iconeArgent = new Image("file:resources/icone-dollar.png");
            this.iconeMaison = new Image("file:resources/icone-maison.png");
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des images: " + e.getMessage());
        }
    }

    // Démarre un nouveau niveau
    private void demarrerNiveau(int numeroNiveau) {
        this.niveauActuel = numeroNiveau;
        this.enChargement = true;
        this.tempsChargement = 3.0;
        this.journauxRestants += 12;
        this.camelot.setJournauxRestants(this.journauxRestants);
        genererNiveau(numeroNiveau);
        // Reset camelot position
        this.camelot.setPosition(new Point2D(largeurEcran / 2, hauteurEcran - 144));
        this.camelot.setVelocite(new Point2D(400, 0));
    }

    // Génère un nouveau niveau avec 12 maisons
    private void genererNiveau(int numeroNiveau) {
        // Clear listes
        this.maisons.clear();
        this.particules.clear();
        this.adressesAbonnees.clear();
        this.journaux.clear();

        // Première adresse random 100-950
        int premiereAdresse = 100 + (int)(Math.random() * 851);
        
        // 12 maisons adresses consécutives, espacées régulièrement
        double espacement = 800.0; // Espacement entre les maisons
        double positionXDepart = premiereAdresse;
        
        for (int i = 0; i < 12; i++) {
            int adresse = premiereAdresse + i;
            double positionX = positionXDepart + i * espacement;
            
            // Random 50% abonnées
            boolean estAbonnee = Math.random() < 0.5;
            if (estAbonnee) {
                adressesAbonnees.add(adresse);
            }
            
            Maison maison = new Maison(adresse, positionX, estAbonnee, hauteurEcran);
            maisons.add(maison);
        }
        
        // Calculer la fin du niveau (après la dernière maison)
        this.finNiveauX = positionXDepart + 12 * espacement;
        
        // Particules niveau 2+ (30/niveau, max 400)
        if (numeroNiveau >= 2) {
            int nombreParticules = Math.min(30 * numeroNiveau, 400);
            for (int i = 0; i < nombreParticules; i++) {
                double x = Math.random() * (finNiveauX + 1000);
                double y = Math.random() * hauteurEcran;
                particules.add(new ParticuleChargee(new Point2D(x, y)));
            }
        }
    }

    // Met à jour la logique du jeu
    public void update(double dt) {
        // Update temps total
        tempsTotal += dt;
        
        // Gérer CHARGEMENT (timer -> EN_COURS)
        if (enChargement) {
            tempsChargement -= dt;
            if (tempsChargement <= 0) {
                enChargement = false;
            }
            return; // Ne pas mettre à jour le jeu pendant le chargement
        }
        
        // Gérer FIN (timer -> restart niveau 1)
        if (partieTerminee) {
            tempsChargement -= dt;
            if (tempsChargement <= 0) {
                // Restart niveau 1
                this.niveauActuel = 1;
                this.argentTotal = 0;
                this.journauxRestants = 12;
                this.partieTerminee = false;
                demarrerNiveau(1);
            }
            return;
        }
        
        // Update camelot
        camelot.update(dt);
        camelot.setJournauxRestants(journauxRestants);
        
        // Récupérer les journaux lancés par le camelot
        List<Journal> journauxLances = camelot.getJournauxLances();
        for (Journal journal : journauxLances) {
            if (!journaux.contains(journal)) {
                journaux.add(journal);
            }
        }
        
        // Update journaux avec champ électrique
        for (Journal journal : journaux) {
            // Appliquer le champ électrique si niveau 2+
            if (niveauActuel >= 2 && !particules.isEmpty()) {
                Point2D champ = ParticuleChargee.champElectrique(particules, journal.getPosition());
                // F = q*E, mais ici on applique directement comme accélération
                // Pour simplifier, on applique le champ comme force
                double masse = 1.5; // Masse moyenne du journal
                Point2D force = champ.multiply(900.0 / masse); // Charge du journal / masse
                journal.setVelocite(journal.getVelocite().add(force.multiply(dt)));
            }
            journal.update(dt);
        }
        
        // Supprimer journaux hors écran
        for (int i = journaux.size() - 1; i >= 0; i--) {
            Journal journal = journaux.get(i);
            Point2D posEcran = camera.coordoEcran(journal.getPosition());
            if (posEcran.getX() < -100 || posEcran.getX() > largeurEcran + 100 ||
                posEcran.getY() > hauteurEcran + 100) {
                journaux.remove(i);
            }
        }
        
        // Collisions
        verifierCollisions();
        
        // Caméra suit camelot
        camera.update(camelot.getPosition().getX(), largeurEcran);
        
        // Check fin niveau/partie
        verifierFinNiveau();
        verifierFinPartie();
    }

    // Dessine tout le jeu à l'écran
    public void dessiner(GraphicsContext gc) {
        // Fond noir
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, largeurEcran, hauteurEcran);
        
        // Si CHARGEMENT: écran chargement avec numéro
        if (enChargement) {
            gc.setFill(Color.WHITE);
            gc.setFont(new Font("Arial", 48));
            String texte = "Niveau " + niveauActuel;
            double largeurTexte = gc.getFont().getSize() * texte.length() * 0.6;
            gc.fillText(texte, (largeurEcran - largeurTexte) / 2, hauteurEcran / 2);
            return;
        }
        
        // Si FIN: écran fin avec argent total
        if (partieTerminee) {
            gc.setFill(Color.WHITE);
            gc.setFont(new Font("Arial", 48));
            String texte = "Partie terminée!";
            double largeurTexte = gc.getFont().getSize() * texte.length() * 0.6;
            gc.fillText(texte, (largeurEcran - largeurTexte) / 2, hauteurEcran / 2 - 50);
            
            gc.setFont(new Font("Arial", 36));
            String texteArgent = "Argent total: $" + argentTotal;
            largeurTexte = gc.getFont().getSize() * texteArgent.length() * 0.6;
            gc.fillText(texteArgent, (largeurEcran - largeurTexte) / 2, hauteurEcran / 2 + 50);
            return;
        }
        
        // Briques
        dessinerArrierePlan(gc);
        
        // Maisons
        for (Maison maison : maisons) {
            maison.draw(gc, camera);
        }
        
        // Particules
        for (ParticuleChargee particule : particules) {
            particule.draw(gc, camera);
        }
        
        // Camelot
        camelot.draw(gc, camera);
        
        // Journaux
        for (Journal journal : journaux) {
            journal.draw(gc, camera);
        }
        
        // HUD
        dessinerHUD(gc);
        
        // Debug si activé
        if (modeDebogageAffichage) {
            dessinerDebogageAffichage(gc);
        }
        
        // Visualisation champ si niveau 2+
        if (modeVisualisationChamp && niveauActuel >= 2) {
            dessinerVisualisationChamp(gc);
        }
    }

    // Dessine l'arrière-plan avec des briques
    private void dessinerArrierePlan(GraphicsContext gc) {
        if (imageBrique == null) return;
        
        // Calculer position départ selon caméra
        double cameraX = camera.getX();
        double startX = Math.floor(cameraX / TAILLE_BRIQUE) * TAILLE_BRIQUE;
        double endX = cameraX + largeurEcran + TAILLE_BRIQUE;
        
        // Dessiner briques visibles
        for (double x = startX; x < endX; x += TAILLE_BRIQUE) {
            for (double y = 0; y < hauteurEcran; y += TAILLE_BRIQUE) {
                Point2D posEcran = camera.coordoEcran(new Point2D(x, y));
                if (posEcran.getX() >= -TAILLE_BRIQUE && posEcran.getX() < largeurEcran) {
                    gc.drawImage(imageBrique, posEcran.getX(), posEcran.getY(), TAILLE_BRIQUE, TAILLE_BRIQUE);
                }
            }
        }
    }

    // Dessine le HUD (interface utilisateur)
    private void dessinerHUD(GraphicsContext gc) {
        // Fond semi-transparent
        gc.setFill(new Color(0, 0, 0, 0.5));
        gc.fillRect(0, 0, largeurEcran, 60);
        
        // Gauche: icône + journaux restants
        if (iconeJournalHUD != null) {
            gc.drawImage(iconeJournalHUD, 10, 10, 40, 40);
        }
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 24));
        gc.fillText(String.valueOf(journauxRestants), 60, 40);
        
        // Milieu: icône + argent total
        if (iconeArgent != null) {
            gc.drawImage(iconeArgent, largeurEcran / 2 - 60, 10, 40, 40);
        }
        gc.fillText("$" + argentTotal, largeurEcran / 2 - 10, 40);
        
        // Droite: icône + adresses abonnées
        if (iconeMaison != null) {
            gc.drawImage(iconeMaison, largeurEcran - 150, 10, 40, 40);
        }
        gc.fillText(String.valueOf(adressesAbonnees.size()), largeurEcran - 100, 40);
    }

    // Vérifie les collisions entre journaux et éléments du niveau
    private void verifierCollisions() {
        for (int i = journaux.size() - 1; i >= 0; i--) {
            Journal journal = journaux.get(i);
            boolean collisionDetectee = false;
            
            // Collision avec boîtes aux lettres
            for (Maison maison : maisons) {
                BoiteAuxLettres boite = maison.getBoiteAuxLettres();
                if (boite != null && journal.verifierCollision(boite)) {
                    collisionDetectee = true;
                    if (maison.isEstAbonnee()) {
                        argentTotal += 1;
                        // Marquer boîte touchée (vert)
                        // Note: BoiteAuxLettres n'a pas de méthode pour changer couleur, 
                        // mais on peut ajouter un état si nécessaire
                    } else {
                        // Rouge (pas d'argent)
                    }
                    break;
                }
            }
            
            // Collision avec fenêtres
            if (!collisionDetectee) {
                for (Maison maison : maisons) {
                    for (Fenetre fenetre : maison.getFenetres()) {
                        if (!fenetre.isEstCassee() && journal.verifierCollision(fenetre)) {
                            collisionDetectee = true;
                            fenetre.setEstCassee(true);
                            if (maison.isEstAbonnee()) {
                                argentTotal -= 2;
                                fenetre.changerCouleur("rouge");
                            } else {
                                argentTotal += 2;
                                fenetre.changerCouleur("vert");
                            }
                            break;
                        }
                    }
                    if (collisionDetectee) break;
                }
            }
            
            // Supprimer journal après collision
            if (collisionDetectee) {
                journaux.remove(i);
            }
        }
    }

    // Vérifie si le niveau est terminé
    private void verifierFinNiveau() {
        if (camelot.getPosition().getX() >= finNiveauX) {
            // Niveau suivant
            demarrerNiveau(niveauActuel + 1);
        }
    }

    // Vérifie si la partie est terminée
    private void verifierFinPartie() {
        if (journauxRestants == 0 && journaux.isEmpty()) {
            partieTerminee = true;
            tempsChargement = 3.0;
        }
    }

    // Dessine les éléments de débogage (rectangles de collision, ligne 20%)
    private void dessinerDebogageAffichage(GraphicsContext gc) {
        // Ligne 20% écran
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(2);
        double ligneY = hauteurEcran * 0.2;
        gc.strokeLine(0, ligneY, largeurEcran, ligneY);
        
        // Rectangles collision journaux
        gc.setStroke(Color.BLUE);
        for (Journal journal : journaux) {
            Point2D posEcran = camera.coordoEcran(journal.getPosition());
            gc.strokeRect(posEcran.getX(), posEcran.getY(), journal.getLargeur(), journal.getHauteur());
        }
        
        // Rectangles collision boîtes
        gc.setStroke(Color.GREEN);
        for (Maison maison : maisons) {
            BoiteAuxLettres boite = maison.getBoiteAuxLettres();
            if (boite != null) {
                Point2D posEcran = camera.coordoEcran(boite.getPosition());
                gc.strokeRect(posEcran.getX(), posEcran.getY(), boite.getLargeur(), boite.getHauteur());
            }
        }
        
        // Rectangles collision fenêtres
        gc.setStroke(Color.RED);
        for (Maison maison : maisons) {
            for (Fenetre fenetre : maison.getFenetres()) {
                Point2D posEcran = camera.coordoEcran(fenetre.getPosition());
                gc.strokeRect(posEcran.getX(), posEcran.getY(), fenetre.getLargeur(), fenetre.getHauteur());
            }
        }
    }

    // Dessine la visualisation du champ électrique
    private void dessinerVisualisationChamp(GraphicsContext gc) {
        ParticuleChargee.dessinerVisualisationChamp(
            particules,
            finNiveauX + 1000,
            hauteurEcran,
            largeurEcran,
            camera,
            gc
        );
    }

    // Gère les touches de débogage appuyées
    public void gererTouchesDebogage(KeyCode code) {
        if (code == KeyCode.D) {
            modeDebogageAffichage = !modeDebogageAffichage;
        } else if (code == KeyCode.Q) {
            journauxRestants += 10;
            camelot.setJournauxRestants(journauxRestants);
        } else if (code == KeyCode.K) {
            journauxRestants = 0;
            camelot.setJournauxRestants(journauxRestants);
        } else if (code == KeyCode.L) {
            demarrerNiveau(niveauActuel + 1);
        } else if (code == KeyCode.F) {
            modeVisualisationChamp = !modeVisualisationChamp;
        } else if (code == KeyCode.I) {
            creerParticulesTest();
        }
    }

    // Crée des particules de test pour le débogage
    private void creerParticulesTest() {
        particules.clear();
        
        // Ligne haut (y=10)
        for (double x = 0; x < largeurEcran * 2; x += 100) {
            particules.add(new ParticuleChargee(new Point2D(x, 10)));
        }
        
        // Ligne bas (y=hauteur-10)
        for (double x = 0; x < largeurEcran * 2; x += 100) {
            particules.add(new ParticuleChargee(new Point2D(x, hauteurEcran - 10)));
        }
    }

    // Getters pour accès depuis les autres classes
    public int getNiveauActuel() {
        return niveauActuel;
    }

    public List<ParticuleChargee> getParticules() {
        return particules;
    }

    public void ajouterJournal(Journal journal) {
        journaux.add(journal);
        if (journauxRestants > 0) {
            journauxRestants--;
            camelot.setJournauxRestants(journauxRestants);
        }
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
