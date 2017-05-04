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

    public DropReactionBackgroundTask(DropReaction a) {
        gameActivity = new WeakReference<DropReaction>(a);
        gActivity = gameActivity.get();
    }

    protected void onPreExecute() {
        gActivity.InitializeDrop();

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

        if (params[0].equals("SLEEP:1000")) {
            Log.v("DropBackgroundTask", "Running background sleep");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "SLEPT";
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return "GAME:NEXTSTEP";
        }
    }

    protected void onProgressUpdate(Integer progress) {

    }

    protected void onPostExecute(String result) {

        Log.v("backgroundTask", "Setting white");
        if (result.equals("GAME:NEXTSTEP")) {
            gActivity.drop();
            //gActivity.setTopRed();
            //gActivity.setBottomBlue();
            //gActivity.startWinningWacListeners();
        } else if (result.equals("SLEPT")) {
            gActivity.runSingleRound();

        }
        // topHalf.setBackgroundColor(Color.WHITE);

        //gActivity.runSimonSaysGame();
    }


}
