package hpiz.reaction.com.reaction.miniGames.surpriseReaction;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Created by cjacobsen on 5/1/2017.
 */

public class SurpriseReactionBackgroundTask extends AsyncTask<String, Integer, String> {
    private final String TAG = "DropReactionBackgroundTask.java";
    private final SurpriseReaction gActivity;
    private final WeakReference<SurpriseReaction> gameActivity;
    private ConstraintLayout contentContainer;

    public SurpriseReactionBackgroundTask(SurpriseReaction a) {
        gameActivity = new WeakReference<SurpriseReaction>(a);
        gActivity = gameActivity.get();
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... params) {
        /*
        if (gActivity.redScore>9){

            gActivity.redWonGame();
            return "";
        }
        if (gActivity.blueScore>9){
            gActivity.blueWonGame();
            return "";
        }
*/
        Log.v("backgroundTask", "Running background task");
        if (params[0].equals("SLEEP:1000")) {
            if (this.isCancelled()) {
                return "CANCEL";
            }
            Log.v("backgroundTask", "Running background sleep");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.isCancelled()) {
                return "CANCEL";
            }
            return "SLEPT";
        } else {
            Log.v("backgroundTask", "Running background game wait");
            if (this.isCancelled()) {
                return "CANCEL";
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.isCancelled()) {
                return "CANCEL";
            }

            if (this.isCancelled()) {
                return "CANCEL";
            }
            return "GAME:NEXTSTEP";
        }
    }

    protected void onProgressUpdate(Integer progress) {

    }

    protected void onPostExecute(String result) {

        Log.v("backgroundTask", "Setting white");
        if (result.equals("GAME:NEXTSTEP")) {
            gActivity.setTopRed();
            gActivity.setBottomBlue();
            gActivity.startButtonListeners();
        } else if (result.equals("SLEPT")) {
            gActivity.runSingleRound();

        } else if (result.equals("CANCEL")) {
            this.cancel(true);

        }
        // topHalf.setBackgroundColor(Color.WHITE);

        //gActivity.runSimonSaysGame();
    }


}
