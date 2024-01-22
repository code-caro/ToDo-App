package com.cw.todocheck;


import com.google.android.material.snackbar.Snackbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.cw.todocheck.adapter.OnTodoClickListener;
import com.cw.todocheck.adapter.RecyclerViewAdapter;
import com.cw.todocheck.model.SharedViewModel;
import com.cw.todocheck.model.Task;
import com.cw.todocheck.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.Menu;
import android.view.MenuItem;

import java.util.List;


public class MainActivity extends AppCompatActivity implements OnTodoClickListener {
    private static final String TAG = "ITEM";
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;

    private boolean isSortedByKeywords = true;

    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this.getApplication())
                .create(TaskViewModel.class);

        sharedViewModel = new ViewModelProvider(this)
                .get(SharedViewModel.class);

        taskViewModel.getAllTasks().observe(this, tasks -> {
            recyclerViewAdapter = new RecyclerViewAdapter(tasks, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showBottomSheetDialog());

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString().trim();
                taskViewModel.searchTasks(query).observe(MainActivity.this, tasks -> {
                    recyclerViewAdapter.setTasks(tasks, isSortedByKeywords);
                    recyclerViewAdapter.notifyDataSetChanged();
                });
            }
        });
    }

    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    private void observeAndSetTasks(LiveData<List<Task>> liveData, boolean isSortedByKeywords) {
        liveData.observe(this, tasks -> {
            recyclerViewAdapter.setTasks(tasks, isSortedByKeywords);
            recyclerViewAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.d(TAG, "About Activity");
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_sort_by_task_asc) {
            Log.d(TAG, "Sorting by task (ascending)");
            taskViewModel.getTasksSortedByTaskAscending().observe(this, tasks -> {
                recyclerViewAdapter.setTasks(tasks, false);
                recyclerViewAdapter.notifyDataSetChanged();
            });
            return true;
        } else if (id == R.id.action_sort_by_task_desc) {
            Log.d(TAG, "Sorting by task (descending)");
            taskViewModel.getTasksSortedByTaskDescending().observe(this, tasks -> {
                recyclerViewAdapter.setTasks(tasks, false);
                recyclerViewAdapter.notifyDataSetChanged();
            });
            return true;
        }
        if (id == R.id.action_sort_priority) {
            Log.d(TAG, "Sorting by priority");
            taskViewModel.getTasksSortedByPriority().observe(this, tasks -> {
                recyclerViewAdapter.setTasks(tasks, false);
                recyclerViewAdapter.notifyDataSetChanged();
            });
            return true;
        } else if (id == R.id.action_sort_due_date) {
            Log.d(TAG, "Sorting by due date");
            taskViewModel.getTasksSortedByDueDate().observe(this, tasks -> {
                recyclerViewAdapter.setTasks(tasks, false);
                recyclerViewAdapter.notifyDataSetChanged();
            });
            return true;
        } else if (id == R.id.action_sort_keywords) {
            taskViewModel.getTasksSortedByKeywords().observe(this, tasks -> {
                recyclerViewAdapter.setTasks(tasks, isSortedByKeywords);
                recyclerViewAdapter.notifyDataSetChanged();
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTodoClick(Task task) {
        sharedViewModel.setSelectedItem(task);
        sharedViewModel.setIsEdit(true);
        //Log.d("Click", "onTodoClick: " + task.getTask());
        showBottomSheetDialog();
    }

    @Override
    public void onTodoRadioButtonClick(Task task) {
        //Log.d("Click", "onRadioButtonClick: " + task.getTask());
        View rootView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView,
                "Do you really want to delete this task?",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Delete", v -> {
            TaskViewModel.delete(task);
            recyclerViewAdapter.notifyDataSetChanged();
        });

        snackbar.show();
    }
}