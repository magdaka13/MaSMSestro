package mg.masmsestro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import android.net.Uri;
import android.database.Cursor;








import mg.masmsestro.DBHelper;

import java.util.ArrayList;

import static java.util.Collections.*;

public class SMSActivity extends AppCompatActivity {

    private List<SMS> SMSList;
    private List<String>SMSList_string=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String folder_name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        setTitle(" MaSMSestro->" + folder_name);

        setContentView(R.layout.sms_list_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSMS);
        setSupportActionBar(toolbar);

        DBHelper dbHelper=new DBHelper(getApplicationContext());
        SMSList=dbHelper.getAllSMSbyFolderName(folder_name);
Log.e("MaSMSestro","Retreived SMSList size="+SMSList.size());

        for (int i=0;i<SMSList.size();i++) {
          //  String sms_short=SMSList.get(i).getTel_no().toString()+System.getProperty("line.separator")+SMSList.get(i).getContent().substring(0,50)+"...";
            String sms_body;
            if (SMSList.get(i).getContent().length()>=20) {
                sms_body = SMSList.get(i).getContent().substring(0, 20);
            }
            else
            {
                sms_body=SMSList.get(i).getContent();
            }

           String sms_short=SMSList.get(i).getTel_no().toString()+System.getProperty("line.separator")+sms_body+"...";
            SMSList_string.add(sms_short);


        }

        if (SMSList_string.size()>0) {
            ListView SMSItems = (ListView) findViewById(R.id.SMSList);
            ArrayAdapter a = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.my_list_item1, SMSList_string
            );
            SMSItems.setAdapter(a);
        }

/*
     //    Log.e("MaSMSestro", "created new activity SMS->" + folder_name);

        DBHelper dbHelper=new DBHelper(getApplicationContext());

        Integer no=dbHelper.numberOfRows();
        Log.e("MaSMSestro",no.toString() );

        if  (no==0)
        {
            dbHelper.insertFolder( "General");
            dbHelper.insertFolder( "SPAM");
        }

        FolderList = dbHelper.getAllFolders();

        ListView SMSItems=(ListView) findViewById(R.id.SMSList);
         ArrayAdapter a=new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.my_list_item1,FolderList
        );
        SMSFolders.setAdapter(a);




        SMSFolders.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> p, View v,int pos,long id) {
                Toast.makeText(getApplicationContext(),FolderList.get(pos), Toast.LENGTH_LONG).show();

            }
        });
*/

        FloatingActionButton NewSMS = (FloatingActionButton) findViewById(R.id.NewSMS);
        NewSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, " New  SMS will be created here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
