package com.example.alexsey.smartnotes.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper для работы с базой данных
 */
public class NoteDbHelper extends SQLiteOpenHelper {


    public NoteDbHelper(Context context) {
        super(context, NoteDbSchema.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "create table " + NoteDbSchema.NoteTable.NAME + "( " +
                NoteDbSchema.NoteTable.Columns.ID_CL + " integer primary key autoincrement, " +
                NoteDbSchema.NoteTable.Columns.TITLE_CL + ", " +
                NoteDbSchema.NoteTable.Columns.DESCRIPTION_CL + ", " +
                NoteDbSchema.NoteTable.Columns.IMPORTANCE_CL + ", " +
                NoteDbSchema.NoteTable.Columns.IMAGE_CL + ")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
