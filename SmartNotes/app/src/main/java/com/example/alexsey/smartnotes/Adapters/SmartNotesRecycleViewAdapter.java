package com.example.alexsey.smartnotes.Adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexsey.smartnotes.Controllers.MainActivity;
import com.example.alexsey.smartnotes.Controllers.NoteActivity;
import com.example.alexsey.smartnotes.Controllers.ViewNoteActivity;
import com.example.alexsey.smartnotes.Database.SmartNotes;
import com.example.alexsey.smartnotes.Helpers.PictureUtils;
import com.example.alexsey.smartnotes.Models.Importance;
import com.example.alexsey.smartnotes.Models.SmartNote;
import com.example.alexsey.smartnotes.R;

import java.io.File;
import java.util.ArrayList;

/**
 * адаптер для списка "умных" заметок
 */
public class SmartNotesRecycleViewAdapter extends RecyclerView.Adapter<SmartNotesRecycleViewAdapter.SmartNoteViewHolder>{

    /** список заметок, выводимых на экран */
    private ArrayList<SmartNote> mNotes;

    /** ссылка на главную активность */
    private MainActivity mMainActivity;

    /** создает новый адаптер для RecyclerView
     * @param parent - ссылка на главную activity
     * @Param notes - список заметок, которые отображаем в нашем RecyclerView
     */
    public SmartNotesRecycleViewAdapter(MainActivity parent, ArrayList<SmartNote> notes){

        mMainActivity = parent;
        mNotes = notes;
    }

    @Override
    public SmartNoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note, parent, false);

        SmartNoteViewHolder vh = new SmartNoteViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SmartNoteViewHolder holder, final int position) {

        final SmartNote nextNote = mNotes.get(position);

        String file = nextNote.getPhotoPath();

        holder.mPhotoFile = SmartNotes.get(mMainActivity).getPhotoFile(file);

        holder.mTitleText.setText(nextNote.getTitle());

        // устанавливаю действие на открытие элемента
        holder.mTitleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NoteActivity.newIntent(mMainActivity, nextNote);
                mMainActivity.startActivityForResult(intent, MainActivity.REQUEST_CODE_UPDATE);
            }
        });

        String description = nextNote.getDescription().length() > 100 ?
                nextNote.getDescription().substring(0, 90) + "..." :
                nextNote.getDescription();

        holder.mBodyText.setText(description);

        Importance i = mNotes.get(position).getImportance();

        // окрашиваю элемент в зависимости от установленного приоритета
        switch (i) {
            case GREEN:
                holder.mCardView.setCardBackgroundColor(Color.GREEN);
                break;
            case YELLOW:
                holder.mCardView.setCardBackgroundColor(Color.YELLOW);
                break;
            case RED:
                holder.mCardView.setCardBackgroundColor(Color.RED);
                break;
            default:
                break;
        }

        // устанавливаю действие на удаление элемента из списка
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(mMainActivity);
                mBuilder.setTitle(R.string.del_title)
                        .setMessage(R.string.del_question)
                        .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SmartNotes.get(mMainActivity).deleteNote(nextNote.getId());
                                mMainActivity.updateUi();
                            }
                        })
                        .setNegativeButton(R.string.action_not, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                mBuilder.create().show();
            }
        });

        if (holder.mPhotoFile == null || !holder.mPhotoFile.exists()) {

            holder.mPreviewImage.setImageDrawable(null);
            holder.mPreviewImage.setVisibility(View.INVISIBLE);
        } else {

            Bitmap bitmap = PictureUtils.getScaledBitmap(holder.mPhotoFile.getPath(), mMainActivity);

            holder.mPreviewImage.setImageBitmap(bitmap);
            holder.mPreviewImage.setVisibility(View.VISIBLE);
        }

        holder.mPreviewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = PictureUtils.getViewImageIntent(mMainActivity, holder.mPhotoFile.getPath());
                mMainActivity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void setNotes(ArrayList<SmartNote> notes){
        mNotes = notes;
    }

    /** ViewHolder для RecyclerView*/
    public static class SmartNoteViewHolder extends RecyclerView.ViewHolder {

        /** CardView для умной заметки */
        CardView mCardView;

        /** заголовок умной заметки */
        TextView mTitleText;

        /** тело заметки */
        TextView mBodyText;

        /** кнопка удаления умной заметки */
        ImageButton mDeleteButton;

        /** превью для фотографии */
        ImageView mPreviewImage;

        /** файл с фотографией */
        File mPhotoFile;

        SmartNoteViewHolder(View itemView){
            super(itemView);

            mTitleText = (TextView) itemView.findViewById(R.id.title_text);

            mBodyText = (TextView) itemView.findViewById(R.id.body_text);

            mCardView = (CardView) itemView.findViewById(R.id.note_card);

            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);

            mPreviewImage = (ImageView) itemView.findViewById(R.id.preview_photo);
        }
    }
}
