package com.example.alexsey.smartnotes.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.alexsey.smartnotes.Controllers.NoteActivity;
import com.example.alexsey.smartnotes.Database.SmartNotes;
import com.example.alexsey.smartnotes.R;

/**
 * собирает данные с приоритетом заметки
 */
public class PriorityDialog extends SomeDialog {

    /** TAG для запуска*/
    public static final String TAG = "com.example.alexsey.smartnotes.PriorityDialog";

    private NoteActivity mNoteActivity;

    public void setNoteActivity(NoteActivity addNoteActivity) {
        mNoteActivity = addNoteActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mBuilder = new AlertDialog.Builder(getContext());

        mBuilder.setTitle(R.string.importance_title)
                .setItems(SmartNotes.get(getContext()).getPrioritys(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNoteActivity.setPriority(SmartNotes.get(getContext()).getPrioritys()[which]);
                        dialog.dismiss();
                    }
                });

        return mBuilder.create();
    }

    /** фабричный метод для создания диалога выбора приоритета */
    public static PriorityDialog newInstance(NoteActivity noteActivity){

        PriorityDialog picker = new PriorityDialog();
        picker.setNoteActivity(noteActivity);

        return picker;
    }
}
