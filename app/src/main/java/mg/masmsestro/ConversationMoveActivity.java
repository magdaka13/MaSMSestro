package mg.masmsestro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ConversationMoveActivity extends AppCompatActivity {

    private List<Conversation> ConversationList;
    private final List<String> ConversationList_string =new ArrayList<>();
    private ListView ConversationItems;
    private final Context context = this;
private Dialog d;
    private ArrayAdapter a;
    private List<String> FolderList = new ArrayList<>();
private DBHelper dbHelper;
    private int selected;
private String folder_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        Bundle ex=intent.getExtras();
        folder_name = ex.getString("FOLDER_NAME");

        setTitle(" MaSMSestro->" + folder_name);

        setContentView(R.layout.conversation_list_move_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarConv);
        setSupportActionBar(toolbar);
        ActionBar ab=getSupportActionBar();
        if (ab != null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        dbHelper=new DBHelper(getApplicationContext());
        ConversationList=dbHelper.getAllConversationbyFolderName(folder_name);
        Log.e("MaSMSestro","Retreived ConversationList size="+ConversationList.size());

        ConversationList_string.clear();
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
                    R.layout.my_list_item_conversation_move, ConversationList_string
            );
            ConversationItems.setAdapter(a);

            //now - let's handle clicking on conversation list
            ConversationItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> p, final View v, int pos, long id) {

                    selected = pos;
                    d = new Dialog(context); // Context, this, etc.

                    d.setTitle("Move");
                    d.setContentView(R.layout.move_dialog);
                    d.setCancelable(true);
                    d.show();


                    final Spinner s = (Spinner) d.findViewById(R.id.FolderName);
                    FolderList = dbHelper.getAllFoldersNames();


                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, FolderList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s.setAdapter(dataAdapter);


                    Button btnCancel = (Button) d.findViewById(R.id.dialog_cancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.cancel();

                        }
                    });

                    Button btnOK = (Button) d.findViewById(R.id.btn_search);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("MaSMSestro", "folder=" + String.valueOf(s.getSelectedItem()));
                            Log.e("MaSMSestro", "conv_id=" + ConversationList.get(selected).getConv_id());
                            int a = dbHelper.moveConversationToFolder(String.valueOf(s.getSelectedItem()), ConversationList.get(selected).getConv_id());
                            Log.e("MaSMSestro", Integer.toString(a));
                            d.dismiss();

                            Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("FOLDER_NAME", folder_name);
                            intent.putExtras(extras);
                            startActivity(intent);

                        }
                    });


                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        ConversationList=dbHelper.getAllConversationbyFolderName(folder_name);
        Log.e("MaSMSestro","Retreived ConversationList size="+ConversationList.size());


        ConversationList_string.clear();
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
                    R.layout.my_list_item_conversation_move, ConversationList_string
            );
            ConversationItems.setAdapter(a);
        }

    }


    @Override
    public void onDestroy() {
        dbHelper.close();
    super.onDestroy();
    }
}
