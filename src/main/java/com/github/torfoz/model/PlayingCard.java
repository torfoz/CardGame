package com.github.torfoz.model;

public class PlayingCard {
  private char suit;
  private char rank;

  public PlayingCard(char suit, char rank) {
    this.suit = suit;
    this.rank = rank;
  }

  public char getSuit() {
    return suit;
  }

  public char getRank() {
    return rank;
  }

  @Override
  public String toString() {
    return "" + rank + suit;
  }
}