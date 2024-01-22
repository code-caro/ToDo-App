package com.cw.todocheck.adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.todocheck.R;
import com.cw.todocheck.model.Task;
import com.cw.todocheck.util.Utils;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<Task> taskList;
    private final OnTodoClickListener todoClickListener;

    private boolean isSortedByKeywords;

    private List<Task> filteredTaskList;

    public void setTasks(List<Task> tasks, boolean isSortedByKeywords) {
        this.isSortedByKeywords = isSortedByKeywords;
        taskList.clear();
        taskList.addAll(tasks);
        notifyDataSetChanged();
    }


    public RecyclerViewAdapter(List<Task> taskList, OnTodoClickListener ontodoClickListener) {
        this.taskList = taskList;
        this.filteredTaskList = new ArrayList<>(taskList);
        todoClickListener = ontodoClickListener;
    }

    public void filterTasks(String query) {
        filteredTaskList.clear();

        for (Task task : taskList) {
            if (isSortedByKeywords && task.getKeywords() != null && task.getKeywords().toLowerCase().contains(query.toLowerCase())) {
                filteredTaskList.add(task);
            } else if (!isSortedByKeywords && task.getTask().toLowerCase().contains(query.toLowerCase())) {
                filteredTaskList.add(task);
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        String formatted = Utils.formatDate(task.getDueDate());

        ColorStateList colorStateList = new ColorStateList(new int[][]{
            new int[]{-android.R.attr.state_enabled},
            new int[]{android.R.attr.state_enabled},
        }, new int[]{
                Color.LTGRAY, //disabled state
                Utils.priorityColor(task)
        });

        holder.task.setText(task.getTask());
        holder.todayChip.setText(formatted);
        holder.todayChip.setTextColor(Utils.priorityColor(task));
        holder.todayChip.setChipIconTint(colorStateList);
        holder.radioButton.setButtonTintList(colorStateList);

        if (isSortedByKeywords) {
            holder.keywordsTextView.setText("Keywords: " + task.getKeywords());
        } else {
            holder.keywordsTextView.setText("");  // Leer lassen, wenn nicht nach Keywords sortiert
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AppCompatRadioButton radioButton;
        public AppCompatTextView task;
        public Chip todayChip;

        public TextView keywordsTextView;

        OnTodoClickListener onTodoClickListener;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.todo_radio_button);
            task = itemView.findViewById(R.id.todo_row_todo);
            todayChip = itemView.findViewById(R.id.todo_row_chip);

            keywordsTextView = itemView.findViewById(R.id.todo_row_keywords);


            this.onTodoClickListener = todoClickListener;

            itemView.setOnClickListener(this);
            radioButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Task currTask = taskList.get(getAdapterPosition());
            int id = view.getId();
            if(id == R.id.todo_row_layout){
                onTodoClickListener.onTodoClick(currTask);
            }else if(id == R.id.todo_radio_button){
                onTodoClickListener.onTodoRadioButtonClick(currTask);
            }
        }
    }
}
