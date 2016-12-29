package com.example.alexsey.smartnotes.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.alexsey.smartnotes.Models.SmartNote;

/**
 * CursorWraper для получения заметок
 */
public class NoteCursorWraper extends CursorWrapper {

    public NoteCursorWraper(Cursor cursor) {
        super(cursor);
    }

    /** получает заметку из Cursor-a*/
    public SmartNote getNote(){

        String id = getString(getColumnIndex(NoteDbSchema.NoteTable.Columns.ID_CL));

        String title = getString(getColumnIndex(NoteDbSchema.NoteTable.Columns.TITLE_CL));

        String description = getString(getColumnIndex(NoteDbSchema.NoteTable.Columns.DESCRIPTION_CL));

        String priority = getString(getColumnIndex(NoteDbSchema.NoteTable.Columns.IMPORTANCE_CL));

        String photo = getString(getColumnIndex(NoteDbSchema.NoteTable.Columns.IMAGE_CL));

        return new SmartNote(id, title, description, priority, photo);
    }
}
