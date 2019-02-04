package com.niemo.hanaber.model;

public class Card {

    private final Suit suit;
    private final int number;

    private boolean isSuitHinted;
    private boolean isNumberHinted;

    public Card(Suit suit, int number) {
        assert number >=1 && number <= 5;
        this.suit = suit;
        this.number = number;
    }

    public String getHintText() {
        if(!isSuitHinted && !isNumberHinted) {
            return "UNKNOWN";
        }
        String hint = "";
        if(isSuitHinted){
            hint += suit;
        }
        if(isNumberHinted) {
            if(isSuitHinted) {
                hint += "_";
            }
            hint += number;
        }
        return hint;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getNumber() {
        return number;
    }

    public void setSuitHinted() {
        isSuitHinted = true;
    }

    public void setNumberHinted() {
        isNumberHinted = true;
    }
}
