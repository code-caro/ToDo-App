package com.cw.todocheck;

import android.os.Bundle;

import com.cw.todocheck.model.Priority;
import com.cw.todocheck.model.SharedViewModel;
import com.cw.todocheck.model.Task;
import com.cw.todocheck.model.TaskViewModel;
import com.cw.todocheck.util.Utils;
import com.google.android.material.chip.Chip;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private EditText enterTodo;
    private EditText enterKeyword;
    private ImageButton calendarButton;
    private CalendarView calendarView;
    private Group calendarGroup;
    private ImageButton priorityButton;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButtonID;
    private ImageButton saveButton;
    private Date dueDate;
    Calendar calendar = Calendar.getInstance();
    private SharedViewModel sharedViewModel;
    private boolean isEdit;
    private Priority priority;


    public BottomSheetFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        enterTodo = view.findViewById(R.id.enter_todo_et);
        enterKeyword = view.findViewById(R.id.enter_keyword);
        calendarButton = view.findViewById(R.id.today_calendar_button);
        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);
        saveButton = view.findViewById(R.id.save_todo_button);

        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(sharedViewModel.getSelectedItem().getValue() != null){
            isEdit = sharedViewModel.getIsEdit();
            Task task = sharedViewModel.getSelectedItem().getValue();
            enterTodo.setText(task.getTask());
            Log.d("Shared", "onViewCreated: " + task.getTask());
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);


        calendarButton.setOnClickListener(view12 -> {
            calendarGroup.setVisibility(
                    calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
            );
            Utils.hideSoftKeyboard(view12);
        });
        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMonth);
            dueDate = calendar.getTime();

            //Log.d("Cal", "onViewCreated: ===> month" + (month + 1) + ", dayOfMonth " + dayOfMonth);
        });

        priorityButton.setOnClickListener(view13 -> {
            Utils.hideSoftKeyboard(view13);
            priorityRadioGroup.setVisibility(
                    priorityRadioGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
            );
            priorityRadioGroup.setOnCheckedChangeListener((radioGroup, checkedID) -> {
                if(priorityRadioGroup.getVisibility() == View.VISIBLE){
                    selectedButtonID = checkedID;
                    selectedRadioButton = view.findViewById(selectedButtonID);
                    if(selectedRadioButton.getId() == R.id.radioButton_high){
                        priority = Priority.HIGH;
                    }else if(selectedRadioButton.getId() == R.id.radioButton_med){
                        priority = Priority.MEDIUM;
                    }else if(selectedRadioButton.getId() == R.id.radioButton_low) {
                        priority = Priority.LOW;
                    }else{
                        priority = Priority.LOW;
                    }
                }else{
                    priority = Priority.LOW;
                }
            });
        });

        saveButton.setOnClickListener(view1 -> {
            String task = enterTodo.getText().toString().trim();
            String keywords = enterKeyword.getText().toString().trim();

            Log.d("SaveButton", "Task: " + task);
            Log.d("SaveButton", "Keywords: " + keywords);
            Log.d("SaveButton", "Due Date: " + dueDate);
            Log.d("SaveButton", "Priority: " + priority);

            if(!TextUtils.isEmpty(task) && dueDate != null && priority != null){
                if (TextUtils.isEmpty(keywords)) {
                    keywords = "";
                }
                Task todoTask = new Task(task, priority,
                        keywords, "tester", "listname",
                       dueDate, Calendar.getInstance().getTime(),
                        false);
                if(isEdit){
                    Task updateTask = sharedViewModel.getSelectedItem().getValue();
                    updateTask.setTask(task);
                    updateTask.setKeywords(keywords);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    updateTask.setPriority(priority);
                    updateTask.setDueDate(dueDate);

                    TaskViewModel.update(updateTask);
                    sharedViewModel.setIsEdit(false);

                }else{
                    TaskViewModel.insert(todoTask);
                }
                enterTodo.setText("");
                if(this.isVisible()){
                    this.dismiss();
                }
            }else{
                Snackbar.make(saveButton, R.string.empty_field, Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.today_chip){
            calendar.add(Calendar.DAY_OF_YEAR, 0);
            dueDate = calendar.getTime();
            Log.d("Time", "onClick: " + dueDate.toString());
        }else if (id == R.id.tomorrow_chip){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dueDate = calendar.getTime();
            Log.d("Time", "onClick: " + dueDate.toString());
        }else if (id == R.id.next_week_chip){
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            dueDate = calendar.getTime();
            Log.d("Time", "onClick: " + dueDate.toString());
        }
    }
}