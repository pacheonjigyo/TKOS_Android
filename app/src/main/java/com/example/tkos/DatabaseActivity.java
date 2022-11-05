package com.example.tkos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

public class DatabaseActivity extends SQLiteOpenHelper {
    private Context context;

    public DatabaseActivity(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TKOS (NUM INTEGER PRIMARY KEY AUTOINCREMENT, ID TEXT, NICKNAME TEXT, SCORE INTEGER, PROFILE BLOB, SKIN INTEGER, MODE INTEGER, MUSIC INTEGER, EFFECT INTEGER, VIBRATE INTEGER, CONTROL INTEGER, LANGUAGE INTEGER, OPTION INTEGER, ENGWORD TEXT, KORWORD TEXT );");

        Toast.makeText(context, "테이블 생성완료", Toast.LENGTH_SHORT).show();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String tkos_id, String tkos_nickname, int tkos_score, String tkos_profile, int tkos_skin, int tkos_mode, int tkos_music, int tkos_effect, int tkos_vibrate, int tkos_control, int tkos_language, int tkos_option, String tkos_engword, String tkos_korword) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO TKOS VALUES(null, '" + tkos_id + "', + '" + tkos_nickname + "', " + tkos_score + ", '" + tkos_profile + "', " + tkos_skin +", " + tkos_mode + ", " + tkos_music + ", " + tkos_effect + ", " + tkos_vibrate + ", " + tkos_control + ", " + tkos_language + ","+tkos_option +", '" + tkos_engword + "', '" + tkos_korword + "');");
        db.close();
    }

    public void setNickname(String tkos_id, String tkos_nickname) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE TKOS SET NICKNAME='" + tkos_nickname + "' WHERE ID='" + tkos_id + "';");
        db.close();
    }

    public void setScore(String tkos_id, int tkos_score) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE TKOS SET SCORE=" + tkos_score + " WHERE ID='" + tkos_id + "';");
        db.close();
    }

    public void setProfile(String tkos_id, byte[] tkos_profile) {
        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement stmt = db.compileStatement("UPDATE TKOS SET PROFILE = ? WHERE ID = '"+ tkos_id +"'");

        stmt.clearBindings();
        stmt.bindBlob(1, tkos_profile);
        stmt.execute();

        db.close();
    }

    public void setSkin(String tkos_id, int tkos_skin) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE TKOS SET SKIN='" + tkos_skin + "' WHERE ID='" + tkos_id + "';");
        db.close();
    }

    public void setMode(String tkos_id, int tkos_mode) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE TKOS SET MODE='" + tkos_mode + "' WHERE ID='" + tkos_id + "';");
        db.close();
    }

    public void setMusic(String tkos_id, int tkos_music) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE TKOS SET MUSIC='" + tkos_music + "' WHERE ID='" + tkos_id + "';");
        db.close();
    }

    public void setEffect(String tkos_id, int tkos_effect) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE TKOS SET EFFECT='" + tkos_effect + "' WHERE ID='" + tkos_id + "';");
        db.close();
    }

    public void setVibrate(String tkos_id, int tkos_vibrate) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE TKOS SET VIBRATE='" + tkos_vibrate + "' WHERE ID='" + tkos_id + "';");
        db.close();
    }

    public void setControl(String tkos_id, int tkos_control) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE TKOS SET CONTROL='" + tkos_control + "' WHERE ID='" + tkos_id + "';");
        db.close();
    }

    public void setLanguage(String tkos_id, int tkos_language) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE TKOS SET LANGUAGE=" + tkos_language + " WHERE ID='" + tkos_id + "';");
        db.close();
    }
    public void setOption(String tkos_id, int tkos_option) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE TKOS SET OPTION=" + tkos_option + " WHERE ID='" + tkos_id + "';");
        db.close();
    }
    public void setEngword(String tkos_id, String tkos_engword) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE TKOS SET ENGWORD ='" + tkos_engword + "' WHERE ID='" + tkos_id + "';");
        db.close();
    }
    public void setKorword(String tkos_id, String tkos_korword) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("UPDATE TKOS SET KORWORD='" + tkos_korword + "' WHERE ID='" + tkos_id + "';");
        db.close();
    }

    public void delete(String tkos_id) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM TKOS WHERE id='" + tkos_id + "';");
        db.close();
    }

    public boolean getID(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        String result = "";

        Cursor cursor = db.rawQuery("SELECT ID FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext())
            return false;

        return true;
    }

    public String getNickname(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        String result = "";

        Cursor cursor = db.rawQuery("SELECT NICKNAME FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getString(0);
        }

        return result;
    }

    public int getScore(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        int result = 0;

        Cursor cursor = db.rawQuery("SELECT SCORE FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public byte[] getProfile(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        byte[] result = new byte[0];

        Cursor cursor = db.rawQuery("SELECT PROFILE FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getBlob(0);
        }

        return result;
    }

    public int getSkin(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        int result = 0;

        Cursor cursor = db.rawQuery("SELECT SKIN FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public int getMode(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        int result = 0;

        Cursor cursor = db.rawQuery("SELECT MODE FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public int getMusic(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        int result = 0;

        Cursor cursor = db.rawQuery("SELECT MUSIC FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public int getEffect(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        int result = 0;

        Cursor cursor = db.rawQuery("SELECT EFFECT FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public int getVibrate(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        int result = 0;

        Cursor cursor = db.rawQuery("SELECT VIBRATE FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public int getControl(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        int result = 0;

        Cursor cursor = db.rawQuery("SELECT CONTROL FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public int getLanguage(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        int result = 0;

        Cursor cursor = db.rawQuery("SELECT LANGUAGE FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }
    public int getOption(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        int result = 0;

        Cursor cursor = db.rawQuery("SELECT OPTION FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }
    public String getEngword(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        String result = "";

        Cursor cursor = db.rawQuery("SELECT ENGWORD FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getString(0);
        }

        return result;
    }
    public String getKorword(String tkos_id) {
        SQLiteDatabase db = getReadableDatabase();

        String result = "";

        Cursor cursor = db.rawQuery("SELECT KORWORD FROM TKOS WHERE ID = '" + tkos_id +"';", null);

        while (cursor.moveToNext()) {
            result = cursor.getString(0);
        }

        return result;
    }

}
