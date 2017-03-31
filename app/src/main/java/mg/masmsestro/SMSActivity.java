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

import mg.masmsestro.DBHelper;

import java.util.ArrayList;

public class SMSActivity extends AppCompatActivity {
    ArrayList<String> SMSList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        setTitle(" MaSMSestro->" + message);

        setContentView(R.layout.sms_list_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSMS);
        setSupportActionBar(toolbar);


        Log.e("MaSMSestro", "created new activity SMS->" + message);
/*
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
