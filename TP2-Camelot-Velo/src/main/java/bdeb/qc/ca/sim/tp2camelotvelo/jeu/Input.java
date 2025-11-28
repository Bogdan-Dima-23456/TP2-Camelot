package bdeb.qc.ca.sim.tp2camelotvelo.jeu;

import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class Input {

    // Ensemble des touches actuellement appuyées
    private static Set<KeyCode> touches = new HashSet<>();

    public static boolean isKeyPressed(KeyCode code){
        return touches.contains(code);
    }

    public static void setKeyPressed(KeyCode code,boolean appuie){
        if(appuie){
            touches.add(code);}

        else{
            touches.remove(code);}
    }

    public static Set<KeyCode> getTouches(){
        return new HashSet<>(touches);
    }
}
