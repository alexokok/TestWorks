package com.example.alexsey.smartnotes.Controllers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.alexsey.smartnotes.Helpers.PictureUtils;
import com.example.alexsey.smartnotes.Helpers.PriorityHelper;
import com.example.alexsey.smartnotes.Models.Importance;
import com.example.alexsey.smartnotes.Models.SmartNote;
import com.example.alexsey.smartnotes.Dialogs.PriorityDialog;
import com.example.alexsey.smartnotes.R;

/** активити добавления новой заметки */
public class AddNoteActivity extends NoteActivity {

    /** название фото для прикрепления к заметке */
    private String mPhotoFileName;

    public AddNoteActivity getAddNodeActivity(){
        return this;
    }

    public AppCompatActivity getActivity(){
        return this;
    }


    /** сохраняет заметку */
    @Override
    public void saveNote() {
        if(!validTitle())
            Toast.makeText(this, R.string.error_empty_title, Toast.LENGTH_SHORT).show();
        else {

            SmartNote note = new SmartNote(mTitle.getText().toString(),
                    mBody.getText().toString(), mImportance.getText().toString(), mPhotoFileName);

            SmartNotes.get(this).insertNote(note);

            setResult(RESULT_OK);

            this.finish();
        }
    }

    /** устанавливает доступ к элементам NavigationView */
    private void setNavigationViewButtons() {
        Menu mNavigatioViewMenu = mNavigationView.getMenu();

        mNavigatioViewMenu.findItem(R.id.save).setVisible(false);
        mNavigatioViewMenu.findItem(R.id.action_abort).setVisible(false);
        mNavigatioViewMenu.findItem(R.id.action_save_txt).setVisible(false);
        mNavigatioViewMenu.findItem(R.id.action_share).setVisible(false);
    }

    /** получает название для нового файла*/
    private String getPhotoFilename() {
        int i = SmartNotes.get(this).getNotes().size() + 1 ;

        String fileName =  "IMG_" + i + ".jpg";

        mPhotoFile = SmartNotes.get(this).getPhotoFile(fileName);

        while (mPhotoFile.exists()){
            i++;

            fileName =  "IMG_" + i + ".jpg";

            mPhotoFile = SmartNotes.get(this).getPhotoFile(fileName);
        }

        return fileName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTitle = (EditText)findViewById(R.id.title_text);

        mBody = (EditText)findViewById(R.id.body_text);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        mImportance = (TextView) findViewById(R.id.importance);
        mImportance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PriorityDialog priorityDialog = PriorityDialog.newInstance(getAddNodeActivity());
                priorityDialog.show(getSupportFragmentManager(), priorityDialog.TAG);
            };
        });
        mImportance.setText(PriorityHelper.getPriorityName(Importance.DEFAULT, this));

        mPhotoTitle = (TextView) findViewById(R.id.image_title);

        mPhotoView = (ImageView) findViewById(R.id.photo_image);

        mPhotoFileName = getPhotoFilename();

        mPhotoFile = SmartNotes.get(this).getPhotoFile(mPhotoFileName);

        mEditButton = (Button) findViewById(R.id.action_edit);
        mEditButton.setVisibility(View.INVISIBLE);

        setNavigationViewButtons();

        enablePhotoButtons(true);

        updatePhotoView();

        hideNoFunctional();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == NoteActivity.REQUEST_PHOTO & resultCode == RESULT_OK)
            updatePhotoView();
    }

    @Override
    public void onBackPressed(){
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }else {
            if(validTitle()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(R.string.exit_title)
                        .setMessage(R.string.exit_without_save)
                        .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveNote();
                                setResult(RESULT_OK);
                                getActivity().finish();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.action_not, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mPhotoFile.delete();
                                setResult(Activity.RESULT_CANCELED);
                                getActivity().finish();
                            }
                        });
                builder.create().show();
            }
            else {
                mPhotoFile.delete();
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note_menu, menu);

        menu.findItem(R.id.action_delete).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_save)
            saveNote();

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
                break;
            case R.id.action_save_txt:
                break;
        }
        return true;
    }

}
