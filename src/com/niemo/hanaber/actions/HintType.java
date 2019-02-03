package com.niemo.hanaber.actions;

import com.niemo.hanaber.model.Suit;

public class HintType {

    public Suit suit;
    public int num;

    public HintType(Suit suit){
        this.suit = suit;
    }

    public HintType(int cardNum) {
        this.num = cardNum;
    }

}
