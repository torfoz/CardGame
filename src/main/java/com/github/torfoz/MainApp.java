package com.github.torfoz;

import com.github.torfoz.model.DeckOfCards;
import com.github.torfoz.model.Hand;
import com.github.torfoz.model.PlayingCard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;
import java.util.stream.Collectors;

public class MainApp extends Application {

  private DeckOfCards deck;
  private Hand hand;

  private FlowPane handDisplay;
  private Label statusHand;
  private Label statusDeck;

  private Button checkHandButton;
  private Label sumOfFacesLabel;
  private Label heartsLabel;
  private Label spadeQueenLabel;
  private Label flushLabel;

  @Override
  public void start(Stage primaryStage) {
    deck = new DeckOfCards();
    hand = new Hand();

    HBox topPanel = new HBox(10);
    Button dealButton = new Button("Deal");
    Button resetButton = new Button("Reset");
    topPanel.getChildren().addAll(dealButton, resetButton);

    handDisplay = new FlowPane();

    HBox bottomPanel = new HBox(10);
    Button clearButton = new Button("Clear Hand");
    statusHand = new Label("Cards in hand: 0");
    statusDeck = new Label("Cards in deck: 52");
    bottomPanel.getChildren().addAll(clearButton, statusHand, statusDeck);

    HBox analysisPanel = new HBox(10);
    checkHandButton = new Button("Check Hand");
    sumOfFacesLabel = new Label("Sum of faces: 0");
    heartsLabel = new Label("Hearts: ?");
    spadeQueenLabel = new Label("Spade queen? ");
    flushLabel = new Label("5-flush? ");
    analysisPanel.getChildren().addAll(checkHandButton, sumOfFacesLabel, heartsLabel, spadeQueenLabel, flushLabel);

    VBox root = new VBox(10);
    root.getChildren().addAll(topPanel, handDisplay, bottomPanel, analysisPanel);

    dealButton.setOnAction(event -> {
      try {
        Map<String, PlayingCard> dealtCards = deck.dealHand(5);
        for (PlayingCard card : dealtCards.values()) {
          hand.addCard(card);
        }
        updateDisplay();
      } catch (NumberFormatException e) {
        statusHand.setText("Invalid number");
      } catch (IllegalArgumentException e) {
        statusHand.setText("Not enough cards in deck");
      }
    });

    resetButton.setOnAction(event -> {
      deck = new DeckOfCards();
      hand.clear();
      updateDisplay();
    });

    clearButton.setOnAction(event -> {
      hand.clear();
      updateDisplay();
    });

    // Ny knapp for å checke hand
    checkHandButton.setOnAction(event -> analyzeHand());

    // Startvisning
    updateDisplay();

    // Scene + Stage
    Scene scene = new Scene(root, 700, 400);
    primaryStage.setTitle("Card Game");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void analyzeHand() {
    int sumOfFaces = hand.getCards().values().stream()
        .mapToInt(card -> rankValue(card.getRank()))
        .sum();
    sumOfFacesLabel.setText("Sum of faces: " + sumOfFaces);

    String hearts = hand.getCards().values().stream()
        .filter(card -> card.getSuit() == 'H')
        .map(PlayingCard::toString)
        .collect(Collectors.joining(" "));
    if (hearts.isEmpty()) {
      heartsLabel.setText("Hearts: No Hearts");
    } else {
      heartsLabel.setText("Hearts: " + hearts);
    }

    boolean hasSpadeQueen = hand.getCards().values().stream()
        .anyMatch(card -> card.toString().equals("QS"));
    spadeQueenLabel.setText("Has spade queen? " + (hasSpadeQueen ? "Yes" : "No"));

    boolean hasFlush = hand.getCards().values().stream()
        .collect(Collectors.groupingBy(PlayingCard::getSuit, Collectors.counting()))
        .values()
        .stream()
        .anyMatch(count -> count >= 5);

    flushLabel.setText("Has 5-flush? " + (hasFlush ? "Yes" : "No"));
  }

  private void updateDisplay() {
    handDisplay.getChildren().clear();
    for (PlayingCard card : hand.getCards().values()) {
      Label cardLabel = new Label(card.toString());
      cardLabel.setStyle("-fx-border-color: black; -fx-padding: 5px;");
      cardLabel.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2) {
          hand.removeCard(card.toString());
          updateDisplay();
        }
      });
      handDisplay.getChildren().add(cardLabel);
    }
    statusHand.setText("Cards in hand: " + hand.getCards().size());
    statusDeck.setText("Cards in deck: " + deck.getCards().size());
  }

  private int rankValue(char rank) {
    return switch (rank) {
      case 'A' -> 1;
      case '2' -> 2;
      case '3' -> 3;
      case '4' -> 4;
      case '5' -> 5;
      case '6' -> 6;
      case '7' -> 7;
      case '8' -> 8;
      case '9' -> 9;
      case 'T' -> 10;
      case 'J' -> 11;
      case 'Q' -> 12;
      case 'K' -> 13;
      default -> 0;
    };
  }

  public static void main(String[] args) {
    launch(args);
  }
}