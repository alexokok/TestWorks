package com.example.alexsey.smartnotes.Helpers;

import android.os.Environment;

import com.example.alexsey.smartnotes.Models.SmartNote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Экспортер данных
 */

public class Exporter {

    public static String DIR_SD = "Smart Notes";

    /** экспорт данных в текстовый файл
     * @param note - заметка, которую необходимо сохранить */
    public static boolean exportInTxt(SmartNote note){

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File sdPath = Environment.getExternalStorageDirectory();

            sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
            sdPath.mkdirs();

            File sdFile = new File(sdPath, note.getTitle());
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));

                bw.write(note.toString());
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            finally {
                return true;
            }
        } else
        return false;
    }
}
