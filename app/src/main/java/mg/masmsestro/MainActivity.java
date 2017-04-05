package mg.masmsestro;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
//import mg.masmsestro.DBHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "";
    private List<String> FolderList = new ArrayList<>();
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(getApplicationContext());
        //dbHelper.onUpgrade(dbHelper.getWritableDatabase(),1,2);

//first read all folders
        Integer no = dbHelper.numberOfRowsFolder();
//        Log.e("MaSMSestro", no.toString());

        if (no == 0) {
            Folder f = new Folder();
            f.setName("Incoming");
            dbHelper.insertFolder(f);

            f.setName("SPAM");
            dbHelper.insertFolder(f);
        }

        FolderList = dbHelper.getAllFoldersNames();


        ListView SMSFolders = (ListView) findViewById(R.id.SMSFolderList);
        ArrayAdapter a = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.my_list_item1, FolderList
        );
        SMSFolders.setAdapter(a);

//second - retreive smses from telephone and put them into DB
        List<String> smsList = new ArrayList<>();
        final String[] projection = new String[]{"*"};

        Uri uri = Uri.parse("content://sms/");
        Cursor c= getContentResolver().query(uri, projection, null ,null,null);


        // Read the sms data and store it in the list
        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
                SMS sms = new SMS();
                sms.setContent(c.getString(c.getColumnIndexOrThrow("body")).toString());
                sms.setTel_no(c.getString(c.getColumnIndexOrThrow("address")).toString());
                sms.setDate_received(new Date(c.getLong(c.getColumnIndexOrThrow("date"))));
                sms.setDate_sent(new Date(c.getLong(c.getColumnIndexOrThrow("date_sent"))));
                sms.setRead(( c.getString(c.getColumnIndexOrThrow("read"))));
                sms.setSeen( c.getString(c.getColumnIndexOrThrow("seen")));
                sms.setPerson(c.getString(c.getColumnIndexOrThrow("person")));
                sms.setThread_id(c.getInt(c.getColumnIndexOrThrow("thread_id")));

                Log.e("MaSMSestro","sms: tel="+sms.getTel_no()+"("+sms.getPerson()+");body="+sms.getContent()+";date_received="+new SimpleDateFormat("MM/dd/yyyy H:m:s").format(sms.getDate_received())+";date_sent="+new SimpleDateFormat("MM/dd/yyyy H:m:s").format(sms.getDate_sent())+";read="+sms.getRead()+";seen="+sms.getSeen()+";thread="+sms.getThread_id());

                SMS sms1=dbHelper.getSMS(sms);
                if (sms1!=null)
                {
                    long z=dbHelper.deleteSMS(sms1);
                    Log.e("MaSMSestro","deleted="+z);
                }

                if (dbHelper.getSMS(sms)==null)
{
    Log.e("MaSMSestro","sms doesnt exist");
long sms_id=dbHelper.insertSMS(sms);
Log.e("MaSMSestro","insertedSMS="+sms_id);

    if (sms_id!=-1) {
        SMSRefFolder ref = new SMSRefFolder();
        ref.setId_folder(dbHelper.getFolderByName("Incoming"));
        ref.setId_SMS((int) sms_id);

        Folder f = dbHelper.getFolderById(ref.getId_folder());
        String name_f = "";
        if (f != null) {
            name_f = f.getName();
        } else
        {
            name_f="folder not found="+ref.getId_folder();
        }

        Log.e("MaSMSestro","inserted to smsmRefFolder="+dbHelper.insertSMSRefFolder(ref)+"sms_id:"+ref.getId_SMS()+"folder_name="+name_f);

    }

}

                c.moveToNext();
            }
        }
        c.close();

 //now - let's handle clicking on folders list
        SMSFolders.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                Toast.makeText(getApplicationContext(), FolderList.get(pos), Toast.LENGTH_LONG).show();

                Log.e("MaSMSestro", FolderList.get(pos));

                if (FolderList.get(pos).equals("Incoming")) {

                    Intent intent = new Intent(getApplicationContext(), SMSActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, FolderList.get(pos));
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        FolderList = dbHelper.getAllFoldersNames();


        ListView SMSFolders = (ListView) findViewById(R.id.SMSFolderList);
        ArrayAdapter a = new ArrayAdapter<>(
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_folder) {

            findViewById(R.id.folder_options_layout).setVisibility(View.VISIBLE);

            Button btn_Save = (Button) findViewById(R.id.btn_Save);
            btn_Save.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                EditText editText = (EditText) findViewById(R.id.edtFolderName);
                                                String s =  editText.getText().toString();
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

        return super.onOptionsItemSelected(item);
    }


}
