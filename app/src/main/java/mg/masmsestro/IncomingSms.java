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

                    SMS_MMS_Reader sms_mms_reader=new SMS_MMS_Reader();
                    sms_mms_reader.read_SMS_MMS(dbHelper,context);
                    dbHelper.close();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("MaSMSestro", "Exception smsReceiver" +e);

        }
    }

}