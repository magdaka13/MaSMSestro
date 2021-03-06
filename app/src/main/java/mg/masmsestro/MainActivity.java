package mg.masmsestro;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import android.widget.ProgressBar;
import android.os.AsyncTask;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends AppCompatActivity {
    private List<String> FolderList = new ArrayList<>();
    private DBHelper dbHelper;
    private ListView SMSFolders;
private Dialog d;
    private ArrayAdapter a;
private final Context context=this;
    private ProgressBar mProgress;
    private TextView mProgressTitle;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //context.deleteDatabase("SMSDB.db");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgressTitle = (TextView) findViewById(R.id.progressBarTitle);

        mProgress.setVisibility(View.VISIBLE);

        dbHelper = new DBHelper(getApplicationContext());


//first read all folders
        Integer no = dbHelper.numberOfRowsFolder();


        if (no == 0) {
            Folder f = new Folder();
            f.setName("Incoming");
            dbHelper.insertFolder(f);

            f.setName("SPAM");
            dbHelper.insertFolder(f);
        }

        FolderList = dbHelper.getAllFoldersNames();


        SMSFolders = (ListView) findViewById(R.id.SMSFolderList);
        a = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.my_list_item1, FolderList
        );
        SMSFolders.setAdapter(a);
        mProgress.setVisibility(View.GONE);

       // dbHelper.deleteAllConversation();
//second - read all conversations and sms
        if (dbHelper.numberOfRowsSMS()==0)
        {

            new ReadSMS_Async().execute(10);


        }


        //now - let's handle clicking on folders list
        SMSFolders.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {

                    Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
                Bundle extras=new Bundle();
                extras.putString("FOLDER_NAME", SMSFolders.getItemAtPosition(pos).toString());
                intent.putExtras(extras);

                    startActivity(intent);

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        FolderList = dbHelper.getAllFoldersNames();


        ListView SMSFolders = (ListView) findViewById(R.id.SMSFolderList);
        a = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.my_list_item1, FolderList
        );
        SMSFolders.setAdapter(a);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new_folder) {

            findViewById(R.id.folder_options_layout).setVisibility(View.VISIBLE);

            Button btn_Save = (Button) findViewById(R.id.btn_Save);
            btn_Save.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                EditText editText = (EditText) findViewById(R.id.edtFolderName);
                                                String s = editText.getText().toString();
                                                if (!s.isEmpty()) {
                                                    //let's check whether folder with the same name exists in DB
                                                    if (dbHelper.getFolderByName(s) == -1) {
                                                        Folder f = new Folder();
                                                        f.setName(s);

                                                        dbHelper.insertFolder(f);
                                                        findViewById(R.id.folder_options_layout).setVisibility(View.GONE);

                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Folder cannot be created - already exists.", Toast.LENGTH_LONG).show();

                                                    }
                                                }

                                            }
                                        }
            );


            Button btn_Cancel = (Button) findViewById(R.id.btn_Cancel);
            btn_Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    findViewById(R.id.folder_options_layout).setVisibility(View.GONE);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }
            });

            return true;
        }

        if (id == R.id.action_edit_folder) {

            Intent intent = new Intent(getApplicationContext(), EditFolder.class);
            startActivity(intent);

        }

        if (id == R.id.action_delete_folder) {

            Intent intent = new Intent(getApplicationContext(), DeleteFolder.class);
            startActivity(intent);

        }

        if (id == R.id.action_refresh)
        {
            dbHelper.deleteAllConversation();
            new ReadSMS_Async().execute(10);


        }

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
                    MainActivity.this.a.getFilter().filter("");
                    d.cancel();

                }
            });

            Button btnSearch = (Button) d.findViewById(R.id.btn_search);
            // if button is clicked, close the custom dialog
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText e=(EditText)d.findViewById(R.id.str_to_search);
                    MainActivity.this.a.getFilter().filter(e.getText());
                    d.cancel();
                }
            });

            return true;
        }

        if (id == R.id.action_rules) {
            Intent intent = new Intent(getApplicationContext(),RuleActivity.class);
            startActivity(intent);

        return true;
        }

 /*
        if (id == R.id.action_DB) {

            Intent dbmanager = new Intent(getApplicationContext(),AndroidDatabaseManager.class);
            startActivity(dbmanager);
        return true;
        }
*/
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy()
    {
        dbHelper.close();
        super.onDestroy();
    }


    private class ReadSMS_Async extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            SMS_MMS_Reader sms_reader = new SMS_MMS_Reader();
            sms_reader.read_SMS_MMS(dbHelper,getApplicationContext());
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            mProgress.setVisibility(View.GONE);
            mProgressTitle.setText("");
        }

        @Override
        protected void onPreExecute() {
            mProgress.setVisibility(View.VISIBLE);
            mProgressTitle.setText(R.string.loading);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.e("MaSMSestro","task running");
        }
    }
}
