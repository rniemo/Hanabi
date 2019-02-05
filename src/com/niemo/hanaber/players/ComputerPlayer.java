package com.niemo.hanaber.players;

import com.niemo.hanaber.actions.Action;
import com.niemo.hanaber.model.GameState;

import java.util.List;

public class ComputerPlayer extends Player {

    @Override
    public Action takeTurn(GameState gameState, List<Player> otherPlayers) {
        return Action.createDiscardAction(this, hand.size());
    }
}
