package mg.masmsestro;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditFolder extends AppCompatActivity {
    private DBHelper dbHelper;
    List<String> FolderList = new ArrayList<>();
    private  String name;

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
            public void onItemClick( AdapterView<?> p, View v,int pos,long id) {
               name=FolderList.get(pos);

                findViewById(R.id.folder_options_layout).setVisibility(View.VISIBLE);

                EditText title=(EditText)findViewById(R.id.title_top);
                title.setText("Edit Folder");

                EditText edt=(EditText)findViewById(R.id.edtFolderName);
                edt.setText(name);



                Button btn_Save= (Button) findViewById(R.id.btn_Save);
                btn_Save.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    EditText editText=( EditText) findViewById(R.id.edtFolderName);
                                                    String s= (String) editText.getText().toString();

                                                    //Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();

                                                    if  (! s.isEmpty() )
                                                    {
                                                        int FolderId;
                                                        try
                                                        {
                                                        FolderId=dbHelper.getFolderByName(name);
                                                        }catch (Exception e){
                                                            throw new RuntimeException(e.toString());
                                                        }

                                                 //       Folder f =new Folder();
//f.setId(  ( Integer) FolderId);
                                                   //     f.setName(s);

                                                        // Integer co= dbHelper.updateFolder(f);
                                                        Toast.makeText(getApplicationContext(),"na- >"+ name, Toast.LENGTH_LONG).show();
                                                        //findViewById(R.id.folder_options_layout).setVisibility(View.GONE);

                                                        //Intent intent = new Intent(getApplicationContext(),  MainActivity.class);
                                                    //    startActivity(intent);
                                                    }

                                                }
                                            }
                );

            }
        });

     }

}
