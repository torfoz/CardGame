package com.github.torfoz.view;

import com.github.torfoz.model.DeckOfCards;
import com.github.torfoz.model.Hand;
import com.github.torfoz.model.PlayingCard;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.control.Button;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainView extends VBox {

  private DeckOfCards deck;
  private final Hand hand;

  private FlowPane handDisplay;
  private Text statusHand;
  private Text statusDeck;

  private Text sumOfFacesText;
  private Text heartsText;
  private Text spadeQueenText;
  private Text flushText;

  public MainView() {
    deck = new DeckOfCards();
    hand = new Hand();

    initialize();
  }

  private void initialize(){
    HBox topPanel = new HBox(10);
    Button dealButton = new Button("Deal");
    Button resetButton = new Button("Reset");
    topPanel.getChildren().addAll(dealButton, resetButton);

    handDisplay = new FlowPane();

    HBox bottomPanel = new HBox(10);
    Button clearButton = new Button("Clear Hand");
    statusHand = new Text("Cards in hand: 0");
    statusDeck = new Text("Cards in deck: 52");
    bottomPanel.getChildren().addAll(clearButton, statusHand, statusDeck);

    HBox analysisPanel = new HBox(10);
    Button checkHandButton = new Button("Check Hand");
    sumOfFacesText = new Text("Sum of faces: 0");
    heartsText = new Text("Hearts: ?");
    spadeQueenText = new Text("Spade queen? ");
    flushText = new Text("5-flush? ");
    analysisPanel.getChildren()
        .addAll(checkHandButton, sumOfFacesText, heartsText, spadeQueenText, flushText);

    this.getChildren().addAll(topPanel, handDisplay, bottomPanel, analysisPanel);

    dealButton.setOnAction(actionEvent -> {
      try {
        Map<String, PlayingCard> dealtCards = deck.dealHand(5);
        for (PlayingCard card : dealtCards.values()) {
          hand.addCard(card);
        }
        displayHand();
      } catch (NumberFormatException e) {
        statusHand.setText("Invalid number");
      } catch (IllegalArgumentException e) {
        statusHand.setText("Not enough cards in deck");
      }
    });

    resetButton.setOnAction(actionEvent -> {
      deck = new DeckOfCards();
      hand.clear();
      displayHand();
    });

    clearButton.setOnAction(actionEvent -> {
      hand.clear();
      displayHand();
    });

    checkHandButton.setOnAction(actionEvent -> checkHand());
  }

  private void checkHand() {
    int sumOfFaces = hand.getCards().values().stream()
        .mapToInt(card -> rankValue(card.getRank()))
        .sum();
    sumOfFacesText.setText("Sum of faces: " + sumOfFaces);

    String hearts = hand.getCards().values().stream()
        .filter(card -> card.getSuit() == 'H')
        .map(PlayingCard::toString)
        .collect(Collectors.joining(" "));
    if (hearts.isEmpty()) {
      heartsText.setText("Hearts: No Hearts");
    } else {
      heartsText.setText("Hearts: " + hearts);
    }

    boolean hasSpadeQueen = hand.getCards().values().stream()
        .anyMatch(card -> card.toString().equals("QS"));
    spadeQueenText.setText("Has spade queen? " + (hasSpadeQueen ? "Yes" : "No"));

    boolean hasFlush = hand.getCards().values().stream()
        .collect(Collectors.groupingBy(PlayingCard::getSuit, Collectors.counting()))
        .values()
        .stream()
        .anyMatch(count -> count >= 5);

    flushText.setText("Has 5-flush? " + (hasFlush ? "Yes" : "No"));
  }

  private void displayHand() {
    handDisplay.getChildren().clear();
    for (PlayingCard card : hand.getCards().values()) {
      BorderPane borderPane = new BorderPane();
      Text cardText = new Text(card.toString());
      borderPane.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 5px;");
      borderPane.setCenter(cardText);
      handDisplay.getChildren().add(borderPane);
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
}

