package hpiz.reaction.com.reaction.miniGames.cardMatchReaction;

import java.util.Random;

/**
 * Created by Chris on 5/6/2017.
 */

public class PlayingCard {
    private String suite;
    private int value;


    public PlayingCard() {
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomCard = rand.nextInt((13 - 1) + 1) + 1;


        int randomSuite = rand.nextInt((4 - 1) + 1) + 1;

        switch (randomSuite) {
            case 1:
                suite = "Diamonds";
                break;
            case 2:
                suite = "Hearts";
                break;
            case 3:
                suite = "Clubs";
                break;
            case 4:
                suite = "Spades";
                break;
        }
    }
}
