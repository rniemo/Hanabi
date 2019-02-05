package com.niemo.hanaber;

import com.niemo.hanaber.actions.Action;
import com.niemo.hanaber.actions.ActionValidator;
import com.niemo.hanaber.model.Card;
import com.niemo.hanaber.model.GameState;
import com.niemo.hanaber.players.ComputerPlayer;
import com.niemo.hanaber.players.HumanPlayer;
import com.niemo.hanaber.players.Player;

import java.util.*;

public class Game {

    private List<Player> players;
    private GameState gameState;

    public Game(int numPlayers, int numSuits, boolean playAsHuman) {
        gameState = new GameState(numSuits);
        players = new ArrayList<>();
        if(playAsHuman){
            players.add(new HumanPlayer());
        }else{
            players.add(new ComputerPlayer());
        }
        for(int i = 1; i < numPlayers; i++) {
            players.add(new ComputerPlayer());
        }
        Collections.shuffle(players);
    }

    public void setup() {
        // reset game state
        gameState.reset();
        // deal out cards
        int cardsPerPlayer = players.size() < 4 ? 5 : 4;
        for(int i = 0; i < players.size(); i++){
            for(int j = 0; j < cardsPerPlayer; j++) {
                players.get(i).addCard(gameState.drawCard());
            }
        }
    }

    public void play() {
        int playerTurn = 0;
        while(!gameIsOver()){
            Player player = players.get(playerTurn);
            Action action = null;
            String actionErrorMessage = null;
            do {
                if(actionErrorMessage != null){
                    System.out.println(actionErrorMessage);
                }
                action = player.takeTurn(gameState, getOtherPlayersCards(player));
                actionErrorMessage = ActionValidator.validate(player, action);
            }
            while(actionErrorMessage != null);
            performAction(action);
            playerTurn = (playerTurn + 1) % players.size();
        }

    }

    private void performAction(Action action) {
        if(action.type == Action.Type.DISCARD || action.type == Action.Type.PLAY) {
            performDiscardOrPlayAction(action);
        } else {
            performHintAction(action);
        }
    }

    private void performDiscardOrPlayAction(Action action) {
        Card card = action.affectedPlayer.removeCard(action.cardNums.get(0));
        if(action.type.equals(Action.Type.DISCARD)){
            gameState.addToDiscard(card);
        } else if(action.type.equals(Action.Type.PLAY)) {
            int playableNumber = gameState.fireworkState.get(card.getSuit()) + 1;
            if(card.getNumber() == playableNumber) {
                gameState.fireworkState.put(card.getSuit(), playableNumber);
            }else {
                gameState.boomsAvailable--;
                gameState.addToDiscard(card);
            }
        }
        action.affectedPlayer.addCard(gameState.drawCard());
    }

    private void performHintAction(Action action) {
        for(int i = 0; i < action.cardNums.size(); i++) {
            int cardIndex = action.cardNums.get(i);
            Card card = action.affectedPlayer.getHand().get(cardIndex);
            if(action.hintType.suit != null) {
                card.setSuitHinted();
            }else{
                card.setNumberHinted();
            }
        }
    }

    /**
     * Returns the hands of other players in the game
     * @param self the player whose cards should not be returned
     * @return
     */
    private Map<Player, List<Card>> getOtherPlayersCards(Player self) {
        Map<Player, List<Card>> map = new HashMap<>();
        for(Player player : players){
            if(!player.equals(self)){
                map.put(player, player.getHand());
            }
        }
        return map;
    }


    /**
     * Game is over when one of these 3 things happens:
     * - The players have played all 25 cards (honestly don't even have to check for this it'll never happen tbh)
     * - The deck has no cards & each player has had another turn to play
     * - 3 booms have happened
     */
    private boolean gameIsOver() {
        if(gameState.currentScore() == gameState.numSuits * 5) {
            return true; // we won!
        }
        if(gameState.boomsAvailable == 0){
            return true; // we lost
        }
        // TODO(niemo): what if team runs out of time
        // TODO(niemo): more robust solution to drawcard being null & end game state
        return false;
    }
}
