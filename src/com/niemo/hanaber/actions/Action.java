package com.niemo.hanaber.actions;

import com.niemo.hanaber.players.Player;

import java.util.Collections;
import java.util.List;

public class Action {

    public Type type;
    public Player affectedPlayer;
    public List<Integer> cardNums;
    public HintType hintType;

    private Action(Type type, Player player, List<Integer> cardNums) {
        this.type = type;
        this.affectedPlayer = player;
        this.cardNums = cardNums;
    }

    private Action(Type type, Player player, List<Integer> cardNums, HintType hintType) {
        this(type, player, cardNums);
        this.hintType = hintType;
    }

    public static Action createDiscardAction(Player self, int cardNum) {
        return new Action(Type.DISCARD, self, Collections.singletonList(cardNum));
    }

    public static Action createPlayAction(Player self, int cardNum) {
        return new Action(Type.PLAY, self, Collections.singletonList(cardNum));
    }

    public static Action createHintAction(Player player, List<Integer> cardNums, HintType hintType) {
        return new Action(Type.HINT, player, cardNums, hintType);
    }

    public enum Type {
        DISCARD,
        PLAY,
        HINT
    }

}