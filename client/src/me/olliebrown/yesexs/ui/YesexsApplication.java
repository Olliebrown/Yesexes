package me.olliebrown.yesexs.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import me.olliebrown.yesexs.ui.controllers.MainController;

import java.io.IOException;
import java.util.Locale;
import java.util.PropertyResourceBundle;

public class YesexsApplication extends Application {

    public static final int VERSION_MAJOR = 1;
    public static final int VERSION_MINOR = 2;
    public static final int VERSION_PATCH = 0;

    public static final String APP_NAME = "Yes ex-s Client";
    public static final String APP_VERSION = VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/Main.fxml"));
            //TODO not hardcode the locale
            loader.setResources(PropertyResourceBundle.getBundle("bundles.Yesexs", Locale.ENGLISH));
            Parent root = loader.load();
            stage.setResizable(true);
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            MainController c = loader.getController();
            c.setStage(stage);
            c.setTitle(null);
            stage.setOnHidden(v -> {
                c.stop();
                Platform.exit();
            });
            stage.show();
            stage.sizeToScene();
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        } catch (Exception e) {
            System.out.println("Exception on FXMLLoader.load()");
            System.out.println(e.getMessage());
            Platform.exit();
        }
    }
}
