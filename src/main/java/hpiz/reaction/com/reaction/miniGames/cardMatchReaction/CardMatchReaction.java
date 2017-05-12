package hpiz.reaction.com.reaction.miniGames.cardMatchReaction;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
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
    private static CardMatchReactionBackgroundTask runGame;
    private int redScore;
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
    private ImageView lCard;
    private ImageView rCard;
    private ImageView lCardPlaceHolder;
    private TextView lCardText_Top;
    private TextView rCardText_Top;
    private TextView cCardText_Top;
    private TextView cCardText_Bottom;
    private TextView rCardText_Bottom;
    private TextView lCardText_Bottom;
    private ImageView cCard;
    private ValueAnimator leftCardAnimator;
    private boolean left;
    private int[] valueHistory = new int[3];
    private String[] suiteHistory = new String[3];
    private String TAG = "CardMatchReaction";
    private ValueAnimator alphaAnimator;
    private ValueAnimator rightCardAnimator;
    private boolean rightCardAnimatorCanceled;
    private boolean leftCardAnimatorCanceled;
    private ValueAnimator delayedHideWinLoseTextAnimator;
    private long flyInDuration = 2000;
    private long cardHistoryExpansionTime = 1000;
    private long cardHistoryContractionTime = 1000;
    private long hideWinLoseTextDelayTime = 500;
    private ImageView rGlow;
    private ImageView bGlow;
    private ValueAnimator showGameWinLoseTextAnimator;
    private boolean cancelBackgroundTask;
    private String topColor;
    private String bottomColor;
    private int counter;

    public CardMatchReaction() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("runningPreferences", MODE_PRIVATE);
        loadSettings();
        setContentView(R.layout.minigame_cardmatchreaction);

        Log.v(TAG, " setting game layout");

        hide();

        initializeObjects();
        configureObjects();
        startButtonListeners();

        runSingleCard();
    }

    private void loadSettings() {
        topColor = sp.getString("topColor", "#BB3500");
        bottomColor = sp.getString("bottomColor", "#3D5B7E");
    }

    private void initializeObjects() {
        cancelBackgroundTask = false;
        left = true;
        counter = 0;
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

        lCard = (ImageView) findViewById(R.id.leftCard);
        rCard = (ImageView) findViewById(R.id.rightCard);
        lCardPlaceHolder = (ImageView) findViewById(R.id.rightCardPlaceHolder);
        cCard = (ImageView) findViewById(R.id.centerCard);
        rGlow = (ImageView) findViewById(R.id.redGlow);
        bGlow = (ImageView) findViewById(R.id.blueGlow);


        // lCard.setVisibility(View.INVISIBLE);
        //  rCard.setVisibility(View.INVISIBLE);
    }

    private void configureObjects() {

        rScoreText.setTextColor(Color.WHITE);
        bScoreText.setTextColor(Color.WHITE);
        rScoreText.setBackgroundColor((Color.parseColor(topColor)));
        bScoreText.setBackgroundColor((Color.parseColor(bottomColor)));
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
    }

    public void runGame() {
        setContentView(R.layout.minigame_surprisereaction);
        Log.v(TAG, " setting game layout");
        hide();
        initializeObjects();
        runSingleCard();

    }

    protected void runSingleCard() {
        if (redScore < winningScore) {
            if (blueScore < winningScore) {
                //updateScores();
                //clearScreen();

                //setEarlyListeners();
                if (!cancelBackgroundTask) {
                    runGame = new CardMatchReactionBackgroundTask(this);
                    runGame.execute("RUN");
                }
            } else {
                blueWonGame();
            }
        } else {
            redWonGame();
        }

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

    @Override
    public void onBackPressed() {
        cancelBackgroundTask = true;
        runGame.cancel(true);


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
                redGlowAnimation();


                if (valueHistory[0] == valueHistory[1]) {
                    if (valueHistory[1] != 0) {
                    } else {
                        bottomWon();
                        animateCardHistoryExpand();
                        if (bottomHalf.hasOnClickListeners()) {
                            bottomHalf.setOnClickListener(null);
                        }
                        return;
                    }
                        topWon();
                        if (redScore > (winningScore - 1)) {
                            redWonGame();
                        } else if (blueScore > (winningScore - 1)) {
                            blueWonGame();
                        } else {
                            animateCardHistoryExpand();
                        }
                    } else {
                        bottomWon();
                        animateCardHistoryExpand();
                    }

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
                blueGlowAnimation();

                if (valueHistory[0] == valueHistory[1]) {
                    if (valueHistory[1] != 0) {
                    } else {
                        topWon();
                        animateCardHistoryExpand();
                        if (topHalf.hasOnClickListeners()) {
                            topHalf.setOnClickListener(null);
                        }
                        return;
                    }

                        bottomWon();
                        if (redScore > (winningScore - 1)) {
                            redWonGame();
                        } else if (blueScore > (winningScore - 1)) {
                            blueWonGame();
                        } else {
                            animateCardHistoryExpand();
                        }
                    } else {
                        topWon();
                        animateCardHistoryExpand();
                    }

                    if (topHalf.hasOnClickListeners()) {
                        topHalf.setOnClickListener(null);
                    }

                // nextRound();
            }
        });
    }


    private void topWon() {
        //topHalf.setOnClickListener(null);
        //bottomHalf.setOnClickListener(null);
        //bottomHalf.setBackgroundColor((Color.parseColor(topColor)));
        bottomHalf.setText(getLoseText());

        topHalf.setText(getWinText());
        redScore++;
        runGame.cancel(true);
        updateScores();

        Log.v(TAG, "Blue Score: " + String.valueOf(blueScore));
        Log.v(TAG, "Red Score: " + String.valueOf(redScore));
    }

    private void bottomWon() {
        //topHalf.setOnClickListener(null);
        //bottomHalf.setOnClickListener(null);
        //bottomHalf.setBackgroundColor((Color.parseColor(topColor)));
        bottomHalf.setText(getWinText());

        topHalf.setText(getLoseText());
        blueScore++;
        if (runGame != null) {
            runGame.cancel(true);
        }
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
        Resources res = getResources();
        String[] winnerStrings = res.getStringArray(R.array.winners_messages);
        int r = rand.nextInt(winnerStrings.length - 1);
        return winnerStrings[r];
    }

    private String getLoseText() {
        Random rand = new Random();
        Resources res = getResources();
        String[] loserStrings = res.getStringArray(R.array.losers_messages);
        int r = rand.nextInt(loserStrings.length - 1);
        return loserStrings[r];


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
        topHalf.setBackgroundColor((Color.parseColor(topColor)));
        bottomHalf.setBackgroundColor((Color.parseColor(topColor)));
        bottomHalf.setText("You lost to Red " + String.valueOf(redScore) + " to " + String.valueOf(blueScore) + ".");
        topHalf.setText("You beat Blue " + String.valueOf(redScore) + " to " + String.valueOf(blueScore) + ".");
        showGameWinLoseText();

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
        topHalf.setBackgroundColor((Color.parseColor(bottomColor)));
        bottomHalf.setBackgroundColor((Color.parseColor(bottomColor)));
        bottomHalf.setText("You beat Red " + String.valueOf(blueScore) + " to " + String.valueOf(redScore) + ".");
        topHalf.setText("You lost to Blue " + String.valueOf(blueScore) + " to " + String.valueOf(redScore) + ".");
        showGameWinLoseText();

    }


    private void updateScores() {
        rScoreText.setText("Red Score: " + String.valueOf(redScore));
        bScoreText.setText("Blue Score: " + String.valueOf(blueScore));
    }


    public void showACard() {
        if (newPlayingCard != null) {
            Random rand = new Random();

            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            int randomNumber = rand.nextInt((50 - 1) + 1) + 1;
            int randomSuite = rand.nextInt((4 - 1) + 1) + 1;
            if (randomNumber < counter + 1) {
                PlayingCard holder = newPlayingCard;
                newPlayingCard = new PlayingCard(holder.getValue(), randomSuite);
                counter = 1;
            } else {
                counter++;
                newPlayingCard = new PlayingCard();

            }

        } else {
            newPlayingCard = new PlayingCard();
        }

        //Log.v(TAG, String.valueOf(valueHistory[0]) + " of " + suiteHistory[0]);
        //Log.v(TAG, String.valueOf(valueHistory[1]) + " of " + suiteHistory[1]);
        //Log.v(TAG, String.valueOf(valueHistory[2]) + " of " + suiteHistory[2]);
/*
        Log.d(TAG, "History addition=" + String.valueOf(valueHistory[0]));
        Log.d(TAG, "History addition=" + String.valueOf(suiteHistory[0]));
*/
        //Log.d(TAG, "newPlayingCard objects Image Resource=" + String.valueOf(newPlayingCard.getImageResource()));
        //Log.d(TAG, "newPlayingCard value=" + String.valueOf(newPlayingCard.getValue()) + " of " + newPlayingCard.getSuite());
        if (left) {

            animateLeftCardIn(newPlayingCard.getImageResource());
            left = false;
        } else {

            animateRightCardIn(newPlayingCard.getImageResource());
            left = true;
        }
        //waitAndRunAgain();
        //runSingleCard();
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
        rightCardAnimatorCanceled = true;
        leftCardAnimatorCanceled = true;
        if (rightCardAnimator != null) {
            rightCardAnimator.cancel();
        }
        if (leftCardAnimator != null) {
            leftCardAnimator.cancel();
        }

        Log.v(TAG, "Invoke animateCardHistoryExpand");
        Log.v(TAG, "cCard Position:" + String.valueOf(cCard.getWidth()));
        Log.v(TAG, "lCardPlaceHolder Position:" + String.valueOf(lCardPlaceHolder.getX()));
        float translationDistance = cCard.getWidth() * 0.66F;
        Log.v(TAG, "Translation Distance = " + String.valueOf(translationDistance));
        alphaAnimator = ValueAnimator.ofFloat(0, 1);
        alphaAnimator.setDuration(cardHistoryExpansionTime);
        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

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
        alphaAnimator.addListener(new Animator.AnimatorListener() {
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


        leftCardAnimator = ValueAnimator.ofFloat(0, translationDistance);
        leftCardAnimator.setDuration(1000);
        leftCardAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        leftCardAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

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
        leftCardAnimator.addListener(new Animator.AnimatorListener() {
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
        leftCardAnimator.start();
        alphaAnimator.start();

    }

    private void animateCardHistoryContract() {
        Log.v(TAG, "Invoke animateCardHistoryExpand");
        Log.v(TAG, "cCard Position:" + String.valueOf(cCard.getWidth()));
        Log.v(TAG, "lCardPlaceHolder Position:" + String.valueOf(lCardPlaceHolder.getX()));
        float translationDistance = cCard.getWidth() * 0.66F;
        Log.v(TAG, "Translation Distance = " + String.valueOf(translationDistance));
        alphaAnimator = ValueAnimator.ofFloat(1, 0);
        alphaAnimator.setDuration(cardHistoryContractionTime);
        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                rCardText_Top.setAlpha(value);
                //lCardText_Top.setAlpha(value);
                cCardText_Top.setAlpha(value);
                rCardText_Bottom.setAlpha(value);
                //lCardText_Bottom.setAlpha(value);
                cCardText_Bottom.setAlpha(value);


            }
        });
        alphaAnimator.addListener(new Animator.AnimatorListener() {
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
                //startButtonListeners();
                runSingleCard();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        leftCardAnimator = ValueAnimator.ofFloat(translationDistance, 0);
        leftCardAnimator.setDuration(1000);
        leftCardAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        leftCardAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

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
        leftCardAnimator.addListener(new Animator.AnimatorListener() {
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
        leftCardAnimator.start();
        alphaAnimator.start();
        if (delayedHideWinLoseTextAnimator != null) {
            delayedHideWinLoseTextAnimator.cancel();
        }
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayedHideWinLoseText();
            }
        }, hideWinLoseTextDelayTime);


    }

    private void animateRightCardIn(int imageResource) {

        rightCardAnimator = ValueAnimator.ofFloat(2000, 0);
        rightCardAnimator.setDuration(flyInDuration);
        rightCardAnimator.setInterpolator(new DecelerateInterpolator(1.5F));

        rightCardAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.v(TAG, "Moving right card to=" + animation.getAnimatedValue());

                rCard.setTranslationX((float) animation.getAnimatedValue());

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
                if (!topHalf.hasOnClickListeners()) {
                    startButtonListeners();
                }
                if (!rightCardAnimatorCanceled) {

                    updateHistory();
                }
                rightCardAnimatorCanceled = true;
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

        leftCardAnimator = ValueAnimator.ofFloat(2000, 0);
        leftCardAnimator.setDuration(flyInDuration);
        //leftCardAnimator.setInterpolator(new DecelerateInterpolator(1.5F));
        leftCardAnimator.setInterpolator(new DecelerateInterpolator());
        leftCardAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.v(TAG, "Moving left card to=" + animation.getAnimatedValue());
                //Log.v(TAG, "Bottom Position=" + String.valueOf(bottomPosition));
                //Log.v(TAG, "bVel=" + String.valueOf(bVel));
                //float value = (float) animation.getAnimatedValue();
                lCard.setTranslationX(-(float) animation.getAnimatedValue());

            }
        });
        leftCardAnimator.addListener(new Animator.AnimatorListener() {
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
                if (!topHalf.hasOnClickListeners()) {
                    startButtonListeners();
                }
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
        leftCardAnimator.start();
    }

    private void redGlowAnimation() {
        ValueAnimator redGlowAnimator = ValueAnimator.ofFloat(0, 1);
        redGlowAnimator.setDuration(100);
        redGlowAnimator.setInterpolator(new AccelerateInterpolator());

        redGlowAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                rGlow.setAlpha(value);

            }
        });
        redGlowAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ValueAnimator redGlowEndAnimator = ValueAnimator.ofFloat(1, 0);
                redGlowEndAnimator.setDuration(100);
                redGlowEndAnimator.setInterpolator(new DecelerateInterpolator());
                redGlowEndAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {


                        float value = (float) animation.getAnimatedValue();
                        rGlow.setAlpha(value);

                    }
                });
                redGlowEndAnimator.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        redGlowAnimator.start();
    }

    private void blueGlowAnimation() {
        ValueAnimator blueGlowAnimator = ValueAnimator.ofFloat(0, 1);
        blueGlowAnimator.setDuration(100);
        blueGlowAnimator.setInterpolator(new AccelerateInterpolator());

        blueGlowAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                bGlow.setAlpha(value);

            }
        });
        blueGlowAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ValueAnimator blueGlowEndAnimator = ValueAnimator.ofFloat(1, 0);
                blueGlowEndAnimator.setDuration(100);
                blueGlowEndAnimator.setInterpolator(new DecelerateInterpolator());
                blueGlowEndAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {


                        float value = (float) animation.getAnimatedValue();
                        bGlow.setAlpha(value);

                    }
                });
                blueGlowEndAnimator.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        blueGlowAnimator.start();
    }

    private void showGameWinLoseText() {
        showGameWinLoseTextAnimator = ValueAnimator.ofFloat(0, 1);
        showGameWinLoseTextAnimator.setDuration(1500);
        showGameWinLoseTextAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        showGameWinLoseTextAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                topHalf.setAlpha(value);
                bottomHalf.setAlpha(value);

            }
        });
        showGameWinLoseTextAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

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
        showGameWinLoseTextAnimator.start();

    }

    private void delayedHideWinLoseText() {
        delayedHideWinLoseTextAnimator = ValueAnimator.ofFloat(1, 0);
        delayedHideWinLoseTextAnimator.setDuration(1500);
        delayedHideWinLoseTextAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        delayedHideWinLoseTextAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                topHalf.setAlpha(value);
                bottomHalf.setAlpha(value);

            }
        });
        delayedHideWinLoseTextAnimator.addListener(new Animator.AnimatorListener() {
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

                //left = true;
                Log.v(TAG, "delayed animator ended");
                bottomHalf.setText("");
                topHalf.setText("");
                //startButtonListeners();
                //runSingleCard();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                topHalf.setAlpha(0);
                bottomHalf.setAlpha(0);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        delayedHideWinLoseTextAnimator.start();

    }


}
