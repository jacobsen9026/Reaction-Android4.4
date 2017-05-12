package hpiz.reaction.com.reaction;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SettingsActivity extends Activity {
    private final String TAG = "com.reaction.hpiz.SettingsActivity";
    private Button discardButton;
    private Button saveButton;
    private LinearLayout contentContainer;
    private SharedPreferences sp;
    private String topColor;
    private String bottomColor;
    private Spinner spinner;
    private Spinner spin2;
    private LinearLayout gameOrderContainer1;


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        initializeObjects();
        configureObjects();
        hide();
        animateShowSettings();

    }

    private void configureObjects() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                animateExitSettings();
            }
        });
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateExitSettings();
            }
        });

        gameOrderContainer1.setOnTouchListener(new View.OnTouchListener() {
            boolean firstTrigger = true;
            float initX;
            float posX;
            float posY;
            float initY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.v("Settings", "Touching box1");
                int action = event.getAction() & MotionEvent.ACTION_MASK;
                if (action == MotionEvent.ACTION_DOWN) {
                    if (event.getX() > v.getWidth() / 7) {
                        return false;
                    }
                    Log.v("settings", "action down");

                    Log.v("Settings", "Initial X Position:" + String.valueOf(event.getRawX()));
                    posX = v.getX();
                    posY = v.getY();
                    initX = event.getRawX();
                    initY = event.getRawY();

                }
                if (action == MotionEvent.ACTION_UP) {
                    Log.v("Settins", "action up");
                    //newPosX=
                    gameOrderContainer1.setTranslationX(0);
                    gameOrderContainer1.setTranslationY(0);
                } else {
                    //Log.v("settings", String.valueOf((initX - event.getRawX())));
                    Log.v("Settings", "raw:" + String.valueOf(event.getRawX()));
                    Log.v("Settings", "relative:" + String.valueOf(event.getX()));
                    Log.v("Settings", "Difference:" + String.valueOf(event.getRawX() - initX));
                    gameOrderContainer1.setTranslationX(event.getRawX() - initX);
                    gameOrderContainer1.setTranslationY(event.getRawY() - initY);
                }
                return true;
            }
        });

    }

    private void initializeObjects() {
        saveButton = (Button) findViewById(R.id.saveButton);
        discardButton = (Button) findViewById(R.id.discardButton);
        sp = getSharedPreferences("runningPreferences", Context.MODE_PRIVATE);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        spinner = (Spinner) findViewById(R.id.spinner);
        gameOrderContainer1 = (LinearLayout) findViewById(R.id.gameOrder1);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void animateShowSettings() {
        setupColorPickers();
        loadSettings();
        ValueAnimator settingsAnimator = ValueAnimator.ofFloat(4000, 0);
        settingsAnimator.setDuration(400);
        settingsAnimator.setInterpolator(new DecelerateInterpolator());

        settingsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                contentContainer.setTranslationY(value);


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

    private void animateExitSettings() {

        ValueAnimator settingsAnimator = ValueAnimator.ofFloat(0, 4000);
        settingsAnimator.setDuration(400);
        settingsAnimator.setInterpolator(new DecelerateInterpolator());

        settingsAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                contentContainer.setTranslationY(value);


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
                Intent i = new Intent(SettingsActivity.this, MainMenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

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

    private void loadSettings() {
        topColor = sp.getString("topColor", "#BB3500");
        bottomColor = sp.getString("bottomColor", "#3D5B7E");
        spinner.setSelection(matchTopColor(topColor));
        spin2.setSelection(matchBottomColor(bottomColor));

    }

    private int matchTopColor(String topColor) {
        int index = -1;
        int i = 0;
        for (String c : getResources().getStringArray(R.array.topColors)) {
            Log.v("Match Colors", c + " " + topColor);
            if (topColor.equals(c)) {
                index = i;
                Log.v("Match Colors", c + " " + topColor + String.valueOf(i));
            }
            i++;
        }
        return index;
    }

    private int matchBottomColor(String topColor) {
        int index = -1;
        int i = 0;
        for (String c : getResources().getStringArray(R.array.bottomColors)) {
            Log.v("Match Colors", c + " " + topColor);
            if (topColor.equals(c)) {
                index = i;
                Log.v("Match Colors", c + " " + topColor);
            }
            i++;
        }
        return index;
    }

    private void setupColorPickers() {


        spinner.setAdapter(new ColorSpinnerAdapter(this, true));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                topColor = getResources().getStringArray(R.array.topColors)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin2.setAdapter(new ColorSpinnerAdapter(this, false));
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bottomColor = getResources().getStringArray(R.array.bottomColors)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //s_hide();
                return false;
            }
        });
    }

    private void saveSettings() {
        sp.edit()
                .putString("topColor", topColor)
                .putString("bottomColor", bottomColor)
                .apply();

    }

    private void hide() {
        // Hide UI first
        //mControlsView.setVisibility(View.GONE);
        //mVisible = false;
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Schedule a runnable to remove the status and navigation bar after a delay
        contentContainer = (LinearLayout) findViewById(R.id.contentContainer2);
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
