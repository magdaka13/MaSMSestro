package mg.masmsestro;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OnClickListener;

public class DeleteFolder extends AppCompatActivity {
    final Context context = this;
    private DBHelper dbHelper;
    private List<String> FolderList = new ArrayList<>();
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


        dbHelper = new DBHelper(getApplicationContext());
        FolderList = dbHelper.getAllFoldersNames();


        SMSFolders = (ListView) findViewById(R.id.SMSFolderListDelete);
        ArrayAdapter a = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.my_listitemdelete, FolderList
        );
        SMSFolders.setAdapter(a);

        CheckBox selectAll = (CheckBox) findViewById(R.id.chkboxSelectAll);

        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)

                {
                    checked = true;
                } else

                {
                    checked = false;
                }

                for (i = 0; i < SMSFolders.getCount(); i++)

                {
                    SMSFolders.setItemChecked(i, checked);
                }
            }
        });

        ImageButton toTrash = (ImageButton) findViewById(R.id.totrash);

        toTrash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (i = 0; i < SMSFolders.getCount(); i++) {
                    if (SMSFolders.isItemChecked(i)) {

                        f = new Folder();
                        String  item = (String) SMSFolders.getItemAtPosition(i);
                        f.setName(item);


                        j = i;
                        dialogs.add(j, (new Dialog(context))); // Context, this, etc.
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
                                Toast.makeText(getApplicationContext(), "cancel " + f.getName(), Toast.LENGTH_LONG).show();
                                j = j - 1;
                            }
                        });

                        Button btnDelete = (Button) dialogs.get(j).findViewById(R.id.dialog_delete);
                        // if button is clicked, close the custom dialog
                        btnDelete.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "delete all", Toast.LENGTH_LONG).show();
                                dialogs.get(j).cancel();
                                // dbHelper.deleteFolder(f);

                                j = j - 1;
                            }
                        });

                        Button btnMove = (Button) dialogs.get(j).findViewById(R.id.dialog_move);
                        // if button is clicked, close the custom dialog
                        btnMove.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "move all", Toast.LENGTH_LONG).show();
                                dialogs.get(j).cancel();
                                //dbHelper.MoveSMSToIncoming(f);
                                // dbHelper.deleteFolder(f);
                                j = j - 1;
                            }
                        });

                    }
                }
            }
        });

    }

}
