package mg.masmsestro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditFolder extends AppCompatActivity {
    private DBHelper dbHelper;
    private List<String> FolderList = new ArrayList<>();
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_folderlayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFolder);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(getApplicationContext());
        FolderList = dbHelper.getAllFoldersNames();


        ListView SMSFolders = (ListView) findViewById(R.id.SMSFolderListEdit);
        ArrayAdapter a = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.my_listitemedit, FolderList
        );
        SMSFolders.setAdapter(a);

        SMSFolders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {
                name = FolderList.get(pos);

                if (!(name.toUpperCase().equals("SPAM") || name.toLowerCase().equals("incoming"))) {
                    findViewById(R.id.folder_options_layout).setVisibility(View.VISIBLE);


                    EditText title = (EditText) findViewById(R.id.title_top);
                    title.setText(R.string.edit_folder);

                    EditText edt = (EditText) findViewById(R.id.edtFolderName);
                    edt.setText(name);


                    Button btn_Save = (Button) findViewById(R.id.btn_Save);
                    btn_Save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText editText = (EditText) findViewById(R.id.edtFolderName);
                            String s = editText.getText().toString();

                            //add ommitting spam and incoming
                            if (!s.isEmpty()) {

                                if (dbHelper.getFolderByName(s) == -1) {
                                    int FolderId;

                                    FolderId = dbHelper.getFolderByName(name);

                                    Folder f = new Folder();
                                    f.setId(FolderId);
                                    f.setName(s);

                                    dbHelper.updateFolder(f);
                                    findViewById(R.id.folder_options_layout).setVisibility(View.GONE);

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Folder with this name already exists", Toast.LENGTH_LONG).show();

                                }
                            }

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "This folder cannot be renamed", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btn_Cancel = (Button) findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.folder_options_layout).setVisibility(View.GONE);

                Intent intent = new Intent(getApplicationContext(), EditFolder.class);
                startActivity(intent);

            }
        });

    }

}
