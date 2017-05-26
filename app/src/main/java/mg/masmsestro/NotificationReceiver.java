package mg.masmsestro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String MarkAsRead = "mg.masmsestro.MarkAsRead";
    private static final String Delete = "mg.masmsestro.Delete";

    @Override
    public void onReceive(Context context, Intent intent) {
        String thread_id;
        String sms_keyword;

        thread_id="";
        sms_keyword="";
        String action = intent.getAction();

        Bundle ex=intent.getExtras();
        try {
            thread_id = ex.getString("THREAD_ID_STRING");
            sms_keyword = ex.getString("SMS_KEYWORD_STRING");
        }
        catch(Exception e)
        {}

        DBHelper dbHelper=new DBHelper(context);
        if (MarkAsRead.equals(action)) {
            dbHelper.markConversationAsRead(dbHelper.getConversationbyThreadId(Integer.parseInt(thread_id)).getConv_id());
        }
        else  if (Delete.equals(action)) {
            SMS s;
            s=dbHelper.getSMSByBodyThread(sms_keyword, Integer.parseInt(thread_id));

            dbHelper.deleteSMS(s.getSms_id());

        }
        dbHelper.close();
    }
}

