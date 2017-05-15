package mg.masmsestro;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.database.MatrixCursor;
import android.database.SQLException;

class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SMSDB.db";

    public DBHelper(Context context) {



        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
/*
        db.execSQL("DROP TABLE IF EXISTS folders");
        db.execSQL("DROP TABLE IF EXISTS conversation");
        db.execSQL("DROP TABLE IF EXISTS sms");
        db.execSQL("DROP TABLE IF EXISTS rule");
        db.execSQL("DROP TABLE IF EXISTS settings");
        db.execSQL("DROP TABLE IF EXISTS convReffolder");
*/



        db.execSQL("create table if not exists folder (id integer primary key, name text)");
        db.execSQL("create table if not exists conversation (conv_id integer primary key, recipient_list text, snippet text,thread_id int,date long,read integer,seen integer)");
        db.execSQL("create table if not exists sms (sms_id integer primary key, tel_no text, content text,date_received long,date_sent long,read integer,seen integer,person text,thread_id integer,type integer)");
        db.execSQL("create table if not exists rule (id_rule integer primary key, rule_name text,rule_number text,rule_keyword text,id_folder integer)");
        db.execSQL("create table if not exists settings (id_settings integer primary key, name text,value text)");
        db.execSQL("create table if not exists convReffolder (id_ref integer primary key, id_conv integer,id_folder integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS folders");
        db.execSQL("DROP TABLE IF EXISTS conversation");
        db.execSQL("DROP TABLE IF EXISTS sms");
        db.execSQL("DROP TABLE IF EXISTS rule");
        db.execSQL("DROP TABLE IF EXISTS settings");
        db.execSQL("DROP TABLE IF EXISTS convReffolder");

        onCreate(db);
    }

/* Folder table */

    public long insertFolder(Folder f) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", f.getName());
        return  db.insert("folder", null, contentValues);


    }

    public Folder getFolderById(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from folder where id=" + id + "", null);

        if (res != null && res.moveToFirst()) {

            Folder folderObj = new Folder();
            folderObj.setId(res.getInt(res.getColumnIndex("id")));
            folderObj.setName(res.getString(res.getColumnIndex("name")));

            res.close();
            return folderObj;
        }
        else
        {
            return null;
        }
    }

    public int getFolderByName(String name) {
int id=0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select id from folder where name='" + name + "'", null);
        if (res != null && res.moveToFirst()) {
            id=Integer.parseInt(res.getString(res.getColumnIndex("id")));
            res.close();
            return id;
        } else {

            return -1;
        }

    }

    public String getFolderByThreadId(int thread_id) {
        String name="";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select name from folder where id in (select id_folder from  convReffolder where id_conv in (select conv_id from conversation where thread_id="+thread_id+"))", null);
        if (res != null && res.moveToFirst()) {
            name=res.getString(res.getColumnIndex("name"));
            res.close();
            return name;
        } else {

            return "";
        }

    }


    public int numberOfRowsFolder() {
        SQLiteDatabase db = this.getReadableDatabase();
        return  (int) DatabaseUtils.queryNumEntries(db, "folder");

    }

    public int updateFolder(Folder f) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", f.getName());
        return db.update("folder", contentValues, "id = ? ", new String[]{Integer.toString(f.getId())});

    }

    public Integer deleteFolder(Folder f) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("folder", "name = ? ", new String[]{f.getName()});
    }

    public List<Folder> getAllFolders() {

        List<Folder> folder_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from folder", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Folder f = new Folder();
            f.setId(res.getInt(res.getColumnIndex("id")));
            f.setName(res.getString(res.getColumnIndex("name")));
            folder_list.add(f);
            res.moveToNext();
        }

        res.close();
        return folder_list;
    }

    public List<String> getAllFoldersNames() {

        List<String> folder_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select name from folder", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {

            folder_list.add(res.getString(res.getColumnIndex("name")));

            res.moveToNext();
        }
        res.close();
        return folder_list;
    }

    /* Folder table-end */

/*Conversation table */
public Conversation getConversation(Conversation s) {
String content;
    SQLiteDatabase db = this.getReadableDatabase();
    if (s.getSnippet()!=null) {
        content = s.getSnippet().replaceAll("\"", "'");
    }
    else
    {
        content="";
    }
    String sql_string="select * from conversation where recipient_list="+"\""+s.getRecipient_list() + "\""+ " and snippet="+"\""+content+"\""+"";

 //   Log.e("MaSMSestro","getConversation - sql="+sql_string);

    Cursor res = db.rawQuery(sql_string,null);
    if (res != null && res.moveToFirst()) {

        Conversation convObj = new Conversation();
        convObj.setConv_id(res.getInt(res.getColumnIndex("conv_id")));
        convObj.setRecipient_list(res.getString(res.getColumnIndex("recipient_list")));
        convObj.setSnippet(res.getString(res.getColumnIndex("snippet")));
        res.close();
        return convObj;
    } else {

        return null;
    }

}

    public List<Conversation> getAllConversationbyFolderName(String folder_name) {
        List<Conversation> conversation_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sql_str="select * from conversation where conv_id in (select id_conv from convReffolder where id_folder in (select id from folder where name='"+folder_name+"')) group by thread_id,recipient_list order by date desc";
        Log.e("MaSMSestro","sql="+sql_str);
        Cursor res = db.rawQuery(sql_str, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {

                Conversation s = new Conversation();
                s.setConv_id(res.getInt(res.getColumnIndex("conv_id")));
                s.setRecipient_list(res.getString(res.getColumnIndex("recipient_list")));
                s.setSnippet(res.getString(res.getColumnIndex("snippet")));
                s.setThread_id(res.getInt(res.getColumnIndex("thread_id")));
                conversation_list.add(s);

            res.moveToNext();
        }
        res.close();


        return conversation_list;
    }

    public Conversation getConversationbyThreadId(int thread_id) {
        Conversation s=null;

        SQLiteDatabase db = this.getReadableDatabase();
        String sql_str="select conv_id from conversation where thread_id="+thread_id;
        Log.e("MaSMSestro","sql="+sql_str);
        Cursor res = db.rawQuery(sql_str, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {

            s = new Conversation();
            s.setConv_id(res.getInt(res.getColumnIndex("conv_id")));

            res.moveToNext();
        }
        res.close();


        return s;
    }


public long insertConversation(Conversation s) {

    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("recipient_list", s.getRecipient_list());
    contentValues.put("snippet", s.getSnippet());
    contentValues.put("thread_id", s.getThread_id());
    contentValues.put("date", s.getDate());
    contentValues.put("read", s.getRead());
    contentValues.put("seen", s.getSeen());

    return db.insert("conversation", null, contentValues);
}

    public Integer deleteConversation(Conversation s) {
        Log.e("MaSMSestro","deleteconversation inside");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("MaSMSestro","conv_id="+s.getConv_id());
        return db.delete("conversation", "conv_id = ? ", new String[]{Integer.toString(s.getConv_id())});

    }

    public Integer deleteAllConversation() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("sms",null,null);
        db.delete("convReffolder",null,null);
        return db.delete("conversation", null, null);

    }


    public int moveConversationToFolder(String folder_name,int conv_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_folder",getFolderByName(folder_name) );
        return db.update("convReffolder", contentValues, "id_conv = ? ", new String[]{Integer.toString(conv_id)});

    }

/*Conversation table - end*/

/* SMS table */

    public long insertSMS(SMS s) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tel_no", s.getTel_no());
        contentValues.put("content", s.getContent());
        contentValues.put("date_received", s.getDate_received());
        contentValues.put("date_sent", s.getDate_sent());
        contentValues.put("read", s.getRead());
        contentValues.put("seen", s.getSeen());
        contentValues.put("person", s.getPerson());
        contentValues.put("thread_id", s.getThread_id());
        contentValues.put("type",s.getType());

        return db.insert("sms", null, contentValues);
    }

    public SMS getSMS(SMS s) {

        SQLiteDatabase db = this.getReadableDatabase();
        String content=s.getContent().replaceAll("\"","'");
        String sql_string="select * from sms where tel_no="+"\""+s.getTel_no().toString() + "\""+ " and content="+"\""+content+"\""+"";

 //      Log.e("MaSMSestro","getSMS - sql="+sql_string);

        Cursor res = db.rawQuery(sql_string,null);



        if (res != null && res.moveToFirst()) {

            SMS smsObj = new SMS();
            smsObj.setSms_id(res.getInt(res.getColumnIndex("sms_id")));
            smsObj.setTel_no(res.getString(res.getColumnIndex("tel_no")));
            smsObj.setContent(res.getString(res.getColumnIndex("content")));
            res.close();
            return smsObj;
        } else {

            return null;
        }

    }

    public List<String> getAllSMSByThread(Integer thread_id) {
         List<String> SMS_List=new ArrayList<String>();
         String sms_string;

        SQLiteDatabase db = this.getReadableDatabase();

        String sql_string="select * from sms where thread_id="+thread_id+" order by date_received asc";

   //     Log.e("MaSMSestro","getSMS - sql="+sql_string);

        Cursor res = db.rawQuery(sql_string,null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            sms_string = res.getString(res.getColumnIndex("tel_no"))+"\n" +res.getString(res.getColumnIndex("content"));
            SMS_List.add(sms_string);
            res.moveToNext();
        }

            res.close();
return SMS_List;
    }

    public List<SMS> getAllSMSByThread_SMS(Integer thread_id) {
        List<SMS> SMS_List=new ArrayList<SMS>();

        SQLiteDatabase db = this.getReadableDatabase();

        String sql_string="select * from sms where thread_id="+thread_id+" order by date_received asc";

        //     Log.e("MaSMSestro","getSMS - sql="+sql_string);

        Cursor res = db.rawQuery(sql_string,null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            SMS s=new SMS();
            s.setPerson(res.getString(res.getColumnIndex("tel_no")));
            s.setContent(res.getString(res.getColumnIndex("content")));
            s.setThread_id(res.getInt(res.getColumnIndex("thread_id")));
            s.setSms_id(res.getInt(res.getColumnIndex("sms_id")));
            SMS_List.add(s);
            res.moveToNext();
        }

        res.close();
        return SMS_List;
    }

    public List<String> getAllSMSByKeyword(String keyword) {
        List<String> SMS_List=new ArrayList<String>();
        String sms_string;

        SQLiteDatabase db = this.getReadableDatabase();

        String sql_string="select * from sms where upper(content) like '%"+keyword.toUpperCase()+"%' order by date_received asc";

        //     Log.e("MaSMSestro","getSMS - sql="+sql_string);

        Cursor res = db.rawQuery(sql_string,null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            sms_string = res.getString(res.getColumnIndex("tel_no"))+"\n" +res.getString(res.getColumnIndex("content"));
            SMS_List.add(sms_string);
            res.moveToNext();
        }

        res.close();
        return SMS_List;
    }


    public int numberOfRowsSMS() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "sms");
    }

    public int numberOfRowsSMS_byThreadId(int thread_id) {
        int count=0;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql_string="select count(*) as countSMS from sms where thread_id="+thread_id;

        //     Log.e("MaSMSestro","getSMS - sql="+sql_string);

        Cursor res = db.rawQuery(sql_string,null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            count=res.getInt(res.getColumnIndex("countSMS"));
            res.moveToNext();
        }

        res.close();
        return count;
    }

    public int updateSMS(SMS s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tel_no", s.getTel_no());
        contentValues.put("content", s.getContent());
        return db.update("sms", contentValues, "id = ? ", new String[]{Integer.toString(s.getSms_id())});

    }

    public Integer deleteSMS(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("sms", "sms_id = ? ", new String[]{Integer.toString(id)});
    }

    public Cursor deleteAllSMS(Folder f) {
        Cursor c;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql_str="delete from sms where thread_id in (select thread_id from conversation where conv_id in (select id_conv from convReffolder where id_folder ="+f.getId()+"))";
        db.rawQuery(sql_str,null);
        c=db.rawQuery("delete from convReffolder where id_folder="+f.getId(),null);
        return c;
    }

    public Cursor deleteAllSMSFromConv(Conversation conv) {
        Cursor c;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql_str="delete from sms where thread_id in (select thread_id from conversation where conv_id ="+conv.getConv_id()+")";
        c=db.rawQuery(sql_str,null);
        return c;
    }

    /*
    public Cursor moveSMSToIncoming(Folder f) {
        Cursor c;
        SQLiteDatabase db = this.getWritableDatabase();
        c=db.rawQuery("update smsReffolder set id_folder="+getFolderByName("Incoming")+" where id_folder="+f.getId(),null);
        return c;
    }
*/

/*
    public List<SMS> getAllSMSbyFolderName(String folder_name) {
        Integer thread_id,prev_thread_id;
        List<SMS> sms_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sql_str="select * from sms where sms_id in (select sms_id from smsReffolder where id_folder in (select id from folder where name='"+folder_name+"')) order by sms_id desc";
        Log.e("MaSMSestro","sql="+sql_str);
        Cursor res = db.rawQuery(sql_str, null);
        res.moveToFirst();

        thread_id=-1;
        prev_thread_id=-1;
        while (!res.isAfterLast()) {

            thread_id=res.getInt(res.getColumnIndexOrThrow("thread_id"));

            if (!thread_id.equals(prev_thread_id))
            {
                SMS s = new SMS();
                s.setSms_id(res.getInt(res.getColumnIndex("sms_id")));
                s.setContent(res.getString(res.getColumnIndex("content")));
                s.setTel_no(res.getString(res.getColumnIndex("tel_no")));
                sms_list.add(s);
            }

            prev_thread_id=thread_id;
            res.moveToNext();
        }
        res.close();


        return sms_list;
    }
    */
/* SMS table-end */

/* Rule table */

    public long insertRule(Rule r) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rule_name",r.getRule_name());
        contentValues.put("rule_number", r.getRule_number());
        contentValues.put("rule_keyword", r.getRule_keyword());
        contentValues.put("folder_id", r.getFolder_id());

        return db.insert("rule", null, contentValues);
    }

    public Rule getRule(Rule r) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from rule where id_rule=" + r.getId_rule() + "", null);

        Rule ruleObj = new Rule();
        ruleObj.setId_rule(res.getInt(res.getColumnIndex("id_rule")));
        ruleObj.setRule_name(res.getString(res.getColumnIndex("rule_name")));
        ruleObj.setRule_number(res.getString(res.getColumnIndex("rule_number")));
        ruleObj.setRule_keyword(res.getString(res.getColumnIndex("rule_keyword")));
        ruleObj.setFolder_id(res.getInt(res.getColumnIndex("folder_id")));

        res.close();
        return ruleObj;
    }

    public int numberOfRowsRule() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "rule");
    }

    public int updateRule(Rule r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rule_name",r.getRule_name());
        contentValues.put("rule_number", r.getRule_number());
        contentValues.put("rule_keyword", r.getRule_keyword());
        contentValues.put("folder_id", r.getFolder_id());
        return db.update("rule", contentValues, "id_rule = ? ", new String[]{Integer.toString(r.getId_rule())});

    }

    public Integer deleteRule(Rule r) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("rule", "id_rule = ? ", new String[]{Integer.toString(r.getId_rule())});
    }

    public List<Rule> getAllRule() {
        List<Rule> rule_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from rule", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Rule r = new Rule();
            r.setId_rule(res.getInt(res.getColumnIndex("id_rule")));
            r.setRule_name(res.getString(res.getColumnIndex("rule_name")));
            r.setRule_number(res.getString(res.getColumnIndex("rule_number")));
            r.setRule_keyword(res.getString(res.getColumnIndex("rule_keyword")));
            r.setFolder_id(res.getInt(res.getColumnIndex("id_folder")));
            rule_list.add(r);
            res.moveToNext();
        }
        res.close();
        return rule_list;
    }
/* Rule table-end */

/* Settings table */

    public long insertSetting(Settings s) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_setting", s.getId_setting());
        contentValues.put("name", s.getName());
        contentValues.put("value", s.getValue());

        return db.insert("settings", null, contentValues);
    }

    public Settings getSetting(Settings s) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from settings where id_setting=" + s.getId_setting() + "", null);

        Settings settingsObj = new Settings();
        settingsObj.setId_setting(res.getInt(res.getColumnIndex("id_setting")));
        settingsObj.setName(res.getString(res.getColumnIndex("name")));
        settingsObj.setValue(res.getString(res.getColumnIndex("value")));

        res.close();
        return settingsObj;
    }

    public int numberOfRowsSettings() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "settings");
    }

    public int updateSettings(Settings s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", s.getName());
        contentValues.put("value", s.getValue());
        return db.update("rule", contentValues, "id_setting = ? ", new String[]{Integer.toString(s.getId_setting())});

    }

    public Integer deleteSetting(Settings s) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("settings", "id_setting = ? ", new String[]{Integer.toString(s.getId_setting())});
    }

    public List<Settings> getAllSettings() {
        List<Settings> settings_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from settings", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Settings s = new Settings();
            s.setId_setting(res.getInt(res.getColumnIndex("id_settings")));
            s.setName(res.getString(res.getColumnIndex("name")));
            s.setValue(res.getString(res.getColumnIndex("value")));
            settings_list.add(s);
            res.moveToNext();
        }
        res.close();
        return settings_list;
    }
/* Settings table-end */

/* SMSrefFolder table */

    public long insertConvRefFolder(ConvRefFolder s) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_folder", s.getId_folder());
        contentValues.put("id_conv", s.getId_Conv());

        return db.insert("ConvReffolder", null, contentValues);
    }

    public ConvRefFolder getConvReffolder(ConvRefFolder s) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ConvReffolder where id_ref=" + s.getId_ref() + "", null);

        ConvRefFolder smsRefFolderObj = new ConvRefFolder();
        smsRefFolderObj.setId_ref(res.getInt(res.getColumnIndex("id_ref")));
        smsRefFolderObj.setId_Conv(res.getInt(res.getColumnIndex("id_conv")));
        smsRefFolderObj.setId_folder(res.getInt(res.getColumnIndex("id_folder")));

        res.close();
        return smsRefFolderObj;
    }

    public int numberOfRowsConvRefFolder() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "ConvReffolder");
    }
/*
    public int updateSMSRefFolder(ConvRefFolder s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_sms", s.getId_Conv());
        contentValues.put("id_folder", s.getId_folder());
        return db.update("ConvReffolder", contentValues, "id_ref = ? ", new String[]{Integer.toString(s.getId_ref())});

    }
*/
    public Integer deleteConvRefFolder(ConvRefFolder s) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("ConvReffolder", "id_ref = ? ", new String[]{Integer.toString(s.getId_ref())});
    }

    public int deleteConvRefFolder_byConvId(int conv_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("ConvReffolder", "id_conv = ? ", new String[]{Integer.toString(conv_id)});
    }

    /*
    public List<SMSRefFolder> getAllSMSReffolder() {
        List<SMSRefFolder> smsRefFolder_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from SMSReffolder", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            SMSRefFolder s = new SMSRefFolder();
            s.setId_ref(res.getInt(res.getColumnIndex("id_ref")));
            s.setId_SMS(res.getInt(res.getColumnIndex("id_sms")));
            s.setId_folder(res.getInt(res.getColumnIndex("id_folder")));
            smsRefFolder_list.add(s);
            res.moveToNext();
        }

        res.close();
        return smsRefFolder_list;
    }
    */

/* SMSRefFolder table-end */


    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

}