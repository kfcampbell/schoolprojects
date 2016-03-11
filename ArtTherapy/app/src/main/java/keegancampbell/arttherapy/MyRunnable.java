package keegancampbell.arttherapy;

import android.content.Context;
import android.media.MediaPlayer;

public class MyRunnable implements Runnable
{
    @Override
    public void run()
    {
        // play eraser sound here
        // use an intent service in addition to or instead of a runnable?

        Context context = null; // similar context issue to other issues
        MediaPlayer mp = MediaPlayer.create(context, R.raw.eraser);
        mp.start();
    }
}
