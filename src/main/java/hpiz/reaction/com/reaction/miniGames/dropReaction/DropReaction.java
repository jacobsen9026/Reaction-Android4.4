package hpiz.reaction.com.reaction.miniGames.dropReaction;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hpiz.reaction.com.reaction.GameActivity;
import hpiz.reaction.com.reaction.R;

import static android.content.ContentValues.TAG;
import static hpiz.reaction.com.reaction.R.id.backToMainMenuButton;
import static hpiz.reaction.com.reaction.R.id.blueScoreText;
import static hpiz.reaction.com.reaction.R.id.playAgainButton;
import static hpiz.reaction.com.reaction.R.id.redScoreText;

/**
 * Created by Chris on 5/2/2017.
 */

public class DropReaction extends Activity {
    private int redScore;
    private DropReactionBackgroundTask runGame;
    private int blueScore;
    private FrameLayout topHalf;
    private FrameLayout bottomHalf;
    private Button pAgainButton;
    private SharedPreferences sp;
    private RelativeLayout contentContainer;
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
    private ValueAnimator va;
    private long dropDuration = 1000;
    private Float travelDistance;
    private TextView tTextView;
    private TextView bTextView;
    private boolean cancelBackgroundTask;
    private String topColor;
    private String bottomColor;
    private double zeroMark;
    private double oneInch;

    public DropReaction() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("runningPreferences", MODE_PRIVATE);
        loadSettings();
        run();
    }

    public void run() {

        setContentView(R.layout.minigame_dropreaction);

        Log.v(TAG, " setting game layout");

        hide();

        initializeButtonReactionObjects();

        runSingleRound();
    }

    public void hide() {

        //mControlsView.setVisibility(View.GONE);
        //mVisible = false;
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Schedule a runnable to remove the status and navigation bar after a delay
        contentContainer = (RelativeLayout) findViewById(R.id.contentContainer);
        contentContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE

                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }

    protected void runSingleRound() {
        topHalf.setBackgroundColor(Color.TRANSPARENT);
        bottomHalf.setBackgroundColor(Color.TRANSPARENT);
        if (redScore < winningScore) {
            if (blueScore < winningScore) {
                updateScores();
                topRulerImage.setTranslationY(0);
                bottomRulerImage.setTranslationY(0);

                //setEarlyListeners();

                if (runGame != null) {
                    runGame.cancel(true);
                }
                if (!cancelBackgroundTask) {
                    runGame = new DropReactionBackgroundTask(this);
                    runGame.execute("RUN");
                }
            } else {
                blueWonGame();
            }
        } else {
            redWonGame();
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
        topHalf.setBackgroundColor(Color.parseColor(topColor));
        bottomHalf.setBackgroundColor(Color.parseColor(topColor));
        //bottomHalf.setText("You Lost");
        double dropInches = (travelDistance - zeroMark) / oneInch;
        tTextView.setText(String.valueOf(dropInches));
        //topHalf.setText("You Won");
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
                Intent i = new Intent(DropReaction.this, GameActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        setBottomBlue();
        setTopRed();
        //bottomHalf.setText("You lost to Red " + String.valueOf(redScore) + " to " + String.valueOf(blueScore) + ".");
        //topHalf.setText("You beat Blue " + String.valueOf(redScore) + " to " + String.valueOf(blueScore) + ".");
    }

    @Override
    public void onBackPressed() {
        cancelBackgroundTask = true;
            runGame.cancel(true);

        if (va != null) {
            va.cancel();
        }
        Intent i = new Intent(DropReaction.this, GameActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void startButtonListeners() {
        topHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                va.cancel();
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
                va.cancel();

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
        topHalf.setBackgroundColor(Color.parseColor(topColor));
    }


    public void setBottomBlue() {
        bottomHalf.setBackgroundColor(Color.parseColor(bottomColor));
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
                Intent i = new Intent(DropReaction.this, GameActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(i);
            }
        });
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        setBottomBlue();
        setTopRed();
        //bottomHalf.setText("You beat Red " + String.valueOf(blueScore) + " to " + String.valueOf(redScore) + ".");
        //topHalf.setText("You lost to Blue " + String.valueOf(blueScore) + " to " + String.valueOf(redScore) + ".");
    }

    private void bottomWon() {
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        bottomHalf.setBackgroundColor(Color.parseColor(bottomColor));
        topHalf.setBackgroundColor(Color.parseColor(bottomColor));
        //bottomHalf.setText("You Won");
        //topHalf.setText("You Lost");
        double dropInches = (travelDistance - zeroMark) / oneInch;
        String dropInchesStr = String.valueOf(dropInches);
        String drop = dropInchesStr.substring(0, dropInchesStr.indexOf(".") + 2);
        bTextView.setText(drop);
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
        cancelBackgroundTask = false;
        redScore = 0;
        blueScore = 0;
        pAgainButton = (Button) super.findViewById(playAgainButton);
        pAgainButton.setVisibility(View.GONE);
        pAgainButton.setOnClickListener(null);
        topRulerImage = (ImageView) findViewById(R.id.topsRuler);
        bTextView = (TextView) findViewById(R.id.bottomMessageBox);
        tTextView = (TextView) findViewById(R.id.topMessageBox);
        bottomRulerImage = (ImageView) findViewById(R.id.bottomsRuler);
        bToMainMenuButton = (Button) findViewById(backToMainMenuButton);
        topHalf = (FrameLayout) findViewById(R.id.topTriggerZone);
        bottomHalf = (FrameLayout) findViewById(R.id.bottomTriggerZone);
        bToMainMenuButton.setVisibility(View.GONE);
        bToMainMenuButton.setOnClickListener(null);
        rScoreText = (TextView) findViewById(redScoreText);
        bScoreText = (TextView) findViewById(blueScoreText);
        rScoreText.setTextColor(Color.WHITE);
        bScoreText.setTextColor(Color.WHITE);
        rScoreText.setBackgroundColor(Color.parseColor(topColor));
        bScoreText.setBackgroundColor(Color.parseColor(bottomColor));
        //topRulerImage.bringToFront();
        //bottomRulerImage.bringToFront();
    }

    public void nextRound() {
        if (!cancelBackgroundTask) {
            Log.v(TAG, "Creating background task");
            DropReactionBackgroundTask runGame = new DropReactionBackgroundTask(this);
            runGame.execute("SLEEP:1000");
        }
    }

    private void loadSettings() {
        topColor = sp.getString("topColor", "#BB3500");
        bottomColor = sp.getString("bottomColor", "#3D5B7E");
    }

    public void drop() {
        va = ValueAnimator.ofFloat(0, screenHeight + 150);
        va.setDuration(dropDuration);
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
                travelDistance = (Float) animation.getAnimatedValue();
                topRulerImage.setTranslationY(-travelDistance);
                bottomRulerImage.setTranslationY(travelDistance);
                //bottomRulerImage.bringToFront();
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                startButtonListeners();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                noOneCaught(animation);
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

    private void noOneCaught(Animator animation) {

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
        double zeroMarkRatio = 24.0 / 1134.0;
        Log.v("Ruler Drop", "Zero Mark Ratio:" + String.valueOf(zeroMarkRatio));
        double oneInchRatio = 90.083333 / 1134.0;
        Log.v("Ruler Drop", "One Inch Ratio:" + String.valueOf(oneInchRatio));
        screenHeight = displayMetrics.heightPixels;
        Log.v("Ruler Drop", "Ruler Height:" + String.valueOf(topRulerImage.getHeight()));
        zeroMark = topRulerImage.getHeight() * zeroMarkRatio;
        oneInch = topRulerImage.getHeight() * oneInchRatio;
        Log.v("Ruler Drop", "Zero Mark:" + String.valueOf(zeroMark));
        Log.v("Ruler Drop", "One Inch:" + String.valueOf(oneInch));



        //ObjectAnimator oa = ObjectAnimator.ofInt(topRulerImage,"y",screenHeight);
        //oa.setDuration(1000);
        //oa.start();
//        topRulerImage.animate().translationY((screenHeight/2)).withLayer();


    }
}
