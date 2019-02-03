package com.niemo.hanaber.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards;

    /**
     * Produces a deck of cards with the number of given suits, shuffled.
     * @param numSuits Number of suits to use, 5 or 6.
     */
    public Deck(int numSuits) {
        assert numSuits == 5 || numSuits == 6;
        int[] cardFrequencyMap = new int[]{3, 2, 2, 2, 1};
        int cardsPerSuit = Arrays.stream(cardFrequencyMap).sum();
        cards = new ArrayList<>(numSuits * cardsPerSuit);
        for(int suit = 0; suit < numSuits; suit++) {
            for(int cardNum = 1; cardNum < 5; cardNum++){
                for(int i = 0; i < cardFrequencyMap[cardNum - 1]; i++){
                    cards.add(new Card(Suit.values()[suit], cardNum));
                }
            }
        }
        Collections.shuffle(cards);
    }

    /**
     * Returns the card on top of the deck, or null if the deck is empty.
     */
    public Card drawCard(){
        if(cards.isEmpty()){
            return null;
        }
        return cards.remove(0);
    }

}
