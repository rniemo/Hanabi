package com.niemo.hanaber.players;

import com.niemo.hanaber.actions.Action;
import com.niemo.hanaber.model.Card;
import com.niemo.hanaber.model.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Player {

    protected List<Card> hand = new ArrayList<>();

    public void addCard(Card card) {
        if(card == null){
            return;
        }
        hand.add(0, card);
    }

    public Card removeCard(int cardIndex) {
        return hand.remove(cardIndex);
    }

    public List<Card> getHand() {
        return hand;
    }

    public abstract Action takeTurn(GameState gameState, Map<Player, List<Card>> otherPlayersCards);

}
