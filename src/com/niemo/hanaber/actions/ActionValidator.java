package com.niemo.hanaber.actions;

import com.niemo.hanaber.model.Card;
import com.niemo.hanaber.players.Player;

public class ActionValidator {

    public static boolean validate(Player player, Action action) {
        if(action.type == Action.Type.DISCARD || action.type == Action.Type.PLAY) {
            return validateDiscardOrPlayAction(player, action);
        } else if(action.type == Action.Type.HINT){
            return validateHintAction(player, action);
        }
        return false;
    }

    /**
     * Valid discard/play actions must meet these requirements:
     * - the player making the action is the affected player of the action
     * - only one card can be discarded
     * - the card index must be between 0 and the number of cards in the player's hand.
     */
    private static boolean validateDiscardOrPlayAction(Player player, Action action) {
        return player.equals(action.affectedPlayer)
                && action.cardNums.size() == 1
                && action.cardNums.get(0) >= 0
                && action.cardNums.get(0) < player.getHand().size();
    }

    /**
     * Valid hint actions must meet these requirements:
     * - the player making the action is NOT the affected player of the action
     * - the number of cards must be between 1 and the number of cards in the player's hand
     * - each card index must be between 0 and the number of cards in the player's hand.
     * - the hint must actually apply to each card in the player's hand
     * - there can only be num or suit specified in the hint type
     */
    private static boolean validateHintAction(Player player, Action action) {
        int numCards = action.cardNums.size();
        int playerHandSize = action.affectedPlayer.getHand().size();
        if(player.equals(action.affectedPlayer) || numCards < 1 || numCards > playerHandSize
        || (action.hintType.suit != null && action.hintType.num != 0)) {
            return false;
        }
        for(int i = 0; i < numCards; i++) {
            int cardIndex = action.cardNums.get(i);
            if(cardIndex < 0 || cardIndex > playerHandSize){
                return false;
            }
            Card card = action.affectedPlayer.getHand().get(cardIndex);
            if(action.hintType.suit != null && !card.suit.equals(action.hintType.suit)) {
                return false;
            } else if(card.number != action.hintType.num){
                return false;
            }
        }
        // TODO(niemo): ensure that the hint hints EVERYTHING in the hand, not just some things
        return true;
    }
}
