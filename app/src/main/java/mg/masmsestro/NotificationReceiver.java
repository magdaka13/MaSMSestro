package mg.masmsestro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String MarkAsRead = "mg.masmsestro.MarkAsRead";
    private static final String Delete = "mg.masmsestro.Delete";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        if (MarkAsRead.equals(action)) {
            Toast.makeText(context, "Mark as Read", Toast.LENGTH_SHORT).show();

        }
        else  if (Delete.equals(action)) {
            Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
        }
    }
}

