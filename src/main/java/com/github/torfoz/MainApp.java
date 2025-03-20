package com.github.torfoz;

import com.github.torfoz.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

  MainView mainView;

  @Override
  public void start(Stage primaryStage) {

    mainView = new MainView();

    Scene scene = new Scene(mainView, 800, 600);
    primaryStage.setTitle("Card Game");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}