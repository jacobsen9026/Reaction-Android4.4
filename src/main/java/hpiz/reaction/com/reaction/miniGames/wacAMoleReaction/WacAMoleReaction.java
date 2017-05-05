package hpiz.reaction.com.reaction.miniGames.wacAMoleReaction;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private int randomMole;
    private int topsMoleImageView;
    private int bottomsSelectedMole;
    private ImageView topMole;
    private ImageView bottomMole;
    private float textSize;
    private RelativeLayout bottomHalfLayout;
    private RelativeLayout topHalfLayout;
    private long backgroundFlashSpeed = 160;
    private boolean bottomHit;
    private boolean topHit;

    public WacAMoleReaction() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        start();
    }

    public void start() {

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
                    topPoint();
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
                    bottomPoint();
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

    private void bottomPoint() {
        bottomHit = true;
        bottomScore++;

        bottomMole.setImageResource(R.drawable.pow);

        ValueAnimator va = ValueAnimator.ofFloat(0F, 1.0F);
        va.setDuration(backgroundFlashSpeed);
        va.setInterpolator(new AccelerateDecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //Log.v(TAG,"Text Size: " + String.valueOf(textSize) + " Animated Value: "+ String.valueOf((Float)animation.getAnimatedValue()));
                bottomHalfLayout.setAlpha((Float) animation.getAnimatedValue());
                topHalfLayout.setAlpha((Float) animation.getAnimatedValue());
                //bottomScoreText.setScaleY((Float) animation.getAnimatedValue());
            }
        });


        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                topHalfLayout.setBackgroundColor(Color.BLUE);
                bottomHalfLayout.setBackgroundColor(Color.BLUE);
                topHalfLayout.setAlpha(0F);
                bottomHalfLayout.setAlpha(0F);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ValueAnimator va2 = ValueAnimator.ofFloat(1.0F, 0F);
                va2.setDuration(backgroundFlashSpeed);
                va2.setInterpolator(new AccelerateDecelerateInterpolator());
                va2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        bottomHalfLayout.setAlpha((Float) animation.getAnimatedValue());
                        topHalfLayout.setAlpha((Float) animation.getAnimatedValue());

                        // bottomScoreText.setScaleX((Float) animation.getAnimatedValue());
                        //  bottomScoreText.setScaleY((Float) animation.getAnimatedValue());
                    }
                });
                va2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        topHalfLayout.setBackgroundColor(Color.TRANSPARENT);
                        bottomHalfLayout.setBackgroundColor(Color.TRANSPARENT);
                        topHalfLayout.setAlpha(1F);
                        bottomHalfLayout.setAlpha(1F);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                va2.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();

        updateScores();
        killWinListeners();
    }

    private void topPoint() {
        topHit = true;
        topScore++;
        topMole.setImageResource(R.drawable.pow);


        ValueAnimator va = ValueAnimator.ofFloat(0F, 1.0F);
        va.setDuration(backgroundFlashSpeed);
        va.setInterpolator(new AccelerateDecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.v(TAG, "Animated Value: " + String.valueOf(animation.getAnimatedValue()));
                bottomHalfLayout.setAlpha((Float) animation.getAnimatedValue());
                topHalfLayout.setAlpha((Float) animation.getAnimatedValue());
                //bottomScoreText.setScaleY((Float) animation.getAnimatedValue());
            }
        });


        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                topHalfLayout.setBackgroundColor(Color.RED);
                bottomHalfLayout.setBackgroundColor(Color.RED);
                topHalfLayout.setAlpha(0F);
                bottomHalfLayout.setAlpha(0F);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ValueAnimator va2 = ValueAnimator.ofFloat(1.0F, 0F);
                va2.setDuration(backgroundFlashSpeed);
                va2.setInterpolator(new AccelerateDecelerateInterpolator());
                va2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        bottomHalfLayout.setAlpha((Float) animation.getAnimatedValue());
                        topHalfLayout.setAlpha((Float) animation.getAnimatedValue());

                        // bottomScoreText.setScaleX((Float) animation.getAnimatedValue());
                        //  bottomScoreText.setScaleY((Float) animation.getAnimatedValue());
                    }
                });
                va2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        topHalfLayout.setBackgroundColor(Color.TRANSPARENT);
                        bottomHalfLayout.setBackgroundColor(Color.TRANSPARENT);
                        topHalfLayout.setAlpha(1F);
                        bottomHalfLayout.setAlpha(1F);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                va2.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();

        updateScores();
        killWinListeners();
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
        bottomHalfLayout = (RelativeLayout) findViewById(R.id.bottomHalfRelativeLayout);
        topHalfLayout = (RelativeLayout) findViewById(R.id.topHalfRelativeLayout);
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
        textSize = bottomScoreText.getTextSize() / 3;

    }

    public void hideSelectedMoles() {
        killWinListeners();
        if (topHit) {
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.v(TAG, "Run delayed thread");
                    topMole = (ImageView) findViewById(topsMoleImageView);
                    bottomMole = (ImageView) findViewById(bottomsSelectedMole);
                    topMole.setImageResource(R.drawable.mole);


                    bottomMole.setImageResource(R.drawable.mole);
                    topMole.setVisibility(View.INVISIBLE);
                    bottomMole.setVisibility(View.INVISIBLE);
                }
            }, 800);
        } else if (bottomHit) {
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.v(TAG, "Run delayed thread");
                    topMole = (ImageView) findViewById(topsMoleImageView);
                    bottomMole = (ImageView) findViewById(bottomsSelectedMole);
                    topMole.setImageResource(R.drawable.mole);


                    bottomMole.setImageResource(R.drawable.mole);
                    topMole.setVisibility(View.INVISIBLE);
                    bottomMole.setVisibility(View.INVISIBLE);
                }
            }, 800);
        } else {
            topMole.setImageResource(R.drawable.mole);
            bottomMole.setImageResource(R.drawable.mole);

            topMole.setVisibility(View.INVISIBLE);
            bottomMole.setVisibility(View.INVISIBLE);
        }
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
        topHit = false;
        bottomHit = false;
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        randomMole = rand.nextInt((6 - 1) + 1) + 1;


        switch (randomMole) {
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
        topMole.setImageResource(R.drawable.mole);


        bottomMole.setImageResource(R.drawable.mole);

    }


}
