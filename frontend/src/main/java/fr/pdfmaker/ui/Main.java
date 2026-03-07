package fr.pdfmaker.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import atlantafx.base.theme.PrimerDark;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("PDF Maker");
        //primaryStage.

        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
