package hpiz.reaction.com.reaction;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;

import hpiz.reaction.com.reaction.miniGames.cardMatchReaction.CardMatchReaction;
import hpiz.reaction.com.reaction.miniGames.surpriseNumberedReaction.SurpriseNumberedReaction;
import hpiz.reaction.com.reaction.miniGames.surpriseReaction.SurpriseReaction;
import hpiz.reaction.com.reaction.miniGames.wacAMoleReaction.WacAMoleReaction;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameActivity extends Activity {
    private ConstraintLayout contentContainer;
    private String TAG = "GameActivity";
    private int gameProgress;
    private SharedPreferences sp;


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // save your sate here
        sp.edit().putInt("gameProgress", gameProgress).apply();
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Invoke onCreate()");
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("runningPreferences", Context.MODE_PRIVATE);
        gameProgress = sp.getInt("gameProgress", 0);
        setContentView(R.layout.activity_game);

        //configureObjects();
        hide();
        Intent i;
        Log.v(TAG, "GameProgress=" + String.valueOf(gameProgress));
        switch (gameProgress) {
            case 3:
                i = new Intent(GameActivity.this, SurpriseReaction.class);
                gameProgress++;
                Log.v(TAG, "GameProgress=" + String.valueOf(gameProgress));
                //sp.edit().putInt("gameProgress",gameProgress).commit();
                startActivity(i);
                break;
            case 2:
                i = new Intent(GameActivity.this, SurpriseNumberedReaction.class);
                gameProgress++;
                //sp.edit().putInt("gameProgress",gameProgress).commit();
                startActivity(i);
                break;

            case 1:
                i = new Intent(GameActivity.this, WacAMoleReaction.class);
                gameProgress++;
                //sp.edit().putInt("gameProgress",gameProgress).commit();
                startActivity(i);
                break;
/*
            case 0:
                i = new Intent(GameActivity.this, CatchReaction.class);
                gameProgress++;
                //sp.edit().putInt("gameProgress",gameProgress).commit();
                startActivity(i);
                break;
                */
            case 0:
                i = new Intent(GameActivity.this, CardMatchReaction.class);
                gameProgress++;
                Log.v(TAG, "GameProgress=" + String.valueOf(gameProgress));
                //sp.edit().putInt("gameProgress",gameProgress).commit();
                startActivity(i);
                break;
            case 4:
                i = new Intent(GameActivity.this, MainMenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //sp.edit().putInt("gameProgress",gameProgress).commit();
                startActivity(i);
                break;
        }

    }

    public void hide() {
        // Hide UI first
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
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

}
