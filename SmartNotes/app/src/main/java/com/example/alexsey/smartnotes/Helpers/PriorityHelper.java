package com.example.alexsey.smartnotes.Helpers;

import android.content.Context;

import com.example.alexsey.smartnotes.Database.SmartNotes;
import com.example.alexsey.smartnotes.Models.Importance;

/**
 * Created by alexsey on 27.12.16.
 */

public class PriorityHelper {

    public static String getPriorityName(Importance i){
        switch (i){
            case RED:
                return "Высокая";
            case YELLOW:
                return "Средняя";
            case GREEN:
                return "Низкая";
        }
        return "Не указана";
    }

    public static String getPriorityName(Importance i, Context context){

        switch (i){
            case RED:
                return SmartNotes.get(context).getPrioritys()[1];
            case YELLOW:
                return SmartNotes.get(context).getPrioritys()[2];
            case GREEN:
                return SmartNotes.get(context).getPrioritys()[3];
        }

        return SmartNotes.get(context).getPrioritys()[0];
    }

    public static Importance getPriorityColor(String name){
        switch (name){
            case "Высокая":
                return Importance.RED;
            case "Средняя":
                return Importance.YELLOW;
            case "Низкая":
                return Importance.GREEN;
        }

        return Importance.DEFAULT;
    }

}
