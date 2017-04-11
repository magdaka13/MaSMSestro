package mg.masmsestro;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class SMSActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private List<String> SMSList_string=new ArrayList<String>();
    private ListView SMSList_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String thread_id = intent.getStringExtra(ConversationActivity.EXTRA_MESSAGE);


        setContentView(R.layout.activity_sms);

        Log.e("MaSMSestro","inside SMS activity");

        dbHelper = new DBHelper(getApplicationContext());

        SMSList_string = dbHelper.getAllSMSByThread( Integer.valueOf(thread_id));


        SMSList_view = (ListView) findViewById(R.id.SMSList);
        ArrayAdapter a = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.my_list_item1, SMSList_string
        );
        SMSList_view.setAdapter(a);

    }

}
