  package mg.masmsestro;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SMSDBName.db";
    public static final String FOLDERS_TABLE_NAME = "folders";
    public static final String FOLDERS_COLUMN_ID = "id";
    public static final String FOLDERS_COLUMN_NAME = "name";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table if not exists folders " +
                        "(id integer primary key, name text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS folders");
        //onCreate(db);
    }

    public boolean insertFolder  (String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
          db.insert("folders", null, contentValues);

        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from folders where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, FOLDERS_TABLE_NAME);
        return numRows;
    }

    public boolean updateFolder(Integer id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        db.update("folders", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteFolder (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("folders",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllFolders() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select name from folders", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(FOLDERS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}