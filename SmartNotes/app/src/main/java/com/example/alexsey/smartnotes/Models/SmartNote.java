package com.example.alexsey.smartnotes.Models;

import com.example.alexsey.smartnotes.Helpers.PriorityHelper;

import java.io.Serializable;

/**
 * Заметка
 */
public class SmartNote implements Serializable {

    /** идентификатор записи */
    private int mId;

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    /** заголовок заметки */
    private String mTitle;

    public String getTitle() {
        return mTitle;
    }

    /** описание заметки */
    private String mDescription;

    public String getDescription() {
        return mDescription;
    }

    /** важность заметки */
    private Importance mImportance;

    public Importance getImportance() {
        return mImportance;
    }

    public String getImportanceString(){
        return PriorityHelper.getPriorityName(mImportance);
    }

    /** путь к файлу */
    private String mPhotoPath;

    public String getPhotoPath() {
        return mPhotoPath;
    }

    /** вызывается в NoteCursorWraper при чтении данных
     * @param id - идентификатор записи
     * @param title - заголовок
     * @param description - описание
     * @param importance - важность
     * @param photoPath - фотография
     */
    public SmartNote(String id, String title, String description,
                     String importance, String photoPath) {
        mTitle = title;
        mDescription = description;
        mId = Integer.parseInt(id);
        mImportance = PriorityHelper.getPriorityColor(importance);
        mPhotoPath = photoPath;
    }

    public SmartNote(String title, String description, String type, String photoPath){
        mTitle = title;
        mDescription = description;
        mId = -1;
        mPhotoPath = photoPath;
        mImportance = PriorityHelper.getPriorityColor(type);
    }

    @Override
    public boolean equals(Object o){

        SmartNote note = (SmartNote) o;

        if(!this.getTitle().equals(note.getTitle()))
            return false;
        else if(!this.getDescription().equals(note.getDescription()))
            return false;
        else if(this.getImportance() != note.getImportance())
            return false;

        return true;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();

        builder.append("Заголовок заметки: ").
                append(mTitle).append("\n").
                append("Текстовая заметка: ").
                append(mDescription);
        return builder.toString();
    }
}
