package com.niemo.hanaber.model;

import com.niemo.hanaber.players.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {

    private final static int MAX_HINTS = 8;
    private final static int MAX_BOOMS = 3;

    public int hintsAvailable = MAX_HINTS;
    public int boomsAvailable = MAX_BOOMS;
    public int numSuits;
    private Deck deck;
    public List<Card> discardPile;
    // Map from suit to the current top number. e.g. at the start, all of them are set to 0, so the next playable
    // number is 1.
    public Map<Suit, Integer> fireworkState;
    // TODO(niemo): add board state


    public GameState(int numSuits) {
        this.numSuits = numSuits;
        reset();
    }

    public void addToDiscard(Card card) {
        discardPile.add(card);
    }

    public Card drawCard() {
        return deck.drawCard();
    }

    public void reset() {
        deck = new Deck(numSuits);
        discardPile = new ArrayList<>();
        fireworkState = new HashMap<>();
        for(int i = 0; i < numSuits; i++){
            fireworkState.put(Suit.values()[i], 0);
        }
    }

    public int currentScore() {
        int score = 0;
        for(Integer value : fireworkState.values()) {
            score += value;
        }
        return score;
    }

    public void printFireworkState() {
        for(Map.Entry<Suit, Integer> entry : fireworkState.entrySet()){
            if(entry.getValue() == 0){
                System.out.print("NO_" + entry.getKey() + " ");
            }else {
                System.out.print(entry.getKey() + "_" + entry.getValue() + " ");
            }
        }
        System.out.println();
    }
}
