package mg.masmsestro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magda on 2017-05-17.
 */

class RuleDetailsActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private List<String> FolderList=new ArrayList<>();
    private String rule_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Rule r;
        r=new Rule();

        rule_id="";

        super.onCreate(savedInstanceState);

        try {
            // Get the Intent that started this activity and extract the string
            Intent intent = getIntent();
            Bundle ex = intent.getExtras();
            rule_id = ex.getString("RULE_ID_STRING");
        }
        catch(Exception e)
        {
//do nothing
        }

        setContentView(R.layout.activity_rule);
        setTitle(" MaSMSestro->Rules Details");

        setContentView(R.layout.ruledetails_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRule);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        Log.e("MaSMSestro", "inside Rule Details activity;rule_id="+rule_id);

        if (!rule_id.isEmpty()) {
            dbHelper = new DBHelper(getApplicationContext());
            r = dbHelper.getRuleById(Integer.parseInt(rule_id));


            final EditText rule_name=(EditText)findViewById(R.id.rule_name);
            rule_name.setText(r.getRule_name());

            final EditText rule_phone=(EditText)findViewById(R.id.RulePhone);
            rule_phone.setText(r.getRule_number());

            final EditText rule_keyword=(EditText)findViewById(R.id.RuleKeyword);
            rule_keyword.setText(r.getRule_keyword());

            final Spinner s = (Spinner) findViewById(R.id.folderlist_rule);
            FolderList = dbHelper.getAllFoldersNames();

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, FolderList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s.setAdapter(dataAdapter);

            int sel=-1;
            for (int i=0;i<FolderList.size();i++) {
            if (FolderList.get(i).equals(dbHelper.getFolderById(r.getFolder_id())))
                {
                    sel=i;
                }
            }

            if (sel>-1) {
                s.setSelection(sel);
            }

            Button save=(Button) findViewById(R.id.btn_Save);
            save.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Rule updated_rule=new Rule();
                                            updated_rule.setId_rule(Integer.valueOf(rule_id));
                                            updated_rule.setRule_name(rule_name.getText().toString());
                                            updated_rule.setRule_number(rule_phone.getText().toString());
                                            updated_rule.setRule_keyword(rule_keyword.getText().toString());
                                            updated_rule.setFolder_id(dbHelper.getFolderByName(String.valueOf(s.getSelectedItem())));
                                            dbHelper.updateRule(updated_rule);

                                            Intent intent = new Intent(getApplicationContext(), RuleActivity.class);
                                            startActivity(intent);

                                        }
                                    });

                    dbHelper.close();
        }
    }
}
