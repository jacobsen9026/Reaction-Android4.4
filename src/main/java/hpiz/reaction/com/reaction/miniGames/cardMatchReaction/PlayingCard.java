package hpiz.reaction.com.reaction.miniGames.cardMatchReaction;

import java.util.Random;

import hpiz.reaction.com.reaction.R;


/**
 * Created by Chris on 5/6/2017.
 */

public class PlayingCard {
    private String suite;
    private int value;
    private int imageResourceId;
    private String TAG = "PlayingCard";


    public PlayingCard() {
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        value = rand.nextInt((13 - 1) + 1) + 1;

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
        setImageResourceId();
    }

    public PlayingCard(int faceValue, String cardSuite) {
        suite = cardSuite;

        value = faceValue;
        setImageResourceId();
    }

    public PlayingCard(int faceValue, int cardSuite) {
        switch (cardSuite) {
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

        value = faceValue;
        setImageResourceId();
    }

    private void setImageResourceId() {
        switch (suite) {
            case "Diamonds":
                switch (value) {
                    case 1:
                        imageResourceId = R.drawable.ace_of_diamonds;
                        break;
                    case 2:
                        imageResourceId = R.drawable.two_of_diamonds;
                        break;
                    case 3:
                        imageResourceId = R.drawable.three_of_diamonds;
                        break;
                    case 4:
                        imageResourceId = R.drawable.four_of_diamonds;
                        break;
                    case 5:
                        imageResourceId = R.drawable.five_of_diamonds;
                        break;
                    case 6:
                        imageResourceId = R.drawable.six_of_diamonds;
                        break;
                    case 7:
                        imageResourceId = R.drawable.seven_of_diamonds;
                        break;
                    case 8:
                        imageResourceId = R.drawable.eight_of_diamonds;
                        break;
                    case 9:
                        imageResourceId = R.drawable.nine_of_diamonds;
                        break;
                    case 10:
                        imageResourceId = R.drawable.ten_of_diamonds;
                        break;
                    case 11:
                        imageResourceId = R.drawable.jack_of_diamonds2;
                        break;
                    case 12:
                        imageResourceId = R.drawable.queen_of_diamonds2;
                        break;
                    case 13:
                        imageResourceId = R.drawable.king_of_diamonds2;
                        break;
                }
                break;
            case "Hearts":
                switch (value) {
                    case 1:
                        imageResourceId = R.drawable.ace_of_hearts;
                        break;
                    case 2:
                        imageResourceId = R.drawable.two_of_hearts;
                        break;
                    case 3:
                        imageResourceId = R.drawable.three_of_hearts;
                        break;
                    case 4:
                        imageResourceId = R.drawable.four_of_hearts;
                        break;
                    case 5:
                        imageResourceId = R.drawable.five_of_hearts;
                        break;
                    case 6:
                        imageResourceId = R.drawable.six_of_hearts;
                        break;
                    case 7:
                        imageResourceId = R.drawable.seven_of_hearts;
                        break;
                    case 8:
                        imageResourceId = R.drawable.eight_of_hearts;
                        break;
                    case 9:
                        imageResourceId = R.drawable.nine_of_hearts;
                        break;
                    case 10:
                        imageResourceId = R.drawable.ten_of_hearts;
                        break;
                    case 11:
                        imageResourceId = R.drawable.jack_of_hearts2;
                        break;
                    case 12:
                        imageResourceId = R.drawable.queen_of_hearts2;
                        break;
                    case 13:
                        imageResourceId = R.drawable.king_of_hearts2;
                        break;
                }
                break;
            case "Clubs":
                switch (value) {
                    case 1:
                        imageResourceId = R.drawable.ace_of_clubs;
                        break;
                    case 2:
                        imageResourceId = R.drawable.two_of_clubs;
                        break;
                    case 3:
                        imageResourceId = R.drawable.three_of_clubs;
                        break;
                    case 4:
                        imageResourceId = R.drawable.four_of_clubs;
                        break;
                    case 5:
                        imageResourceId = R.drawable.five_of_clubs;
                        break;
                    case 6:
                        imageResourceId = R.drawable.six_of_clubs;
                        break;
                    case 7:
                        imageResourceId = R.drawable.seven_of_clubs;
                        break;
                    case 8:
                        imageResourceId = R.drawable.eight_of_clubs;
                        break;
                    case 9:
                        imageResourceId = R.drawable.nine_of_clubs;
                        break;
                    case 10:
                        imageResourceId = R.drawable.ten_of_clubs;
                        break;
                    case 11:
                        imageResourceId = R.drawable.jack_of_clubs2;
                        break;
                    case 12:
                        imageResourceId = R.drawable.queen_of_clubs2;
                        break;
                    case 13:
                        imageResourceId = R.drawable.king_of_clubs2;
                        break;
                }
                break;
            case "Spades":
                switch (value) {
                    case 1:
                        imageResourceId = R.drawable.ace_of_spades;
                        break;
                    case 2:
                        imageResourceId = R.drawable.two_of_spades;
                        break;
                    case 3:
                        imageResourceId = R.drawable.three_of_spades;
                        break;
                    case 4:
                        imageResourceId = R.drawable.four_of_spades;
                        break;
                    case 5:
                        imageResourceId = R.drawable.five_of_spades;
                        break;
                    case 6:
                        imageResourceId = R.drawable.six_of_spades;
                        break;
                    case 7:
                        imageResourceId = R.drawable.seven_of_spades;
                        break;
                    case 8:
                        imageResourceId = R.drawable.eight_of_spades;
                        break;
                    case 9:
                        imageResourceId = R.drawable.nine_of_spades;
                        break;
                    case 10:
                        imageResourceId = R.drawable.ten_of_spades;
                        break;
                    case 11:
                        imageResourceId = R.drawable.jack_of_spades2;
                        break;
                    case 12:
                        imageResourceId = R.drawable.queen_of_spades2;
                        break;
                    case 13:
                        imageResourceId = R.drawable.king_of_spades2;
                        break;
                }
                break;
        }

    }

    public int getImageResource() {
        return imageResourceId;
    }

    public String getSuite() {
        return suite;
    }

    public int getValue() {
        return value;
    }
}
