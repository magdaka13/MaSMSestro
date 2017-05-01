package mg.masmsestro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ConversationDeleteActivity extends AppCompatActivity {

    private List<Conversation> ConversationList;
    private List<String> ConversationList_string =new ArrayList<>();
    private ListView ConversationItems;
    private final Context context = this;
private Dialog d;
    private ArrayAdapter a;
    private List<String> FolderList = new ArrayList<>();
private DBHelper dbHelper;
    private int selected;
private String folder_name;
    private int i;
    private Boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        Bundle ex=intent.getExtras();
        folder_name = ex.getString("FOLDER_NAME");

        setTitle(" MaSMSestro->" + folder_name);

        setContentView(R.layout.conversation_list_delete_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarConv);
        setSupportActionBar(toolbar);

        dbHelper=new DBHelper(getApplicationContext());
        ConversationList=dbHelper.getAllConversationbyFolderName(folder_name);
        Log.e("MaSMSestro","Retreived ConversationList size="+ConversationList.size());


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        for (int i=0;i<ConversationList.size();i++) {

            String snippet;
            snippet = ConversationList.get(i).getSnippet();

            String conversation_short=ConversationList.get(i).getRecipient_list()+System.getProperty("line.separator")+snippet;
            if ((ConversationList.get(i).getRecipient_list()!=null) && (snippet!=null))
            {
                ConversationList_string.add(conversation_short);
            }

        }


        if (ConversationList_string.size()>0) {
            ConversationItems = (ListView) findViewById(R.id.ConversationList);
            a = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.my_list_item_conversation_delete, ConversationList_string
            );
            ConversationItems.setAdapter(a);
        }

        CheckBox selectAll = (CheckBox) findViewById(R.id.checkBoxSelectAll);

        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;

                for (i = 0; i < ConversationItems.getCount(); i++)

                {
                    ConversationItems.setItemChecked(i, checked);
                }
            }
        });

        ImageButton toTrash = (ImageButton) findViewById(R.id.trash);

        toTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (i = 0; i < ConversationItems.getCount(); i++) {
                    if (ConversationItems.isItemChecked(i)) {

                        dbHelper.deleteConversation(ConversationList.get(i));
                        dbHelper.deleteAllSMSFromConv(ConversationList.get(i));
Log.e("MaSMSestro","checked "+ConversationItems.getItemAtPosition(i).toString());

                    }


                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent();
        Bundle extras=new Bundle();
        extras.putString("FOLDER_NAME", folder_name);
        setResult(RESULT_OK,intent);
        intent.putExtras(extras);

        finish();

        Log.e("MaSMSestro","onbackpressed...:"+folder_name);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        ConversationList=dbHelper.getAllConversationbyFolderName(folder_name);
        Log.e("MaSMSestro","Retreived ConversationList size="+ConversationList.size());


        for (int i=0;i<ConversationList.size();i++) {

            String snippet;
            snippet = ConversationList.get(i).getSnippet();

            String conversation_short=ConversationList.get(i).getRecipient_list()+System.getProperty("line.separator")+snippet;
            if ((ConversationList.get(i).getRecipient_list()!=null) && (snippet!=null))
            {
                ConversationList_string.add(conversation_short);
            }

        }



        if (ConversationList_string.size()>0) {
            ConversationItems = (ListView) findViewById(R.id.ConversationList);
            a = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.my_list_item_conversation_delete, ConversationList_string
            );
            ConversationItems.setAdapter(a);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
        Bundle extras=new Bundle();
        extras.putString("FOLDER_NAME", folder_name);
        intent.putExtras(extras);
        startActivity(intent);
        Log.e("MaSMSestro","onbackpressed:"+folder_name);

    }

    @Override
    public void onDestroy() {
        dbHelper.close();
    super.onDestroy();
    }
}
