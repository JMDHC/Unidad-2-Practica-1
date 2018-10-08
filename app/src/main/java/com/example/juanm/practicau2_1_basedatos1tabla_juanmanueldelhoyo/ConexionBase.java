package com.example.juanm.practicau2_1_basedatos1tabla_juanmanueldelhoyo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionBase extends SQLiteOpenHelper{
    public ConexionBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Rutinas(Id INTEGER PRIMARY KEY AUTOINCREMENT, Dias varchar(200), Descripcion varchar(500), CaloriasQuemadas INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
