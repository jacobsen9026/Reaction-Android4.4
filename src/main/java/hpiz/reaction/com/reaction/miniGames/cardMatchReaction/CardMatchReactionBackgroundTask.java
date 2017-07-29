package hpiz.reaction.com.reaction.miniGames.cardMatchReaction;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

/**
 * Created by cjacobsen on 5/1/2017.
 */

public class CardMatchReactionBackgroundTask extends AsyncTask<String, Integer, String> {
    private final String TAG = "cardmach background";
    private final CardMatchReaction gActivity;
    private final WeakReference<CardMatchReaction> gameActivity;

    private long cardDrawInterval = 1200;


    public CardMatchReactionBackgroundTask(CardMatchReaction a) {
        gameActivity = new WeakReference<CardMatchReaction>(a);
        gActivity = gameActivity.get();
    }

    protected void onPreExecute() {

    }
    @Override
    protected String doInBackground(String... params) {
        if (isCancelled()) {
            return null;
        }
        while (!isCancelled()) {
            try {
                Thread.sleep(cardDrawInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isCancelled()) {
                return null;
            } else {
                onProgressUpdate(1);
                //gActivity.showACard();
            }/*
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
            if (isCancelled()) {
                return null;
            } else {
                //gActivity.runSingleCard();
            }


        }
        return null;
    }

    protected void onProgressUpdate(Integer progress) {
        if (isCancelled()) {
            return;
        }
        if (progress == 1) {
            //gActivity.showACard();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    gActivity.showACard();
                }
            });

        }
    }

    protected void onPostExecute(String result) {
        return;

        // topHalf.setBackgroundColor(Color.WHITE);

        //gActivity.runSimonSaysGame();
    }


}
