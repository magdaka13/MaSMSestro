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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class SMSDeleteActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private List<String> SMSList_string=new ArrayList<String>();
    private List<SMS> SMSList_obj=new ArrayList<SMS>();
    private ListView SMSList_view;
    private ArrayAdapter a;
    private String thread_id,sms_keyword;
 private Boolean checked;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_sms);
        setTitle(" MaSMSestro");

        setContentView(R.layout.sms_list_delete_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSMS);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle ex=intent.getExtras();
        thread_id = ex.getString("THREAD_ID_STRING");

        if (!thread_id.equals("")) {
            sms_keyword=new String("");

            setTitle(" MaSMSestro-> SMS");

            Log.e("MaSMSestro", "inside SMSDelete activity");

            dbHelper = new DBHelper(getApplicationContext());

            SMSList_string = dbHelper.getAllSMSByThread(Integer.valueOf(thread_id));
            SMSList_obj = dbHelper.getAllSMSByThread_SMS(Integer.valueOf(thread_id));


            SMSList_view = (ListView) findViewById(R.id.SMSList);
            a = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.my_listitemdelete, SMSList_string
            );
            SMSList_view.setAdapter(a);

        }

        CheckBox selectAll = (CheckBox) findViewById(R.id.checkBoxSelectAll);

        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;

                for (i = 0; i < SMSList_view.getCount(); i++)

                {
                    SMSList_view.setItemChecked(i, checked);
                }
            }
        });

        ImageButton toTrash = (ImageButton) findViewById(R.id.trash);

        toTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (i = 0; i < SMSList_view.getCount(); i++) {
                    if (SMSList_view.isItemChecked(i)) {

                        if (i < SMSList_obj.size()) {
                            dbHelper.deleteSMS(SMSList_obj.get(i).getSms_id());
                            if (dbHelper.numberOfRowsSMS_byThreadId(SMSList_obj.get(i).getThread_id())==0)
                            {
Conversation c;
                                c=dbHelper.getConversationbyThreadId(SMSList_obj.get(i).getThread_id());
if (c!=null) {
    dbHelper.deleteConversation(c);
    dbHelper.deleteConvRefFolder_byConvId(c.getConv_id());
}
                            }

                        }

                    }

                    Intent intent = new Intent(getApplicationContext(), SMSDeleteActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("THREAD_ID_STRING", thread_id);
                    intent.putExtras(extras);
                    startActivity(intent);

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent(getApplicationContext(), SMSActivity.class);
        Bundle extras = new Bundle();
        extras.putString("THREAD_ID_STRING", thread_id);
        intent.putExtras(extras);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }


}
