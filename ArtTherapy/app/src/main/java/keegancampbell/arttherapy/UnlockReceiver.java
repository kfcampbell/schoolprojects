package keegancampbell.arttherapy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

// check when phone is unlocked and display a notification

// TODO (kcampbell)
// 1. solve cannot resolve getSystemService error.
public class UnlockReceiver extends BroadcastReceiver
{

    public void onReceive(Context context, Intent intent)
    {

        if(intent.getAction().equals(Intent.ACTION_USER_PRESENT))
        {
            // send notification
            int requestCode = 0;
            int flags = 0;
            Intent paint = new Intent(context, PaintActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, paint, flags);

            int id = 8;
            Notification notification = new Notification.Builder(context)
                    .setContentTitle("Drawing is fun!")
                    .setContentText("You should really draw")
                    .setSmallIcon(android.R.drawable.ic_menu_day)
                    .setContentIntent(pendingIntent)
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, notification);
        }

    }

}