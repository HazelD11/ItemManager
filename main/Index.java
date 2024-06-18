package main;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Index extends Application{
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Login.fxml"));
            Parent root = loader.load();
            GridPane grid = new GridPane();
            grid.getChildren().add(root);
            grid.setPadding(new Insets(20));
            grid.setAlignment(Pos.CENTER);
            Scene scene = new Scene(grid);
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
            primaryStage.show();
            LoginController loginController = loader.getController();
            loginController.setMainWindow(primaryStage);
            // loginController.setIndex(this); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public void closeIndexScene() {
        if (primaryStage != null) {
            // Close the Index scene
            primaryStage.close();
        } else {
            System.err.println("Primary stage is null.");
        }
    }
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
