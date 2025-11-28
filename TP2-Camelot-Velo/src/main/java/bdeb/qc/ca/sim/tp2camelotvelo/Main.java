package bdeb.qc.ca.sim.tp2camelotvelo;

import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Input;
import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Partie;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static final int LARGEUR = 900;
    public static final int HAUTEUR = 580;

    private Partie partie;

    @Override
    public void start(Stage stage) {
        // Créer le canvas
        Canvas canvas = new Canvas(LARGEUR, HAUTEUR);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // Créer la partie
        partie = new Partie(LARGEUR, HAUTEUR);
        
        // Créer la scène
        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, LARGEUR, HAUTEUR);
        
        // Configurer la fenêtre
        stage.setTitle("TP2 Camelot Velo");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}