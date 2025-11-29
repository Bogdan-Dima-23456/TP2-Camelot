package bdeb.qc.ca.sim.tp2camelotvelo;

import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Input;
import bdeb.qc.ca.sim.tp2camelotvelo.jeu.Partie;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Set;

import static javafx.application.Application.launch;

public class Main extends Application {
    //Attributs
    public static final int LARGEUR = 900;
    public static final int HAUTEUR = 580;
    private Partie partie;
    private long dernierTemps;

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

        //Gestion du clavier

        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();


            Input.setKeyPressed(code, true);

            if (code == KeyCode.ESCAPE) {
                stage.close();
            } else {
                partie.gererTouchesDebogage(code);
            }
        });


        scene.setOnKeyReleased(e -> {
            Input.setKeyPressed(e.getCode(), false);
        });

        // Configurer la fenêtre
        stage.setTitle("TP2 Camelot Velo");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


        dernierTemps = System.nanoTime();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long maintenant) {
                if (dernierTemps == 0) {
                    dernierTemps = maintenant;
                    return;
                }
                double deltaTemps = (maintenant - dernierTemps) * 1e-9;
                dernierTemps = maintenant;
                partie.update(deltaTemps);
                partie.dessiner(gc);
            }
        };
        timer.start();
    }


    public static void main(String[] args) {
        launch();
    }
}