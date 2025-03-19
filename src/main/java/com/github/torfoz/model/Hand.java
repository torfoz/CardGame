package com.github.torfoz.model;

import java.util.HashMap;
import java.util.Map;

public class Hand {
  private final Map<String, PlayingCard> cards = new HashMap<>();

  public void addCard(PlayingCard card) {
    cards.put(card.toString(), card);
  }

  public Map<String, PlayingCard> getCards() {
    return cards;
  }

  public void removeCard(String key) {
    cards.remove(key);
  }

  public void clear() {
    cards.clear();
  }
}