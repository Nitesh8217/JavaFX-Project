package jforkts.in.myjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {

    //DBConnect db=new DBConnect();
    @Override
    public void start(Stage stage) throws IOException {
        try {
            //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 686, 38);
            stage.setTitle("Student Information System");
           /* URL url = this.getClass().getResource("application.css");
            if (url == null) {
                System.out.println("Resource not found. Aborting.");
                System.exit(-1);
            }
            String css = url.toExternalForm();
            scene.getStylesheets().add(css);*/

            String css = this.getClass().getResource("/application.css").toExternalForm();
            scene.getStylesheets().add(css);

            scene.setFill(new RadialGradient(
                    0, 0, 0, 0, 1, true,                  //sizing
                    CycleMethod.NO_CYCLE,                 //cycling
                    new Stop(0, Color.web("#81c483")),    //colors
                    new Stop(1, Color.web("#fcc200")))
            );
            stage.setFullScreenExitHint("");
            stage.setFullScreen(true);
            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}