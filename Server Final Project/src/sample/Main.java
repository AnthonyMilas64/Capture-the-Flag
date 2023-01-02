package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author rcortez
 */
public class Main extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);

        Main.stage.setScene(scene);
        Main.stage.show();

    }

    public static void setFullScreen(boolean on){
        stage.setMaximized(on);
    }

    static double getHeight(){
        return stage.getHeight();
    }

    static double getWidth(){
        return stage.getWidth();
    }

    static void setHeight(double height){
        stage.setWidth(height);
    }

    static void setWidth(double width){
        stage.setWidth(width);
    }

    static Stage getStage(){
        return stage;
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

