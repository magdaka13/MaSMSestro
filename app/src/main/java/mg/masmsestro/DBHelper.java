  package mg.masmsestro;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SMSDB.db";


    private SMS smsObj;
    private Rule ruleObj;
    private Settings settingsObj;
    private SMSRefFolder smsRefFolderObj;

    public DBHelper(Context context)
     {
        super(context, DATABASE_NAME , null, 1);

         ruleObj=new Rule();
         settingsObj=new Settings();
         smsRefFolderObj=new SMSRefFolder();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table if not exists folder (id integer primary key, name text)");
        db.execSQL("create table if not exists sms (sms_id integer primary key, tel_no text, content text)");
        db.execSQL("create table if not exists rule (id_rule integer primary key, rule text,id_folder integer)");
        db.execSQL("create table if not exists settings (id_settings integer primary key, name text,value text)");
        db.execSQL("create table if not exists smsReffolder (id_ref integer primary key, id_sms integer,id_folder integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS folders");
        //onCreate(db);
    }

    public long insertFolder  (Folder f) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", f.getName());
          long id=db.insert("folder", null, contentValues);

        return id;
    }

    public Folder getFolder(Folder f) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from folder where id="+f.getId()+"", null );

        Folder folderObj=new Folder();
        folderObj.setId(res.getInt(res.getColumnIndex("id")));
        folderObj.setName(res.getString(res.getColumnIndex("name")));

        return folderObj;
    }

    public int numberOfRowsFolder(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "folder");
        return numRows;
    }

    public int updateFolder(Folder f) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", f.getName());
        return db.update("folder", contentValues, "id = ? ", new String[] { Integer.toString(f.getId()) } );

    }

    public Integer deleteFolder (Folder f) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("folder","id = ? ",new String[] { Integer.toString(f.getId()) });
    }

    public List<Folder> getAllFolders() {
        List<Folder> folder_list = new ArrayList<Folder>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from folder", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Folder f=new Folder();
            f.setId(res.getInt(res.getColumnIndex("id")));
            f.setName(res.getString(res.getColumnIndex("name")));
            folder_list.add(f);
            res.moveToNext();
        }
        return folder_list;
    }

    public long insertSMS  (SMS s) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tel_no",s.getTel_no());
        contentValues.put("content", s.getContent());
        long id=db.insert("sms", null, contentValues);

        return id;
    }

    public SMS getSMS(SMS s) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from sms where id_sms="+s.getSms_id()+"", null );

        SMS smsObj=new SMS();
        smsObj.setSms_id(res.getInt(res.getColumnIndex("sms_id")));
        smsObj.setTel_no(res.getString(res.getColumnIndex("tel_no")));
        smsObj.setContent(res.getString(res.getColumnIndex("content")));

        return smsObj;
    }

    public int numberOfRowsSMS(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "sms");
        return numRows;
    }

    public int updateSMS(SMS s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tel_no", s.getTel_no());
        contentValues.put("content",s.getContent());
        return db.update("sms", contentValues, "id = ? ", new String[] { Integer.toString(s.getSms_id()) } );

    }

    public Integer deleteSMS (SMS s) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("sms","id = ? ",new String[] { Integer.toString(s.getSms_id()) });
    }

    public List<SMS> getAllSMS() {
        List<SMS> sms_list = new ArrayList<SMS>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from sms", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            SMS s=new SMS();
            s.setSms_id(res.getInt(res.getColumnIndex("sms_id")));
            s.setContent(res.getString(res.getColumnIndex("content")));
            s.setTel_no(res.getString(res.getColumnIndex("tel_no")));
            sms_list.add(s);
            res.moveToNext();
        }
        return sms_list;
    }

}