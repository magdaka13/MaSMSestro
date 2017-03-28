package mg.masmsestro;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton;


import java.util.ArrayList;
import java.util.List;

import static android.view.View.*;

public class DeleteFolder extends AppCompatActivity {
    private DBHelper dbHelper;
    List<String> FolderList = new ArrayList<>();
    private String name;
    private Boolean checked;
    ListView SMSFolders;

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

                for (int i = 0; i < SMSFolders.getCount(); i++)

                {
                    SMSFolders.setItemChecked(i, checked);
                }
            }
        });

        ImageButton toTrash = (ImageButton) findViewById(R.id.totrash);

        toTrash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < SMSFolders.getCount(); i++) {
                    if (SMSFolders.isItemChecked(i)) {
                        Folder f = new Folder();

                        String item;
                        try {
                            item = (String) SMSFolders.getItemAtPosition(i);

                        } catch (Exception e) {
                            throw (e);
                        }

                        f.setName(item);
                        // dbHelper.deleteFolder(f);
                        Toast.makeText(getApplicationContext(), "name " + f.getName(), Toast.LENGTH_LONG).show();
                        // AlertDialog.Builder d=new AlertDialog.Builder(getApplicationContext());
                        //d.setTitle("Folder: "+f.getName()+" will be deleted");
                        //d.s
                    }
                }
            }
        });

        /*
        SMSFolders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> p, View v,int pos,long id) {
                name=FolderList.get(pos);

                if (!(name.toUpperCase().equals("SPAM") || name.toLowerCase().equals("incoming")))
                {
                    findViewById(R.id.folder_options_layout).setVisibility(View.VISIBLE);


                    EditText title = (EditText) findViewById(R.id.title_top);
                    title.setText("Edit Folder");

                    EditText edt = (EditText) findViewById(R.id.edtFolderName);
                    edt.setText(name);


                    Button btn_Save = (Button) findViewById(R.id.btn_Save);
                    btn_Save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText editText = (EditText) findViewById(R.id.edtFolderName);
                            String s = (String) editText.getText().toString();

                            //add ommitting spam and incoming
                            if (!s.isEmpty()) {

                                if (dbHelper.getFolderByName(s)==-1) {
                                    int FolderId;

                                    FolderId = dbHelper.getFolderByName(name);

                                    Folder f = new Folder();
                                    f.setId((Integer) FolderId);
                                    f.setName(s);

                                    Integer co = dbHelper.updateFolder(f);
                                    findViewById(R.id.folder_options_layout).setVisibility(View.GONE);

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Folder with this name already exists", Toast.LENGTH_LONG).show();

                                }
                            }

                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "This folder cannot be renamed", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btn_Cancel= (Button) findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.folder_options_layout).setVisibility(View.GONE);

                Intent intent = new Intent(getApplicationContext(), EditFolder.class);
                startActivity(intent);

            }
        });
*/
    }

}
