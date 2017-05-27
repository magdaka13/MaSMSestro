package mg.masmsestro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class RuleDeleteActivity extends AppCompatActivity {

    private DBHelper dbHelper;
private List<Rule>RuleList_obj=new ArrayList<>();
    private final List<String>RuleList_string=new ArrayList<>();
private ListView RuleList_view;
    private Boolean checked;


    @Override
  protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Intent intent = getIntent();
        setContentView(R.layout.activity_rule_delete);
        setTitle(" MaSMSestro");

        setContentView(R.layout.rule_list_delete_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRule);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }


        setTitle(" MaSMSestro-> Delete Rule");

        Log.e("MaSMSestro", "inside RuleDelete activity");

        dbHelper = new DBHelper(getApplicationContext());

        RuleList_obj = dbHelper.getAllRule();
Log.e("MaSMSestro","retreived rules="+RuleList_obj.size());

for (Rule r:RuleList_obj)
{
    RuleList_string.add(r.getRule_name());
}


        RuleList_view = (ListView) findViewById(R.id.RuleList);
        ArrayAdapter<String> a = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.my_listitemdelete, RuleList_string
        );
        RuleList_view.setAdapter(a);



    CheckBox selectAll = (CheckBox) findViewById(R.id.checkBoxSelectAll);

        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

    {
        public void onCheckedChanged (CompoundButton buttonView,boolean isChecked){
        checked = isChecked;

        for (int i = 0; i < RuleList_view.getCount(); i++)

        {
            RuleList_view.setItemChecked(i, checked);
        }
    }
    });

    ImageButton toTrash = (ImageButton) findViewById(R.id.trash);

        toTrash.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){

        for (int i = 0; i < RuleList_view.getCount(); i++) {
            if (RuleList_view.isItemChecked(i)) {

                if (i < RuleList_obj.size()) {
                    dbHelper.deleteRule(RuleList_obj.get(i));
                    }

                }

            }

            Intent intent = new Intent(getApplicationContext(), RuleDeleteActivity.class);
            startActivity(intent);

        }

    });
}


@Override
public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
        }

@Override
public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        Intent intent = new Intent(getApplicationContext(), RuleActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
        }


}
