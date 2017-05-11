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
    private long waitTime = 1200;
    private long minimumRunTime = 1000;
    private int maximumRunTime = 6000;

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
                Thread.sleep(waitTime);
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
                Thread.sleep(minimumRunTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.isCancelled()) {
                return "CANCEL";
            }
            Random rand = new Random();

            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            long randomTime = rand.nextInt((int) (((maximumRunTime - minimumRunTime) - 1) + 1)) + 1;
            try {
                Thread.sleep(randomTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
            gActivity.setTopColor();
            gActivity.setBottomColor();
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
