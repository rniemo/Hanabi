package com.niemo.hanaber.players;

import com.niemo.hanaber.actions.Action;
import com.niemo.hanaber.model.Card;
import com.niemo.hanaber.model.GameState;

import java.util.List;
import java.util.Map;

public class ComputerPlayer extends Player {

    @Override
    public Action takeTurn(GameState gameState, Map<Player, List<Card>> otherPlayersCards) {
        return Action.createDiscardAction(this, hand.size() - 1);
    }
}
