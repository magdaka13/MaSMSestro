package mg.masmsestro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class SMSActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private List<String> SMSList_string=new ArrayList<String>();
    private ListView SMSList_view;
private ArrayAdapter a;
    private String thread_id,sms_keyword;
private Dialog d;
    private Context context=this;
    private List<String> FolderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_sms);
        setTitle(" MaSMSestro");

        setContentView(R.layout.smslist_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSMS);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

Bundle ex=intent.getExtras();
    thread_id = ex.getString("THREAD_ID_STRING");
    sms_keyword = ex.getString("SMS_KEYWORD_STRING");

        if (!thread_id.equals("")) {
            sms_keyword=new String("");

            setTitle(" MaSMSestro-> Conversation");


            Log.e("MaSMSestro", "inside SMS activity thread_id="+thread_id);

            dbHelper = new DBHelper(getApplicationContext());

            SMSList_string = dbHelper.getAllSMSByThread(Integer.valueOf(thread_id));


            SMSList_view = (ListView) findViewById(R.id.SMSList);
            a = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.my_list_item1, SMSList_string
            );
            SMSList_view.setAdapter(a);

            SMSList_view.post(new Runnable() {
                @Override
                public void run() {
                    // Select the last row so it will scroll into view...
                    SMSList_view.setSelection(a.getCount() - 1);
                }
            });
        }

        if (!sms_keyword.equals(""))
        {
            thread_id=new String("");

            setTitle(" MaSMSestro-> Found SMS");

            dbHelper = new DBHelper(getApplicationContext());

            SMSList_string = dbHelper.getAllSMSByKeyword(sms_keyword);


            SMSList_view = (ListView) findViewById(R.id.SMSList);
            a = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.my_list_item1, SMSList_string
            );
            SMSList_view.setAdapter(a);

            SMSList_view.post(new Runnable() {
                @Override
                public void run() {
                    // Select the last row so it will scroll into view...
                    SMSList_view.setSelection(a.getCount() - 1);
                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
if (!getTitle().equals(" MaSMSestro-> Found SMS")) {
    getMenuInflater().inflate(R.menu.menu_sms, menu);
}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (!getTitle().equals(" MaSMSestro-> Found SMS")) {

            if (id == R.id.action_move_conversation) {

                d = new Dialog(context); // Context, this, etc.

                d.setTitle("Move");
                d.setContentView(R.layout.move_dialog);
                d.setCancelable(true);
                d.show();


                final Spinner s = (Spinner) d.findViewById(R.id.FolderName);
                FolderList = dbHelper.getAllFoldersNames();


                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, FolderList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(dataAdapter);


                Button btnCancel = (Button) d.findViewById(R.id.dialog_cancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.cancel();

                    }
                });

                Button btnOK = (Button) d.findViewById(R.id.btn_search);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("MaSMSestro", "folder=" + String.valueOf(s.getSelectedItem()));
                        Log.e("MaSMSestro", "conv_id=" + dbHelper.getConversationbyThreadId(Integer.valueOf(thread_id)).getConv_id());
                        String folder_name=dbHelper.getFolderByThreadId(Integer.valueOf(thread_id));
                        int a = dbHelper.moveConversationToFolder(String.valueOf(s.getSelectedItem()), dbHelper.getConversationbyThreadId(Integer.valueOf(thread_id)).getConv_id());
                        Log.e("MaSMSestro", Integer.toString(a));
                        d.dismiss();

                        Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("FOLDER_NAME", folder_name);
                        intent.putExtras(extras);
                        startActivity(intent);

                    }
                });

                return true;
            }

            if (id == R.id.action_delete_conversation) {

                String folder_name=dbHelper.getFolderByThreadId(Integer.valueOf(thread_id));
                Conversation s=dbHelper.getConversationbyThreadId(Integer.valueOf(thread_id));
                dbHelper.deleteConversation(s);
                dbHelper.deleteAllSMSFromConv(s);

                Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
                Bundle extras = new Bundle();
                extras.putString("FOLDER_NAME", folder_name);
                intent.putExtras(extras);
                startActivity(intent);

                return true;
            }

            if (id==R.id.action_new_sms)
            {
                Intent intent = new Intent(getApplicationContext(), SMSNewActivity.class);
                startActivity(intent);

                return true;
            }

            if (id==R.id.action_delete_sms)
            {
                Intent intent = new Intent(getApplicationContext(), SMSDeleteActivity.class);
                Bundle extras = new Bundle();
                extras.putString("THREAD_ID_STRING", thread_id);
                intent.putExtras(extras);
                startActivity(intent);

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


}
