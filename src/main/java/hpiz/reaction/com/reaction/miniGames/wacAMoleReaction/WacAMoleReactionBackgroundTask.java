package hpiz.reaction.com.reaction.miniGames.wacAMoleReaction;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Created by cjacobsen on 5/1/2017.
 */

public class WacAMoleReactionBackgroundTask extends AsyncTask<String, Integer, String> {
    private static final String TAG = WacAMoleReaction.class.getSimpleName();
    private final WacAMoleReaction gActivity;
    private final WeakReference<WacAMoleReaction> gameActivity;
    private ConstraintLayout contentContainer;

    public WacAMoleReactionBackgroundTask(WacAMoleReaction a) {
        gameActivity = new WeakReference<WacAMoleReaction>(a);
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
        Log.v(TAG, "Running background task");
        if (params[0].equals("SLEEP:1000")) {
            Log.v("backgroundTask", "Running background sleep");
            if (this.isCancelled()) {
                return "CANCEL";
            }
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.isCancelled()) {
                return "CANCEL";
            }

            return "SLEPT";
        } else {
            if (this.isCancelled()) {
                return "CANCEL";
            }
            Log.v(TAG, "Running background game wait");

            try {
                Thread.sleep(400);
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

        Log.v(TAG, "Setting white");
        if (result.equals("GAME:NEXTSTEP")) {
            gActivity.pickMole();
            gActivity.startWinningWacListeners();
            gActivity.waitForWin();
            //gActivity.startWinningWacListeners();
        } else if (result.equals("SLEPT")) {
            gActivity.hideSelectedMoles();
            gActivity.runSingleRound();

        } else if (result.equals("CANCEL")) {
            this.cancel(true);

        }
        // topHalf.setBackgroundColor(Color.WHITE);

        //gActivity.runSimonSaysGame();
    }


}
