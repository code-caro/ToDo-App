package com.cw.todocheck.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cw.todocheck.data.DocheckRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    public static DocheckRepository repository;
    public final LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new DocheckRepository(application);
        allTasks = repository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {return allTasks;}
    public static void insert(Task task){repository.insert(task);}
    public LiveData<Task> get(long id) {return repository.get(id);}
    public static void update(Task task) {repository.update(task);}
    public static void delete(Task task) {repository.delete(task);}

    public LiveData<List<Task>> getTasksSorted(String orderBy) {
        return repository.getTasksSorted(orderBy);
    }

    public LiveData<List<Task>> getTasksSortedByTaskAscending() {
        return repository.getTasksSortedByTaskAscending();
    }

    public LiveData<List<Task>> getTasksSortedByTaskDescending() {
        return repository.getTasksSortedByTaskDescending();
    }

    public LiveData<List<Task>> getTasksSortedByPriority() {
        return repository.getTasksSortedByPriority();
    }

    public LiveData<List<Task>> getTasksSortedByDueDate() {
        return repository.getTasksSortedByDueDate();
    }

    public LiveData<List<Task>> getTasksSortedByKeywords() {
        return repository.getTasksSortedByKeywords();
    }

    public LiveData<List<Task>> searchTasks(String query) {
        return repository.searchTasks(query);
    }
}
