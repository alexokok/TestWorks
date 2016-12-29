package com.example.alexsey.smartnotes.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alexsey.smartnotes.Adapters.SmartNotesRecycleViewAdapter;
import com.example.alexsey.smartnotes.Database.SmartNotes;
import com.example.alexsey.smartnotes.Models.SmartNote;
import com.example.alexsey.smartnotes.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /** константа для добавления данных */
    public static final String EXTRA_FOR_READ_AND_UPDATE = "com.example.alexey.smartnotes.read_update";

    /** возвращаемый код, в случае, если была добавлена запись */
    private static final int REQUEST_CODE_ADD = 1;

    /** возвращаемый код, в случае, если запись была отредактирована */
    public static final int REQUEST_CODE_UPDATE = 2;

        /** RecyclerView для списка "умных заметок" */
    private RecyclerView mRecyclerView;

    /** LayoutManager для RecyclerView списка умных заметок */
    private RecyclerView.LayoutManager mLayoutManager;

    /** адаптер для RecylcerView */
    private SmartNotesRecycleViewAdapter mNotesAdapter;

    /** обновляет Ui активности */
    public void updateUi(){

        SmartNotes  notes = SmartNotes.get(this);

        ArrayList<SmartNote> notesList = notes.getNotes();

        if(mNotesAdapter == null){
            mNotesAdapter = new SmartNotesRecycleViewAdapter(this, notesList);
            mRecyclerView.setAdapter(mNotesAdapter);
        }else{
            mNotesAdapter.setNotes(notesList);
            mNotesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.note_recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        updateUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

         switch (item.getItemId()){
            case R.id.action_add:
                Intent i = NoteActivity.newIntent(MainActivity.this);
                startActivityForResult(i, MainActivity.REQUEST_CODE_ADD);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if((requestCode == MainActivity.REQUEST_CODE_ADD && resultCode == Activity.RESULT_OK) ||
           (requestCode == MainActivity.REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK)){
            updateUi();
        }
    }
}
