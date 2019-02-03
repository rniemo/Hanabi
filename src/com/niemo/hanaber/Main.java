package com.niemo.hanaber;


public class Main {

    public static void main(String[] args) {
        Game game = new Game(2, 5, true);
        game.setup();
        game.play();
    }
}
