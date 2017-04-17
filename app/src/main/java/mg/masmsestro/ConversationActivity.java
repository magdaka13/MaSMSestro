package mg.masmsestro;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Context;

import java.util.List;


import java.util.ArrayList;

public class ConversationActivity extends AppCompatActivity {

    private List<Conversation> ConversationList;
    private List<String> ConversationList_string =new ArrayList<>();
    private ListView ConversationItems;
    public static final String EXTRA_MESSAGE = "";
    private final Context context = this;
private Dialog d;
    private ArrayAdapter a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String folder_name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        setTitle(" MaSMSestro->" + folder_name);

        setContentView(R.layout.conversation_list_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarConv);
        setSupportActionBar(toolbar);

        DBHelper dbHelper=new DBHelper(getApplicationContext());
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
                    R.layout.my_list_item_conversation, ConversationList_string
            );
            ConversationItems.setAdapter(a);

            //now - let's handle clicking on conversation list
            ConversationItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> p, View v, int pos, long id) {

                    Intent intent = new Intent(getApplicationContext(), SMSActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, ConversationList.get(pos).getThread_id().toString());
                    startActivity(intent);

                }
            });


        }


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
        getMenuInflater().inflate(R.menu.menu_conv, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
                    ConversationActivity.this.a.getFilter().filter("");
                    d.cancel();

                }
            });

            Button btnSearch = (Button) d.findViewById(R.id.btn_search);
            // if button is clicked, close the custom dialog
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText e=(EditText)d.findViewById(R.id.str_to_search);
                    ConversationActivity.this.a.getFilter().filter(e.getText());
                    d.cancel();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
