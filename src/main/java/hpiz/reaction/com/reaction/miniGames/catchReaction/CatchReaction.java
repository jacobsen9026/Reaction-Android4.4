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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private ImageView topRulerImage;
    private int winningScore = 10;
    private int bFloor;
    private int tFloor;
    private int tCursor;
    private int bCursor;
    private float tVel;
    private float bVel;
    private int screenHeight;
    private ImageView bottomRulerImage;
    private float topPosition;
    private float bottomPosition;

    public CatchReaction() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("runningPreferences", MODE_PRIVATE);

        run();
    }

    public void run() {

        setContentView(R.layout.minigame_catchreaction);

        Log.v(TAG, " setting game layout");

        hide();

        initializeButtonReactionObjects();

        runSingleRound();
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

    protected void runSingleRound() {

        if (redScore < winningScore) {
            if (blueScore < winningScore) {
                updateScores();
                topRulerImage.setTranslationY(0);
                bottomRulerImage.setTranslationY(0);

                //setEarlyListeners();
                if (runGame != null) {
                    runGame.cancel(true);
                }
                runGame = new CatchReactionBackgroundTask(this);
                runGame.execute("RUN");
            } else {
                blueWonGame();
            }
        } else {
            redWonGame();
        }

    }

    private void clearScreen() {

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
                runGame();
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
        if (runGame != null) {
            runGame.cancel(true);
            runGame = null;
        }
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

    public void runGame() {
        setContentView(R.layout.minigame_surprisereaction);
        Log.v(TAG, " setting game layout");
        hide();
        initializeButtonReactionObjects();
        runSingleRound();

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
        topRulerImage = (ImageView) findViewById(R.id.topBall1);
        bottomRulerImage = (ImageView) findViewById(R.id.bottomBall1);
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

    public void drop() {
        tVel = 0;
        bVel = 0;
        topPosition = 0;
        bottomPosition = 0;
        ValueAnimator va = ValueAnimator.ofFloat(0, screenHeight + 150);
        va.setDuration(5000);
        va.setInterpolator(new AccelerateInterpolator(1.99999F));
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
                tVel = (250 - ((Float) animation.getAnimatedValue()));
                tVel = tVel * 0.5F;
                bVel = (-250 + ((Float) animation.getAnimatedValue()));
                bVel = bVel * 0.5F;
                topPosition = topPosition + tVel;
                bottomPosition = bottomPosition + bVel;
                Log.v(TAG, "Bottom Position=" + String.valueOf(bottomPosition));

                if (bottomPosition > 0) {

                    attemptCatch();

                }

                topRulerImage.setTranslationY(topPosition);
                bottomRulerImage.setTranslationY(bottomPosition);

            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                noOneCaught();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();
    }

    private void attemptCatch() {

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

        //ObjectAnimator oa = ObjectAnimator.ofInt(topRulerImage,"y",screenHeight);
        //oa.setDuration(1000);
        //oa.start();
//        topRulerImage.animate().translationY((screenHeight/2)).withLayer();


    }
}
