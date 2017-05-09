package mg.masmsestro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.List;

public class SMSNewActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private List<String> SMSList_string=new ArrayList<String>();
    private List<SMS> SMSList_obj=new ArrayList<SMS>();
    private ListView SMSList_view;
    private ArrayAdapter a;
    private String thread_id,sms_keyword;
 private Boolean checked;
    int i;
    private final int PICK_CONTACT=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.sms_new_layout);
        setTitle(" MaSMSestro->New SMS");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSMS);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);



            Log.e("MaSMSestro", "inside SMSnew activity");


EditText contact=(EditText) findViewById(R.id.SMScontact);


        contact.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        Button sendBtn=(Button) findViewById(R.id.SMSsend);
        sendBtn.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick (View v)
        {
            EditText contact=(EditText) findViewById(R.id.SMScontact);
            EditText body=(EditText) findViewById(R.id.SMSbody);

            SmsManager smsManager = SmsManager.getDefault();
for(String part:smsManager.divideMessage(String.valueOf(body.getText())))
            {

                smsManager.sendTextMessage(String.valueOf(contact.getText()), null, part, null, null);
                Toast.makeText(getApplicationContext(), "SMS was sent",
                        Toast.LENGTH_LONG).show();

            }
        }
        }
        );

    }

    @Override public void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);

        switch(reqCode)
        {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK)
                {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst())
                    {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =
                                c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1"))
                        {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            EditText contact=(EditText) findViewById(R.id.SMScontact);
                            String prevcontact= String.valueOf(contact.getText());
                            if (!prevcontact.isEmpty()) {
                                contact.setText(prevcontact + "," + cNumber);
                            }
                            else
                            {
                                contact.setText(cNumber);
                            }
                        }
                    }
                }
        }
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
        int id = item.getItemId();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }


}
