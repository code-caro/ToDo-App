package com.cw.todocheck.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.cw.todocheck.model.Priority;
import com.cw.todocheck.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String formatDate(Date date){
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("EEE, MMM d");

        return simpleDateFormat.format(date);
    }
    public static void hideSoftKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
        );
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int priorityColor(Task task) {
       int color;
       if(task.getPriority() == Priority.HIGH){
           color = Color.argb(200, 195, 21, 18);
       }else if(task.getPriority() == Priority.MEDIUM){
           color = Color.argb(200, 247, 137, 0);
       }else {
           color = Color.argb(200, 18, 157, 195);
       }
       return color;
    }
}
