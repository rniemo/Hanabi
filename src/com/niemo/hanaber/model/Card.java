package com.niemo.hanaber.model;

public class Card {

    public final Suit suit;
    public final int number;

    public Suit hintedSuit;
    public int hintedNumber;

    public Card(Suit suit, int number) {
        assert number >=1 && number <= 5;
        this.suit = suit;
        this.number = number;
    }

    public String getHintText() {
        if(hintedNumber == 0 && hintedSuit == null) {
            return "UNKNOWN";
        }
        String hint = "";
        if(hintedSuit != null){
            hint += hintedSuit;
        }
        if(hintedNumber != 0) {
            if(hintedSuit != null) {
                hint += "_";
            }
            hint += hintedNumber;
        }
        return hint;
    }
}
