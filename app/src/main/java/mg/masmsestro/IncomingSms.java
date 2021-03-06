package mg.masmsestro;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.telephony.SmsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.app.PendingIntent;
import android.app.NotificationManager;

import mg.masmsestro.DBHelper;
import android.util.Log;

/**
 * Created by magda on 2017-05-11.
 */

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    @SuppressWarnings("CanBeFinal")
    private int mId;

    public void onReceive(Context context, Intent intent) {


        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (Object aPdusObj : pdusObj != null ? pdusObj : new Object[0]) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String message = currentMessage.getDisplayMessageBody();

                    DBHelper dbHelper = new DBHelper(context);

                    SMS_MMS_Reader sms_mms_reader = new SMS_MMS_Reader();
                    int thread_id = sms_mms_reader.read_SMS_MMS(dbHelper, context);
                    Log.e("Masmsestro", "thread_id=" + thread_id);
                    dbHelper.close();

                    String msg = phoneNumber + ": " + message;
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("MaSMSestro")
                                    .setContentText(msg)
                                    .setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_SOUND);
// Creates an explicit intent for an Activity in your app
                    Intent resultIntent = new Intent(context, SMSActivity.class);
                    Bundle extras2 = new Bundle();
                    extras2.putString("THREAD_ID_STRING", String.valueOf(thread_id));
                    extras2.putString("SMS_KEYWORD_STRING", message);
                    resultIntent.putExtras(extras2);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
                    stackBuilder.addParentStack(SMSActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );

                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.

                    Intent MarkAsRead = new Intent();
                    MarkAsRead.setAction(AppConstant.MarkAsRead);
                    Bundle extras = new Bundle();
                    extras.putString("THREAD_ID_STRING", String.valueOf(thread_id));
                    extras.putString("SMS_KEYWORD_STRING", message);
                    MarkAsRead.putExtras(extras);
                    PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, 12345, MarkAsRead, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.addAction(R.mipmap.ic_launcher, "Mark as read", pendingIntentYes);


                    Intent Delete = new Intent();
                    Delete.setAction(AppConstant.Delete);
                    Bundle extras1 = new Bundle();
                    extras1.putString("THREAD_ID_STRING", String.valueOf(thread_id));
                    extras1.putString("SMS_KEYWORD_STRING", "");
                    Delete.putExtras(extras1);
                    PendingIntent pendingIntentYes2 = PendingIntent.getBroadcast(context, 12345, Delete, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.addAction(R.mipmap.ic_launcher, "Delete", pendingIntentYes2);

                    Notification notification = mBuilder.build();
                    notification.tickerText = msg;

                    mNotificationManager.notify(mId, notification);

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("MaSMSestro", "Exception smsReceiver" +e);

        }
    }

}