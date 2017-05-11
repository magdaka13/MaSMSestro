package mg.masmsestro;

import android.content.BroadcastReceiver;
import android.telephony.SmsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import android.net.Uri;
import android.database.DatabaseUtils;
import android.database.Cursor;

import mg.masmsestro.DBHelper;
import android.util.Log;

/**
 * Created by magda on 2017-05-11.
 */

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    private DBHelper dbHelper;


    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    dbHelper=new DBHelper(context);
                    SMS s=new SMS();

                    int thread_id;

                    thread_id= (int) getId(currentMessage,"thread",context);
                    Log.e("MaSMSestro","thread_id="+thread_id);

                    s.setThread_id(thread_id);
                    s.setContent(message);
                    s.setTel_no(senderNum);
                    s.setDate_received(currentMessage.getTimestampMillis());
                    dbHelper.insertSMS(s);

                    if (thread_id==0) {
                        Conversation c = new Conversation();
                        c.setRecipient_list(senderNum);
                        c.setThread_id(thread_id);
                        c.setDate(currentMessage.getTimestampMillis());
                        c.setSnippet(currentMessage.getDisplayMessageBody());
                        dbHelper.insertConversation(c);
                    }

                    ConvRefFolder cF=new ConvRefFolder();
                    cF.setId_Conv(dbHelper.getConversationbyThreadId(thread_id).getConv_id());
                    cF.setId_folder(dbHelper.getFolderByName("Incoming"));
                    dbHelper.insertConvRefFolder(cF);

                    dbHelper.close();
                 //   Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

/*
                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();
*/


                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("MaSMSestro", "Exception smsReceiver" +e);

        }
    }

    /**
     * Tries to locate the message id or thread id given the address (phone number or email) of the message sender.
     * @return long.
     */
    public long getId(SmsMessage msg,String idType,Context context){
        String SMS_CONTENT_INBOX="content://sms/inbox";
        Uri uriSms=Uri.parse(SMS_CONTENT_INBOX);
        StringBuilder sb=new StringBuilder();
        sb.append("address='" + msg.getOriginatingAddress() + "' AND ");
        sb.append("body=" + DatabaseUtils.sqlEscapeString(msg.getDisplayMessageBody()));
        Cursor c=context.getContentResolver().query(uriSms,null,sb.toString(),null,null);
        if (c.getCount() > 0 && c != null) {
            c.moveToFirst();
            if (idType.equals("id")) {
                return c.getLong(c.getColumnIndex("_id"));
            }
            else     if (idType.equals("thread")) {
                return c.getLong(c.getColumnIndex("thread_id"));
            }
            c.close();
        }
        return 0;
    }
}