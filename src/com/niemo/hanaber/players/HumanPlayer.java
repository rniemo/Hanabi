package com.niemo.hanaber.players;

import com.niemo.hanaber.actions.Action;
import com.niemo.hanaber.actions.HintType;
import com.niemo.hanaber.model.Card;
import com.niemo.hanaber.model.GameState;
import com.niemo.hanaber.model.Suit;

import java.util.*;

public class HumanPlayer extends Player {

    @Override
    public Action takeTurn(GameState gameState, List<Player> otherPlayers) {
        Action parsedAction = null;
        displayInformation(gameState, otherPlayers);
        do {
            System.out.print("Enter your action, or 'help' if you're lost: ");
            Scanner scanner = new Scanner(System.in);
            // get their input as a String
            String input = scanner.nextLine();
            if (input.equals("help")) {
                displayHelp();
            } else if (input.equals("info")) {
                displayInformation(gameState, otherPlayers);
            } else {
                try {

                    parsedAction = parseAction(input, otherPlayers);
                }catch(NumberFormatException e) {
                    System.out.println("Expected integer in command, failed to parse.");
                }
            }
        }while(parsedAction == null);
        return parsedAction;
    }

    /**
     * Displays the information that the current player can see. E.g. game state, other players' cards, etc.
     */
    private void displayInformation(GameState gameState, List<Player> otherPlayers) {
        System.out.println();
        System.out.println("Explosions remaining: " + gameState.boomsAvailable);
        System.out.println("Hints remaining: " + gameState.hintsAvailable);
        int playerNum = 0;
        for(Player player : otherPlayers) {
            System.out.print("Player " + (playerNum + 1) + ": ");
            for(Card card : player.getHand()) {
                System.out.print(card.getSuit() + "_" + card.getNumber() + ", ");
            }
            System.out.println();
        }
        System.out.println("Board state:");
        gameState.printFireworkState();
        System.out.println();
        System.out.print("Your cards: ");
        for(Card card : this.hand) {
            System.out.print(card.getHintText() + ", ");
        }
        System.out.println();
    }

    /**
     * Takes in an input string from the user and the other players' data, and attempts to convert
     * the string to an Action. Performs basic boundary checks on indices.
     * Returns null if the action could not be parsed.
     */
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
            return parseHintAction(inputs, players);
        }
        return null;
    }

    /**
     * Parses a hint action from a list of inputs provided by the user, and a list of players.
     * The inputs are the arguments to the "hint" command. i.e. input[0] is not "hint", but instead the player #
     */
    private Action parseHintAction(String[] inputs, List<Player> otherPlayers ) {
        if(inputs.length < 4){
            System.out.println("Expected at least 3 inputs for command hint");
            return null;
        }
        int playerNum = Integer.parseInt(inputs[1]);
        if(playerNum < 1 || playerNum > otherPlayers.size()) {
            System.out.println("Can't hint to a player # outside of range 1-" + otherPlayers.size() + ".");
            return null;
        }
        Player player = otherPlayers.get(playerNum - 1);
        String[] cardNumStrings = inputs[2].split(",");
        List<Integer> cardNums = new ArrayList<>();
        for(String str : cardNumStrings) {
            int cardNum = Integer.parseInt(str);
            if(cardNum < 1 || cardNum > player.hand.size()) {
                System.out.println("Can't hint card outside of range 1-" + player.hand.size() + ".");
                return null;
            }
            cardNums.add(cardNum);
        }
        HintType hintType = parseHintType(inputs[3]);
        if(hintType == null){
            return null;
        }
        return Action.createHintAction(player, cardNums, hintType);
    }

    /**
     * Parses the hint type for a given user input. Attempts to first parse the input as a suit. For the input to be
     * a suit, it can either be the full suit e.g. "red" or just the first letter of the suit e.g. "r". If the input
     * could not be parsed as a suit, it's parsed as an integer and checked to be between 1-5.
     * If the input could not be parsed, null is returned.
     */
    private HintType parseHintType(String input) {
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

    /**
     * Parses a play or discard action from a list of inputs.
     * The inputs are the arguments to the command. i.e. input[0] is not "hint", but instead the card #.
     */
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
            return Action.createDiscardAction(this, cardNum);
        }
        return Action.createPlayAction(this, cardNum);
    }

    /**
     * Displays the commands the user can use to play.
     */
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
