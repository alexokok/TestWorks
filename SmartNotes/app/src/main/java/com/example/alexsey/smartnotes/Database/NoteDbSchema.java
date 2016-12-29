package com.example.alexsey.smartnotes.Database;

/**
 * схема для описания базы данных по заметкам в SQLite (уровень 2)
 */
public class NoteDbSchema{

    public static final String DB_NAME = "SmartNotes";

    /** таблица с заметками */
    public static final class NoteTable{

        /** название таблицы */
        public static final String NAME = "Note";

        /** столбцы таблицы с заметками*/
        public static final class Columns{

            /** идентификатор заметки */
            public static final String ID_CL = "id";

            /** заголовок заметки*/
            public static final String TITLE_CL = "title";

            /** тело заметки */
            public static final String DESCRIPTION_CL = "description";

            public static final String IMPORTANCE_CL = "importance";

            /** название файла с картинкой */
            public static final String IMAGE_CL = "image";
        }
    }
}
