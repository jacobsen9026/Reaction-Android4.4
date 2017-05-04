package hpiz.reaction.com.reaction.miniGames.wacAMoleReaction;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import hpiz.reaction.com.reaction.GameActivity;
import hpiz.reaction.com.reaction.R;

import static hpiz.reaction.com.reaction.R.id.backToMainMenuButton;
import static hpiz.reaction.com.reaction.R.id.blueScoreText;
import static hpiz.reaction.com.reaction.R.id.playAgainButton;
import static hpiz.reaction.com.reaction.R.id.redScoreText;

/**
 * Created by Chris on 5/2/2017.
 */

public class WacAMoleReaction extends Activity {
    private static final String TAG = WacAMoleReaction.class.getSimpleName();
    private int topScore;
    private WacAMoleReactionBackgroundTask runGame;
    private int bottomScore;
    private TextView topHalf;
    private TextView bottomHalf;
    private Button pAgainButton;
    private ConstraintLayout contentContainer;
    private Button bToMainMenuButton;
    private TextView bottomScoreText;
    private TextView topScoreText;
    private ImageView tImage;
    private ImageView bImage;
    private ConstraintLayout cContainer;
    private int askingFor;
    private int topsMoleImageView;
    private int bottomsSelectedMole;
    private ImageView topMole;
    private ImageView bottomMole;

    public WacAMoleReaction() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        run();
    }

    public void run() {

        setContentView(R.layout.minigame_wacamolereaction);

        Log.v(TAG, " setting game layout");

        hide();

        initializeButtonReactionObjects();

        runSingleRound();
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
        contentContainer = (ConstraintLayout) findViewById(R.id.contentContainer);
        contentContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE

                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }

    protected void runSingleRound() {
        //tImage.setVisibility(View.GONE);
        //bImage.setVisibility(View.GONE);
        //topHalf.setVisibility(View.VISIBLE);
        //bottomHalf.setVisibility(View.VISIBLE);
        if (topScore < 10) {
            if (bottomScore < 10) {
                updateScores();
                //clearScreen();

                //setEarlyListeners();
                if (runGame != null) {
                    runGame.cancel(true);

                }
                runGame = new WacAMoleReactionBackgroundTask(this);
                runGame.execute("RUN");
            } else {
                blueWonGame();
            }
        } else {
            redWonGame();
        }

    }


    public void redWonGame() {
        pAgainButton.setVisibility(View.VISIBLE);
        pAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runGameAgain();
            }
        });
        bToMainMenuButton.setVisibility(View.VISIBLE);
        bToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        topHalf.setBackgroundColor(Color.RED);
        bottomHalf.setBackgroundColor(Color.RED);

        bottomHalf.setText("You lost to Red " + String.valueOf(topScore) + " to " + String.valueOf(bottomScore) + ".");
        topHalf.setText("You beat Blue " + String.valueOf(topScore) + " to " + String.valueOf(bottomScore) + ".");
    }

    public void startWinningWacListeners() {

        Log.v(TAG, "Setup Listeners");

        topMole.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(TAG, "topMole pressed");
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                if (action == MotionEvent.ACTION_UP) {
                    Log.v(TAG, "topMole released");
                    topScore++;

                    updateScores();
                    killWinListeners();
                    return true;
                } else if (action == MotionEvent.ACTION_POINTER_UP) {
                    bottomScore++;
                    updateScores();
                    killWinListeners();
                }
                return true;
            }
        });

        bottomMole.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(TAG, "bottomMole pressed");
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                if (action == MotionEvent.ACTION_UP) {
                    Log.v(TAG, "bottomMole released");
                    bottomScore++;
                    updateScores();
                    killWinListeners();
                    return true;
                } else if (action == MotionEvent.ACTION_POINTER_UP) {
                    topScore++;
                    updateScores();
                    killWinListeners();
                }
                return true;
            }
        });

    }

    public void runGameAgain() {
        setContentView(R.layout.minigame_wacamolereaction);
        Log.v(TAG, " setting game layout");
        hide();
        initializeButtonReactionObjects();
        runSingleRound();

    }


    public void blueWonGame() {
        pAgainButton.setVisibility(View.VISIBLE);
        pAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runGameAgain();
            }
        });
        bToMainMenuButton.setVisibility(View.VISIBLE);
        bToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WacAMoleReaction.this, GameActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        topHalf.setBackgroundColor(Color.BLUE);
        bottomHalf.setBackgroundColor(Color.BLUE);
        bottomHalf.setText("You beat Red " + String.valueOf(bottomScore) + " to " + String.valueOf(topScore) + ".");
        topHalf.setText("You lost to Blue " + String.valueOf(bottomScore) + " to " + String.valueOf(topScore) + ".");
    }


    @Override
    public void onBackPressed() {
        if (runGame != null) {
            runGame.cancel(true);
            runGame = null;
        }
        Intent i = new Intent(WacAMoleReaction.this, GameActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void updateScores() {
        topScoreText.setText("Red Score: " + String.valueOf(topScore));
        bottomScoreText.setText("Blue Score: " + String.valueOf(bottomScore));
    }

    private void initializeButtonReactionObjects() {
        cContainer = (ConstraintLayout) findViewById(R.id.contentContainer);
        topScore = 0;
        bottomScore = 0;
        pAgainButton = (Button) super.findViewById(playAgainButton);
        pAgainButton.setVisibility(View.GONE);
        pAgainButton.setOnClickListener(null);
        tImage = (ImageView) findViewById(R.id.topImage);
        bImage = (ImageView) findViewById(R.id.bottomImage);
        bToMainMenuButton = (Button) findViewById(backToMainMenuButton);

        bToMainMenuButton.setVisibility(View.GONE);
        bToMainMenuButton.setOnClickListener(null);
        topHalf = (TextView) findViewById(R.id.topHalf);
        bottomHalf = (TextView) findViewById(R.id.bottomHalf);
        topScoreText = (TextView) findViewById(redScoreText);
        bottomScoreText = (TextView) findViewById(blueScoreText);
        topScoreText.setTextColor(Color.WHITE);
        bottomScoreText.setTextColor(Color.WHITE);
        topScoreText.setBackgroundColor(Color.RED);
        bottomScoreText.setBackgroundColor(Color.BLUE);
        cContainer.setBackgroundColor(Color.WHITE);
    }

    public void hideSelectedMoles() {
        topMole.setVisibility(View.INVISIBLE);
        bottomMole.setVisibility(View.INVISIBLE);
    }

    public void killWinListeners() {
        topMole.setOnTouchListener(null);
        bottomMole.setOnTouchListener(null);
    }

    public void waitForWin() {
        bottomMole.setVisibility(View.VISIBLE);
        topMole.setVisibility(View.VISIBLE);
        if (runGame != null) {

            runGame.cancel(true);

        }
        runGame = new WacAMoleReactionBackgroundTask(this);
        runGame.execute("SLEEP:1000");
    }

    public void pickMole() {
        Random r = new Random();
        r.setSeed((long) 15);
        askingFor = 3;
        switch (askingFor) {
            case 1:
                topsMoleImageView = R.id.topMole1;
                bottomsSelectedMole = R.id.bottomMole1;
                break;
            case 2:
                topsMoleImageView = R.id.topMole2;
                bottomsSelectedMole = R.id.bottomMole2;
                break;
            case 3:
                topsMoleImageView = R.id.topMole3;
                bottomsSelectedMole = R.id.bottomMole3;
                break;
            case 4:
                topsMoleImageView = R.id.topMole4;
                bottomsSelectedMole = R.id.bottomMole4;
                break;
            case 5:
                topsMoleImageView = R.id.topMole5;
                bottomsSelectedMole = R.id.bottomMole5;
                break;
            case 6:
                topsMoleImageView = R.id.topMole6;
                bottomsSelectedMole = R.id.bottomMole6;
                break;
        }
        topMole = (ImageView) findViewById(topsMoleImageView);
        bottomMole = (ImageView) findViewById(bottomsSelectedMole);

    }


}
