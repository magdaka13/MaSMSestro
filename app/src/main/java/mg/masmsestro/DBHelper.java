package mg.masmsestro;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SMSDB.db";

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
        return  db.insert("folder", null, contentValues);


    }

    public Folder getFolderById(Folder f) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from folder where id=" + f.getId() + "", null);

        Folder folderObj = new Folder();
        folderObj.setId(res.getInt(res.getColumnIndex("id")));
        folderObj.setName(res.getString(res.getColumnIndex("name")));

        res.close();
        return folderObj;
    }

    public int getFolderByName(String name) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select id from folder where name='" + name + "'", null);
        if (res != null && res.moveToFirst()) {
            res.close();
            return Integer.parseInt(res.getString(res.getColumnIndex("id")));
        } else {

            return -1;
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

/* SMS table */

    public long insertSMS(SMS s) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tel_no", s.getTel_no());
        contentValues.put("content", s.getContent());

        return db.insert("sms", null, contentValues);
    }

    public SMS getSMS(SMS s) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from sms where id_sms=" + s.getSms_id() + "", null);

        SMS smsObj = new SMS();
        smsObj.setSms_id(res.getInt(res.getColumnIndex("sms_id")));
        smsObj.setTel_no(res.getString(res.getColumnIndex("tel_no")));
        smsObj.setContent(res.getString(res.getColumnIndex("content")));

        res.close();
        return smsObj;
    }

    public int numberOfRowsSMS() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "sms");
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

    public Cursor deleteAllSMS(Folder f) {
        Cursor c;
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("delete from sms where sms_id in (select id_SMS from smsReffolder where id_folder="+f.getId()+")",null);
        c=db.rawQuery("delete from smsReffolder where id_folder="+f.getId(),null);
        return c;
    }

    public Cursor moveSMSToIncoming(Folder f) {
        Cursor c;
        SQLiteDatabase db = this.getWritableDatabase();
        c=db.rawQuery("update smsReffolder set id_folder="+getFolderByName("Incoming")+" where id_folder="+f.getId(),null);
        return c;
    }


    public List<SMS> getAllSMS() {
        List<SMS> sms_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from sms", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            SMS s = new SMS();
            s.setSms_id(res.getInt(res.getColumnIndex("sms_id")));
            s.setContent(res.getString(res.getColumnIndex("content")));
            s.setTel_no(res.getString(res.getColumnIndex("tel_no")));
            sms_list.add(s);
            res.moveToNext();
        }
        res.close();
        return sms_list;
    }
/* SMS table-end */

/* Rule table */

    public long insertRule(Rule r) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rule", r.getRule());
        contentValues.put("folder_id", r.getFolder_id());

        return db.insert("rule", null, contentValues);
    }

    public Rule getRule(Rule r) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from rule where id_rule=" + r.getId_rule() + "", null);

        Rule ruleObj = new Rule();
        ruleObj.setId_rule(res.getInt(res.getColumnIndex("id_rule")));
        ruleObj.setRule(res.getString(res.getColumnIndex("rule")));
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
        contentValues.put("rule", r.getRule());
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
            r.setRule(res.getString(res.getColumnIndex("rule")));
            r.setFolder_id(res.getInt(res.getColumnIndex("folder_id")));
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

    public long insertSMSRefFolder(SMSRefFolder s) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_folder", s.getId_folder());
        contentValues.put("id_SMS", s.getId_SMS());

        return db.insert("SMSReffolder", null, contentValues);
    }

    public SMSRefFolder getSMSReffolder(SMSRefFolder s) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from SMSReffolder where id_ref=" + s.getId_ref() + "", null);

        SMSRefFolder smsRefFolderObj = new SMSRefFolder();
        smsRefFolderObj.setId_ref(res.getInt(res.getColumnIndex("id_ref")));
        smsRefFolderObj.setId_SMS(res.getInt(res.getColumnIndex("id_sms")));
        smsRefFolderObj.setId_folder(res.getInt(res.getColumnIndex("id_folder")));

        res.close();
        return smsRefFolderObj;
    }

    public int numberOfRowsSMSRefFolder() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "SMSReffolder");
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
/* SMSRefFolder table-end */


}