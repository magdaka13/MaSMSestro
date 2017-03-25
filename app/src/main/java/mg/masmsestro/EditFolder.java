package mg.masmsestro;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EditFolder extends AppCompatActivity {
    private   DBHelper dbHelper;
    List<String> FolderList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_folderlayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarFolder);
        setSupportActionBar(toolbar);

        dbHelper=new DBHelper(getApplicationContext());
        FolderList = dbHelper.getAllFoldersNames();


        ListView SMSFolders=(ListView) findViewById(R.id.SMSFolderListEdit);
        ArrayAdapter a= new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.my_listitemedit, FolderList
        );
        SMSFolders.setAdapter(a);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
