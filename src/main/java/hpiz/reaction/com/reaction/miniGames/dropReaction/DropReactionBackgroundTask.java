package hpiz.reaction.com.reaction.miniGames.dropReaction;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Created by cjacobsen on 5/1/2017.
 */

public class DropReactionBackgroundTask extends AsyncTask<String, Integer, String> {
    private final String TAG = "DropReactionBackgroundTask.java";
    private final DropReaction gActivity;
    private final WeakReference<DropReaction> gameActivity;
    private ConstraintLayout contentContainer;
    private long minimumRunTime = 1000;
    private long maximumRunTime = 4000;

    public DropReactionBackgroundTask(DropReaction a) {
        gameActivity = new WeakReference<DropReaction>(a);
        gActivity = gameActivity.get();
    }

    protected void onPreExecute() {
        if (!isCancelled()) {
            gActivity.InitializeDrop();
        }
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
        if (isCancelled()) {
            return null;
        }
        if (params[0].equals("SLEEP:1000")) {
            Log.v("DropBackgroundTask", "Running background sleep");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isCancelled()) {
                return null;
            }
            return "SLEPT";
        } else {
            try {
                Thread.sleep(minimumRunTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
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

            if (isCancelled()) {
                return null;
            }
            return "GAME:NEXTSTEP";
        }
    }

    protected void onProgressUpdate(Integer progress) {

    }

    protected void onPostExecute(String result) {
        if (!isCancelled()) {
            Log.v("backgroundTask", "dropping ruler");
            if (result.equals("GAME:NEXTSTEP")) {
                gActivity.drop();
                //gActivity.setTopColor();
                //gActivity.setBottomColor();
                //gActivity.startWinningWacListeners();
            } else if (result.equals("SLEPT")) {
                gActivity.runSingleRound();

            }
            // topHalf.setBackgroundColor(Color.WHITE);

            //gActivity.runSimonSaysGame();
        }
    }


}
