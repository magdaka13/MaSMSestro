package mg.masmsestro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class SMSActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private List<String> SMSList_string=new ArrayList<String>();
    private ListView SMSList_view;
private ArrayAdapter a;
    private String thread_id,sms_keyword;

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


            Log.e("MaSMSestro", "inside SMS activity");

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
        getMenuInflater().inflate(R.menu.menu_sms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
d=new Dialog(context); // Context, this, etc.

            d.setTitle(R.string.action_search);
            d.setContentView(R.layout.search_dialog);
            d.setCancelable(true);
            d.show();


            Button btnCancel = (Button) d.findViewById(R.id.dialog_cancel);
            // if button is clicked, close the custom dialog
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConversationActivity.this.a.getFilter().filter("");
                    d.cancel();

                }
            });

            Button btnSearch = (Button) d.findViewById(R.id.btn_search);
            // if button is clicked, close the custom dialog
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText e=(EditText)d.findViewById(R.id.str_to_search);
                    ConversationActivity.this.a.getFilter().filter(e.getText());
                    d.cancel();
                }
            });

            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }


}
