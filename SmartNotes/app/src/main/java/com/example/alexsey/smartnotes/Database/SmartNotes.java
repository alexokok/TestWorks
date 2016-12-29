package com.example.alexsey.smartnotes.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.example.alexsey.smartnotes.Models.SmartNote;
import com.example.alexsey.smartnotes.R;

import java.io.File;
import java.util.ArrayList;

/**
 * singletone-класс, который хранит в себе данные по заметкам
 */
public class SmartNotes {

    /** база данных */
    private SQLiteDatabase mDatabase;

    /** переменная, содержащая данные по заметкам */
    private static SmartNotes sSmartNotes;

    /** контекст для получения доступа к действиям над активностью*/
    private Context mContext;

    public Context getContext() {
        return mContext;
    }


    /** приоритеты встреч */
    private String[] mPrioritys;

    public String[] getPrioritys() {
        return mPrioritys;
    }

    /** получает доступ к переменной  */
    public static SmartNotes get(Context context){
        if(sSmartNotes == null)
            sSmartNotes = new SmartNotes(context);

        return  sSmartNotes;
    }

    /** приватный конструктор
     * @param context - ссылка на контекст
     */
    private SmartNotes(Context context){

        mContext = context.getApplicationContext();

        mDatabase = new NoteDbHelper(mContext).getWritableDatabase();

        mPrioritys = mContext.getResources().getStringArray(R.array.prioritys);
    }

    /** устанавливает положение файла */
    public File getPhotoFile(String photoFilename){

        // получаем директорию для хранения файлов
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // если директория не найдена - возвращаем null
        if(externalFilesDir == null)
            return null;
        // возвращаем файл с нужной директорией и именем
        return new File(externalFilesDir, photoFilename);
    }

    /** возвращает список заметок из базы */
    public ArrayList<SmartNote> getNotes() {
        ArrayList<SmartNote> notes = new ArrayList<>();

        NoteCursorWraper cursor = queryNotes(null, null);

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()){
                SmartNote n = cursor.getNote();
                notes.add(n);
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return notes;
    }

    /** добавляет новую заметку
     * @param note - заметка для добавления */
    public void insertNote(SmartNote note){

        ContentValues values = getContentValues(note);

        mDatabase.insert(NoteDbSchema.NoteTable.NAME, null, values);
    }

    /** обновляет данные по заметке
     * @param note обновляемая заметка
     * */
    public void updateNote(SmartNote note){
        ContentValues values = getContentValues(note);

        String uid = Integer.toString(note.getId());

        mDatabase.update(NoteDbSchema.NoteTable.NAME, values,
                NoteDbSchema.NoteTable.Columns.ID_CL + " = ?",
                new String[]{ uid });
    }


    /** удаляет выбранную заметку
     * @param id - идентификатор заметки для удаления
     */
    public void deleteNote(int id) {

        mDatabase.delete(NoteDbSchema.NoteTable.NAME,
                NoteDbSchema.NoteTable.Columns.ID_CL + " = ?",
                new String[] { Integer.toString(id)});
    }

    /** получает ContentValue c данными по заметке */
    public static ContentValues getContentValues(SmartNote note){
        ContentValues values = new ContentValues();

        values.put(NoteDbSchema.NoteTable.Columns.TITLE_CL, note.getTitle());
        values.put(NoteDbSchema.NoteTable.Columns.DESCRIPTION_CL, note.getDescription());
        values.put(NoteDbSchema.NoteTable.Columns.IMPORTANCE_CL, note.getImportanceString());
        values.put(NoteDbSchema.NoteTable.Columns.IMAGE_CL, note.getPhotoPath());

        return values;
    }

    /** запрос на получение списка заметок  */
    private NoteCursorWraper queryNotes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                NoteDbSchema.NoteTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new NoteCursorWraper(cursor);
    }
}
