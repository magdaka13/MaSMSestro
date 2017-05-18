package mg.masmsestro;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by magda on 2017-04-25.
 */

public class SMS_MMS_Reader {

    public int read_SMS_MMS(DBHelper dbHelper,Context context)
    {
        int thread_id_ret;
        thread_id_ret=-1;

        //List<String> smsList = new ArrayList<>();
        final String[] projection = new String[]{"_id","ct_t","address","body","thread_id","date","read"};


        Uri uri = Uri.parse("content://mms-sms/conversations/");
        Cursor c = context.getContentResolver().query(uri, projection, null, null, "date DESC");


        // Read the sms data and store it in the list
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {
                Integer id=c.getInt(c.getColumnIndex("_id"));
                String string = c.getString(c.getColumnIndex("ct_t"));

                String recipient_list=c.getString(c.getColumnIndex("address"));
                String snippet=c.getString(c.getColumnIndex("body"));
                Integer thread_id=c.getInt(c.getColumnIndex("thread_id"));
                long date=c.getLong(c.getColumnIndex("date"));
                Integer read=c.getInt(c.getColumnIndex("read"));
                //Integer seen=c.getInt(c.getColumnIndex("seen"));

                Conversation conv=new Conversation();
                conv.setRecipient_list(recipient_list);
                conv.setSnippet(snippet);
                conv.setThread_id(thread_id);
                conv.setDate(date);
                conv.setRead(read);
                //conv.setSeen(seen);


                //              Log.e("MaSMSestro", ":conversaion -> recipient=" + conv.getRecipient_list() +  ");snippet=" + conv.getSnippet() + ";date_received=" + new SimpleDateFormat("MM/dd/yyyy H:m:s").format(new Date(conv.getDate()))  + ";read=" + conv.getRead() + ";seen=" + conv.getSeen() + ";thread=" + conv.getThread_id());
/*                Conversation conv1 = dbHelper.getConversation(conv);
                if (conv1 != null) {
                    long z = dbHelper.deleteConversation(conv1);
                    Log.e("MaSMSestro", "deleted=" + z);
                }
*/

           if ((conv.getRecipient_list()!=null) && (conv.getSnippet()!=null)) {
               if (dbHelper.getConversation(conv) == null) {
                   // Log.e("MaSMSestro", "conv doesnt exist");
                   long conv_id = dbHelper.insertConversation(conv);
                   // Log.e("MaSMSestro", "insertedConversation=" + conv_id);

                   if (conv_id != -1) {
List <Rule>RulesArray=new ArrayList<>();
RulesArray=dbHelper.getAllRule();

for (Rule r:RulesArray)
{
    String keyword=r.getRule_keyword();
            String phone=r.getRule_number();
                    int folder_id=r.getFolder_id();

    if ((phone.isEmpty())&&(keyword.isEmpty()))
    {
        //do nothing
    }


    //compare convrsaton with keyword
    if ((phone.isEmpty())&&(!keyword.isEmpty()))
    {

    }

    //compare conversation with phone
    if ((!phone.isEmpty())&&(keyword.isEmpty()))
    {

    }

    //compare conversation with keyword and phone
    if ((!phone.isEmpty())&&(!keyword.isEmpty()))
    {

    }

}

                       ConvRefFolder ref = new ConvRefFolder();
                       ref.setId_folder(dbHelper.getFolderByName("Incoming"));
                       ref.setId_Conv((int) conv_id);

                       Folder f = dbHelper.getFolderById(ref.getId_folder());
                       String name_f = "";
                       if (f != null) {
                           name_f = f.getName();
                       } else {
                           name_f = "folder not found=" + ref.getId_folder();
                       }


                       long id_ref = dbHelper.insertConvRefFolder(ref);

                       Log.e("MaSMSestro", "inserted to ConvRefFolder=" + id_ref + "conv_id:" + ref.getId_Conv() + "folder_name=" + name_f);

                   }

               }

               if ("application/vnd.wap.multipart.related".equals(string)) {

                   //mms
                   Log.e("MaSMSestro", "MMS");
               } else  //sms
               {
                   String selection = "thread_id = " + conv.getThread_id();
                   Uri uri1 = Uri.parse("content://sms");
                   Cursor cursor = context.getContentResolver().query(uri1, null, selection, null, null);

                   if (cursor.moveToFirst()) {
                       for (int j = 0; j < cursor.getCount(); j++) {
                           SMS sms = new SMS();
                           sms.setContent(cursor.getString(cursor.getColumnIndexOrThrow("body")).toString());
                           sms.setTel_no(cursor.getString(cursor.getColumnIndexOrThrow("address")).toString());
                           sms.setDate_received(cursor.getLong(cursor.getColumnIndexOrThrow("date")));
                           sms.setDate_sent(cursor.getLong(cursor.getColumnIndexOrThrow("date_sent")));
                           sms.setRead((cursor.getString(cursor.getColumnIndexOrThrow("read"))));
                           sms.setSeen(cursor.getString(cursor.getColumnIndexOrThrow("seen")));
                           sms.setPerson(cursor.getString(cursor.getColumnIndexOrThrow("person")));
                           sms.setThread_id(cursor.getInt(cursor.getColumnIndexOrThrow("thread_id")));
                           sms.setType(cursor.getInt(cursor.getColumnIndexOrThrow("type")));


                           //                      Log.e("MaSMSestro", "sms: tel=" + sms.getTel_no() + "(" + sms.getPerson() + ");body=" + sms.getContent() + ";date_received=" + new SimpleDateFormat("MM/dd/yyyy H:m:s").format(new Date(sms.getDate_received())) + ";date_sent=" + new SimpleDateFormat("MM/dd/yyyy H:m:s").format(new Date(sms.getDate_sent())) + ";read=" + sms.getRead() + ";seen=" + sms.getSeen() + ";thread=" + sms.getThread_id()+"type="+sms.getType());
/*
                            SMS sms1 = dbHelper.getSMS(sms);
                            if (sms1 != null) {
                                long z = dbHelper.deleteSMS(sms1);
                                Log.e("MaSMSestro", "deleted=" + z);
                            }
*/
                           if (dbHelper.getSMS(sms) == null) {
                               //                              Log.e("MaSMSestro", "sms doesnt exist");
                               long sms_id = dbHelper.insertSMS(sms);
                               Log.e("MaSMSestro", "insertedSMS=" + sms.getThread_id());

                               if ((thread_id_ret==-1)&&(sms.getType()==1))
                               {
                                   thread_id_ret = sms.getThread_id();
                               }
                           }
                           cursor.moveToNext();
                       }
                       cursor.close();

                   }
               }
               c.moveToNext();
           }
            }
            c.close();
        }

       return thread_id_ret;
    }
}
