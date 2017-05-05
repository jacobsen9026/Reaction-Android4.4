package hpiz.reaction.com.reaction.miniGames.catchReaction;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import hpiz.reaction.com.reaction.GameActivity;
import hpiz.reaction.com.reaction.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Chris on 5/2/2017.
 */

public class CatchReaction extends Activity {
    private int redScore;
    private CatchReactionBackgroundTask runGame;
    private int blueScore;
    private TextView topHalf;
    private TextView bottomHalf;
    private Button pAgainButton;
    private SharedPreferences sp;
    private ConstraintLayout contentContainer;
    private Button bToMainMenuButton;
    private TextView bScoreText;
    private TextView rScoreText;
    private ImageView topBall;
    private int winningScore = 10;
    private int bFloor;
    private int tFloor;
    private int tCursor;
    private int bCursor;
    private float tVel;
    private float bVel;
    private int screenHeight;
    private ImageView bottomBall;
    private float topPosition;
    private float bottomPosition;
    private ValueAnimator va;
    private boolean cancelled;
    private int randomBall;

    public CatchReaction() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("runningPreferences", MODE_PRIVATE);
        cancelled = false;
        run();
    }

    public void run() {

        setContentView(R.layout.minigame_catchreaction);

        Log.v(TAG, " setting game layout");

        hide();

        initializeButtonReactionObjects();
        pickNextBall();
        throwUp();
    }

    public void hide() {

        // Hide UI first
        //mControlsView.setVisibility(View.GONE);
        //mVisible = false;
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Schedule a runnable to remove the status and navigation bar after a delay
        contentContainer = (ConstraintLayout) findViewById(R.id.contentContainer);
        contentContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE

                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }

    protected void checkForWinner() {
        if (redScore < winningScore) {
            if (blueScore < winningScore) {
                updateScores();
                pickNextBall();


                //setEarlyListeners();

                //Log.v(TAG,"Run Game");

            } else {
                blueWonGame();
            }
        } else {
            redWonGame();
        }

    }

    private void pickNextBall() {
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        randomBall = rand.nextInt((5 - 1) + 1) + 1;
        switch (randomBall) {
            case 1:
                topBall = (ImageView) findViewById(R.id.topBall1);
                bottomBall = (ImageView) findViewById(R.id.bottomBall1);
                break;
            case 2:
                topBall = (ImageView) findViewById(R.id.topBall2);
                bottomBall = (ImageView) findViewById(R.id.bottomBall2);
                break;
            case 3:
                topBall = (ImageView) findViewById(R.id.topBall3);
                bottomBall = (ImageView) findViewById(R.id.bottomBall3);
                break;
            case 4:
                topBall = (ImageView) findViewById(R.id.topBall4);
                bottomBall = (ImageView) findViewById(R.id.bottomBall4);
                break;
            case 5:
                topBall = (ImageView) findViewById(R.id.topBall5);
                bottomBall = (ImageView) findViewById(R.id.bottomBall5);
                break;
        }

    }


    public void setEarlyListeners() {
        topHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //setBottomWhite();
                bottomWon();


                runGame.cancel(true);
                nextRound();
            }
        });
        bottomHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //setTopWhite();
                topWon();


                runGame.cancel(true);
                nextRound();
            }
        });
    }


    private void topWon() {
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        topHalf.setBackgroundColor(Color.RED);
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
                run();
            }
        });
        bToMainMenuButton.setVisibility(View.VISIBLE);
        bToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CatchReaction.this, GameActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        setBottomBlue();
        setTopRed();
        bottomHalf.setText("You lost to Red " + String.valueOf(redScore) + " to " + String.valueOf(blueScore) + ".");
        topHalf.setText("You beat Blue " + String.valueOf(redScore) + " to " + String.valueOf(blueScore) + ".");
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "BACK PRESSED");
        cancelled = true;
        va.cancel();
        Intent i = new Intent(CatchReaction.this, GameActivity.class);
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


    public void setTopRed() {
        topHalf.setBackgroundColor(Color.RED);
    }


    public void setBottomBlue() {
        bottomHalf.setBackgroundColor(Color.BLUE);
    }


    public void blueWonGame() {
        runGame.cancel(true);
        pAgainButton.setVisibility(View.VISIBLE);
        pAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                run();
            }
        });
        bToMainMenuButton.setVisibility(View.VISIBLE);
        bToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CatchReaction.this, GameActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(i);
            }
        });
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        setBottomBlue();
        setTopRed();
        bottomHalf.setText("You beat Red " + String.valueOf(blueScore) + " to " + String.valueOf(redScore) + ".");
        topHalf.setText("You lost to Blue " + String.valueOf(blueScore) + " to " + String.valueOf(redScore) + ".");
    }

    private void bottomWon() {
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        bottomHalf.setBackgroundColor(Color.BLUE);
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

        redScore = 0;
        blueScore = 0;
        pAgainButton = (Button) findViewById(R.id.playAgainButton);
        pAgainButton.setVisibility(View.GONE);
        pAgainButton.setOnClickListener(null);
        topBall = (ImageView) findViewById(R.id.topBall1);
        bottomBall = (ImageView) findViewById(R.id.bottomBall1);
        bToMainMenuButton = (Button) findViewById(R.id.backToMainMenuButton);

        bToMainMenuButton.setVisibility(View.GONE);
        bToMainMenuButton.setOnClickListener(null);
        rScoreText = (TextView) findViewById(R.id.redScoreText);
        bScoreText = (TextView) findViewById(R.id.blueScoreText);
        rScoreText.setTextColor(Color.WHITE);
        bScoreText.setTextColor(Color.WHITE);
        rScoreText.setBackgroundColor(Color.RED);
        bScoreText.setBackgroundColor(Color.BLUE);
    }

    public void nextRound() {
        CatchReactionBackgroundTask runGame = new CatchReactionBackgroundTask(this);
        runGame.execute("SLEEP:1000");
    }

    public void throwUp() {
        tVel = 0;
        bVel = 0;
        topBall.setTranslationY(410 * -1);
        bottomBall.setTranslationY(410);
        topBall.setVisibility(View.VISIBLE);
        bottomBall.setVisibility(View.VISIBLE);
        topPosition = topBall.getY();
        bottomPosition = bottomBall.getY();
        va = ValueAnimator.ofFloat(0, screenHeight + 410);
        va.setDuration(3000);
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

                tVel = (500 - ((Float) animation.getAnimatedValue()));
                tVel = tVel * 0.18F;
                bVel = (-500 + ((Float) animation.getAnimatedValue()));
                bVel = bVel * 0.18F;
                topPosition = topPosition + tVel;
                bottomPosition = bottomPosition + bVel;


                topBall.setTranslationY(topPosition);
                bottomBall.setTranslationY(bottomPosition);

            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fallDown();


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                topBall.setTranslationY(410 * -1);
                bottomBall.setTranslationY(410);
                topBall.setVisibility(View.INVISIBLE);
                bottomBall.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();
    }

    public void fallDown() {
        tVel = 0;
        bVel = 0;
        topPosition = topBall.getY();
        bottomPosition = bottomBall.getY();
        va = ValueAnimator.ofFloat(screenHeight + 410, 0);
        va.setDuration(3000);
        va.setInterpolator(new AccelerateInterpolator(1.5F));
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

                tVel = (500 - ((Float) animation.getAnimatedValue()));
                tVel = tVel * 0.18F;
                bVel = (-500 + ((Float) animation.getAnimatedValue()));
                bVel = bVel * 0.18F;
                topPosition = topPosition - tVel;
                bottomPosition = bottomPosition - bVel;


                topBall.setTranslationY(topPosition);
                bottomBall.setTranslationY(bottomPosition);

            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setBallsInvisible();

                if (!cancelled) {
                    pickNextBall();
                    throwUp();
                }


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                topBall.setTranslationY(410 * -1);
                bottomBall.setTranslationY(410);
                topBall.setVisibility(View.INVISIBLE);
                bottomBall.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();
    }

    private void setBallsInvisible() {
        Log.v(TAG, "Set Balls Invisible");
        ImageView t1 = (ImageView) findViewById(R.id.topBall1);
        t1.setVisibility(View.INVISIBLE);
        ImageView b1 = (ImageView) findViewById(R.id.bottomBall1);
        b1.setVisibility(View.INVISIBLE);
        ImageView t2 = (ImageView) findViewById(R.id.topBall2);
        t2.setVisibility(View.INVISIBLE);
        ImageView b2 = (ImageView) findViewById(R.id.bottomBall2);
        b2.setVisibility(View.INVISIBLE);
        ImageView t3 = (ImageView) findViewById(R.id.topBall3);
        t3.setVisibility(View.INVISIBLE);
        ImageView b3 = (ImageView) findViewById(R.id.bottomBall3);
        b3.setVisibility(View.INVISIBLE);
        ImageView t4 = (ImageView) findViewById(R.id.topBall4);
        t4.setVisibility(View.INVISIBLE);
        ImageView b4 = (ImageView) findViewById(R.id.bottomBall4);
        b4.setVisibility(View.INVISIBLE);
        ImageView t5 = (ImageView) findViewById(R.id.topBall5);
        t5.setVisibility(View.INVISIBLE);
        ImageView b5 = (ImageView) findViewById(R.id.bottomBall5);
        b5.setVisibility(View.INVISIBLE);


    }


    private void noOneCaught() {

        nextRound();
    }

    public void stepDrop(int x) {

        /*
        if(bCursor>bFloor) {
            tVel = (float) (-0.0098 * x * x);
            bVel = (float) (0.0098 * x * x);
            tCursor = (int) (tCursor - tVel);
            bCursor = (int) (bCursor - bVel);
            Log.v(TAG, "Cursors: " + String.valueOf(tCursor) + " " + String.valueOf(bCursor));
        }
        */
    }

    public void InitializeDrop() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenHeight = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        bFloor = 0;
        tFloor = screenHeight;
        bCursor = screenHeight / 2;
        tCursor = screenHeight / 2;
        tVel = 0;
        bVel = 0;

        //ObjectAnimator oa = ObjectAnimator.ofInt(topBall,"y",screenHeight);
        //oa.setDuration(1000);
        //oa.start();
//        topBall.animate().translationY((screenHeight/2)).withLayer();


    }

}
