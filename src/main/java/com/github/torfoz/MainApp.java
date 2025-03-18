package com.github.torfoz;

import com.github.torfoz.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

  private BorderPane root;

  @Override
  public void start(Stage primaryStage) {
    root = new BorderPane();

    root.setCenter(new MainView(this));

    Scene scene = new Scene(root, 1280, 720);
    primaryStage.setTitle("CardGame");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

}
