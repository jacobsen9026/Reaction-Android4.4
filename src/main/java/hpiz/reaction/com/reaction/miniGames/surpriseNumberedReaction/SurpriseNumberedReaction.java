package hpiz.reaction.com.reaction.miniGames.surpriseNumberedReaction;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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

import static android.content.ContentValues.TAG;
import static hpiz.reaction.com.reaction.R.id.backToMainMenuButton;
import static hpiz.reaction.com.reaction.R.id.blueScoreText;
import static hpiz.reaction.com.reaction.R.id.playAgainButton;
import static hpiz.reaction.com.reaction.R.id.redScoreText;

/**
 * Created by Chris on 5/2/2017.
 */

public class SurpriseNumberedReaction extends Activity {
    private int redScore;
    private SurpriseNumberedReactionBackgroundTask runGame;
    private int blueScore;
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
    private int askingForImage;
    private SharedPreferences sp;
    private String topColor;
    private String bottomColor;

    public SurpriseNumberedReaction() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("runningPreferences", MODE_PRIVATE);
        loadSettings();
        setContentView(R.layout.minigame_surprisenumberedreaction);

        Log.v(TAG, " setting game layout");

        hide();

        initializeObjects();
        configureObjects();

        runSingleRound();
    }

    private void configureObjects() {
        topScoreText.setTextColor(Color.WHITE);
        bottomScoreText.setTextColor(Color.WHITE);
        topScoreText.setBackgroundColor((Color.parseColor(topColor)));
        bottomScoreText.setBackgroundColor((Color.parseColor(bottomColor)));
        cContainer.setBackgroundColor(Color.WHITE);
    }


    private void loadSettings() {
        topColor = sp.getString("topColor", "#BB3500");
        bottomColor = sp.getString("bottomColor", "#3D5B7E");
    }

    private void initializeObjects() {
        cContainer = (ConstraintLayout) findViewById(R.id.contentContainer);
        redScore = 0;
        blueScore = 0;
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

    public void runGame() {
        setContentView(R.layout.minigame_surprisenumberedreaction);
        Log.v(TAG, " setting game layout");
        hide();
        initializeObjects();
        runSingleRound();

    }

    protected void runSingleRound() {
        tImage.setVisibility(View.GONE);
        bImage.setVisibility(View.GONE);
        topHalf.setVisibility(View.VISIBLE);
        bottomHalf.setVisibility(View.VISIBLE);
        if (redScore < 10) {
            if (blueScore < 10) {
                updateScores();
                clearScreen();

                setEarlyListeners();
                if (runGame != null) {
                    runGame.cancel(true);

                }
                runGame = new SurpriseNumberedReactionBackgroundTask(this);
                runGame.execute("RUN");
            } else {
                blueWonGame();
            }
        } else {
            redWonGame();
        }

    }

    private void clearScreen() {
        topHalf.setBackgroundColor(Color.BLACK);
        bottomHalf.setBackgroundColor(Color.BLACK);
        topHalf.setText("");
        bottomHalf.setText("");
    }

    public void setEarlyListeners() {
        topHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //setBottomWhite();
                Log.v(TAG, "Top was early");
                bottomWon(true);
                if (bottomHalf.hasOnClickListeners()) {
                    bottomHalf.setOnClickListener(null);
                }
                runGame.cancel(true);
                nextRound();
            }
        });
        bottomHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //setTopWhite();
                topWon(true);
                if (topHalf.hasOnClickListeners()) {
                    topHalf.setOnClickListener(null);
                }
                runGame.cancel(true);
                nextRound();
            }
        });
    }


    private void topWon(boolean byEarly) {
        tImage.setVisibility(View.GONE);
        bImage.setVisibility(View.GONE);
        topHalf.setVisibility(View.VISIBLE);
        bottomHalf.setVisibility(View.VISIBLE);
        tImage.setOnTouchListener(null);
        bImage.setOnTouchListener(null);
        Log.v(TAG, "INVOKE TopWon");
        topHalf.setBackgroundColor((Color.parseColor(topColor)));
        bottomHalf.setBackgroundColor((Color.parseColor(topColor)));
        if (byEarly) {
            bottomHalf.setText(getTooSoonLoseText());
        } else {
            bottomHalf.setText(getLoseText());

            topHalf.setText(getWinText());
        }
        redScore++;
        updateScores();
        if (redScore > 9) {
            redWonGame();
        }
        if (blueScore > 9) {
            blueWonGame();
        }
        Log.v(TAG, "Blue Score: " + String.valueOf(blueScore));
        Log.v(TAG, "Red Score: " + String.valueOf(redScore));
    }

    public void redWonGame() {
        showButtons();
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        topHalf.setBackgroundColor((Color.parseColor(topColor)));
        bottomHalf.setBackgroundColor((Color.parseColor(topColor)));

        bottomHalf.setText("You lost to Red " + String.valueOf(redScore) + " to " + String.valueOf(blueScore) + ".");
        topHalf.setText("You beat Blue " + String.valueOf(redScore) + " to " + String.valueOf(blueScore) + ".");
    }

    public void blueWonGame() {
        showButtons();
        topHalf.setOnClickListener(null);
        bottomHalf.setOnClickListener(null);
        topHalf.setBackgroundColor((Color.parseColor(bottomColor)));
        bottomHalf.setBackgroundColor((Color.parseColor(bottomColor)));
        bottomHalf.setText("You beat Red " + String.valueOf(blueScore) + " to " + String.valueOf(redScore) + ".");
        topHalf.setText("You lost to Blue " + String.valueOf(blueScore) + " to " + String.valueOf(redScore) + ".");
    }

    private void showButtons() {
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
                Intent i = new Intent(SurpriseNumberedReaction.this, GameActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    public void startButtonListeners() {
        Log.v(TAG, "Setup Listeners");
        topHalf.setOnClickListener(null);


        bottomHalf.setOnClickListener(null);

        tImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //bottomHalf.setBackgroundColor(Color.WHITE);
                //bottomHalf.setText(String.valueOf(event.getPointerCount()==askingFor));
                Log.v(TAG, String.valueOf(event.getPointerCount()));
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                if (action == MotionEvent.ACTION_POINTER_UP) {
                    Log.v(TAG, "Asking for: " + String.valueOf(askingFor));
                    Log.v(TAG, "Have: " + String.valueOf(event.getPointerCount()));
                    if (event.getPointerCount() == askingFor) {
                        topWon(false);
                        nextRound();
                        return true;
                    } else {
                        bottomWon(false);
                        nextRound();
                        return true;
                    }
                } else if (action == MotionEvent.ACTION_UP) {
                    if (event.getPointerCount() == askingFor) {
                        topWon(false);
                        nextRound();
                        return true;
                    } else {
                        bottomWon(false);
                        nextRound();
                        return true;
                    }
                }
                return true;
            }
        });
        /*
        topHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                topWon();
                if (bottomHalf.hasOnClickListeners()) {
                    bottomHalf.setOnClickListener(null);
                }
                waitForWin();
            }
        });
        */
        /*
        bottomHalf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomWon();
                if (topHalf.hasOnClickListeners()) {
                    topHalf.setOnClickListener(null);
                }
                waitForWin();
            }
        });
        */
        bImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                if (action == MotionEvent.ACTION_POINTER_UP) {
                    Log.v(TAG, "Asking for: " + String.valueOf(askingFor));
                    Log.v(TAG, "Have: " + String.valueOf(event.getPointerCount()));
                    if (event.getPointerCount() == askingFor) {
                        bottomWon(false);
                        nextRound();
                        return true;
                    } else {
                        topWon(false);
                        nextRound();
                        return true;
                    }
                } else if (action == MotionEvent.ACTION_UP) {
                    if (event.getPointerCount() == askingFor) {
                        bottomWon(false);
                        nextRound();
                        return true;
                    } else {
                        topWon(false);
                        nextRound();
                        return true;
                    }
                }
                return true;
            }
        });
    }


    private void bottomWon(boolean byEarly) {
        tImage.setVisibility(View.GONE);
        bImage.setVisibility(View.GONE);
        topHalf.setVisibility(View.VISIBLE);
        bottomHalf.setVisibility(View.VISIBLE);
        Log.v(TAG, "INVOKE BottomWon");
        tImage.setOnTouchListener(null);
        bImage.setOnTouchListener(null);
        topHalf.setBackgroundColor((Color.parseColor(bottomColor)));
        bottomHalf.setBackgroundColor((Color.parseColor(bottomColor)));
        if (byEarly) {
            topHalf.setText(getTooSoonLoseText());
        } else {
            bottomHalf.setText(getWinText());
            topHalf.setText(getLoseText());
        }
        blueScore++;
        updateScores();
        if (redScore > 9) {
            redWonGame();
        }
        if (blueScore > 9) {
            blueWonGame();
        }
        Log.v(TAG, "Blue Score: " + String.valueOf(blueScore));
        Log.v(TAG, "Red Score: " + String.valueOf(redScore));
    }

    @Override
    public void onBackPressed() {
        if (runGame != null) {
            runGame.cancel(true);
            runGame = null;
        }
        Intent i = new Intent(SurpriseNumberedReaction.this, GameActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void updateScores() {
        topScoreText.setText("Red Score: " + String.valueOf(redScore));
        bottomScoreText.setText("Blue Score: " + String.valueOf(blueScore));
    }


    public void nextRound() {
        if (topHalf.hasOnClickListeners()) {
            topHalf.setOnClickListener(null);
        }
        if (bottomHalf.hasOnClickListeners()) {
            bottomHalf.setOnClickListener(null);
        }
        if (runGame != null) {
            if (!runGame.isCancelled()) {
                runGame.cancel(true);
            }
        }
        runGame = new SurpriseNumberedReactionBackgroundTask(this);
        runGame.execute("SLEEP:1000");
    }

    public void pickNumber() {
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        askingFor = rand.nextInt((3 - 1) + 1) + 1;
        switch (askingFor) {
            case 1:
                askingForImage = R.drawable.onefinger;
                break;
            case 2:
                askingForImage = R.drawable.twofingers;
                break;
            case 3:
                askingForImage = R.drawable.threefingers;
                break;
        }

    }


    public void setImages() {
        bImage.setImageResource(askingForImage);
        bImage.setVisibility(View.VISIBLE);
        bottomHalf.setVisibility(View.GONE);
        bottomHalf.setBackgroundColor(Color.GRAY);
        topHalf.setText(String.valueOf(askingFor));
        tImage.setImageResource(askingForImage);
        tImage.setVisibility(View.VISIBLE);
        topHalf.setVisibility(View.GONE);
        topHalf.setBackgroundColor(Color.GRAY);

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
