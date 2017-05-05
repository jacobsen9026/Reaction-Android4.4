package hpiz.reaction.com.reaction.miniGames.catchReaction;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by cjacobsen on 5/1/2017.
 */

public class CatchReactionBackgroundTask extends AsyncTask<String, Integer, String> {
    private final String TAG = "DropReactionBackgroundTask.java";
    private final CatchReaction gActivity;
    private final WeakReference<CatchReaction> gameActivity;
    private ConstraintLayout contentContainer;

    public CatchReactionBackgroundTask(CatchReaction a) {
        gameActivity = new WeakReference<CatchReaction>(a);
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
        if (isCancelled()) {
            return "CANCEL";
        }
        if (params[0].equals("SLEEP:1000")) {
            Log.v("DropBackgroundTask", "Running background sleep");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isCancelled()) {
                return "CANCEL";
            }
            return "SLEPT";
        } else {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isCancelled()) {
                return "CANCEL";
            }


            return "GAME:NEXTSTEP";
        }
    }

    protected void onProgressUpdate(Integer progress) {

    }

    protected void onPostExecute(String result) {
        if (isCancelled()) {
            return;
        }

        if (result.equals("GAME:NEXTSTEP")) {
            Log.v("CatchReactionBackground", "throw ball");
            gActivity.throwUp();
            gActivity.checkForWinner();
            //gActivity.setTopRed();
            //gActivity.setBottomBlue();
            //gActivity.startWinningWacListeners();
        } else if (result.equals("SLEPT")) {
            Log.v("CatchReactionBackground", "runagain");
            gActivity.checkForWinner();

        }

        // topHalf.setBackgroundColor(Color.WHITE);

        //gActivity.runSimonSaysGame();
    }


}
