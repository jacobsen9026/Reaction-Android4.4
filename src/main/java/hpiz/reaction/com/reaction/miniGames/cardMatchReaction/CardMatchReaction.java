package hpiz.reaction.com.reaction.miniGames.cardMatchReaction;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import hpiz.reaction.com.reaction.GameActivity;
import hpiz.reaction.com.reaction.R;

import static hpiz.reaction.com.reaction.R.id.backToMainMenuButton;
import static hpiz.reaction.com.reaction.R.id.blueScoreText;
import static hpiz.reaction.com.reaction.R.id.centerCardText_Bottom;
import static hpiz.reaction.com.reaction.R.id.centerCardText_Top;
import static hpiz.reaction.com.reaction.R.id.leftCardText_Bottom;
import static hpiz.reaction.com.reaction.R.id.leftCardText_Top;
import static hpiz.reaction.com.reaction.R.id.playAgainButton;
import static hpiz.reaction.com.reaction.R.id.redScoreText;
import static hpiz.reaction.com.reaction.R.id.rightCardText_Bottom;
import static hpiz.reaction.com.reaction.R.id.rightCardText_Top;

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
    private ImageView lCardPlaceHolder;
    private TextView lCardText_Top;
    private TextView rCardText_Top;
    private TextView cCardText_Top;
    private TextView cCardText_Bottom;
    private TextView rCardText_Bottom;
    private TextView lCardText_Bottom;
    private ImageView rCardPlaceHolder;
    private ImageView cCard;
    private ValueAnimator va;
    private boolean left;
    private long flyInDuration = 300;
    private int[] valueHistory = new int[3];
    private String[] suiteHistory = new String[3];
    private String TAG = "CardMatchReaction";
    private ValueAnimator va2;
    private ValueAnimator rightCardAnimator;
    private boolean rightCardAnimatorCanceled;
    private boolean leftCardAnimatorCanceled;

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
        startButtonListeners();

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
        //topHalf.setOnClickListener(null);
        //bottomHalf.setOnClickListener(null);
        //bottomHalf.setBackgroundColor(Color.RED);
        bottomHalf.setText(getLoseText());

        topHalf.setText(getWinText());
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

    private void bottomWon() {
        //topHalf.setOnClickListener(null);
        //bottomHalf.setOnClickListener(null);
        //bottomHalf.setBackgroundColor(Color.RED);
        bottomHalf.setText(getWinText());

        topHalf.setText(getLoseText());
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

    private String getWinText() {
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int r = rand.nextInt((4 - 1) + 1) + 1;
        switch (r) {
            case 1:
                return "Your faster than a speeding ticket!";
            case 2:
                return "Nice one!";
            case 3:
                return "Don't break the screen!";
            case 4:
                return "Winner winner chicken dinner.";
            default:
                return "Nice one!";
        }

    }

    private String getLoseText() {
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int r = rand.nextInt((4 - 1) + 1) + 1;
        switch (r) {
            case 1:
                return "Go drink some coffee.";
            case 2:
                return "Pick up the pace!";
            case 3:
                return "Not quick enough.";
            case 4:
                return "Are you awake over there.";
            default:
                return "Not quick enough.";
        }

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
                topHalf.setOnClickListener(null);
                runGame.cancel(true);
                if (valueHistory[0] == valueHistory[1]) {


                    topWon();
                }
                animateCardHistoryExpand();
                if (bottomHalf.hasOnClickListeners()) {
                    bottomHalf.setOnClickListener(null);
                }
                //nextRound();
            }
        });
        bottomHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomHalf.setOnClickListener(null);
                runGame.cancel(true);
                if (valueHistory[0] == valueHistory[1]) {


                    bottomWon();
                }
                animateCardHistoryExpand();
                if (topHalf.hasOnClickListeners()) {
                    topHalf.setOnClickListener(null);
                }
                // nextRound();
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


    private void updateScores() {
        rScoreText.setText("Red Score: " + String.valueOf(redScore));
        bScoreText.setText("Blue Score: " + String.valueOf(blueScore));
    }

    private void initializeButtonReactionObjects() {

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
        rCardText_Top = (TextView) findViewById(rightCardText_Top);
        rCardText_Bottom = (TextView) findViewById(rightCardText_Bottom);
        lCardText_Bottom = (TextView) findViewById(leftCardText_Bottom);
        lCardText_Top = (TextView) findViewById(leftCardText_Top);
        cCardText_Top = (TextView) findViewById(centerCardText_Top);
        cCardText_Bottom = (TextView) findViewById(centerCardText_Bottom);
        bScoreText = (TextView) findViewById(blueScoreText);
        rScoreText.setTextColor(Color.WHITE);
        bScoreText.setTextColor(Color.WHITE);
        rScoreText.setBackgroundColor(Color.RED);
        bScoreText.setBackgroundColor(Color.BLUE);
        lCard = (ImageView) findViewById(R.id.leftCard);
        rCard = (ImageView) findViewById(R.id.rightCard);
        lCardPlaceHolder = (ImageView) findViewById(R.id.rightCardPlaceHolder);
        rCardPlaceHolder = (ImageView) findViewById(R.id.leftCardPlaceHolder);
        cCard = (ImageView) findViewById(R.id.centerCard);
        lCard.setTranslationX(9000);
        //rCard.setTranslationX(9000);
        cCard.setTranslationX(9000);
        lCardText_Bottom.setAlpha(0);
        lCardText_Top.setAlpha(0);
        rCardText_Bottom.setAlpha(0);
        rCardText_Top.setAlpha(0);
        cCardText_Bottom.setAlpha(0);
        cCardText_Top.setAlpha(0);
        bottomHalf.setAlpha(0);
        topHalf.setAlpha(0);

        // lCard.setVisibility(View.INVISIBLE);
        //  rCard.setVisibility(View.INVISIBLE);
    }

    public void nextRound() {
        runGame = new CardMatchReactionBackgroundTask(this);
        runGame.execute("SLEEP:1000");
    }


    public void showACard() {
        if (newPlayingCard != null) {
            Random rand = new Random();

            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            int randomNumber = rand.nextInt((20 - 1) + 1) + 1;
            int randomSuite = rand.nextInt((4 - 1) + 1) + 1;
            if (randomNumber == 1) {
                PlayingCard holder = newPlayingCard;
                newPlayingCard = new PlayingCard(holder.getValue(), randomSuite);
            } else {
                newPlayingCard = new PlayingCard();

            }

        } else {
            newPlayingCard = new PlayingCard();
        }

        Log.v(TAG, String.valueOf(valueHistory[0]) + " of " + suiteHistory[0]);
        Log.v(TAG, String.valueOf(valueHistory[1]) + " of " + suiteHistory[1]);
        Log.v(TAG, String.valueOf(valueHistory[2]) + " of " + suiteHistory[2]);
/*
        Log.d(TAG, "History addition=" + String.valueOf(valueHistory[0]));
        Log.d(TAG, "History addition=" + String.valueOf(suiteHistory[0]));
*/
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

    private void updateHistory() {

        if (valueHistory == null) {

        } else {
            valueHistory[2] = valueHistory[1];
            valueHistory[1] = valueHistory[0];

            valueHistory[0] = newPlayingCard.getValue();
        }
        if (suiteHistory == null) {

        } else {
            suiteHistory[2] = suiteHistory[1];
            suiteHistory[1] = suiteHistory[0];

            suiteHistory[0] = newPlayingCard.getSuite();
        }
    }

    private void animateCardHistoryExpand() {
        if (rightCardAnimator != null) {
            rightCardAnimator.cancel();
        }
        Log.v(TAG, "Invoke animateCardHistoryExpand");
        Log.v(TAG, "cCard Position:" + String.valueOf(cCard.getWidth()));
        Log.v(TAG, "lCardPlaceHolder Position:" + String.valueOf(lCardPlaceHolder.getX()));
        float translationDistance = cCard.getWidth() * 0.66F;
        Log.v(TAG, "Translation Distance = " + String.valueOf(translationDistance));
        va2 = ValueAnimator.ofFloat(0, 1);
        va2.setDuration(1000);
        va2.setInterpolator(new AccelerateDecelerateInterpolator());

        va2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                rCardText_Top.setAlpha(value);
                //lCardText_Top.setAlpha(value);
                cCardText_Top.setAlpha(value);
                rCardText_Bottom.setAlpha(value);
                //lCardText_Bottom.setAlpha(value);
                cCardText_Bottom.setAlpha(value);
                topHalf.setAlpha(value);
                bottomHalf.setAlpha(value);


            }
        });
        va2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateCardHistoryContract();
                    }
                }, 1000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        va = ValueAnimator.ofFloat(0, translationDistance);
        va.setDuration(1000);
        va.setInterpolator(new AccelerateDecelerateInterpolator());

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                rCard.setTranslationX(value);
                cCard.setTranslationX(-value);

                rCardText_Top.setTranslationX(value);
                cCardText_Top.setTranslationX(-value);
                rCardText_Bottom.setTranslationX(value);
                cCardText_Bottom.setTranslationX(-value);


            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                lCard.setTranslationX(10000);
                cCard.setTranslationX(0);
                rCard.setTranslationX(0);
                lCard.setVisibility(View.INVISIBLE);
                if (suiteHistory[2] != null) {
                    Log.v(TAG, String.valueOf(valueHistory[0]) + " of " + suiteHistory[0]);
                    Log.v(TAG, String.valueOf(valueHistory[1]) + " of " + suiteHistory[1]);
                    Log.v(TAG, String.valueOf(valueHistory[2]) + " of " + suiteHistory[2]);
                    PlayingCard firstCard = new PlayingCard(valueHistory[0], suiteHistory[0]);
                    PlayingCard secondCard = new PlayingCard(valueHistory[1], suiteHistory[1]);
                    PlayingCard thirdCard = new PlayingCard(valueHistory[2], suiteHistory[2]);
                    lCard.setImageResource(thirdCard.getImageResource());


                    lCard.bringToFront();
                    cCard.setImageResource(secondCard.getImageResource());
                    cCard.bringToFront();
                    rCard.setImageResource(firstCard.getImageResource());
                    rCard.bringToFront();

                    cCard.setVisibility(View.VISIBLE);
                } else if (suiteHistory[1] != null) {
                    Log.v(TAG, suiteHistory[0]);
                    Log.v(TAG, suiteHistory[0]);
                    Log.v(TAG, suiteHistory[1]);
                    PlayingCard firstCard = new PlayingCard(valueHistory[0], suiteHistory[0]);
                    PlayingCard secondCard = new PlayingCard(valueHistory[1], suiteHistory[1]);
                    lCard.setImageResource(R.drawable.playing_card_back);
                    lCard.bringToFront();
                    cCard.setImageResource(secondCard.getImageResource());
                    cCard.bringToFront();
                    rCard.setImageResource(firstCard.getImageResource());
                    rCard.bringToFront();


                } else if (suiteHistory[0] != null) {
                    Log.v(TAG, suiteHistory[0]);
                    PlayingCard firstCard = new PlayingCard(valueHistory[0], suiteHistory[0]);
                    lCard.setImageResource(R.drawable.playing_card_back);
                    lCard.bringToFront();
                    cCard.setImageResource(R.drawable.playing_card_back);
                    cCard.bringToFront();
                    rCard.setImageResource(firstCard.getImageResource());
                    rCard.bringToFront();

                } else {
                    lCard.setImageResource(R.drawable.playing_card_back);
                    lCard.bringToFront();
                    cCard.setImageResource(R.drawable.playing_card_back);
                    cCard.bringToFront();
                    rCard.setImageResource(R.drawable.playing_card_back);
                    rCard.bringToFront();


                }


            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();
        va2.start();

    }

    private void animateCardHistoryContract() {
        Log.v(TAG, "Invoke animateCardHistoryExpand");
        Log.v(TAG, "cCard Position:" + String.valueOf(cCard.getWidth()));
        Log.v(TAG, "lCardPlaceHolder Position:" + String.valueOf(lCardPlaceHolder.getX()));
        float translationDistance = cCard.getWidth() * 0.66F;
        Log.v(TAG, "Translation Distance = " + String.valueOf(translationDistance));
        va2 = ValueAnimator.ofFloat(1, 0);
        va2.setDuration(1000);
        va2.setInterpolator(new AccelerateDecelerateInterpolator());

        va2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                rCardText_Top.setAlpha(value);
                //lCardText_Top.setAlpha(value);
                cCardText_Top.setAlpha(value);
                rCardText_Bottom.setAlpha(value);
                //lCardText_Bottom.setAlpha(value);
                cCardText_Bottom.setAlpha(value);
                topHalf.setAlpha(value);
                bottomHalf.setAlpha(value);


            }
        });
        va2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (suiteHistory[0] != null) {
                    PlayingCard lastCard = new PlayingCard(valueHistory[0], suiteHistory[0]);
                    rCard.setImageResource(lastCard.getImageResource());

                } else {
                    rCard.setImageResource(R.drawable.playing_card_back);
                }

                left = true;
                bottomHalf.setText("");
                topHalf.setText("");
                startButtonListeners();
                runSingleCard();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        va = ValueAnimator.ofFloat(translationDistance, 0);
        va.setDuration(1000);
        va.setInterpolator(new AccelerateDecelerateInterpolator());

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                rCard.setTranslationX(value);
                cCard.setTranslationX(-value);

                rCardText_Top.setTranslationX(value);
                cCardText_Top.setTranslationX(-value);
                rCardText_Bottom.setTranslationX(value);
                cCardText_Bottom.setTranslationX(-value);


            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lCard.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();
        va2.start();

    }

    private void animateRightCardIn(int imageResource) {

        rightCardAnimator = ValueAnimator.ofFloat(2000, 0);
        rightCardAnimator.setDuration(flyInDuration);
        rightCardAnimator.setInterpolator(new DecelerateInterpolator(1.5F));

        rightCardAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                rCard.setTranslationX(value);

            }
        });
        rightCardAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                rCard.setTranslationX(10000);
                rCard.bringToFront();

                rCard.setImageResource(newPlayingCard.getImageResource());
                rightCardAnimatorCanceled = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!rightCardAnimatorCanceled) {
                    updateHistory();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                rightCardAnimatorCanceled = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        rightCardAnimator.start();
    }

    private void animateLeftCardIn(int imageResource) {

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
                lCard.setTranslationX(10000);
                lCard.bringToFront();
                lCard.setImageResource(View.VISIBLE);
                lCard.setImageResource(newPlayingCard.getImageResource());
                leftCardAnimatorCanceled = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!leftCardAnimatorCanceled) {
                    updateHistory();
                }
                //updateHistory();
                // setBallsInvisible();

                // if (!cancelled) {
                //     pickNextBall();
                //     throwUp();
                // }


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                leftCardAnimatorCanceled = true;                // topBall.setTranslationY(410 * -1);
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


}
