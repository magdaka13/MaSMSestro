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

public class ConversationDeleteActivity extends AppCompatActivity {

    private List<Conversation> ConversationList;
    private final List<String> ConversationList_string =new ArrayList<>();
    private ListView ConversationItems;
    private ArrayAdapter a;
    private DBHelper dbHelper;
    private String folder_name;
    private int i;
    private Boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            final Intent intent = getIntent();
            Bundle ex = intent.getExtras();
            folder_name = ex.getString("FOLDER_NAME");
        }catch(Exception e)
        {
            //do nothing
        }

        setTitle(" MaSMSestro->" + folder_name);

        setContentView(R.layout.conversation_list_delete_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarConv);
        setSupportActionBar(toolbar);

        dbHelper=new DBHelper(getApplicationContext());
        ConversationList=dbHelper.getAllConversationbyFolderName(folder_name);
        Log.e("MaSMSestro","Retreived ConversationList size="+ConversationList.size());


        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        if (ab != null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }


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
            a = new ArrayAdapter<>(
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

                            if (i < ConversationList.size()) {
                                dbHelper.deleteConversation(ConversationList.get(i));
                                dbHelper.deleteAllSMSFromConv(ConversationList.get(i));
                                Log.e("MaSMSestro", "checked " + ConversationItems.getItemAtPosition(i).toString());
                            }

                        }

                        Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
                        Bundle extras=new Bundle();
                        extras.putString("FOLDER_NAME", folder_name);
                        intent.putExtras(extras);
                        startActivityForResult(intent,1);


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
//        int id = item.getItemId();

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
            a = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.my_list_item_conversation_delete, ConversationList_string
            );
            ConversationItems.setAdapter(a);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                folder_name = data.getStringExtra("FOLDER_NAME");
            }
        }
    }


    @Override
    public void onDestroy() {
        dbHelper.close();
    super.onDestroy();
    }
}
