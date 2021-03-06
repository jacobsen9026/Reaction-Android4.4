package hpiz.reaction.com.reaction.miniGames.surpriseReaction;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

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

public class SurpriseReaction extends Activity {
    private int redScore;
    private SurpriseReactionBackgroundTask runGame;
    private int blueScore;
    private TextView topHalf;
    private TextView bottomHalf;
    private Button pAgainButton;
    private SharedPreferences sp;
    private ConstraintLayout contentContainer;
    private Button bToMainMenuButton;
    private TextView bScoreText;
    private TextView rScoreText;
    private int winningScore = 10;
    private String topColor;
    private String bottomColor;

    public SurpriseReaction() {

    }

    @Override
    public void onBackPressed() {
        if (runGame != null) {
            runGame.cancel(true);
            runGame = null;
        }
        Intent i = new Intent(SurpriseReaction.this, GameActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("runningPreferences", MODE_PRIVATE);
        loadSettings();
        setContentView(R.layout.minigame_surprisereaction);

        Log.v(TAG, " setting game layout");

        hide();

        initializeButtonReactionObjects();

        runSingleRound();
    }


    public void runGame() {
        setContentView(R.layout.minigame_surprisereaction);
        Log.v(TAG, " setting game layout");
        hide();
        initializeButtonReactionObjects();
        runSingleRound();

    }


    protected void runSingleRound() {
        if (redScore < winningScore) {
            if (blueScore < winningScore) {
                updateScores();
                clearScreen();

                setEarlyListeners();
                runGame = new SurpriseReactionBackgroundTask(this);
                runGame.execute("RUN");
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
        contentContainer = (ConstraintLayout) findViewById(R.id.contentContainer);
        contentContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE

                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }

    private void clearScreen() {
        setTopBlack();
        setBottomBlack();
        topHalf.setText("");
        bottomHalf.setText("");
    }

    public void setEarlyListeners() {
        topHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //setBottomWhite();
                bottomWon(true);


                runGame.cancel(true);
                nextRound();
            }
        });
        bottomHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //setTopWhite();
                topWon(true);


                runGame.cancel(true);
                nextRound();
            }
        });
    }

    public void startButtonListeners() {
        topHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                topWon(false);
                if (bottomHalf.hasOnClickListeners()) {
                    bottomHalf.setOnClickListener(null);
                }
                nextRound();
            }
        });
        bottomHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomWon(false);
                if (topHalf.hasOnClickListeners()) {
                    topHalf.setOnClickListener(null);
                }
                nextRound();
            }
        });
    }

    private void topWon(boolean byEarly) {
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        bottomHalf.setBackgroundColor((Color.parseColor(topColor)));
        if (byEarly) {
            bottomHalf.setText(getTooSoonLoseText());
        } else {
            bottomHalf.setText(getLoseText());
            topHalf.setText(getWinText());
        }
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

    private void bottomWon(boolean byEarly) {
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        topHalf.setBackgroundColor((Color.parseColor(bottomColor)));
        if (byEarly) {
            topHalf.setText(getTooSoonLoseText());
        } else {
            bottomHalf.setText(getWinText());
            topHalf.setText(getLoseText());
        }
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
                Intent i = new Intent(SurpriseReaction.this, GameActivity.class);
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
                Intent i = new Intent(SurpriseReaction.this, GameActivity.class);
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
    }


    public void setTopColor() {
        topHalf.setBackgroundColor((Color.parseColor(topColor)));
    }


    public void setBottomColor() {
        bottomHalf.setBackgroundColor((Color.parseColor(bottomColor)));
    }

    public void setTopBlack() {
        topHalf.setBackgroundColor(Color.BLACK);
    }

    public void setBottomBlack() {
        bottomHalf.setBackgroundColor(Color.BLACK);
    }





    private void updateScores() {
        rScoreText.setText("Red Score: " + String.valueOf(redScore));
        bScoreText.setText("Blue Score: " + String.valueOf(blueScore));
    }

    private void initializeButtonReactionObjects() {

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
        rScoreText.setBackgroundColor((Color.parseColor(topColor)));
        bScoreText.setBackgroundColor((Color.parseColor(bottomColor)));
    }

    private void loadSettings() {
        topColor = sp.getString("topColor", "#BB3500");
        bottomColor = sp.getString("bottomColor", "#3D5B7E");
    }

    public void nextRound() {
        runGame = new SurpriseReactionBackgroundTask(this);
        runGame.execute("SLEEP:1000");
    }

    private String getWinText() {
        Random rand = new Random();
        Resources res = getResources();
        String[] winnerStrings = res.getStringArray(R.array.winners_messages);
        int r = rand.nextInt(winnerStrings.length - 1);
        return winnerStrings[r];
    }

    private String getTooSoonLoseText() {
        Random rand = new Random();
        Resources res = getResources();
        String[] tooSoonLoseStrings = res.getStringArray(R.array.too_soon_lose_messages);
        int r = rand.nextInt(tooSoonLoseStrings.length - 1);
        return tooSoonLoseStrings[r];
    }

    private String getLoseText() {
        Random rand = new Random();
        Resources res = getResources();
        String[] loserStrings = res.getStringArray(R.array.losers_messages);
        int r = rand.nextInt(loserStrings.length - 1);
        return loserStrings[r];


    }

}
