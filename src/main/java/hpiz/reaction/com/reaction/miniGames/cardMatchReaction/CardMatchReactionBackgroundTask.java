package hpiz.reaction.com.reaction.miniGames.cardMatchReaction;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;

import java.lang.ref.WeakReference;

/**
 * Created by cjacobsen on 5/1/2017.
 */

public class CardMatchReactionBackgroundTask extends AsyncTask<String, Integer, String> {
    private final String TAG = "cardmach background";
    private final CardMatchReaction gActivity;
    private final WeakReference<CardMatchReaction> gameActivity;
    private ConstraintLayout contentContainer;
    private Handler uiHandler = new Handler(Looper.getMainLooper());


    public CardMatchReactionBackgroundTask(CardMatchReaction a) {
        gameActivity = new WeakReference<CardMatchReaction>(a);
        gActivity = gameActivity.get();
    }

    protected void onPreExecute() {
        gActivity.startButtonListeners();
    }
    @Override
    protected String doInBackground(String... params) {
        if (isCancelled()) {
            return null;
        }
        while (!isCancelled()) {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isCancelled()) {
                return null;
            } else {
                onProgressUpdate(1);
                //gActivity.showACard();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isCancelled()) {
                return null;
            } else {
                //gActivity.runSingleCard();
            }


        }
        return null;
    }

    protected void onProgressUpdate(Integer progress) {
        if (progress == 1) {
            uiHandler.post(new Runnable() {
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
