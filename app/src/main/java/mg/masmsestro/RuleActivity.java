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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magda on 2017-05-14.
 */

public class RuleActivity extends AppCompatActivity {
    private List<Rule> RuleList= new ArrayList<>();
    private final List<String> RuleList_string=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Intent intent = getIntent();
        setContentView(R.layout.activity_rule);
        setTitle(" MaSMSestro->Rules");

        setContentView(R.layout.rulelist_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRule);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }



        Log.e("MaSMSestro", "inside Rule activity");

        DBHelper dbHelper = new DBHelper(getApplicationContext());


        RuleList = dbHelper.getAllRule();

        for (Rule rule : RuleList)
        {
          //  String str="Tel no: "+rule.getRule_number()+";keyword: ("+rule.getRule_keyword()+"),will be redirected to Folder:"+dbHelper.getFolderById(rule.getFolder_id()).getName();
            String str=rule.getRule_name();
            RuleList_string.add(str);
        }

        dbHelper.close();

        ListView ruleList_view = (ListView) findViewById(R.id.RuleList);
        ArrayAdapter<String> a = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.my_list_item1, RuleList_string
        );
        ruleList_view.setAdapter(a);

        ruleList_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int pos, long id) {

                Intent intent = new Intent(getApplicationContext(), RuleDetailsActivity.class);
                Bundle extras=new Bundle();
                extras.putString("RULE_ID_STRING", RuleList.get(pos).getId_rule().toString());
                intent.putExtras(extras);
                startActivity(intent);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id==R.id.action_new_rule)
        {

                    Intent intent = new Intent(getApplicationContext(), RuleDetailsActivity.class);
                    startActivity(intent);

            return true;
        }

        if (id==R.id.action_delete_rule)
        {
            Intent intent = new Intent(getApplicationContext(), RuleDeleteActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}