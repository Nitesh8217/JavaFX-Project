package jforkts.in.myjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AuthenticateController implements Initializable {
    @FXML
    private ImageView collegeImageView;
@FXML
    private TextField login;
@FXML
    private PasswordField password1;
@FXML
   private Label message;
Image myImage;
public void authenticateUser() throws IOException{
    if(login.getText().equals("Ajay") && password1.getText().equals("abc")) {
        System.out.println("Welcome User");
        message.setText("Welcome User..."+login.getText());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent mainView = loader.load();

        // Get the current stage
        Stage stage = (Stage) login.getScene().getWindow();


        stage.setFullScreen(true);

        Scene myscene=new Scene(mainView);
        myscene.setFill(new RadialGradient(
                0, 0, 0, 0, 1, true,                  //sizing
                CycleMethod.NO_CYCLE,                 //cycling
                new Stop(0, Color.web("#81c483")),    //colors
                new Stop(1, Color.web("#fcc200")))
        );
        // Set the scene with the main view

        String css = this.getClass().getResource("/application.css").toExternalForm();
        myscene.getStylesheets().add(css);

        RadialGradient gradient1 = new RadialGradient(0,
                .1,
                100,
                100,
                200,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.YELLOW),
                new Stop(1, Color.RED));
       // myscene.setFill(gradient1);
        stage.setScene(myscene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setTitle("Main Application");


    }
        else {
        System.out.println("Access Denined...");
        message.setText("Access Denied...");
    }
}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         login.setText("Ajay");
         myImage=new Image(getClass().getResourceAsStream("/images/gssCollege.jpg"));
         collegeImageView.setImage(myImage);

    }
}
