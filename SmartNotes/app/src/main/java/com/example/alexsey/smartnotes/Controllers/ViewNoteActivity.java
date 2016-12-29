package com.example.alexsey.smartnotes.Controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexsey.smartnotes.Database.SmartNotes;
import com.example.alexsey.smartnotes.Dialogs.PriorityDialog;
import com.example.alexsey.smartnotes.Helpers.Exporter;
import com.example.alexsey.smartnotes.Helpers.PictureUtils;
import com.example.alexsey.smartnotes.Models.SmartNote;
import com.example.alexsey.smartnotes.R;

public class ViewNoteActivity extends NoteActivity {

    public static String NOTE_EDIT = "com.example.alexsey.smartnotes.edit";

    private boolean isEditable;

    /** заметка для просмотра и редактирования */
    private SmartNote mNote;

    public ViewNoteActivity getActivity(){
        return this;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance){
        savedInstance.putString(NoteActivity.PHOTO_PATH, mNote.getPhotoPath());
        savedInstance.putString(NoteActivity.PRIORITY_STRING, mImportance.getText().toString());
        savedInstance.putBoolean(NOTE_EDIT, isEditable);

        //savedInstance.putSerializable(NOTE, mNote);
    }

    private void deleteNote() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.del_title)
               .setMessage(R.string.del_question)
               .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       SmartNotes.get(getApplicationContext()).deleteNote(mNote.getId());
                       setResult(RESULT_OK);
                       finish();
                   }
               })
               .setNegativeButton(R.string.action_not, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });
        builder.create().show();
    }

    /** устанавливает доступ к элементам контроллера */
    private void enableElement(boolean enabled){
        mTitle.setEnabled(enabled);
        mBody.setEnabled(enabled);
        mImportance.setEnabled(enabled);
    }

    private void setItsEdit(){
        enableElement(true);
        mEditButton.setVisibility(View.INVISIBLE);
    }

    private void abortChanges(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.abort_title)
                .setMessage(R.string.abort_question)
                .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTitle.setText(mNote.getTitle());
                        mBody.setText(mNote.getDescription());
                        mImportance.setText(mNote.getImportanceString());

                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.action_not, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    public void saveNote() {
        SmartNote newNote = new SmartNote(Integer.toString(mNote.getId()),
                mTitle.getText().toString(), mBody.getText().toString(),
                mImportance.getText().toString(), mNote.getPhotoPath());

        if(!mNote.equals(newNote)){
            SmartNotes.get(this).updateNote(newNote);
            this.setResult(RESULT_OK);
        }else{
            setResult(RESULT_CANCELED);
        }
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNote = (SmartNote)getIntent().getExtras().
                getSerializable(MainActivity.EXTRA_FOR_READ_AND_UPDATE);

        setContentView(R.layout.activity_note);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mToggle.syncState();

        mDrawerLayout.addDrawerListener(mToggle);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });


        mTitle = (EditText)findViewById(R.id.title_text);
        mTitle.setText(mNote.getTitle());

        mBody = (EditText)findViewById(R.id.body_text);
        mBody.setText(mNote.getDescription());

        mImportance = (TextView) findViewById(R.id.importance);
        mImportance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PriorityDialog priorityDialog = PriorityDialog.newInstance(getActivity());
                priorityDialog.show(getSupportFragmentManager(), priorityDialog.TAG);
            };
        });

        String importanceText = savedInstanceState != null ?
                savedInstanceState.getString(NoteActivity.PRIORITY_STRING):
                mNote.getImportanceString();

        mImportance.setText(importanceText);

        mPhotoTitle = (TextView) findViewById(R.id.image_title);

        mPhotoView = (ImageView) findViewById(R.id.photo_image);

        String photoPath = savedInstanceState != null ?
                savedInstanceState.getString(NoteActivity.PHOTO_PATH):
                mNote.getPhotoPath();

        mPhotoFile = SmartNotes.get(this).getPhotoFile(photoPath);

        mEditButton = (Button) findViewById(R.id.action_edit);

        isEditable = savedInstanceState == null ? false :
                savedInstanceState.getBoolean(NOTE_EDIT);

        if(isEditable)
            setItsEdit();
        else
            enableElement(false);

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditable= true;
                setItsEdit();
            }
        });

        hideNoFunctional();
        updatePhotoView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == NoteActivity.REQUEST_PHOTO & resultCode == RESULT_OK)
            updatePhotoView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_note_menu, menu);

        menu.findItem(R.id.action_save).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                deleteNote();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_camera:
                Intent i = PictureUtils.getCameraIntent(mPhotoFile);
                mDrawerLayout.closeDrawer(GravityCompat.END);
                startActivityForResult(i, NoteActivity.REQUEST_PHOTO);
                break;
            case R.id.action_clear_camera:
                mPhotoFile.delete();
                updatePhotoView();
                mDrawerLayout.closeDrawer(GravityCompat.END);
                break;
            case R.id.save:
                saveNote();
                mDrawerLayout.closeDrawer(GravityCompat.END);
                break;
            case R.id.action_save_txt:
                boolean result = Exporter.exportInTxt(mNote);
                mDrawerLayout.closeDrawer(GravityCompat.END);

                if(result){
                    Toast.makeText(this, R.string.ok_save, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_abort:
                abortChanges();
                mDrawerLayout.closeDrawer(GravityCompat.END);
                break;
        }
        return true;
    }
}
