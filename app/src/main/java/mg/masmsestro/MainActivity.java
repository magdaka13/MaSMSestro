package mg.masmsestro;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import mg.masmsestro.DBHelper;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "";
    private List<String> FolderList = new ArrayList<>();
    private DBHelper dbHelper;
    private ListView SMSFolders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        ArrayAdapter a = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.my_list_item1, FolderList
        );
        SMSFolders.setAdapter(a);

//second - retreive smses and mmses from telephone and put them into DB
        //List<String> smsList = new ArrayList<>();
        final String[] projection = new String[]{"_id","ct_t","address","body","thread_id","date","read"};


        Uri uri = Uri.parse("content://mms-sms/conversations/");
        Cursor c = getContentResolver().query(uri, projection, null, null, "date DESC");


        // Read the sms data and store it in the list
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {
                Integer id=c.getInt(c.getColumnIndex("_id"));
                String string = c.getString(c.getColumnIndex("ct_t"));

                String recipient_list=c.getString(c.getColumnIndex("address"));
                String snippet=c.getString(c.getColumnIndex("body"));
                Integer thread_id=c.getInt(c.getColumnIndex("thread_id"));
                long date=c.getLong(c.getColumnIndex("date"));
                Integer read=c.getInt(c.getColumnIndex("read"));
                //Integer seen=c.getInt(c.getColumnIndex("seen"));

                Conversation conv=new Conversation();
                conv.setRecipient_list(recipient_list);
                conv.setSnippet(snippet);
                conv.setThread_id(thread_id);
                conv.setDate(date);
                conv.setRead(read);
                //conv.setSeen(seen);


                Log.e("MaSMSestro", ":conversaion -> recipient=" + conv.getRecipient_list() +  ");snippet=" + conv.getSnippet() + ";date_received=" + new SimpleDateFormat("MM/dd/yyyy H:m:s").format(new Date(conv.getDate()))  + ";read=" + conv.getRead() + ";seen=" + conv.getSeen() + ";thread=" + conv.getThread_id());
                Conversation conv1 = dbHelper.getConversation(conv);
                if (conv1 != null) {
                    long z = dbHelper.deleteConversation(conv1);
                    Log.e("MaSMSestro", "deleted=" + z);
                }

                if (dbHelper.getConversation(conv) == null) {
                    Log.e("MaSMSestro", "conv doesnt exist");
                    long conv_id = dbHelper.insertConversation(conv);
                    Log.e("MaSMSestro", "insertedConversation=" + conv_id);

                    if (conv_id != -1) {
                        ConvRefFolder ref = new ConvRefFolder();
                        ref.setId_folder(dbHelper.getFolderByName("Incoming"));
                        ref.setId_Conv((int) conv_id);

                        Folder f = dbHelper.getFolderById(ref.getId_folder());
                        String name_f = "";
                        if (f != null) {
                            name_f = f.getName();
                        } else {
                            name_f = "folder not found=" + ref.getId_folder();
                        }

                        long id_ref=dbHelper.insertConvRefFolder(ref);

                        Log.e("MaSMSestro", "inserted to ConvRefFolder=" + id_ref + "conv_id:" + ref.getId_Conv() + "folder_name=" + name_f);

                    }

                }

                if ("application/vnd.wap.multipart.related".equals(string)) {

                    //mms
                    Log.e("MaSMSestro","MMS");
                }
                 else  //sms
                    {
                    String selection = "thread_id = "+conv.getThread_id();
                    Uri uri1 = Uri.parse("content://sms");
                    Cursor cursor = getContentResolver().query(uri1, null, selection, null, null);

                    if (cursor.moveToFirst()) {
                        for (int j = 0; j < cursor.getCount(); j++) {
                            SMS sms = new SMS();
                            sms.setContent(cursor.getString(cursor.getColumnIndexOrThrow("body")).toString());
                            sms.setTel_no(cursor.getString(cursor.getColumnIndexOrThrow("address")).toString());
                            sms.setDate_received(cursor.getLong(cursor.getColumnIndexOrThrow("date")));
                            sms.setDate_sent(cursor.getLong(cursor.getColumnIndexOrThrow("date_sent")));
                            sms.setRead((cursor.getString(cursor.getColumnIndexOrThrow("read"))));
                            sms.setSeen(cursor.getString(cursor.getColumnIndexOrThrow("seen")));
                            sms.setPerson(cursor.getString(cursor.getColumnIndexOrThrow("person")));
                            sms.setThread_id(cursor.getInt(cursor.getColumnIndexOrThrow("thread_id")));
                            sms.setType(cursor.getInt(cursor.getColumnIndexOrThrow("type")));

                            Log.e("MaSMSestro", "sms: tel=" + sms.getTel_no() + "(" + sms.getPerson() + ");body=" + sms.getContent() + ";date_received=" + new SimpleDateFormat("MM/dd/yyyy H:m:s").format(new Date(sms.getDate_received())) + ";date_sent=" + new SimpleDateFormat("MM/dd/yyyy H:m:s").format(new Date(sms.getDate_sent())) + ";read=" + sms.getRead() + ";seen=" + sms.getSeen() + ";thread=" + sms.getThread_id()+"type="+sms.getType());

                            SMS sms1 = dbHelper.getSMS(sms);
                            if (sms1 != null) {
                                long z = dbHelper.deleteSMS(sms1);
                                Log.e("MaSMSestro", "deleted=" + z);
                            }

                            if (dbHelper.getSMS(sms) == null) {
                                Log.e("MaSMSestro", "sms doesnt exist");
                                long sms_id = dbHelper.insertSMS(sms);
                                Log.e("MaSMSestro", "insertedSMS=" + sms_id);

                                }
                            cursor.moveToNext();
                            }
                            cursor.close();

                        }
                    }
                c.moveToNext();
                }
                c.close();
            }

        //now - let's handle clicking on folders list
        SMSFolders.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {

                    Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, FolderList.get(pos));
                    startActivity(intent);

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

        return super.onOptionsItemSelected(item);
    }


}
