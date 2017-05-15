package mg.masmsestro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magda on 2017-05-14.
 */

public class RuleActivity extends AppCompatActivity {
private DBHelper dbHelper;
    private List<Rule> RuleList=new ArrayList<Rule>();
    private List<String> RuleList_string=new ArrayList<>();
    private ListView RuleList_view;
    private ArrayAdapter<String> a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_rule);
        setTitle(" MaSMSestro->Rules");

        setContentView(R.layout.rulelist_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRule);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        Log.e("MaSMSestro", "inside Rule activity");

        dbHelper = new DBHelper(getApplicationContext());


        RuleList = dbHelper.getAllRule();

        for (Rule rule : RuleList)
        {
          //  String str="Tel no: "+rule.getRule_number()+";keyword: ("+rule.getRule_keyword()+"),will be redirected to Folder:"+dbHelper.getFolderById(rule.getFolder_id()).getName();
            String str=rule.getRule_name();
            RuleList_string.add(str);
        }

        dbHelper.close();

        RuleList_view = (ListView) findViewById(R.id.RuleList);
        a = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.my_list_item1, RuleList_string
        );
        RuleList_view.setAdapter(a);

        RuleList_view.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                RuleList_view.setSelection(a.getCount() - 1);
            }
        });

    }
}