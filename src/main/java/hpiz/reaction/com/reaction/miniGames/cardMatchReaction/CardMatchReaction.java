package hpiz.reaction.com.reaction.miniGames.cardMatchReaction;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hpiz.reaction.com.reaction.GameActivity;
import hpiz.reaction.com.reaction.R;

import static android.R.attr.value;
import static hpiz.reaction.com.reaction.R.id.backToMainMenuButton;
import static hpiz.reaction.com.reaction.R.id.blueScoreText;
import static hpiz.reaction.com.reaction.R.id.playAgainButton;
import static hpiz.reaction.com.reaction.R.id.redScoreText;

/**
 * Created by Chris on 5/2/2017.
 */

public class CardMatchReaction extends Activity {
    private int redScore;
    private CardMatchReactionBackgroundTask runGame;
    private int blueScore;
    private TextView topHalf;
    private TextView bottomHalf;
    private Button pAgainButton;
    private SharedPreferences sp;
    private RelativeLayout contentContainer;
    private Button bToMainMenuButton;
    private TextView bScoreText;
    private TextView rScoreText;
    private int winningScore = 10;
    private PlayingCard newPlayingCard;
    private PlayingCard lastPlayingCard;
    private ImageView lCard;
    private ImageView rCard;
    private int imageResourceId;
    private ValueAnimator va;
    private boolean left;
    private boolean firstCard;
    private long flyInDuration = 200;
    private int[] valueHistory;
    private String[] suiteHistory;
    private String TAG = "CardMatchReaction";

    public CardMatchReaction() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("runningPreferences", MODE_PRIVATE);

        run();
    }

    public void run() {

        setContentView(R.layout.minigame_cardmatchreaction);

        Log.v(TAG, " setting game layout");

        hide();

        initializeButtonReactionObjects();

        runSingleCard();
    }

    public void hide() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Hide UI first
        //mControlsView.setVisibility(View.GONE);
        //mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay

        contentContainer = (RelativeLayout) findViewById(R.id.contentContainer);
        contentContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE

                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


    }


    protected void runSingleCard() {
        if (redScore < winningScore) {
            if (blueScore < winningScore) {
                //updateScores();
                //clearScreen();

                //setEarlyListeners();
                runGame = new CardMatchReactionBackgroundTask(this);
                runGame.execute("RUN");
            } else {
                blueWonGame();
            }
        } else {
            redWonGame();
        }

    }


    private void topWon() {
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        bottomHalf.setBackgroundColor(Color.RED);
        bottomHalf.setText("You Lost");

        topHalf.setText("You Won");
        redScore++;
        runGame.cancel(true);
        updateScores();
        if (redScore > (winningScore - 1)) {
            redWonGame();
        }
        if (blueScore > (winningScore - 1)) {
            blueWonGame();
        }
        Log.v(TAG, "Blue Score: " + String.valueOf(blueScore));
        Log.v(TAG, "Red Score: " + String.valueOf(redScore));
    }

    public void redWonGame() {
        runGame.cancel(true);

        pAgainButton.setVisibility(View.VISIBLE);
        pAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runGame();
            }
        });
        bToMainMenuButton.setVisibility(View.VISIBLE);
        bToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CardMatchReaction.this, GameActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        topHalf.setBackgroundColor(Color.RED);
        bottomHalf.setBackgroundColor(Color.RED);
        bottomHalf.setText("You lost to Red " + String.valueOf(redScore) + " to " + String.valueOf(blueScore) + ".");
        topHalf.setText("You beat Blue " + String.valueOf(redScore) + " to " + String.valueOf(blueScore) + ".");
    }

    @Override
    public void onBackPressed() {
        if (runGame != null) {
            runGame.cancel(true);
            runGame = null;
        }
        Intent i = new Intent(CardMatchReaction.this, GameActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void startButtonListeners() {
        topHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                topWon();
                if (bottomHalf.hasOnClickListeners()) {
                    bottomHalf.setOnClickListener(null);
                }
                nextRound();
            }
        });
        bottomHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomWon();
                if (topHalf.hasOnClickListeners()) {
                    topHalf.setOnClickListener(null);
                }
                nextRound();
            }
        });
    }

    public void runGame() {
        setContentView(R.layout.minigame_surprisereaction);
        Log.v(TAG, " setting game layout");
        hide();
        initializeButtonReactionObjects();
        runSingleCard();

    }

    public void setTopRed() {
        topHalf.setBackgroundColor(Color.RED);
    }


    public void setBottomBlue() {
        bottomHalf.setBackgroundColor(Color.BLUE);
    }

    public void setTopBlack() {
        topHalf.setBackgroundColor(Color.BLACK);
    }

    public void setBottomBlack() {
        bottomHalf.setBackgroundColor(Color.BLACK);
    }


    public void blueWonGame() {
        runGame.cancel(true);
        pAgainButton.setVisibility(View.VISIBLE);
        pAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runGame();
            }
        });
        bToMainMenuButton.setVisibility(View.VISIBLE);
        bToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CardMatchReaction.this, GameActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(i);
            }
        });
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        topHalf.setBackgroundColor(Color.BLUE);
        bottomHalf.setBackgroundColor(Color.BLUE);
        bottomHalf.setText("You beat Red " + String.valueOf(blueScore) + " to " + String.valueOf(redScore) + ".");
        topHalf.setText("You lost to Blue " + String.valueOf(blueScore) + " to " + String.valueOf(redScore) + ".");
    }

    private void bottomWon() {
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        topHalf.setBackgroundColor(Color.BLUE);
        bottomHalf.setText("You Won");
        topHalf.setText("You Lost");
        blueScore++;
        runGame.cancel(true);
        updateScores();
        if (redScore > (winningScore - 1)) {
            redWonGame();
        }
        if (blueScore > (winningScore - 1)) {
            blueWonGame();
        }
        Log.v(TAG, "Blue Score: " + String.valueOf(blueScore));
        Log.v(TAG, "Red Score: " + String.valueOf(redScore));
    }

    private void updateScores() {
        rScoreText.setText("Red Score: " + String.valueOf(redScore));
        bScoreText.setText("Blue Score: " + String.valueOf(blueScore));
    }

    private void initializeButtonReactionObjects() {
        firstCard = true;
        left = true;
        redScore = 0;
        blueScore = 0;
        pAgainButton = (Button) super.findViewById(playAgainButton);
        pAgainButton.setVisibility(View.GONE);
        pAgainButton.setOnClickListener(null);

        bToMainMenuButton = (Button) findViewById(backToMainMenuButton);

        bToMainMenuButton.setVisibility(View.GONE);
        bToMainMenuButton.setOnClickListener(null);
        topHalf = (TextView) findViewById(R.id.topHalf);
        bottomHalf = (TextView) findViewById(R.id.bottomHalf);
        rScoreText = (TextView) findViewById(redScoreText);
        bScoreText = (TextView) findViewById(blueScoreText);
        rScoreText.setTextColor(Color.WHITE);
        bScoreText.setTextColor(Color.WHITE);
        rScoreText.setBackgroundColor(Color.RED);
        bScoreText.setBackgroundColor(Color.BLUE);
        lCard = (ImageView) findViewById(R.id.leftCard);
        rCard = (ImageView) findViewById(R.id.rightCard);
        // lCard.setVisibility(View.INVISIBLE);
        //  rCard.setVisibility(View.INVISIBLE);
    }

    public void nextRound() {
        runGame = new CardMatchReactionBackgroundTask(this);
        runGame.execute("SLEEP:1000");
    }


    public void showACard() {
        if (newPlayingCard != null) {
            lastPlayingCard = newPlayingCard;
        }
        newPlayingCard = new PlayingCard();

        int id = getImageResourceId();


        if (valueHistory == null) {
            valueHistory = new int[3];
        } else {
            valueHistory[1] = valueHistory[0];
            valueHistory[2] = valueHistory[1];
            valueHistory[0] = newPlayingCard.getValue();
        }
        if (suiteHistory == null) {
            suiteHistory = new String[3];
        } else {
            suiteHistory[1] = suiteHistory[0];
            suiteHistory[2] = suiteHistory[1];
            suiteHistory[0] = newPlayingCard.getSuite();
        }
        Log.d(TAG, "History addition=" + String.valueOf(valueHistory[0]));
        Log.d(TAG, "History addition=" + String.valueOf(suiteHistory[0]));


        Log.d(TAG, "newPlayingCard local Image Resource=" + String.valueOf(id));
        Log.d(TAG, "newPlayingCard objects Image Resource=" + String.valueOf(newPlayingCard.getImageResource()));
        Log.d(TAG, "newPlayingCard value=" + String.valueOf(newPlayingCard.getValue()) + " of " + newPlayingCard.getSuite());

        if (left) {

            animateLeftCardIn(newPlayingCard.getImageResource());
            left = false;
        } else {

            animateRightCardIn(newPlayingCard.getImageResource());
            left = true;
        }
        //waitAndRunAgain();
        runSingleCard();
    }

    private void animateRightCardIn(int imageResource) {
        rCard.bringToFront();
        va = ValueAnimator.ofFloat(2000, 0);
        va.setDuration(flyInDuration);
        va.setInterpolator(new DecelerateInterpolator(1.5F));
        // va.setInterpolator(new AccelerateInterpolator());
        /*
        va.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return (float) (0.098*input*input);
            }
        });
        */
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                //Log.v(TAG, "Moving ruler to y=" + animation.getAnimatedValue());
                //Log.v(TAG, "Bottom Position=" + String.valueOf(bottomPosition));
                //Log.v(TAG, "bVel=" + String.valueOf(bVel));
                float value = (float) animation.getAnimatedValue();
                rCard.setTranslationX(value);

            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


                rCard.setImageResource(newPlayingCard.getImageResource());
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // setBallsInvisible();

                // if (!cancelled) {
                //     pickNextBall();
                //     throwUp();
                // }


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // topBall.setTranslationY(410 * -1);
                // bottomBall.setTranslationY(410);
                // topBall.setVisibility(View.INVISIBLE);
                // bottomBall.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();
    }

    private void waitAndRunAgain() {
        runGame = new CardMatchReactionBackgroundTask(this);
        runGame.execute("SLEEP:1000");
    }

    private void animateLeftCardIn(int imageResource) {
        lCard.bringToFront();
        va = ValueAnimator.ofFloat(2000, 0);
        va.setDuration(flyInDuration);
        va.setInterpolator(new DecelerateInterpolator(1.5F));
        // va.setInterpolator(new AccelerateInterpolator());
        /*
        va.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return (float) (0.098*input*input);
            }
        });
        */
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //Log.v(TAG, "Moving ruler to y=" + animation.getAnimatedValue());
                //Log.v(TAG, "Bottom Position=" + String.valueOf(bottomPosition));
                //Log.v(TAG, "bVel=" + String.valueOf(bVel));
                float value = (float) animation.getAnimatedValue();
                lCard.setTranslationX(-value);

            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                lCard.setImageResource(View.VISIBLE);
                lCard.setImageResource(newPlayingCard.getImageResource());

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // setBallsInvisible();

                // if (!cancelled) {
                //     pickNextBall();
                //     throwUp();
                // }


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // topBall.setTranslationY(410 * -1);
                // bottomBall.setTranslationY(410);
                // topBall.setVisibility(View.INVISIBLE);
                // bottomBall.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();
    }


    private int getImageResourceId() {
        switch (newPlayingCard.getSuite()) {
            case "Diamonds":
                switch (newPlayingCard.getValue()) {
                    case 1:
                        return R.drawable.ace_of_diamonds;
                    case 2:
                        return R.drawable.two_of_diamonds;
                    case 3:
                        return R.drawable.three_of_diamonds;
                    case 4:
                        return R.drawable.four_of_diamonds;
                    case 5:
                        return R.drawable.five_of_diamonds;
                    case 6:
                        return R.drawable.six_of_diamonds;
                    case 7:
                        return R.drawable.seven_of_diamonds;
                    case 8:
                        return R.drawable.eight_of_diamonds;
                    case 9:
                        return R.drawable.nine_of_diamonds;
                    case 10:
                        return R.drawable.ten_of_diamonds;
                    case 11:
                        return R.drawable.jack_of_diamonds;
                    case 12:
                        return R.drawable.queen_of_diamonds;
                    case 13:
                        return R.drawable.king_of_diamonds;
                }
                break;
            case "Hearts":
                switch (value) {
                    case 1:
                        return R.drawable.ace_of_hearts;
                    case 2:
                        return R.drawable.two_of_hearts;
                    case 3:
                        return R.drawable.three_of_hearts;
                    case 4:
                        return R.drawable.four_of_hearts;
                    case 5:
                        return R.drawable.five_of_hearts;
                    case 6:
                        return R.drawable.six_of_hearts;
                    case 7:
                        return R.drawable.seven_of_hearts;
                    case 8:
                        return R.drawable.eight_of_hearts;
                    case 9:
                        return R.drawable.nine_of_hearts;
                    case 10:
                        return R.drawable.ten_of_hearts;
                    case 11:
                        return R.drawable.jack_of_hearts;
                    case 12:
                        return R.drawable.queen_of_hearts;
                    case 13:
                        return R.drawable.king_of_hearts;
                }
                break;
            case "Clubs":
                switch (value) {
                    case 1:
                        return R.drawable.ace_of_clubs;
                    case 2:
                        return R.drawable.two_of_clubs;
                    case 3:
                        return R.drawable.three_of_clubs;
                    case 4:
                        return R.drawable.four_of_clubs;
                    case 5:
                        return R.drawable.five_of_clubs;
                    case 6:
                        return R.drawable.six_of_clubs;
                    case 7:
                        return R.drawable.seven_of_clubs;
                    case 8:
                        return R.drawable.eight_of_clubs;
                    case 9:
                        return R.drawable.nine_of_clubs;
                    case 10:
                        return R.drawable.ten_of_clubs;
                    case 11:
                        return R.drawable.jack_of_clubs;
                    case 12:
                        return R.drawable.queen_of_clubs;
                    case 13:
                        return R.drawable.king_of_clubs;
                }
                break;
            case "Spades":
                switch (value) {
                    case 1:
                        return R.drawable.ace_of_spades;
                    case 2:
                        return R.drawable.two_of_spades;
                    case 3:
                        return R.drawable.three_of_spades;
                    case 4:
                        return R.drawable.four_of_spades;
                    case 5:
                        return R.drawable.five_of_spades;
                    case 6:
                        return R.drawable.six_of_spades;
                    case 7:
                        return R.drawable.seven_of_spades;
                    case 8:
                        return R.drawable.eight_of_spades;
                    case 9:
                        return R.drawable.nine_of_spades;
                    case 10:
                        return R.drawable.ten_of_spades;
                    case 11:
                        return R.drawable.jack_of_spades;
                    case 12:
                        return R.drawable.queen_of_spades;
                    case 13:
                        return R.drawable.king_of_spades;
                }
                break;
        }
        return 0;
    }
}
