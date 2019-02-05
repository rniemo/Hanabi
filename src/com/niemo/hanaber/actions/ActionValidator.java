package com.niemo.hanaber.actions;

import com.niemo.hanaber.model.Card;
import com.niemo.hanaber.players.Player;

public class ActionValidator {

    /**
     * Validates that the given player can perform the given action.
     * Returns a user-friendly error message if not, otherwise null.
     */
    public static String validate(Player player, Action action) {
        if(action.type == Action.Type.DISCARD || action.type == Action.Type.PLAY) {
            return validateDiscardOrPlayAction(player, action);
        } else if(action.type == Action.Type.HINT){
            return validateHintAction(player, action);
        }
        return null;
    }

    /**
     * Valid discard/play actions must meet these requirements:
     * - the player making the action is the affected player of the action
     * - only one card can be discarded
     * - the card index must be between 0 and the number of cards in the player's hand.
     */
    private static String validateDiscardOrPlayAction(Player player, Action action) {
        if(!player.equals(action.affectedPlayer)){
            return "Player cannot " + action.type + " cards that aren't theirs.";
        }
        if(action.cardNums.size() != 1) {
            return "Player cannot " + action.type + " more than one card at once";
        }
        int cardIndex = action.cardNums.get(0);
        if(cardIndex < 1 || cardIndex > player.getHand().size()) {
            return "Card index \"" + cardIndex + "\" must be in the player's hard -- i.e. card index is between 1 and "
                    + player.getHand().size() + ".";
        }
        return null;
    }

    /**
     * Valid hint actions must meet these requirements:
     * - the player making the action is NOT the affected player of the action
     * - the number of cards must be between 1 and the number of cards in the player's hand
     * - each card index must be between 0 and the number of cards in the player's hand.
     * - the hint must actually apply to each card in the player's hand
     * - there can only be num or suit specified in the hint type
     */
    private static String validateHintAction(Player player, Action action) {
        int numCards = action.cardNums.size();
        int playerHandSize = action.affectedPlayer.getHand().size();
        if(player.equals(action.affectedPlayer)) {
            return "Player cannot hint their own cards.";
        }
        if(numCards < 1 || numCards > playerHandSize) {
            return "Cannot hint " + numCards + " cards. Must be between 1 and " + playerHandSize + ".";
        }
        if(action.hintType.suit != null && action.hintType.num != 0) {
            return "Hint must have a hint type, either a number or suit.";
        }
        for(int i = 0; i < numCards; i++) {
            int cardIndex = action.cardNums.get(i);
            if(cardIndex < 1 || cardIndex > playerHandSize){
                return "Hinted card index \"" + cardIndex + "\" must be between 1 and " + playerHandSize;
            }
            Card card = action.affectedPlayer.getHand().get(cardIndex - 1);
            if(action.hintType.suit != null && !card.getSuit().equals(action.hintType.suit)) {
                return "Hinted suit \"" + action.hintType.suit + "\" must match card suit \"" + card.getSuit() + "\".";
            } else if(card.getNumber() != action.hintType.num){
                return "Hinted number \"" + action.hintType.num + "\" must match card number \"" + card.getNumber() + "\".";
            }
        }
        // TODO(niemo): ensure that the hint hints EVERYTHING in the hand, not just some things
        return null;
    }
}
