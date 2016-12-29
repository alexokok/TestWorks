package com.example.alexsey.smartnotes.Controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexsey.smartnotes.Helpers.PictureUtils;
import com.example.alexsey.smartnotes.Models.SmartNote;
import com.example.alexsey.smartnotes.R;

import java.io.File;

/**
 * абстрактный класс, необходим для реализации разных контроллеров для одного представления
 */
public abstract class NoteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static String PRIORITY_STRING = "com.example.smartnotes.priority";

    public static String PHOTO_PATH = "com.example.smartnotes.photo";

    /** запрос на получение фотографии */
    public static final int REQUEST_PHOTO = 1;

    /** toolbar с элементами меню */
    public Toolbar mToolbar;

    /** EditText c заголовком заметки */
    public EditText mTitle;

    /** EditText с телом заметки */
    public EditText mBody;

    /** Важность заметки */
    public TextView mImportance;

    /** заголовок перед фотографией */
    public TextView mPhotoTitle;

    /** фотография */
    public ImageView mPhotoView;

    public NavigationView mNavigationView;

    /** DrawerLayout для меню действий */
    public DrawerLayout mDrawerLayout;

    /** ссылка на файл */
    public File mPhotoFile;

    /** разрешает редактирование активности */
    public Button mEditButton;

    public ActionBarDrawerToggle mToggle;

    /** проверка заголовка заметки */
    public boolean validTitle() {

        if (mTitle.getText().toString().trim().isEmpty())
            return false;

        return true;
    }

    /** устанавливает приоритет заметки*/
    public void setPriority(String priority) {
        mImportance.setText(priority);
    }

    /** устанавливает доступ к кнопкам камеры*/
    public void enablePhotoButtons(boolean enable){
        Menu menu = mNavigationView.getMenu();

        menu.findItem(R.id.action_clear_camera).setEnabled(!enable);
        menu.findItem(R.id.action_camera).setEnabled(enable);
    }

    /** скрывает не реализованный функционал (он здесь временно)*/
    public void hideNoFunctional(){
        Menu menu = mNavigationView.getMenu();

        menu.findItem(R.id.action_location).setEnabled(false);
        menu.findItem(R.id.action_location_map).setEnabled(false);
        menu.findItem(R.id.action_share).setEnabled(false);
    }

    /** обновляет ImageView с фотографией */
    public void updatePhotoView() {
        if(mPhotoFile == null || !mPhotoFile.exists()) {

            mPhotoView.setImageDrawable(null);
            mPhotoView.setVisibility(View.INVISIBLE);
            mPhotoTitle.setVisibility(View.INVISIBLE);
            enablePhotoButtons(true);
        }
        else{

            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), this);

            mPhotoView.setImageBitmap(bitmap);
            mPhotoView.setVisibility(View.VISIBLE);
            mPhotoTitle.setVisibility(View.VISIBLE);

            enablePhotoButtons(false);
        }
    }

    /** сохраняет заметку */
    public abstract void saveNote();

    /** метод для формирования Intent-a вызова активности добавления данных
     * @param packageContext - контекст, из которого вызывается intent
     */
    public static Intent newIntent(Context packageContext){
        Intent i = new Intent(packageContext, AddNoteActivity.class);
        return i;
    }

    /** метод для формирования Intent-а вызова активности редактирования данных
     * @param  packageContext - контекст для запуска данных
     * @param editableNode - изменяемая заметка
     * */
    public static Intent newIntent(Context packageContext, SmartNote editableNode){
        Intent i = new Intent(packageContext, ViewNoteActivity.class);

        i.putExtra(MainActivity.EXTRA_FOR_READ_AND_UPDATE, editableNode);

        return i;
    }

}