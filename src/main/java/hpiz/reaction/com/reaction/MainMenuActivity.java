package hpiz.reaction.com.reaction;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainMenuActivity extends Activity {
    private Button startGameButton;
    private Button settingsButton;
    private TextView mainMenuTitle;
    private ImageView backgroundImage;
    private ImageView backgroundImageHidden;
    private Button quitGameButton;
    private ConstraintLayout contentContainer;
    private int screenWidth;
    private int screenHeight;
    private SharedPreferences sp;
    private ValueAnimator buttonAnimator;
    private Button discardButton;
    private String topColor;
    private String bottomColor;
    private Button saveButton;
    private ValueAnimator backgroundAnimator;
    private ValueAnimator backgroundAnimator2;
    private ScrollView contentContainer2;


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

/*
    @Override
    public void onResume() {
        Log.v("MAINMenu", "onResume");
        super.onResume();

    }

*/

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu_activity);
        mm_hide();
        initializeObjects();
        configureObjects();
        animateShowMainMenu();


    }

    private void animateShowMainMenu() {

        buttonAnimator = ValueAnimator.ofFloat(4000, 0);
        buttonAnimator.setDuration(500);
        buttonAnimator.setInterpolator(new AccelerateInterpolator());

        buttonAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                settingsButton.setTranslationX(value);
                startGameButton.setTranslationX(-value);
                quitGameButton.setTranslationY(value);
                mainMenuTitle.setTranslationY(-value);

            }
        });
        buttonAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                settingsButton.bringToFront();
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                        /*
                        setContentView(R.layout.activity_settings);
                        s_hide();
                        initializeSettingsObjects();
                        configureSettingsObjects();
                        animateShowSettings();
                        */
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        buttonAnimator.start();

    }

    private void configureObjects() {
        backgroundImage.setMinimumWidth(screenWidth);
        backgroundImageHidden.setMinimumWidth(screenWidth);
        backgroundImage.setMinimumHeight(screenHeight);
        backgroundImageHidden.setMinimumHeight(screenHeight);
        quitGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateExitGame();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateHideMainMenu();
            }
        });
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonAnimator = ValueAnimator.ofFloat(0, 4000);
                buttonAnimator.setDuration(500);
                buttonAnimator.setInterpolator(new AccelerateInterpolator());

                buttonAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {


                        float value = (float) animation.getAnimatedValue();
                        settingsButton.setTranslationX(value);
                        startGameButton.setTranslationX(-value);
                        quitGameButton.setTranslationY(value);
                        mainMenuTitle.setTranslationY(-value);
                        backgroundImage.setAlpha((4000 - value) / 4000);
                        backgroundImageHidden.setAlpha((4000 - value) / 4000);

                    }
                });
                buttonAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                        settingsButton.bringToFront();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //Intent i = new Intent(MainMenuActivity.this, SettingsActivity.class);
                        //startActivity(i);
                        sp.edit().putInt("gameProgress", 0).apply();
                        Intent i = new Intent(MainMenuActivity.this, GameActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                buttonAnimator.start();

            }
        });
    }

    private void animateHideMainMenu() {

        buttonAnimator = ValueAnimator.ofFloat(0, 4000);
        buttonAnimator.setDuration(500);
        buttonAnimator.setInterpolator(new AccelerateInterpolator());

        buttonAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                settingsButton.setTranslationX(value);
                startGameButton.setTranslationX(-value);
                quitGameButton.setTranslationY(value);
                mainMenuTitle.setTranslationY(-value);

            }
        });
        buttonAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                settingsButton.bringToFront();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent i = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(i);
                        /*
                        setContentView(R.layout.activity_settings);
                        s_hide();
                        initializeSettingsObjects();
                        configureSettingsObjects();
                        animateShowSettings();
                        */
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        buttonAnimator.start();

    }


    private void saveSettings() {
        sp.edit()
                .putString("topColor", topColor)
                .putString("bottomColor", bottomColor)
                .apply();

    }


    private void animateExitGame() {
        ValueAnimator settingsAnimator = ValueAnimator.ofFloat(1, 0);
        settingsAnimator.setDuration(300);
        settingsAnimator.setInterpolator(new AccelerateInterpolator());

        settingsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                contentContainer.setAlpha(value);


            }
        });
        settingsAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //Intent i = new Intent(MainMenuActivity.this, SettingsActivity.class);
                //startActivity(i);
                System.exit(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        settingsAnimator.start();
    }


    private void initializeObjects() {
        startGameButton = (Button) findViewById(R.id.startGameButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        quitGameButton = (Button) findViewById(R.id.quitGameButton);
        mainMenuTitle = (TextView) findViewById(R.id.main_menu_title);
        backgroundImage = (ImageView) findViewById(R.id.main_menu_background_image);
        backgroundImageHidden = (ImageView) findViewById(R.id.main_menu_background_image_animate);
        sp = getSharedPreferences("runningPreferences", Context.MODE_PRIVATE);
//refreshBackground();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    private void mm_hide() {
        // Hide UI first

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //mControlsView.setVisibility(View.GONE);
        //mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        contentContainer = (ConstraintLayout) findViewById(R.id.contentContainer);
        /*
        contentContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE

                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                */

    }

















}



