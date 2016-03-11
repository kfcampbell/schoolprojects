package keegancampbell.arttherapy;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

// create a Broadcast Receiver that listens to SMS messages
// in the case that the emulator can't properly detect shaking

// TODO (kcampbell)
// 1. solve cannot resolve getSystemService error.


public class SmsReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

        // boilerplate code to read the text message
        Bundle bundle = intent.getExtras();
        Object[] messages = (Object[]) bundle.get("pdus");
        SmsMessage message = SmsMessage.createFromPdu((byte[]) messages[0]);

        // Log the message
        Log.d("MYRECEIVER", message.getMessageBody() + " from " + message.getOriginatingAddress());

        // send notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context) // Context error, as per usual.
                        .setContentTitle("Drawing is fun!")
                        .setContentText("You should really draw");
        // launch draw activity when clicked
        Intent resultIntent = new Intent(context, PaintActivity.class);

        // more boilerplate android code
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context); // Same Context error
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(PaintActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        Integer mId = 8;
        mNotificationManager.notify(mId, mBuilder.build());



    }
}
