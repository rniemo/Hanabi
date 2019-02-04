package com.niemo.hanaber.model;

public class Card {

    public final Suit suit;
    public final int number;

    public boolean isSuitHinted;
    public boolean isNumberHinted;

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
}
