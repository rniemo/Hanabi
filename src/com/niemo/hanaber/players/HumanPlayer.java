package com.niemo.hanaber.players;

import com.niemo.hanaber.actions.Action;
import com.niemo.hanaber.actions.HintType;
import com.niemo.hanaber.model.Card;
import com.niemo.hanaber.model.GameState;
import com.niemo.hanaber.model.Suit;

import java.util.*;

public class HumanPlayer extends Player {

    @Override
    public Action takeTurn(GameState gameState, Map<Player, List<Card>> otherPlayersCards) {
        Action parsedAction = null;
        displayInformation(gameState, otherPlayersCards);
        do {
            System.out.print("Enter your action, or 'help' if you're lost: ");
            Scanner scanner = new Scanner(System.in);
            // get their input as a String
            String input = scanner.nextLine();
            if (input.equals("help")) {
                displayHelp();
            } else if (input.equals("info")) {
                displayInformation(gameState, otherPlayersCards);
            } else {
                try {

                    parsedAction = parseAction(input, new ArrayList<>(otherPlayersCards.keySet()));
                }catch(NumberFormatException e) {
                    System.out.println("Expected integer in command, failed to parse.");
                }
            }
        }while(parsedAction == null);
        return parsedAction;
    }

    private void displayInformation(GameState gameState, Map<Player, List<Card>> otherPlayersCards) {
        System.out.println("Explosions remaining: " + gameState.boomsAvailable);
        System.out.println("Hints remaining: " + gameState.hintsAvailable);
        int playerNum = 0;
        for(Map.Entry<Player, List<Card>> entry : otherPlayersCards.entrySet()) {
            System.out.print("Player " + (playerNum + 1) + ": ");
            for(Card card : entry.getValue()) {
                System.out.print(card.getSuit() + "_" + card.getNumber() + ", ");
            }
            System.out.println();
        }
        System.out.print("Your cards: ");
        for(Card card : this.hand) {
            System.out.print(card.getHintText() + ", ");
        }
        System.out.println();
        System.out.println("Board state:");
        gameState.printFireworkState();
    }

    private Action parseAction(String input, List<Player> players) throws NumberFormatException{
        String[] inputs = input.split(" ");
        String command = inputs[0];
        if(command.equals("discard") || command.equals("d")) {
            return parsePlayOrDiscardAction(inputs, Action.Type.DISCARD);
        }
        if(command.equals("play") || command.equals("p")) {
            return parsePlayOrDiscardAction(inputs, Action.Type.PLAY);
        }
        if(command.equals("hint") || command.equals("h")) {
            if(inputs.length < 4){
                System.out.println(input);
                for(String in : inputs){
                    System.out.println(in);
                }
                System.out.println("Expected at least 3 inputs for command " + command);
                return null;
            }
            int playerNum = Integer.parseInt(inputs[1]);
            if(playerNum < 1 || playerNum > players.size()) {
                System.out.println("Can't hint to a player # outside of range 1-" + players.size() + ".");
                return null;
            }
            Player player = players.get(playerNum - 1);
            String[] cardNumStrings = inputs[2].split(",");
            List<Integer> cardNums = new ArrayList<>();
            for(String str : cardNumStrings) {
                int cardNum = Integer.parseInt(str);
                if(cardNum < 1 || cardNum > player.hand.size()) {
                    System.out.println("Can't hint card outside of range 1-" + player.hand.size() + ".");
                    return null;
                }
                cardNums.add(cardNum - 1);
            }
            HintType hintType = parseHintType(inputs[3], player);
            if(hintType == null){
                return null;
            }
            return Action.createHintAction(player, cardNums, hintType);
        }
        return null;
    }

    private HintType parseHintType(String input, Player player) {
        Map<String, Suit> validSuitInputs = new HashMap<>();
        for(Suit suit : Suit.values()) {
            String suitName = suit.toString().toLowerCase();
            validSuitInputs.put(suitName, suit);
            validSuitInputs.put(suitName.substring(0, 1), suit);
        }

        if(validSuitInputs.containsKey(input)) {
            return new HintType(validSuitInputs.get(input));
        }
        int cardNum = Integer.parseInt(input);
        if(cardNum < 1 || cardNum > 5) {
            System.out.println("Can't hint for a card number outside of range 1-5");
            return null;
        }
        return new HintType(cardNum);
    }

    private Action parsePlayOrDiscardAction(String[] inputs, Action.Type type) throws NumberFormatException {
        String command = "play";
        if(type == Action.Type.DISCARD){
            command = "discard";
        }
        if(inputs.length < 2) {
            System.out.println("Expected second input for command " + command);
            return null;
        }
        int cardNum = Integer.parseInt(inputs[1]);
        if(cardNum < 1 || cardNum > hand.size()){
            System.out.println("Can't " + command + " card outside of range 1-" + hand.size() + ".");
            return null;
        }
        if(type == Action.Type.DISCARD) {
            return Action.createDiscardAction(this, cardNum - 1);
        }
        return Action.createPlayAction(this, cardNum - 1);
    }

    private void displayHelp() {
        System.out.println("Commands: ");
        System.out.println("  'discard {N}' or short form 'd N' to discard the Nth in your hand from the left");
        System.out.println("  'play {N}' or short form 'p N' to play the Nth card in your hand from the left");
        System.out.println("  'hint {P} {N,M,...} {S or N}' or short form 'h' to hint player P's cards.");
        System.out.println("    e.g. 'hint 2 2,5 Red' to hint player 2's 2nd and 5th cards as red.");
        System.out.println("    Suits can also be shortened to their beginning letter");
        System.out.println("  'info' to re-print the game information");
    }
}
