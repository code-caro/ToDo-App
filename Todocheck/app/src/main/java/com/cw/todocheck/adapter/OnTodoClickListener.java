package com.cw.todocheck.adapter;

import com.cw.todocheck.model.Task;

public interface OnTodoClickListener {
    void onTodoClick(Task task);
    void onTodoRadioButtonClick(Task task);
}
