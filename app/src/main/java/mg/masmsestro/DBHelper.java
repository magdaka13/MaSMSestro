package mg.masmsestro;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SMSDB.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists folder (id integer primary key, name text)");
        db.execSQL("create table if not exists sms (sms_id integer primary key, tel_no text, content text)");
        db.execSQL("create table if not exists rule (id_rule integer primary key, rule text,id_folder integer)");
        db.execSQL("create table if not exists settings (id_settings integer primary key, name text,value text)");
        db.execSQL("create table if not exists smsReffolder (id_ref integer primary key, id_sms integer,id_folder integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS folders");
        db.execSQL("DROP TABLE IF EXISTS sms");
        db.execSQL("DROP TABLE IF EXISTS rule");
        db.execSQL("DROP TABLE IF EXISTS settings");
        db.execSQL("DROP TABLE IF EXISTS smsReffolder");

        onCreate(db);
    }

/* Folder table */

    public long insertFolder(Folder f) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", f.getName());
        long id = db.insert("folder", null, contentValues);

        return id;
    }

    public Folder getFolderById(Folder f) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from folder where id=" + f.getId() + "", null);

        Folder folderObj = new Folder();
        folderObj.setId(res.getInt(res.getColumnIndex("id")));
        folderObj.setName(res.getString(res.getColumnIndex("name")));

        return folderObj;
    }

    public int getFolderByName(String name) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select id from folder where name='" + name + "'", null);
        if (res != null && res.moveToFirst()) {

            return Integer.parseInt(res.getString(res.getColumnIndex("id")));
        } else {
            return -1;
        }
    }


    public int numberOfRowsFolder() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "folder");
        return numRows;
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

        List<Folder> folder_list = new ArrayList<Folder>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from folder", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Folder f = new Folder();
            f.setId(res.getInt(res.getColumnIndex("id")));
            f.setName(res.getString(res.getColumnIndex("name")));
            folder_list.add(f);
            res.moveToNext();
        }
        return folder_list;
    }

    public List<String> getAllFoldersNames() {

        List<String> folder_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select name from folder", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            //Folder f=new Folder();
            //f.setId(res.getInt(res.getColumnIndex("id")));
            //f.setName(res.getString(res.getColumnIndex("name")));

            folder_list.add(res.getString(res.getColumnIndex("name")));

            res.moveToNext();
        }
        return folder_list;
    }

    /* Folder table-end */

/* SMS table */

    public long insertSMS(SMS s) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tel_no", s.getTel_no());
        contentValues.put("content", s.getContent());
        long id = db.insert("sms", null, contentValues);

        return id;
    }

    public SMS getSMS(SMS s) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from sms where id_sms=" + s.getSms_id() + "", null);

        SMS smsObj = new SMS();
        smsObj.setSms_id(res.getInt(res.getColumnIndex("sms_id")));
        smsObj.setTel_no(res.getString(res.getColumnIndex("tel_no")));
        smsObj.setContent(res.getString(res.getColumnIndex("content")));

        return smsObj;
    }

    public int numberOfRowsSMS() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "sms");
        return numRows;
    }

    public int updateSMS(SMS s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tel_no", s.getTel_no());
        contentValues.put("content", s.getContent());
        return db.update("sms", contentValues, "id = ? ", new String[]{Integer.toString(s.getSms_id())});

    }

    public Integer deleteSMS(SMS s) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("sms", "id = ? ", new String[]{Integer.toString(s.getSms_id())});
    }

    public List<SMS> getAllSMS() {
        List<SMS> sms_list = new ArrayList<SMS>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from sms", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            SMS s = new SMS();
            s.setSms_id(res.getInt(res.getColumnIndex("sms_id")));
            s.setContent(res.getString(res.getColumnIndex("content")));
            s.setTel_no(res.getString(res.getColumnIndex("tel_no")));
            sms_list.add(s);
            res.moveToNext();
        }
        return sms_list;
    }
/* SMS table-end */

/* Rule table */

    public long insertRule(Rule r) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rule", r.getRule());
        contentValues.put("folder_id", r.getFolder_id());
        long id = db.insert("rule", null, contentValues);

        return id;
    }

    public Rule getRule(Rule r) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from rule where id_rule=" + r.getId_rule() + "", null);

        Rule ruleObj = new Rule();
        ruleObj.setId_rule(res.getInt(res.getColumnIndex("id_rule")));
        ruleObj.setRule(res.getString(res.getColumnIndex("rule")));
        ruleObj.setFolder_id(res.getInt(res.getColumnIndex("folder_id")));

        return ruleObj;
    }

    public int numberOfRowsRule() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "rule");
        return numRows;
    }

    public int updateRule(Rule r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rule", r.getRule());
        contentValues.put("folder_id", r.getFolder_id());
        return db.update("rule", contentValues, "id_rule = ? ", new String[]{Integer.toString(r.getId_rule())});

    }

    public Integer deleteRule(Rule r) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("rule", "id_rule = ? ", new String[]{Integer.toString(r.getId_rule())});
    }

    public List<Rule> getAllRule() {
        List<Rule> rule_list = new ArrayList<Rule>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from rule", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Rule r = new Rule();
            r.setId_rule(res.getInt(res.getColumnIndex("id_rule")));
            r.setRule(res.getString(res.getColumnIndex("rule")));
            r.setFolder_id(res.getInt(res.getColumnIndex("folder_id")));
            rule_list.add(r);
            res.moveToNext();
        }
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

        long id = db.insert("settings", null, contentValues);

        return id;
    }

    public Settings getSetting(Settings s) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from settings where id_setting=" + s.getId_setting() + "", null);

        Settings settingsObj = new Settings();
        settingsObj.setId_setting(res.getInt(res.getColumnIndex("id_setting")));
        settingsObj.setName(res.getString(res.getColumnIndex("name")));
        settingsObj.setValue(res.getString(res.getColumnIndex("value")));

        return settingsObj;
    }

    public int numberOfRowsSettings() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "settings");
        return numRows;
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
        List<Settings> settings_list = new ArrayList<Settings>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from settings", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Settings s = new Settings();
            s.setId_setting(res.getInt(res.getColumnIndex("id_settings")));
            s.setName(res.getString(res.getColumnIndex("name")));
            s.setValue(res.getString(res.getColumnIndex("value")));
            settings_list.add(s);
            res.moveToNext();
        }
        return settings_list;
    }
/* Settings table-end */

/* SMSrefFolder table */

    public long insertSMSRefFolder(SMSRefFolder s) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_folder", s.getId_folder());
        contentValues.put("id_SMS", s.getId_SMS());

        long id = db.insert("SMSReffolder", null, contentValues);

        return id;
    }

    public SMSRefFolder getSMSReffolder(SMSRefFolder s) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from SMSReffolder where id_ref=" + s.getId_ref() + "", null);

        SMSRefFolder smsRefFolderObj = new SMSRefFolder();
        smsRefFolderObj.setId_ref(res.getInt(res.getColumnIndex("id_ref")));
        smsRefFolderObj.setId_SMS(res.getInt(res.getColumnIndex("id_sms")));
        smsRefFolderObj.setId_folder(res.getInt(res.getColumnIndex("id_folder")));

        return smsRefFolderObj;
    }

    public int numberOfRowsSMSRefFolder() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "SMSReffolder");
        return numRows;
    }

    public int updateSMSRefFolder(SMSRefFolder s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_sms", s.getId_SMS());
        contentValues.put("id_folder", s.getId_folder());
        return db.update("SMSReffolder", contentValues, "id_ref = ? ", new String[]{Integer.toString(s.getId_ref())});

    }

    public Integer deleteSMSRefFolder(SMSRefFolder s) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("SMSReffolder", "id_ref = ? ", new String[]{Integer.toString(s.getId_ref())});
    }

    public List<SMSRefFolder> getAllSMSReffolder() {
        List<SMSRefFolder> smsRefFolder_list = new ArrayList<SMSRefFolder>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from SMSReffolder", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            SMSRefFolder s = new SMSRefFolder();
            s.setId_ref(res.getInt(res.getColumnIndex("id_ref")));
            s.setId_SMS(res.getInt(res.getColumnIndex("id_sms")));
            s.setId_folder(res.getInt(res.getColumnIndex("id_folder")));
            smsRefFolder_list.add(s);
            res.moveToNext();
        }
        return smsRefFolder_list;
    }
/* SMSRefFolder table-end */


}