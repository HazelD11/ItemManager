package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Dashboard extends Application{
    
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Dashboard.fxml"));
            Parent root = loader.load();
            GridPane grid = new GridPane();
            grid.getChildren().add(root);
            // grid.setPadding(new Insets(10));
            grid.setAlignment(Pos.TOP_LEFT);
            Scene scene = new Scene(grid);
            primaryStage.setFullScreen(true);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
}



    

