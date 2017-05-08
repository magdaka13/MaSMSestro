package mg.masmsestro;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class SMSNewActivity extends AppCompatActivity {

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
        setContentView(R.layout.sms_new_layout);
        setTitle(" MaSMSestro->New SMS");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSMS);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);



            Log.e("MaSMSestro", "inside SMSnew activity");


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

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }


}
