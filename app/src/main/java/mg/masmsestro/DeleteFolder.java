package mg.masmsestro;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.util.Log;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OnClickListener;

public class DeleteFolder extends AppCompatActivity {
    private final Context context = this;
    private DBHelper dbHelper;
    private List<String> entries = new ArrayList<>();
    private String name;
    private Boolean checked;
    private ListView SMSFolders, DeleteChoiceList;
    private List<Dialog> dialogs = new ArrayList<>();
    private int i, j;
    private Folder f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_folderlayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFolderDelete);
        setSupportActionBar(toolbar);
        setTitle(" MaSMSestro-> Delete Folder");

        dbHelper = new DBHelper(getApplicationContext());
        List<String> folderList = dbHelper.getAllFoldersNames();


        SMSFolders = (ListView) findViewById(R.id.SMSFolderListDelete);
        ArrayAdapter a = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.my_listitemdelete, folderList
        );
        SMSFolders.setAdapter(a);

        CheckBox selectAll = (CheckBox) findViewById(R.id.chkboxSelectAll);

        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;

                for (i = 0; i < SMSFolders.getCount(); i++)

                {
                    SMSFolders.setItemChecked(i, checked);
                }
            }
        });

        ImageButton toTrash = (ImageButton) findViewById(R.id.totrash);

        j=0;
        toTrash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (i = 0; i < SMSFolders.getCount(); i++) {
                    if (SMSFolders.isItemChecked(i)) {

                        f = new Folder();
                        String  item = (String) SMSFolders.getItemAtPosition(i);
                        f.setName(item);
                        f.setId(dbHelper.getFolderByName(item));



                        if (j>=0) {

                            Log.e("MaSMSestro","j="+j);
                            dialogs.add((new Dialog(context))); // Context, this, etc.
                            dialogs.get(j).setContentView(R.layout.delete_dialog);
                            dialogs.get(j).setTitle(getString(R.string.dialog_title) + " " + f.getName());
                            dialogs.get(j).setCancelable(true);
                            dialogs.get(j).show();


                            Button btnCancel = (Button) dialogs.get(j).findViewById(R.id.dialog_cancel);
                            // if button is clicked, close the custom dialog
                            btnCancel.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialogs.get(j).cancel();
                                    j = j - 1;
                                }
                            });

                            Button btnDelete = (Button) dialogs.get(j).findViewById(R.id.dialog_delete);
                            // if button is clicked, close the custom dialog
                            btnDelete.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                 //   Toast.makeText(getApplicationContext(), "delete all", Toast.LENGTH_LONG).show();
                                    dialogs.get(j).cancel();
                                    dbHelper.deleteAllSMS(f);
                                    dbHelper.deleteFolder(f);
                                    j = j - 1;

                                    if (j<0)
                                    {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                    }
                                }
                            });

                            Button btnMove = (Button) dialogs.get(j).findViewById(R.id.dialog_move);
                            // if button is clicked, close the custom dialog
                            btnMove.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Toast.makeText(getApplicationContext(), "move all", Toast.LENGTH_LONG).show();
                                    dialogs.get(j).cancel();
                                  //  dbHelper.moveSMSToIncoming(f);
                                     dbHelper.deleteFolder(f);
                                    j = j - 1;

                                    if (j<0)
                                    {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                    }
                                }
                            });



                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            if (j<SMSFolders.getCheckedItemCount()-1) {
                                j = j + 1;
                            }
                        }
                    }

                }
            }
        });

    }



}
