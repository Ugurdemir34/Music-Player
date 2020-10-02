package com.example.uur.musicplayer_v200;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by UĞUR on 17.04.2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    static String DB_PATH = "";

    static String DB_NAME = "myDb.db";

    SQLiteDatabase myDatabase;

    final Context myContext;

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, 1);

        this.myContext = context;

        this.DB_PATH = myContext.getFilesDir().getParent() + "/databases/";
        //myContext.getDatabasePath(DB_NAME).getAbsolutePath();
        Log.e("Path 1", DB_PATH);

    }

    //Assest dizinine koyduğumuz sql dosyasını, Android projesi içine oluşturma ve kopyalamna işlemi yapıldı..
    public void CreateDataBase() {
        Log.w("check", "kopya öncesi");

        boolean dbExists = checkDataBase();

        if (!dbExists) {
            this.getWritableDatabase();

            try {
                Log.w("", "Veritabanı kopyalandı");
                copyDataBase();
            } catch (Exception ex) {
                Log.w("hata", "Veritabanı kopyalanamıyor");
                throw new Error("Veritabanı kopyalanamıyor.");
            }
        } else {
            Log.v("db var", "fsdffds");
        }
    }

    //Sqlite veritabanı dosyasını açıp, veritabanımı kontrol ediyoruz
    boolean checkDataBase()
    {
       /* SQLiteDatabase checkDB = null;

        try
        {
            String myPath = DB_PATH + DB_NAME;

            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch (Exception ex)
        {
            Log.w("hata","Veritabanı açılamadı");
        }

        if (checkDB != null)
            checkDB.close();

        return checkDB != null ? true : false;*/
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH + "myDb.db", null,
                    SQLiteDatabase.OPEN_READWRITE);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }
    ///Assest dizinine koyduğumuz sql dosyasındaki verileri kopyalıyoruz
    void copyDataBase() {
        try {
            InputStream myInput = myContext.getAssets().open("myDb.db");
            String outFileName = DB_PATH + "myDb.db";
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            Log.w("", "." + outFileName);
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (Exception ex) {
            Log.w("hata", "Kopya oluşturma hatası.");
        }
    }
    //Veritabannı açma işlemi yapıldı
    void openDataBase()
    {
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
    @Override
    public synchronized void close() {
        if (myDatabase != null /*&& myDatabase.isOpen()*/)
            myDatabase.close();
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
       /* if(newVersion>oldVersion)
            copyDataBase();*/
    }
    public void ekle(long id,String ad,String sanatci)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql="INSERT INTO Favori(sarkiid,Ad,Sanatci) VALUES(?,?,?)";
        db.execSQL(sql,new Object[]{id,ad,sanatci});
        db.close();
    }
    public void sil(long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Favori","sarkiid=?",new String[]{String.valueOf(id)});
        db.close();
    }
    public  ArrayList<Sarki> favorikelimelistesi(SQLiteDatabase db)
    {
        //db=dbHelper.getReadableDatabase();
        String sorgu="Select sarkiid,Ad,Sanatci From Favori order by Ad asc";
        Cursor imlec=db.rawQuery(sorgu,null);

        //ListView listview=(ListView)findViewById(R.id.listview);
        ArrayList<Sarki> sonuc=new ArrayList<Sarki>();
        // ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,student);
        while(imlec.moveToNext())
        {
            //  String student_name=imlec.getString(imlec.getColumnIndex("SozcukAdi"));
            String ad=imlec.getString(imlec.getColumnIndex("Ad"));
            long id=imlec.getLong(imlec.getColumnIndex("sarkiid"));
            String sanatci=imlec.getString(imlec.getColumnIndex("Sanatci"));
            Sarki sarkim = new Sarki((long)id,ad,sanatci);
            sonuc.add(sarkim);
        }
        imlec.close();
        db.close();
        return sonuc;

    }
}
