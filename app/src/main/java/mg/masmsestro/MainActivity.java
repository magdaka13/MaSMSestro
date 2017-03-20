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

//import mg.masmsestro.DBHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    List<Folder> FolderList=new ArrayList<Folder>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DBHelper dbHelper=new DBHelper(getApplicationContext());

        Integer no=dbHelper.numberOfRowsFolder();
        Log.e("MaSMSestro",no.toString() );

        if  (no==0)
        {
            Folder f=new Folder();
            f.setName("Incoming");
            dbHelper.insertFolder( f);

            f.setName("SPAM");
            dbHelper.insertFolder( f);
        }

        FolderList = dbHelper.getAllFolders();


        ListView SMSFolders=(ListView) findViewById(R.id.SMSFolderList);
        ArrayAdapter a=new ArrayAdapter<Folder>(
                getApplicationContext(),
                R.layout.my_list_item1,FolderList
        );
         SMSFolders.setAdapter(a);




         SMSFolders.setOnItemClickListener(new OnItemClickListener() {
             @Override
             public void onItemClick( AdapterView<?> p, View v,int pos,long id) {
                 Toast.makeText(getApplicationContext(),FolderList.get(pos).getName(), Toast.LENGTH_LONG).show();

                 Log.e  ("MaSMSestro",FolderList.get(pos).getName());

                 if (FolderList.get(pos).getName().equals("General"))
                 {

                      Log.e  ("MaSMSestro","inside");
                     Intent intent = new Intent(getApplicationContext(), SMSActivity.class);
                     intent.putExtra(EXTRA_MESSAGE,FolderList.get(pos).getName() );
                           startActivity(intent);
                 }
             }
         });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
